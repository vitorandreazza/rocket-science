package com.example.rocketscience

import androidx.lifecycle.SavedStateHandle
import com.example.rocketscience.utils.TestData.companyInfo
import com.example.rocketscience.utils.TestData.launches
import com.example.rocketscience.data.SpaceXRepository
import com.example.rocketscience.domain.GetSpaceXLaunchesUseCase
import com.example.rocketscience.ui.HomeUiState
import com.example.rocketscience.ui.HomeViewModel
import com.example.rocketscience.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val spaceXRepository = mock(SpaceXRepository::class.java)
    private val getSpaceXLaunchesUseCase = GetSpaceXLaunchesUseCase(spaceXRepository)

    private lateinit var viewModel: HomeViewModel

    private fun instantiateViewModel() {
        viewModel = HomeViewModel(
            spaceXRepository = spaceXRepository,
            getSpaceXLaunchesUseCase = getSpaceXLaunchesUseCase,
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `when viewmodel init assert state is loading`() = runTest {
        instantiateViewModel()

        assertEquals(HomeUiState.Loading, viewModel.homeUiState.value)
    }

    @Test
    fun `when api fail assert state is error`() = runTest {
        `when`(spaceXRepository.getCompanyInfo()).thenReturn(flow { throw IOException("API failed") })
        instantiateViewModel()

        val job = launch(UnconfinedTestDispatcher()) { viewModel.homeUiState.collect() }

        assertEquals(HomeUiState.Error, viewModel.homeUiState.value)

        job.cancel()
    }

    @Test
    fun `when api success assert state is success`() = runTest {
        `when`(spaceXRepository.getCompanyInfo()).thenReturn(flowOf(companyInfo))
        `when`(spaceXRepository.getLaunches()).thenReturn(flowOf(launches))
        instantiateViewModel()

        val job = launch(UnconfinedTestDispatcher()) { viewModel.homeUiState.collect() }

        val result = viewModel.homeUiState.value
        assertTrue(result is HomeUiState.Success)

        assertEquals(companyInfo, result.companyInfo)
        assertEquals(launches, result.launches)

        job.cancel()
    }
}