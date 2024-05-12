package com.dacs.vku.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import com.dacs.vku.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image:Int,
)

val modifiera = {
    Modifier.fillMaxSize()
}
val pages = listOf<Page>(
    Page(
        title = "VKU Application",
        description = "Design by two handsome boy for students to reach the notification",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Le Tang Phu Quy",
        description = "Computer-man in group",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Phan Minh Gia Huy",
        description = "The most handsome boy in the group",
        image = R.drawable.onboarding3
    )
)