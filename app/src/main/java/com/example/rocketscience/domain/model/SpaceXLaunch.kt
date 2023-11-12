package com.example.rocketscience.domain.model

import com.example.rocketscience.data.network.SpaceXLaunchLinksNetwork
import com.example.rocketscience.data.network.SpaceXLaunchNetwork
import com.example.rocketscience.data.network.SpaceXRocketNetwork
import kotlinx.datetime.Instant

data class SpaceXLaunch(
    val flightNumber: Int,
    val missionName: String,
    val launchDate: Instant,
    val launchSuccess: Boolean = false,
    val rocket: SpaceXRocket,
    val links: SpaceXLaunchLinks,
)

data class SpaceXRocket(
    val name: String,
    val type: String,
)

data class SpaceXLaunchLinks(
    val missionPatch: String?,
    val article: String?,
    val wikipedia: String?,
    val videoLink: String?,
)

fun SpaceXLaunchNetwork.asExternalModel() = SpaceXLaunch(
    flightNumber = flightNumber,
    missionName = missionName,
    launchDate = launchDate,
    launchSuccess = launchSuccess,
    rocket = rocket.asExternalModel(),
    links = links.asExternalModel(),
)

fun SpaceXRocketNetwork.asExternalModel() = SpaceXRocket(
    name = name,
    type = type
)

fun SpaceXLaunchLinksNetwork.asExternalModel() = SpaceXLaunchLinks(
    missionPatch = missionPatch,
    article = article,
    wikipedia = wikipedia,
    videoLink = videoLink
)