package com.example.rocketscience

import com.example.rocketscience.data.SpaceXRepository
import com.example.rocketscience.domain.GetSpaceXLaunchesUseCase
import com.example.rocketscience.domain.SpaceXLaunchesFilter
import com.example.rocketscience.utils.TestData.launches
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetSpaceXLaunchesUseCaseTest {

    private val spaceXRepository = Mockito.mock(SpaceXRepository::class.java)
    private val useCase = GetSpaceXLaunchesUseCase(spaceXRepository)

    @Before
    fun setup() {
        `when`(spaceXRepository.getLaunches()).thenReturn(flowOf(launches))
    }

    @Test
    fun `when year filtered assert launches year within the range`() =
        runTest(UnconfinedTestDispatcher()) {
            val year = 2012
            val filter = SpaceXLaunchesFilter(
                yearRange = year to year,
                launchSuccessful = false,
                ascOrder = true
            )
            val filteredLaunches = useCase(filter)

            assertTrue(filteredLaunches.first().all {
                val launchDate = it.launchDate.toLocalDateTime(TimeZone.UTC)
                launchDate.year in year..year
            })
        }

    @Test
    fun `when launches successful filtered assert all launches are successful`() =
        runTest {
            val filter = SpaceXLaunchesFilter(
                yearRange = 2002 to 2023,
                launchSuccessful = true,
                ascOrder = true
            )
            val filteredLaunches = useCase(filter)

            assertTrue(filteredLaunches.first().all { it.launchSuccess })
        }

    @Test
    fun `when launches desc order assert launches are desc order`() =
        runTest {
            val filter = SpaceXLaunchesFilter(
                yearRange = 2002 to 2023,
                launchSuccessful = false,
                ascOrder = false
            )
            val filteredLaunches = useCase(filter)

            assertEquals(launches.asReversed(), filteredLaunches.first())
        }
}