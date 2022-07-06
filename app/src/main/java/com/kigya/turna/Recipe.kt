package com.kigya.turna

import androidx.annotation.StringRes
import java.sql.Time

data class Recipe(@StringRes val nameId: Int, @StringRes val descriptionId: Int, val time: Time)


