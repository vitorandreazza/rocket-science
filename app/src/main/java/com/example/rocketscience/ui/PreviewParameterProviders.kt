package com.example.rocketscience.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import com.example.rocketscience.domain.model.SpaceXLaunchLinks
import com.example.rocketscience.domain.model.SpaceXRocket
import com.example.rocketscience.ui.PreviewParameterData.companyInfo
import com.example.rocketscience.ui.PreviewParameterData.launches
import kotlinx.datetime.Instant

class SpaceXParameterProvider : PreviewParameterProvider<Pair<SpaceXCompanyInfo, List<SpaceXLaunch>>> {
    override val values: Sequence<Pair<SpaceXCompanyInfo, List<SpaceXLaunch>>> =
        sequenceOf(companyInfo to launches)
}

object PreviewParameterData {
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
            launchDate = Instant.fromEpochSeconds(1143239400),
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
            launchDate = Instant.fromEpochSeconds(1174439400),
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
}