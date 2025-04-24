package com.example.coincap.vm

import com.example.coincap.rp.model.Asset

/**
 * This class is single source of truth for main screen viewmodel. It contains UI state, user event and side effect for the current view.
 */
class AssetListContract{

    /**
     * Sealed class for possible UI input event.
     */
    sealed class Event{
        data class AssetSelection(val asset: Asset): Event()
    }

    /**
     * Data class for possible UI state.
     */
    data class State(
        val assets: List<Asset> = listOf(),
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    /**
     * Sealed class for possible side effects as a result of user event.
     */
    sealed class Effect{
        data object Loaded: Effect()

        sealed class Preview : Effect(){
            data class ToPreview(val id: String): Preview()
        }
    }
}


