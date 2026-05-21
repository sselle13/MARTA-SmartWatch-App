package com.example.marta_cobblinc_app.presentation


import okhttp3.OkHttpClient
import okhttp3.Request

object ApiClient {
    private val client = OkHttpClient()

    fun get(url: String): String? {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) return null
            return resp.body?.string()
        }
    }
}
