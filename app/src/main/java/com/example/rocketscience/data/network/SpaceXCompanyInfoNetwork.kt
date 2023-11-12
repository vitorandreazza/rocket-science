package com.example.rocketscience.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceXCompanyInfoNetwork(
    @SerialName("name") val name: String,
    @SerialName("founder") val founder: String,
    @SerialName("founded") val founded: Int,
    @SerialName("employees") val employees: Int,
    @SerialName("launch_sites") val launchSites: Int,
    @SerialName("valuation") val valuation: Long,
)
