// app/src/main/java/com/rc/app/MainActivity.kt
package com.rc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rc.app.ui.theme.RCTheme
import com.rc.feature.offers.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RCTheme { AppNavHost() } }
    }
}
