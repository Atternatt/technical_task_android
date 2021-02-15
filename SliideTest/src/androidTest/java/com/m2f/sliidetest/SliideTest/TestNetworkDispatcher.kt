package com.m2f.sliidetest.SliideTest

import com.m2f.sliidetest.SliideTest.helpers.readFile
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

object TestNetworkDispatcher: Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        val response = MockResponse().apply {
            setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        }

        if(request.path?.contains("/users") == true) {
            val file  = when(request.method) {
                "DELETE" -> "delete_user_ok_response.json"
                "GET" -> "users_response_ok.json"
                "POST" -> "create_user_response.json"
                else -> null
            }

            if(file != null) {
                response.setResponseCode(HttpURLConnection.HTTP_OK)
                response.setBody(readFile(file))
            }
        }

        return response
    }
}