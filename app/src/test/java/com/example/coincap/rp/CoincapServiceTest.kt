package com.example.coincap.rp

import com.example.coincap.rp.model.Asset
import com.example.coincap.rp.model.SingleAssetModel
import com.example.coincap.rp.network.ApiClient
import com.example.coincap.rs.CoincapService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CoincapServiceTest {

    private val apiClient = mockk<ApiClient>()
    private val coincapService = CoincapService(apiClient, Dispatchers.IO)

    @Test
    fun `when getAssets called then should call getAssets from API`() = runTest {
        // Given
        val limit = "10"
        val assetList = listOf(Asset())
        coEvery { apiClient.getAssets(limit).data } returns assetList

        // When
        val result = coincapService.getAssets()

        // Then
        assert(result.isSuccess)
        coVerify(exactly = 1) { apiClient.getAssets(limit).data }
        confirmVerified(apiClient)
    }


    @Test
    fun `given an exception when getAssets called then return failure`() = runTest {
        // Given
        val limit = "10"
        coEvery { apiClient.getAssets(limit) } throws Exception("")

        // When
        val result = coincapService.getAssets()

        // Then
        assert(result.isFailure)
    }


    @Test
    fun `when getAsset called then should call getAsset from API`() = runTest {
        // Given
        val id = "bitcoin"
        coEvery { apiClient.getAsset(any()) } returns SingleAssetModel(Asset(), 1245454)

        // When
        val result = coincapService.getAsset(id)

        // Then
        assert(result.isSuccess)
        coVerify(exactly = 1) { apiClient.getAsset(id) }
        confirmVerified(apiClient)
    }


    @Test
    fun `given an exception when getAsset called then return failure`() = runTest {
        // Given
        val id = "bitcoin"
        coEvery { apiClient.getAsset(id) } throws Exception("")

        // When
        val result = coincapService.getAsset(id)

        // Then
        assert(result.isFailure)
    }


}