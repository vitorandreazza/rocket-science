package com.example.rocketscience.utils

import com.example.rocketscience.data.network.SpaceXLaunchLinksNetwork
import com.example.rocketscience.data.network.SpaceXLaunchNetwork
import com.example.rocketscience.data.network.SpaceXRocketNetwork
import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import com.example.rocketscience.domain.model.SpaceXLaunchLinks
import com.example.rocketscience.domain.model.SpaceXRocket
import kotlinx.datetime.Instant

object TestData {
    val companyInfo = SpaceXCompanyInfo(
        name = "SpaceX",
        founder = "Elon Musk",
        founded = 2002,
        employees = 7000,
        launchSites = 3,
        valuation = 15000000000
    )

    val launches = listOf(
        SpaceXLaunch(
            flightNumber = 1,
            missionName = "FalconSat",
            launchDate = Instant.parse("2006-03-24T22:30:00.000Z"),
            launchSuccess = false,
            rocket = SpaceXRocket(
                name = "Falcon 1",
                type = "Merlin A"
            ),
            links = SpaceXLaunchLinks(
                missionPatch = null,
                article = null,
                wikipedia = null,
                videoLink = null
            )
        ),
        SpaceXLaunch(
            flightNumber = 2,
            missionName = "DemoSat",
            launchDate = Instant.parse("2012-03-24T22:30:00.000Z"),
            launchSuccess = true,
            rocket = SpaceXRocket(
                name = "Falcon 2",
                type = "Merlin B"
            ),
            links = SpaceXLaunchLinks(
                missionPatch = null,
                article = null,
                wikipedia = null,
                videoLink = null
            )
        ),
    )

    val launchesNetwork = listOf(
        SpaceXLaunchNetwork(
            flightNumber = 1,
            missionName = "FalconSat",
            launchDate = Instant.parse("2006-03-24T22:30:00.000Z"),
            launchSuccess = false,
            rocket = SpaceXRocketNetwork(
                name = "Falcon 1",
                type = "Merlin A"
            ),
            links = SpaceXLaunchLinksNetwork(
                missionPatch = null,
                article = null,
                wikipedia = null,
                videoLink = null
            )
        ),
        SpaceXLaunchNetwork(
            flightNumber = 2,
            missionName = "DemoSat",
            launchDate = Instant.parse("2012-03-24T22:30:00.000Z"),
            launchSuccess = true,
            rocket = SpaceXRocketNetwork(
                name = "Falcon 2",
                type = "Merlin B"
            ),
            links = SpaceXLaunchLinksNetwork(
                missionPatch = null,
                article = null,
                wikipedia = null,
                videoLink = null
            )
        ),
    )
}