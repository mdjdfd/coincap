package com.example.coincap.util

import com.example.coincap.di.MainDispatcher
import com.example.coincap.rp.model.Asset
import com.example.coincap.vm.AssetListContract
import com.example.coincap.vm.AssetPreviewContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Class is responsible to handle item and back press event.
 * @param mainDispatcher instance of coroutine main Dispatcher.
 */
class EventHandler @Inject constructor(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    private val _tapEventSharedFlow = MutableSharedFlow<AssetListContract.Event>()
    val tapEventSharedFlow = _tapEventSharedFlow.asSharedFlow()

    fun tapped(asset: Asset) {
        CoroutineScope(mainDispatcher).launch {
            _tapEventSharedFlow.emit(AssetListContract.Event.AssetSelection(asset))
        }
    }


    private val _tapEventBackPressed = MutableSharedFlow<AssetPreviewContract.Event>()
    val tapEventBackPressed = _tapEventBackPressed.asSharedFlow()

    fun tapped(backPressed: AssetPreviewContract.Event) {
        CoroutineScope(mainDispatcher).launch {
            _tapEventBackPressed.emit(backPressed)
        }
    }
}