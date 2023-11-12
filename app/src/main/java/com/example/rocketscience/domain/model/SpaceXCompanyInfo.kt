package com.example.rocketscience.domain.model

import com.example.rocketscience.data.network.SpaceXCompanyInfoNetwork

data class SpaceXCompanyInfo(
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val launchSites: Int,
    val valuation: Long,
)

fun SpaceXCompanyInfoNetwork.asExternalModel() = SpaceXCompanyInfo(
    name = name,
    founder = founder,
    founded = founded,
    employees = employees,
    launchSites = launchSites,
    valuation = valuation
)