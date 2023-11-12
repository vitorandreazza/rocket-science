package com.example.rocketscience.data

import com.example.rocketscience.data.dispatchers.Dispatcher
import com.example.rocketscience.data.dispatchers.RocketScienceDispatchers.IO
import com.example.rocketscience.data.network.SpaceXApi
import com.example.rocketscience.data.network.SpaceXLaunchNetwork
import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import com.example.rocketscience.domain.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class DefaultSpaceXRepository @Inject constructor(
    private val spaceXApi: SpaceXApi,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : SpaceXRepository {

    private val launchesMutex = Mutex()
    private var launches: List<SpaceXLaunchNetwork> = emptyList()

    override fun getCompanyInfo(): Flow<SpaceXCompanyInfo> = flow {
        emit(spaceXApi.getCompanyInfo().asExternalModel())
    }.flowOn(ioDispatcher)

    override fun getLaunches(): Flow<List<SpaceXLaunch>> = flow {
        if (launches.isEmpty()) {
            val launchesResponse = spaceXApi.getLaunches()
            launchesMutex.withLock { launches = launchesResponse }
        }
        emit(launchesMutex.withLock { launches.map(SpaceXLaunchNetwork::asExternalModel) })
    }.flowOn(ioDispatcher)
}