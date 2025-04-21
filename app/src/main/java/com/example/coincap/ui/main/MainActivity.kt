package com.example.coincap.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.coincap.ui.compose.route.CoincapNavigation
import com.example.coincap.ui.theme.CoinCapTheme
import com.example.coincap.util.EventHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var eventHandler: EventHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinCapTheme(darkTheme = true) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CoincapNavigation(eventHandler)
                }
            }
        }
    }
}