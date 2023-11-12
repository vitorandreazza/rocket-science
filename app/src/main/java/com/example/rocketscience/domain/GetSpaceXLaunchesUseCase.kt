package com.example.rocketscience.domain

import android.os.Parcelable
import com.example.rocketscience.data.SpaceXRepository
import com.example.rocketscience.domain.model.SpaceXLaunch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class GetSpaceXLaunchesUseCase @Inject constructor(
    private val spaceXRepository: SpaceXRepository
) {
    operator fun invoke(filter: SpaceXLaunchesFilter? = null): Flow<List<SpaceXLaunch>> {
        return spaceXRepository.getLaunches()
            .map { launches ->
                if (filter == null) {
                    launches
                } else {
                    launches
                        .run { if (filter.launchSuccessful) filter { it.launchSuccess } else this }
                        .filter {
                            val launchDate = it.launchDate.toLocalDateTime(TimeZone.UTC)
                            launchDate.year in filter.yearRange.first..filter.yearRange.second
                        }
                        .run { if (!filter.ascOrder) asReversed() else this }
                }
            }
    }
}

@Parcelize
data class SpaceXLaunchesFilter(
    val yearRange: Pair<Int, Int>,
    val launchSuccessful: Boolean,
    val ascOrder: Boolean
): Parcelable