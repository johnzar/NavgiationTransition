package com.johnzar.navigationtransition

import android.content.Context
import android.util.AttributeSet
import android.view.View

class StatusBarCoverView constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    init {
        @Suppress("DEPRECATION")
        systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}