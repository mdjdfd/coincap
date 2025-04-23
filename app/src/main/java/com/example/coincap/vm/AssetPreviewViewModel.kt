package com.example.coincap.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.util.EventHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
import kotlin.time.Duration.Companion.minutes

@HiltViewModel(assistedFactory = AssetPreviewViewModel.AssetPreviewViewModelFactory::class)
class AssetPreviewViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val coincapRepository: CoincapRepository,
    private val eventHandler: EventHandler
) : ContainerHost<AssetPreviewContract.State, AssetPreviewContract.Effect>, ViewModel() {


    override val container =
        container<AssetPreviewContract.State, AssetPreviewContract.Effect>(
            AssetPreviewContract.State(
                null
            )
        ) {
            collectTap()
        }


    private fun collectTap() = intent {
        viewModelScope.launch {
            eventHandler.tapEventBackPressed.collectLatest {
                when (it) {
                    is AssetPreviewContract.Event.BackPressed -> postSideEffect(AssetPreviewContract.Effect.Back.ToList)
                }
            }
        }
    }


    fun collectAssetDetails() = intent {
        viewModelScope.launch {
            reduce {
                state.copy(isLoading = true, isError = false)
            }
            coincapRepository.getAsset(id).collect {
                it.onSuccess {
                    reduce {
                        state.copy(asset = it, isLoading = false)
                    }
                    postSideEffect(AssetPreviewContract.Effect.Loaded)
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
                collectAssetDetails()
                delay(1.minutes)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        updateTask = null
        viewModelScope.cancel()
    }

    @AssistedFactory
    interface AssetPreviewViewModelFactory {
        fun create(id: String): AssetPreviewViewModel
    }


    init {
        startUpdateTask()
    }

    companion object {
        private const val TAG = "AssetPreviewViewModel:"
    }

}