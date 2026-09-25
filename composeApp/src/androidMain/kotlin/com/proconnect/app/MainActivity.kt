package com.proconnect.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.WHITE
        window.navigationBarColor = android.graphics.Color.BLACK
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(ComposeView(this).apply { setContent { ProConnectSplashScreen() } })
    }
}
