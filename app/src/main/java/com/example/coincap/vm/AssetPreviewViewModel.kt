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

/**
 * ViewModel is responsible for collecting data from service layer and provide is to view layer. It also works as a bridge between view and data.
 * HiltViewModel will be consumed by composable functions that consists of constructor injection. ContainerHost hosts current UI state and Side effects.
 * @id id of the selected item.
 * @param coincapRepository instance of repository that provides data.
 * @param eventHandler instance of event handler.
 */

@HiltViewModel(assistedFactory = AssetPreviewViewModel.AssetPreviewViewModelFactory::class)
class AssetPreviewViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val coincapRepository: CoincapRepository,
    private val eventHandler: EventHandler
) : ContainerHost<AssetPreviewContract.State, AssetPreviewContract.Effect>, ViewModel() {


    /**
     * Initial UI state
     */
    override val container =
        container<AssetPreviewContract.State, AssetPreviewContract.Effect>(
            AssetPreviewContract.State(
                null
            )
        ) {
            collectTap()
        }


    /**
     * Collect item click event as a hot flow.
     */
    private fun collectTap() = intent {
        viewModelScope.launch {
            eventHandler.tapEventBackPressed.collectLatest {
                when (it) {
                    is AssetPreviewContract.Event.BackPressed -> postSideEffect(AssetPreviewContract.Effect.Back.ToList)
                }
            }
        }
    }


    /**
     * Collect data from the repository and update UI state as well as side effects.
     */
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


    /**
     * Update the data periodically after one minute.
     */
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

    /**
     * Clear the viewmodel scope
     */
    override fun onCleared() {
        super.onCleared()
        updateTask = null
        viewModelScope.cancel()
    }

    /**
     * Interface that provides item id from the JetpackCompose to Viewmodel as a result of user click.
     */
    @AssistedFactory
    interface AssetPreviewViewModelFactory {
        fun create(id: String): AssetPreviewViewModel
    }


    /**
     * Starting point of the viewmodel.
     */
    init {
        startUpdateTask()
    }

    companion object {
        private const val TAG = "AssetPreviewViewModel:"
    }

}