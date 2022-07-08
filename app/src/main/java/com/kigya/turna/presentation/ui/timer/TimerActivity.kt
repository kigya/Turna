package com.kigya.turna.presentation.ui.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.R
import com.kigya.turna.databinding.ActivityMainBinding
import com.kigya.turna.launchWhenStarted
import com.kigya.turna.presentation.common.RecipePicker
import com.kigya.turna.presentation.ui.recipe.RecipeActivity
import kotlinx.coroutines.flow.onEach

class TimerActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val timerViewModel: TimerViewModel by viewModels()

    private val recipePicker = RecipePicker(
        activityResultRegistry,
        this
    ) { timeValue: Long? ->
        timerViewModel.setTimer(timeValue ?: 0)
        timerViewModel.actualRecipeTimeMillis = timeValue ?: 0
        setActualMillisToView()
    }
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            clearButton.setOnClickListener {
                setOnClickAction(it, false)
                restoreStateIfRunning()
                timerViewModel.setTimer(0)
                timerViewModel.actualRecipeTimeMillis = 0
                animatedEggView.pauseAnimation()
            }
            refreshButton.setOnClickListener {
                setOnClickAction(it, false)
                restoreStateIfRunning()
                animatedEggView.pauseAnimation()
                timerViewModel.setTimer(timerViewModel.actualRecipeTimeMillis)
            }
            actionButton.setOnClickListener {
                setOnClickAction(it, true)
                if (timerViewModel.isRunning.value) {
                    timerViewModel.setRunningState(false)
                } else {
                    timerViewModel.setRunningState(true)
                }
            }
            chooseModeButton.setOnClickListener {
                val intent = RecipeActivity.newIntent(this@TimerActivity)
                recipePicker.pickRecipe(intent)
            }

            timerViewModel.isRunning.onEach {
                if (it) {
                    startCountDownTimer(timerViewModel.actualMilliseconds.value ?: 0)
                    animatedEggView.playAnimation()
                } else {
                    stopCountDownTimer()
                    animatedEggView.pauseAnimation()
                }
            }.launchWhenStarted(lifecycleScope)

            timerViewModel.actualMilliseconds.onEach {
                setActualMillisToView()
                if (timerViewModel.actualMilliseconds.value == 0L) {
                    animatedEggView.progress = 0f
                }
            }.launchWhenStarted(lifecycleScope)

            //circularProgressBar.setProgressWithAnimation(1f, 2000)

            circularProgressBar.onProgressChangeListener = {
                circularProgressBar.progress =
                    (timerViewModel.actualRecipeTimeMillis - timerViewModel.actualMilliseconds.value) * 100f /
                            (timerViewModel.actualRecipeTimeMillis + .1f)
            }

        }
    }

    private fun ActivityMainBinding.restoreStateIfRunning() {
        if (timerViewModel.isRunning.value) {
            timerViewModel.setRunningState(false)
            actionButton.setImageResource(PLAY_BUTTON_DRAWABLE)
        }
    }

    override fun onResume() {
        super.onResume()
        setActualMillisToView()
    }

    private fun setActualMillisToView() {
        val milliseconds = timerViewModel
            .actualMilliseconds.value
        if (milliseconds != 0L) {
            val minutes = milliseconds / 1000 / 60
            val seconds = milliseconds / 1000 % 60
            if (seconds < 10) {
                binding.timerTextView.text = "$minutes:0$seconds"
            } else {
                binding.timerTextView.text = "$minutes:$seconds"
            }
        } else {
            binding.timerTextView.text = "Ready"
        }

    }

    override fun onStop() {
        super.onStop()
        timerViewModel.setRunningState(false)
    }

    private fun ActivityMainBinding.setOnClickAction(
        it: View,
        resourceChange: Boolean
    ) =
        it.animate()
            .alphaBy(-0.3f)
            .setDuration(100)
            .withStartAction {
                if (resourceChange) {
                    if (!timerViewModel.isRunning.value) {
                        actionButton.setImageResource(STOP_BUTTON_DRAWABLE)
                    } else {
                        actionButton.setImageResource(PLAY_BUTTON_DRAWABLE)
                    }
                }
                it.isEnabled = false
            }
            .withEndAction {
                it.isEnabled = true
                it.animate()
                    .alphaBy(0.3f)
                    .setDuration(100)
                    .start()
            }
            .start()

    private fun startCountDownTimer(timeMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeMillis, 1) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTextView.text = millisUntilFinished.toString()
                timerViewModel.setTimer(millisUntilFinished)
            }

            override fun onFinish() {
                if (timerViewModel.actualRecipeTimeMillis != 0L) {
                    binding.timerTextView.text = "Done!"
                    stopCountDownTimer()
                }

            }
        }.start()
    }

    private fun stopCountDownTimer() {
        timer?.cancel()
    }

    private companion object {
        private const val PLAY_BUTTON_DRAWABLE = R.drawable.ic_play_button
        private const val STOP_BUTTON_DRAWABLE = R.drawable.ic_stop_button
    }


}
