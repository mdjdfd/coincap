package com.example.coincap.rp

import com.example.coincap.getListOfAssets
import com.example.coincap.rp.model.Asset
import com.example.coincap.rp.model.AssetListModel
import com.example.coincap.rp.model.SingleAssetModel
import com.example.coincap.rp.network.ApiClient
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClientTest {

    private val mockWebServer = MockWebServer()



    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val apiBuilder = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(ApiClient::class.java)


    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `test api status code ok when fetching specified number of assets`(){
        // Given
        val limit = "3"
        mockWebServer.enqueResponse(
            fileName = "asset_list.json",
            code = 200
        )
        val expected = AssetListModel(
            data = getListOfAssets(),
            timestamp = 1744916573918
        )

        // When
        val actual = runBlocking { apiBuilder.getAssets(limit) }
        val request = mockWebServer.takeRequest()

        // Then
        assertEquals(expected, actual)
        assertEquals("/v3/assets?limit=$limit", request.path)
    }

    @Test
    fun `test api status code ok when fetching asset by id`(){
        // Given
        val id = "bitcoin"
        mockWebServer.enqueResponse(
            fileName = "asset_details.json",
            code = 200
        )
        val expected = SingleAssetModel(
            data = getListOfAssets()[0],
            timestamp = 1744984436494
        )

        // When
        val actual = runBlocking { apiBuilder.getAsset(id) }
        val request = mockWebServer.takeRequest()

        // Then
        assertEquals(expected, actual)
        assertEquals("/v3/assets/$id", request.path)
    }

}
