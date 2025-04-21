package com.example.coincap.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost

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

    return container.stateFlow.collectAsState().value
}