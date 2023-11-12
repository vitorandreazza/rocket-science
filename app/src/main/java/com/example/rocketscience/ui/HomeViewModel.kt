package com.example.rocketscience.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocketscience.data.SpaceXRepository
import com.example.rocketscience.data.result.Result
import com.example.rocketscience.data.result.asResult
import com.example.rocketscience.domain.GetSpaceXLaunchesUseCase
import com.example.rocketscience.domain.SpaceXLaunchesFilter
import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val LAUNCHES_FILTER = "launchesFilter"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    spaceXRepository: SpaceXRepository,
    private val getSpaceXLaunchesUseCase: GetSpaceXLaunchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val launchesFilter =
        savedStateHandle.getStateFlow<SpaceXLaunchesFilter?>(LAUNCHES_FILTER, null)

    private val launches: Flow<List<SpaceXLaunch>> =
        getSpaceXLaunchesUseCase().flatMapLatest {
            launchesFilter.flatMapLatest { filter ->
                getSpaceXLaunchesUseCase(filter)
            }
        }

    val homeUiState: StateFlow<HomeUiState> = homeUiState(spaceXRepository)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    private fun homeUiState(spaceXRepository: SpaceXRepository): Flow<HomeUiState> {
        return combine(spaceXRepository.getCompanyInfo(), launches, ::Pair)
            .asResult()
            .map { spaceXInfoResult ->
                when (spaceXInfoResult) {
                    is Result.Success -> {
                        val (companyInfo, launches) = spaceXInfoResult.data
                        HomeUiState.Success(companyInfo, launches)
                    }

                    is Result.Error -> HomeUiState.Error
                    is Result.Loading -> HomeUiState.Loading
                }
            }
    }

    fun filterLaunches(launchesFilter: SpaceXLaunchesFilter) {
        savedStateHandle[LAUNCHES_FILTER] = launchesFilter
    }
}

sealed interface HomeUiState {
    data class Success(
        val companyInfo: SpaceXCompanyInfo,
        val launches: List<SpaceXLaunch>,
    ) : HomeUiState

    data object Error : HomeUiState
    data object Loading : HomeUiState
}