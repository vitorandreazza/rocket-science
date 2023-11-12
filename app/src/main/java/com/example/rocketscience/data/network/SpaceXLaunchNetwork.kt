package com.example.rocketscience.data.network

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceXLaunchNetwork(
    @SerialName("flight_number") val flightNumber: Int,
    @SerialName("mission_name") val missionName: String,
    @SerialName("launch_date_utc") val launchDate: Instant,
    @SerialName("launch_success") val launchSuccess: Boolean = false,
    @SerialName("rocket") val rocket: SpaceXRocketNetwork,
    @SerialName("links") val links: SpaceXLaunchLinksNetwork,
)

@Serializable
data class SpaceXRocketNetwork(
    @SerialName("rocket_name") val name: String,
    @SerialName("rocket_type") val type: String,
)

@Serializable
data class SpaceXLaunchLinksNetwork(
    @SerialName("mission_patch_small") val missionPatch: String?,
    @SerialName("article_link") val article: String?,
    @SerialName("wikipedia") val wikipedia: String?,
    @SerialName("video_link") val videoLink: String?,
)