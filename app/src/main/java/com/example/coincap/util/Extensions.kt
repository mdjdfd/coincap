package com.example.coincap.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost

/**
 * Extension function that observe the current UI state and handle side effect for each state. The function works as bridge between viewmodel and view layer.
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.observe(
    sideEffectHandler: (suspend (sideEffect: SIDE_EFFECT) -> Unit)? = null
): STATE {
    if(sideEffectHandler != null){
        LaunchedEffect(this){
            launch {
                container.sideEffectFlow.collect{sideEffectHandler(it)}
            }
        }
    }
    return container.stateFlow.collectAsStateWithLifecycle().value
}