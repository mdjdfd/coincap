package com.example.coincap.vm

import com.example.coincap.rp.model.Asset

class AssetPreviewContract {

    sealed class Event{
        data object BackPressed: Event()
    }

    data class State(
        val asset: Asset? = null,
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    sealed class Effect{
        data object Loaded: Effect()

        sealed class Back : Effect(){
            data object ToList : Back()
        }
    }
}