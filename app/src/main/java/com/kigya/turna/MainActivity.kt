package com.kigya.turna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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