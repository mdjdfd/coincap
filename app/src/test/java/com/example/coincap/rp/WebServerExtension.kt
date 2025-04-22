package com.example.coincap.rp

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

internal fun MockWebServer.enqueResponse(fileName: String, code: Int){
    val inputStream = javaClass.classLoader?.getResourceAsStream("response/$fileName")

    val bufferedSource = inputStream?.let { inputStream.source().buffer() }
    bufferedSource?.let {
        enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(bufferedSource.readString(StandardCharsets.UTF_8))
        )
    }
}