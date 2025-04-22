package com.example.coincap.vm

import com.example.coincap.getListOfAssets
import com.example.coincap.rp.CoincapRepository
import com.example.coincap.util.EventHandler
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@DelicateCoroutinesApi
class AssetListViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val eventHandler = EventHandler(Dispatchers.Main)

    private val coincapRepository = mockk<CoincapRepository>()


    @Test
    fun `when viewmodel initialzed then should emit initial view state`() = runTest {
        // Given
        val initialViewState = AssetListContract.State(
            assets = listOf(),
            isLoading = true,
            isError = false
        )

        // When
        val viewModel = AssetListViewModel(coincapRepository, eventHandler)

        // Then
        assertEquals(initialViewState, viewModel.container.stateFlow.value)
    }

    @Test
    fun `when getAssets called then should emit view state`() = runTest {

        // Given
        val assets = getListOfAssets()
        val viewState = AssetListContract.State(
            assets = assets,
            isLoading = false,
            isError = false
        )
        coEvery { coincapRepository.getAssets() } returns flowOf(Result.success(assets))

        // When
        val viewModel = AssetListViewModel(coincapRepository, eventHandler)


        assertEquals(viewState, viewModel.container.stateFlow.value)

    }
}


