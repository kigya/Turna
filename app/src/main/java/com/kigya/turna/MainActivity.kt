package com.kigya.turna

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.databinding.ActivityMainBinding

const val SHARED_TIMER_KEY = "shared_timer_key"

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private var currentCookDuration: Long = 0
    private lateinit var sharedPreferences: SharedPreferences

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentCookDuration = result.data?.getLongExtra(RECIPE_INDEX_MAIN, 0) ?: 0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(SHARED_TIMER_KEY, MODE_PRIVATE)
        currentCookDuration = sharedPreferences.getLong(SHARED_TIMER_KEY, 0)
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
                launcher.launch(
                    RecipeActivity
                        .newIntent(this@MainActivity)
                )
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
