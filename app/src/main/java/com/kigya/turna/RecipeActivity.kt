package com.kigya.turna

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.databinding.ActivityRecipeBinding
import github.com.st235.lib_expandablebottombar.Menu
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor
import java.sql.Time

const val RECIPE_INDEX_MAIN = "recipeIndexMain"

class RecipeActivity : AppCompatActivity(R.layout.activity_recipe) {

    private val binding by viewBinding(ActivityRecipeBinding::bind)
    private val recipeViewModel: MainViewModel by viewModels()
    private var recipeIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeIndex = recipeViewModel.currentIndex

        with(binding) {
            with(expandableBottomBar) {
                menu.addItems()
                onItemSelectedListener = { _, menuItem, _ ->
                    recipeIndex = BottomMenuItems.values()
                        .find { getString(it.string) == menuItem.text }?.index!!
                    setScreenDescriptionText()
                    getSharedPreferences(SHARED_TIMER_KEY, Context.MODE_PRIVATE)
                        .edit()
                        .putLong(RECIPE_INDEX_MAIN, recipeViewModel.currentCookTime)
                        .apply()
                }
                setScreenDescriptionText()
            }

            okButton.setOnClickListener {
                recipeViewModel.currentIndex = recipeIndex
                setIndexResult(recipeViewModel.recipeBank[recipeIndex].time)
                finish()
            }

        }

    }

    private fun ActivityRecipeBinding.setScreenDescriptionText() {
        recipeTextView.text =
            getString(recipeViewModel.recipeBank[recipeIndex].descriptionId)
    }

    private fun Menu.addItems() {
        add(
            MenuItemDescriptor.Builder(
                this@RecipeActivity,
                R.id.easy,
                R.drawable.ic_egg,
                R.string.easy_name,
                resources.getColor(R.color.light, theme)
            ).build()
        )
        add(
            MenuItemDescriptor.Builder(
                this@RecipeActivity,
                R.id.medium,
                R.drawable.ic_egg,
                R.string.medium_name,
                Color.WHITE
            ).build()
        )
        add(
            MenuItemDescriptor.Builder(
                this@RecipeActivity,
                R.id.well,
                R.drawable.ic_egg,
                R.string.well_name,
                Color.WHITE
            ).build()
        )
        add(
            MenuItemDescriptor.Builder(
                this@RecipeActivity,
                R.id.sunny,
                R.drawable.ic_egg,
                R.string.sunny_side_up_name,
                Color.WHITE,
            ).build()
        )
        add(
            MenuItemDescriptor.Builder(
                this@RecipeActivity,
                R.id.hard,
                R.drawable.ic_egg,
                R.string.hard_name,
                Color.WHITE
            ).build()
        )
    }

    private fun setIndexResult(time: Time) {
        val milliseconds = recipeViewModel.currentCookTime
        val data = Intent().apply {
            putExtra(RECIPE_INDEX_MAIN, milliseconds)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, RecipeActivity::class.java)
        }
    }
}