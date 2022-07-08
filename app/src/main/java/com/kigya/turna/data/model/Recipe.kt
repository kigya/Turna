package com.kigya.turna.data.model

import androidx.annotation.StringRes
import java.sql.Time

data class Recipe(
    @StringRes val menuId: Int,
    @StringRes val nameId: Int,
    @StringRes val descriptionId: Int,
    val time: Time
)


