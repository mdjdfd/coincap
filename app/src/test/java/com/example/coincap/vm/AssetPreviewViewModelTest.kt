package com.example.coincap.vm

import app.cash.turbine.test
import com.example.coincap.getListOfAssets
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.util.EventHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@DelicateCoroutinesApi
class AssetPreviewViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val eventHandler = EventHandler(Dispatchers.Main)

    private val coincapRepository = mockk<CoincapRepository>(relaxed = true)



    @Before
    fun setUp(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `when viewmodel initialized then should emit initial view state`() = runTest {
        // Given
        val id = "bitcoin"
        val initialViewState = AssetPreviewContract.State(
            asset = null,
            isLoading = true,
            isError = false
        )

        // When
        val viewModel = AssetPreviewViewModel(id, coincapRepository, eventHandler)

        // Then
        assertEquals(initialViewState, viewModel.container.stateFlow.value)
    }

    @Test
    fun `when getAsset called then should emit view state`() = runTest {
        // Given
        val id = "bitcoin"
        val asset = getListOfAssets()[0]
        val viewState = AssetPreviewContract.State(
            asset = asset,
            isLoading = false,
            isError = false
        )
        coEvery { coincapRepository.getAsset(id) } returns flowOf(Result.success(viewState.asset!!))

        // When
        val viewModel = AssetPreviewViewModel(id, coincapRepository, eventHandler)

        backgroundScope.launch {
            viewModel.collectAssetDetails()
        }

        // Then
        viewModel.container.stateFlow.test {
            assertEquals(viewModel.container.stateFlow.value, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}