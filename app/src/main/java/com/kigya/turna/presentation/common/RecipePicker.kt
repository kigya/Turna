package com.kigya.turna.presentation.common

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class RecipePicker(
    activityResultRegistry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    private val callback: (timeMilliseconds: Long?) -> Unit
) {
    private val launcher =
        activityResultRegistry.register(
            REGISTRY_KEY,
            lifecycleOwner,
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            callback(result.data?.getLongExtra(EXTRA_TIME_MILLISECONDS, 0))
        }

    fun pickRecipe(intent: Intent) {
        launcher.launch(intent)
    }

    companion object {
        private const val REGISTRY_KEY = "recipe_picker"
        const val EXTRA_TIME_MILLISECONDS = "time_milliseconds"
    }
}


