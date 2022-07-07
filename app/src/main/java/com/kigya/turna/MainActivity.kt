package com.kigya.turna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private var currentCookDuration: Long = 0
    private val recipePicker = RecipePicker(
        activityResultRegistry,
        this
    ) { timeValue: Long? ->
        currentCookDuration = timeValue ?: 0
        binding.timerTextView.text = currentCookDuration.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            clearButton.setOnClickListener {
                setOnClickAction(it, false)
            }
            refreshButton.setOnClickListener {
                setOnClickAction(it, false)
            }
            actionButton.setOnClickListener {
                setOnClickAction(it, true)
            }
            chooseModeButton.setOnClickListener {
                val intent = RecipeActivity.newIntent(this@MainActivity)
                recipePicker.pickRecipe(intent)
            }
            timerTextView.text = currentCookDuration.toString()
        }

    }

    private fun ActivityMainBinding.setOnClickAction(it: View, resourceChange: Boolean) =
        it.animate()
            .alphaBy(-0.3f)
            .setDuration(100)
            .withStartAction {
                if (resourceChange) {
                    actionButton.setImageResource(R.drawable.ic_stop_button)
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
}
