package com.kigya.turna

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.turna.RecipePicker.Companion.EXTRA_TIME_MILLISECONDS
import com.kigya.turna.databinding.ActivityRecipeBinding
import github.com.st235.lib_expandablebottombar.Menu
import github.com.st235.lib_expandablebottombar.MenuItem
import github.com.st235.lib_expandablebottombar.MenuItemDescriptor


class RecipeActivity : AppCompatActivity(R.layout.activity_recipe) {

    private val binding by viewBinding(ActivityRecipeBinding::bind)
    private val recipeViewModel: MainViewModel by viewModels()
    private lateinit var recipeBank: List<Recipe>
    private var recipeIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeIndex = recipeViewModel.currentIndex
        recipeBank = recipeViewModel.recipeBank

        with(binding) {
            with(expandableBottomBar) {
                menu.addItems()
                onItemSelectedListener = { _, menuItem, _ ->
                    findCurrentMenuIndex(menuItem)
                    setScreenDescriptionText()
                }
                setScreenDescriptionText()
            }
            okButton.setOnClickListener {
                recipeViewModel.currentIndex = recipeIndex
                transferActivityResult()
                finish()
            }
        }
    }

    private fun findCurrentMenuIndex(menuItem: MenuItem) {
        recipeIndex = recipeBank
            .withIndex()
            .first { it.value.menuId == menuItem.id }.index
    }

    private fun ActivityRecipeBinding.setScreenDescriptionText() {
        recipeTextView.text =
            getString(recipeBank[recipeIndex].descriptionId)
    }

    private fun Menu.addItems() {
        val iconId = R.drawable.ic_egg
        val context = this@RecipeActivity
        val activeColor = resources.getColor(R.color.light, theme)
        (recipeBank.indices).forEach { index ->
            add(
                MenuItemDescriptor.Builder(
                    context = context,
                    itemId = recipeBank[index].menuId,
                    iconId = iconId,
                    textId = recipeBank[index].nameId,
                    activeColor = activeColor
                ).build()
            )
        }
    }

    private fun transferActivityResult() {
        val data = Intent().apply {
            putExtra(EXTRA_TIME_MILLISECONDS, recipeViewModel.currentCookTime)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, RecipeActivity::class.java)
        }
    }
}