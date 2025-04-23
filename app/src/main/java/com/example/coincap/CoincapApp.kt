package com.example.coincap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application starting point.
 * HiltAndroidApp annotation indicate application class where dagger components will be generated
 */

@HiltAndroidApp
class CoincapApp : Application()