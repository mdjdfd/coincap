package com.example.coincap.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.util.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes


@HiltViewModel
class AssetListViewModel @Inject constructor(
    private val coincapRepository: CoincapRepository,
    private val eventHandler: EventHandler
) : ContainerHost<AssetListContract.State, AssetListContract.Effect>, ViewModel() {

    override val container =
        container<AssetListContract.State, AssetListContract.Effect>(AssetListContract.State()) {
            collectTap()
        }


    private fun collectTap() = intent {
        viewModelScope.launch {
            eventHandler.tapEventSharedFlow.collectLatest {
                when (it) {
                    is AssetListContract.Event.AssetSelection -> postSideEffect(
                        AssetListContract.Effect.Preview.ToPreview(
                            it.asset.id
                        )
                    )
                }
            }
        }
    }


    fun collectAssets() = intent {
        viewModelScope.launch {
            reduce {
                state.copy(isLoading = true, isError = false)
            }

            coincapRepository.getAssets().collect {
                it.onSuccess {
                    reduce {
                        state.copy(assets = it, isLoading = false)
                    }
                    postSideEffect(AssetListContract.Effect.Loaded)
                }.onFailure {
                    reduce {
                        state.copy(isError = true, isLoading = false)
                    }
                }
            }
        }
    }


    private var updateTask: Job? = null
    private fun startUpdateTask() {
        updateTask?.cancel()
        updateTask = viewModelScope.launch {
            while (true) {
                collectAssets()
                delay(1.minutes)
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        updateTask = null
        viewModelScope.cancel()
    }


    init {
        startUpdateTask()
    }


    companion object {
        private const val TAG = "AssetListViewModel:"
    }

}