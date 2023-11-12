package com.example.rocketscience.data

import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import kotlinx.coroutines.flow.Flow

interface SpaceXRepository {

    fun getCompanyInfo(): Flow<SpaceXCompanyInfo>

    fun getLaunches(): Flow<List<SpaceXLaunch>>
}