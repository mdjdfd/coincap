package com.example.coincap.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.rp.model.buildAssetPreview
import com.example.coincap.util.EventHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel(assistedFactory = AssetPreviewViewModel.AssetPreviewViewModelFactory::class)
class AssetPreviewViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val coincapRepository: CoincapRepository,
    private val eventHandler: EventHandler
) : ContainerHost<AssetPreviewContract.State, AssetPreviewContract.Effect>, ViewModel() {


    override val container =
        container<AssetPreviewContract.State, AssetPreviewContract.Effect>(AssetPreviewContract.State()){
            collectTap()
        }


    private fun collectTap() = intent {
        viewModelScope.launch {
            eventHandler.tapEventBackPressed.collectLatest {
                when(it){
                    is AssetPreviewContract.Event.BackPressed -> postSideEffect(AssetPreviewContract.Effect.Back.ToList)
                }
            }
        }
    }


    private fun collectAssetDetails() = intent {
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

                // Test only
                reduce {
                    state.copy(asset = buildAssetPreview(), isLoading = false)
                }
                postSideEffect(AssetPreviewContract.Effect.Loaded)
                //

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    @AssistedFactory
    interface AssetPreviewViewModelFactory{
        fun create(id: String): AssetPreviewViewModel
    }


    init {
        intent {
            collectAssetDetails()
        }
    }

    companion object{
        private const val TAG = "AssetPreviewViewModel:"
    }

}