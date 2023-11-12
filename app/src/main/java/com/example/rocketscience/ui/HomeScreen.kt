@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.rocketscience.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.rocketscience.R
import com.example.rocketscience.domain.SpaceXLaunchesFilter
import com.example.rocketscience.domain.model.SpaceXCompanyInfo
import com.example.rocketscience.domain.model.SpaceXLaunch
import com.example.rocketscience.domain.model.SpaceXLaunchLinks
import com.example.rocketscience.ui.theme.RocketScienceTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.math.abs

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    HomeScreen(
        homeUiState = homeUiState,
        onFilterLaunches = viewModel::filterLaunches
    )
}

@Composable
fun HomeScreen(homeUiState: HomeUiState, onFilterLaunches: (SpaceXLaunchesFilter) -> Unit) {
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        RocketScienceAppBar(onFilterClick = { showFilterDialog = true })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (homeUiState) {
                is HomeUiState.Success -> {
                    FilterDialog(
                        companyFoundation = homeUiState.companyInfo.founded,
                        showFilterDialog = showFilterDialog,
                        onDismiss = { showFilterDialog = false },
                        onFilterLaunches = {
                            showFilterDialog = false
                            onFilterLaunches(it)
                        }
                    )

                    Column {
                        CompanyInfo(homeUiState.companyInfo)
                        LaunchesList(homeUiState.launches)
                    }
                }

                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.api_error_msg),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(80.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            trackColor = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RocketScienceAppBar(onFilterClick: () -> Unit) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.spacex))
            }
        },
        actions = {
            IconButton(onClick = { onFilterClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun CompanyInfo(
    companyInfo: SpaceXCompanyInfo,
    modifier: Modifier = Modifier,
) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.company).uppercase(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
        with(companyInfo) {
            Text(
                text = stringResource(
                    R.string.company_info,
                    name,
                    founder,
                    founded,
                    employees,
                    launchSites,
                    valuation.compactNumberFormat(Locale.US)
                ),
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun LaunchesList(
    launches: List<SpaceXLaunch>,
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.launches).uppercase(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
        var showLaunchLinksIndex by rememberSaveable { mutableIntStateOf(-1) }
        if (launches.isEmpty()) {
            LaunchesEmpty()
        } else {
            LazyColumn(
                modifier = modifier,
            ) {
                itemsIndexed(items = launches) { index, launch ->
                    LaunchItem(launch = launch, onLaunchClick = { showLaunchLinksIndex = index })
                }
            }
            val uriHandler = LocalUriHandler.current
            if (showLaunchLinksIndex != -1) {
                LaunchLinksDialog(
                    links = launches[showLaunchLinksIndex].links,
                    onDismiss = { showLaunchLinksIndex = -1 },
                    onLinkClick = { url -> uriHandler.openUri(url) }
                )
            }
        }
    }
}

@Composable
fun LaunchItem(
    launch: SpaceXLaunch,
    onLaunchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clickable { onLaunchClick() }
    ) {
        LaunchImage(launch.links.missionPatch)
        Column(modifier = Modifier
            .weight(1f)
            .padding(8.dp)) {
            LaunchInfo(launch)
        }
        Icon(
            painter = painterResource(
                if (launch.launchSuccess) R.drawable.ic_check else R.drawable.ic_close
            ),
            contentDescription = stringResource(
                if (launch.launchSuccess) R.string.successful_launch else R.string.unsuccessful_launch
            ),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(48.dp)
        )
    }
}

@Composable
fun LaunchImage(imageUrl: String?, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = imageUrl,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        },
        contentDescription = stringResource(R.string.mission_patch_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(50.dp, 50.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun LaunchInfo(launch: SpaceXLaunch) {
    val launchDate = launch.launchDate.formatDate(FormatStyle.SHORT)
    val launchTime = launch.launchDate.formatTime(FormatStyle.SHORT)
    val days = launch.launchDate.daysUntil(Clock.System.now(), TimeZone.UTC)

    Text(text = stringResource(R.string.launch_mission_name, launch.missionName))
    Text(text = stringResource(R.string.launch_date, launchDate, launchTime))
    Text(text = stringResource(R.string.launch_rocket, launch.rocket.name, launch.rocket.type))
    Text(
        text = stringResource(
            if (days < 0) R.string.launch_days_since else R.string.launch_days_since,
            abs(days)
        )
    )
}

@Composable
private fun LaunchesEmpty(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.empty_launches),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun FilterDialog(
    companyFoundation: Int,
    showFilterDialog: Boolean,
    onDismiss: () -> Unit,
    onFilterLaunches: (SpaceXLaunchesFilter) -> Unit
) {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
    val yearMaxRange = companyFoundation.toFloat()..currentYear.toFloat()
    var yearRange by rememberSaveable { mutableStateOf(yearMaxRange.start to yearMaxRange.endInclusive) }
    var launchSuccess by rememberSaveable { mutableStateOf(false) }
    var ascOrder by rememberSaveable { mutableStateOf(true) }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = stringResource(R.string.filter_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            text = {
                Divider()
                Column {
                    RangeSlider(
                        value = yearRange.first..yearRange.second,
                        steps = (yearMaxRange.endInclusive - yearMaxRange.start).toInt(),
                        valueRange = yearMaxRange,
                        onValueChange = { yearRange = it.start to it.endInclusive },
                    )
                    Text(
                        text = stringResource(
                            R.string.filter_year_range,
                            yearRange.first.toInt(),
                            yearRange.second.toInt()
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.filter_launch_success),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Switch(checked = launchSuccess, onCheckedChange = { launchSuccess = it })
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectableGroup()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(text = stringResource(R.string.asc_order))
                            RadioButton(selected = ascOrder, onClick = { ascOrder = true })
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(text = stringResource(R.string.desc_order))
                            RadioButton(selected = !ascOrder, onClick = { ascOrder = false })
                        }
                    }
                }
            },
            confirmButton = {
                Text(
                    text = stringResource(R.string.ok).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onFilterLaunches(
                                SpaceXLaunchesFilter(
                                    yearRange.first.toInt() to yearRange.second.toInt(),
                                    launchSuccess,
                                    ascOrder
                                )
                            )
                        },
                )
            }
        )
    }
}

@Composable
fun LaunchLinksDialog(
    links: SpaceXLaunchLinks,
    onDismiss: () -> Unit,
    onLinkClick: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.launch_links_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Divider()
                links.article?.let {
                    LaunchLinkItem(
                        description = stringResource(R.string.launch_link_article),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLinkClick(it) }
                            .padding(vertical = 16.dp)
                    )
                }
                links.wikipedia?.let {
                    LaunchLinkItem(
                        description = stringResource(R.string.launch_link_wikipedia),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLinkClick(it) }
                            .padding(vertical = 16.dp)
                    )
                }
                links.videoLink?.let {
                    LaunchLinkItem(
                        description = stringResource(R.string.launch_link_video),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLinkClick(it) }
                            .padding(vertical = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.close).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        }
    )
}

@Composable
fun LaunchLinkItem(description: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = description)
        Icon(
            painter = painterResource(id = R.drawable.ic_link),
            contentDescription = stringResource(R.string.link_description, description),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(SpaceXParameterProvider::class)
    spaceXInfo: Pair<SpaceXCompanyInfo, List<SpaceXLaunch>>
) {
    val (companyInfo, launches) = spaceXInfo
    RocketScienceTheme {
        HomeScreen(
            homeUiState = HomeUiState.Success(companyInfo, launches),
            onFilterLaunches = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenEmptyLaunchesPreview(
    @PreviewParameter(SpaceXParameterProvider::class)
    spaceXInfo: Pair<SpaceXCompanyInfo, List<SpaceXLaunch>>
) {
    val (companyInfo, _) = spaceXInfo
    RocketScienceTheme {
        HomeScreen(
            homeUiState = HomeUiState.Success(companyInfo, emptyList()),
            onFilterLaunches = {}
        )
    }
}