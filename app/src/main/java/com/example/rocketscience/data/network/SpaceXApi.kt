package com.example.rocketscience.data.network

import retrofit2.http.GET

interface SpaceXApi {

    @GET("info")
    suspend fun getCompanyInfo(): SpaceXCompanyInfoNetwork

    @GET("launches")
    suspend fun getLaunches(): List<SpaceXLaunchNetwork>
}