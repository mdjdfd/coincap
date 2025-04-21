package com.example.coincap.vm

import com.example.coincap.rp.model.Asset

class AssetListContract{

    sealed class Event{
        data class AssetSelection(val asset: Asset): Event()
    }

    data class State(
        val assets: List<Asset> = listOf(),
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    sealed class Effect{
        data object Loaded: Effect()

        sealed class Preview : Effect(){
            data class ToPreview(val id: String): Preview()
        }
    }
}


