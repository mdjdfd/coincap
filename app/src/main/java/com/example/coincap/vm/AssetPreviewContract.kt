package com.example.coincap.vm

import com.example.coincap.rp.model.Asset

/**
 * This class is single source of truth for preview screen viewmodel. It contains UI state, user event and side effect for the current view.
 */
class AssetPreviewContract {

    /**
     * Sealed class for possible UI input event.
     */
    sealed class Event{
        data object BackPressed: Event()
    }

    /**
     * Data class for possible UI state.
     */
    data class State(
        val asset: Asset?,
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    /**
     * Sealed class for possible side effects as a result of user event.
     */
    sealed class Effect{
        data object Loaded: Effect()

        sealed class Back : Effect(){
            data object ToList : Back()
        }
    }
}