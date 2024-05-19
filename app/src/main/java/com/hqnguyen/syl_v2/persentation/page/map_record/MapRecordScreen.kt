package com.hqnguyen.syl_v2.persentation.page.map_record

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.ui.theme.SYLTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

const val TAG = "MapRecordScreen"

private var isFirst = true

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapRecordScreen(
    viewModel: MapRecordViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val mapUiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var cardVisible by remember { mutableStateOf(true) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = true, block = {
        Log.d(TAG, "launchMultiplePermissionRequest")
        locationPermissionsState.launchMultiplePermissionRequest()
    })

    val mapViewportState = rememberMapViewportState {
        // Set the initial camera position
        setCameraOptions {
            center(Point.fromLngLat(0.0, 0.0))
            zoom(0.0)
            pitch(0.0)
        }
    }

    if (locationPermissionsState.allPermissionsGranted) {
//        LaunchedEffect(LocationManager.currentInfoTracking) {
//            LocationManager.currentInfoTracking.collect {
//                if (it.speed == 0f && it.kCal == 0f && it.distance == BigDecimal(0)) return@collect
//                val newDistance =
//                    if (it.distance == BigDecimal(0)) mapUiState.infoTracking.distance else it.distance + mapUiState.infoTracking.distance
//                Log.d(TAG, "currentInfoTracking: $it")
//                val newInfoTracking = InfoTracking(
//                    speed = it.speed,
//                    kCal = it.kCal + mapUiState.infoTracking.kCal,
//                    distance = newDistance
//                )
//                viewModel.handleEvent(MapEvent.UpdateInfoTracking(newInfoTracking))
//            }
//        }

        LaunchedEffect(key1 = null, block = {
            viewModel.handleEvent(MapEvent.GetCurrentLocation)
        })

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
//            MapBoxView(mapUiState.infoTracking.location, mapViewportState)

            IconBack {
                onBack()
            }

            AnimatedVisibility(
                visible = cardVisible,
                enter = slideInVertically(animationSpec = tween(durationMillis = 1000)) { it } + fadeIn(
                    animationSpec = tween(durationMillis = 1000)
                ),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 1000)) { it } + fadeOut(
                    animationSpec = tween(durationMillis = 1000)
                ),
                modifier = Modifier.align(Alignment.BottomCenter)) {
                CardInfo(
                    isRecord = mapUiState.isRecord,
                    speedMeter = mapUiState.infoTracking.speed,
                    distance = mapUiState.infoTracking.distance,
                    calo = mapUiState.infoTracking.kCal,
                    countTime = mapUiState.countTime,
                    onHide = { cardVisible = false },
                    focusLocation = {
                        CoroutineScope(Dispatchers.IO).launch {
                            isFirst = true
                            delay(1000)
                            isFirst = false
                        }
                    }
                ) {
                    if (!mapUiState.isRecord) {
//                        LocationManager.startLocationTracking(context, 2000, 1f)
                        viewModel.handleEvent(MapEvent.Start)
                    } else {
//                        LocationManager.stopLocationTracking()
                        viewModel.handleEvent(MapEvent.Stop)
                    }
                }
            }

            AnimatedVisibility(
                visible = !cardVisible,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = null,
                    modifier = Modifier.clickable { cardVisible = true })
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Screen need have permission location for tracking location",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                i.data = uri
                context.startActivity(i)
            }) {
                Text(text = "Setting")
            }
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapBoxView() {
//    MapboxMap(
//        modifier = Modifier.fillMaxSize(),
//        mapViewportState = mapViewportState,
//        mapInitOptionsFactory = { context ->
//            MapInitOptions(
//                context = context,
//                styleUri = Style.MAPBOX_STREETS,
//            )
//        }
//    ) {
//        PointAnnotation(point = Point.fromLngLat(location.lng, location.lat))
//    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IconBack(onBack: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        modifier = Modifier
            .padding(top = 32.dp, start = 16.dp)
            .size(32.dp),
        onClick = onBack,
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Composable
fun CardInfo(
    modifier: Modifier = Modifier,
    speedMeter: Float = 0f,
    calo: Float = 0f,
    countTime: Long = 0L,
    distance: BigDecimal = BigDecimal(0),
    isRecord: Boolean = false,
    onHide: () -> Unit = {},
    focusLocation: () -> Unit = {},
    onChangeRecord: () -> Unit = {},
) {
    val hours = countTime / 3600
    val minutes = (countTime % 3600) / 60
    val remainingSeconds = countTime % 60
    Surface(
        shape = RoundedCornerShape(16.dp), color = Color.White, modifier = modifier
            .padding(32.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = "",
                tint = Color.LightGray,
                modifier = Modifier
                    .clickable { onHide() }
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Running time",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format(
                            "%02d:%02d:%02d",
                            hours,
                            minutes,
                            remainingSeconds
                        ),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(40.dp)
                            .clickable { focusLocation() },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MyLocation,
                            contentDescription = "Focus",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(40.dp)
                            .clickable { onChangeRecord() },
                    ) {
                        Icon(
                            imageVector = if (isRecord) Icons.Outlined.Pause else Icons.Outlined.PlayCircleOutline,
                            contentDescription = "Record",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRunning(speedMeter, distance, calo)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun PreviewMapRecordScreen() {
    SYLTheme {
        MapRecordScreen()
    }
}

@Preview
@Composable
fun PreviewIconBack() {
    SYLTheme {
        IconBack {}
    }
}

@Preview
@Composable
fun PreviewCardInfo() {
    SYLTheme {
        CardInfo()
    }
}

@Preview
@Composable
fun PreviewInfoRunning() {
    SYLTheme {
        InfoRunning()
    }
}

@Preview
@Composable
fun PreviewItemInfoRunning() {
    SYLTheme {
        ItemInfoRunning(iconId = R.drawable.ic_kcal, number = 20f, unit = "km")
    }
}