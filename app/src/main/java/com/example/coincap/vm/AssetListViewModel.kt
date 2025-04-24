package com.example.coincap.vm

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


/**
 * ViewModel is responsible for collecting data from service layer and provide is to view layer. It also works as a bridge between view and data.
 * HiltViewModel will be consumed by composable functions that consists of constructor injection. ContainerHost hosts current UI state and Side effects.
 * @param coincapRepository instance of repository that provides data.
 * @param eventHandler instance of event handler.
 */

@HiltViewModel
class AssetListViewModel @Inject constructor(
    private val coincapRepository: CoincapRepository,
    private val eventHandler: EventHandler
) : ContainerHost<AssetListContract.State, AssetListContract.Effect>, ViewModel() {

    /**
     * Initial UI state
     */
    override val container =
        container<AssetListContract.State, AssetListContract.Effect>(AssetListContract.State()) {
            collectTap()
        }


    /**
     * Collect item click event as a hot flow.
     */
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


    /**
     * Collect data from the repository and update UI state as well as side effects.
     */
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


    /**
     * Update the data periodically after one minute.
     */
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

    /**
     * Clear the viewmodel scope
     */
    override fun onCleared() {
        super.onCleared()
        updateTask = null
        viewModelScope.cancel()
    }


    /**
     * Starting point of the viewmodel.
     */
    init {
        startUpdateTask()
    }


    companion object {
        private const val TAG = "AssetListViewModel:"
    }

}