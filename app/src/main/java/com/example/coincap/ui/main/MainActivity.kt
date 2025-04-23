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

/**
 * Activity to start the Jetpack compose UI from the onCreate
 * AndroidEntryPoint annotation indicates class to be setup for injection using dagger hilt android component.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Field injection of EventHandler for handling user event
     */
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