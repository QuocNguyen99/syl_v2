package com.hqnguyen.syl_v2.ui.page.map_record

import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.ui.theme.SYLTheme
import com.hqnguyen.syl_v2.utils.calculateDistance
import com.hqnguyen.syl_v2.utils.calculatorSpeed
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.text.DecimalFormat


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapRecordScreen(navigation: NavHostController? = null) {
    val TAG = "MapRecordScreen"

    var cardVisible by remember { mutableStateOf(true) }
    var isRecord by remember { mutableStateOf(false) }
    var speedMeterSate by remember { mutableDoubleStateOf(0.0) }
    var kcalState by remember { mutableDoubleStateOf(0.0) }
    var distanceState by remember { mutableDoubleStateOf(0.0) }

    var currentBearing = 0.0
    var currentPosition: Point = Point.fromLngLat(0.0, 0.0)
    var currentTime: Long = 0
    var saveLocal = true

    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(key1 = true) {
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    if (locationPermissionsState.allPermissionsGranted) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            AndroidView(modifier = Modifier, factory = { context ->
                ResourceOptionsManager.getDefault(
                    context,
                    context.getString(R.string.mapbox_access_token)
                )

                MapView(context).apply {
                    getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {}
                }
            }, update = { mapView ->
                mapView.location.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                }

                val timer = object : CountDownTimer(2000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        saveLocal = true
                    }
                }

                val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
                    if (it != currentBearing) {
                        currentBearing = it
                        Log.d(TAG, "onIndicatorBearingChangedListener: $it")
                        mapView.getMapboxMap()
                            .setCamera(CameraOptions.Builder().bearing(it).zoom(14.0).build())
                    }
                }

                val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
                    if (currentPosition.latitude() != it.latitude() || currentPosition.longitude() != it.longitude()) {


                        mapView.getMapboxMap()
                            .setCamera(CameraOptions.Builder().center(it).zoom(14.0).build())
                        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)

                        if (saveLocal) {
                            if (currentTime != 0L) {
                                speedMeterSate = calculatorSpeed(
                                    currentPosition,
                                    it,
                                    currentTime,
                                    System.currentTimeMillis()
                                )

                                distanceState += calculateDistance(currentPosition, it)
                                Log.d("calculateDistance", "calculateDistance: $distanceState")
                            }
                            currentTime = System.currentTimeMillis()

                            currentPosition = it
                            Log.d(TAG, "onIndicatorPositionChangedListener: ${it.latitude()} - ${it.longitude()}")

                            Log.d(TAG, "saveLocal: ${it.latitude()} - ${it.longitude()}")
                            saveLocal = false
                            timer.start()
                        }
                    }
                }

                mapView.location.addOnIndicatorPositionChangedListener(
                    onIndicatorPositionChangedListener
                )
                mapView.location.addOnIndicatorBearingChangedListener(
                    onIndicatorBearingChangedListener
                )
            })

            IconBack {
                navigation?.popBackStack()
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
                    isRecord = isRecord,
                    speedMeter = speedMeterSate,
                    distance = distanceState,
                    onHide = { cardVisible = false },
                    onChangeRecord = { isRecord = !isRecord })
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
                i.setData(uri)
                context.startActivity(i)
            }) {
                Text(text = "Setting")
            }
        }
    }
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
    speedMeter: Double = 0.0,
    distance: Double = 0.0,
    isRecord: Boolean = false,
    onHide: () -> Unit = {},
    onChangeRecord: () -> Unit = {}
) {
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
                    Text(text = "01:09:44", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(40.dp)
                        .clickable { onChangeRecord() },
                ) {
                    Icon(
                        imageVector = if (isRecord) Icons.Outlined.Pause else Icons.Outlined.PlayCircleOutline,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRunning(speedMeter, distance)
        }
    }
}

@Composable
fun InfoRunning(
    speedMeter: Double = 0.0,
    distance: Double = 0.0,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemInfoRunning(
                R.drawable.ic_speed_meter,
                speedMeter,
                "km/h"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(R.drawable.ic_kcal, 20.0, "km")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(
                R.drawable.ic_distance,
                distance,
                "km"
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ItemInfoRunning(@DrawableRes iconId: Int, number: Double, unit: String) {
    val decimalFormat = DecimalFormat("0.00")
    val painted = painterResource(id = iconId)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painted, contentDescription = "", modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = decimalFormat.format(number), color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = unit, color = Color.Black)
        }
    }
}

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
        IconBack({})
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
        ItemInfoRunning(R.drawable.ic_kcal, 20.0, "km")
    }
}