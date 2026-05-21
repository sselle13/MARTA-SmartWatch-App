package com.example.marta_cobblinc_app.presentation


data class TransitState(
    val loading: Boolean = true,
    val error: String? = null,
    val system: String? = null,
    val routeName: String? = null,
    val stopName: String? = null,
    val arrivalTime: String? = null,
    val timeLeftSeconds: Int? = null,
    val effectiveLeftSeconds: Int? = null,
    val urgency: String? = null
)
