package com.example.rocketscience

import com.example.rocketscience.data.DefaultSpaceXRepository
import com.example.rocketscience.data.network.SpaceXApi
import com.example.rocketscience.utils.TestData.launchesNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultSpaceXRepositoryTest {

    private val spaceXApi = mock(SpaceXApi::class.java)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: DefaultSpaceXRepository

    @Before
    fun setup() = runTest {
        `when`(spaceXApi.getLaunches()).thenReturn(launchesNetwork)

        repository = DefaultSpaceXRepository(
            spaceXApi = spaceXApi,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `assert get launches are being cached`() = runTest {
        val launches1 = repository.getLaunches().first()
        val launches2 = repository.getLaunches().first()

        verify(spaceXApi, times(1)).getLaunches()
        assertEquals(launches1, launches2)
    }
}