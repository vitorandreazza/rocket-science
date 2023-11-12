package com.example.rocketscience.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindsSpaceXRepository(spaceXRepository: DefaultSpaceXRepository): SpaceXRepository
}