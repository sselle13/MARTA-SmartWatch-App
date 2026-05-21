package com.example.marta_cobblinc_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@JsonClass(generateAdapter = true)
data class BackendResponse(
    val system: String?,
    val routeName: String?,
    val stopName: String?,
    val stopId: String?,
    val arrivalTime: String?,
    val timeLeftSeconds: Int?,
    val walkingTimeSeconds: Int?,
    val effectiveLeftSeconds: Int?,
    val urgency: String?,
    val error: String? = null
)

class TransitViewModel : ViewModel() {

    private val _state = MutableStateFlow(TransitState())
    val state = _state.asStateFlow()

    // TODO: replace with your machine's LAN IP
    private val baseUrl = "http://192.168.1.24:3000"
    // Choose one:
    private val query = "?station=Lindbergh%20Center"
    // or: private val query = "?stopId=1234"

    private val moshi = Moshi.Builder().build()
    private val adapter = moshi.adapter(BackendResponse::class.java)

    init {
        viewModelScope.launch {
            while (true) {
                fetchOnce()
                delay(10_000) // refresh every 10s
            }
        }
    }

    private fun fetchOnce() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val url = "$baseUrl/next-arrival$query"
                val body = ApiClient.get(url)
                if (body == null) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = "No response"
                    )
                    return@launch
                }

                val resp = adapter.fromJson(body)
                if (resp == null || resp.error != null) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = resp?.error ?: "Parse error"
                    )
                    return@launch
                }

                _state.value = TransitState(
                    loading = false,
                    error = null,
                    system = resp.system,
                    routeName = resp.routeName,
                    stopName = resp.stopName ?: resp.stopId,
                    arrivalTime = resp.arrivalTime,
                    timeLeftSeconds = resp.timeLeftSeconds,
                    effectiveLeftSeconds = resp.effectiveLeftSeconds,
                    urgency = resp.urgency
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
