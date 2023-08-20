package com.hqnguyen.syl_v2.ui.page.map_record

import android.os.CountDownTimer
import android.util.Log
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
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.ui.theme.SYLTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapRecordScreen(navigation: NavHostController? = null) {
    val TAG = "MapRecordScreen"
    var currentBearing = 0.0
    var currentPosition: Point = Point.fromLngLat(0.0, 0.0)
    var saveLocal = true
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier,
            factory = { context ->
                ResourceOptionsManager.getDefault(
                    context,
                    context.getString(R.string.mapbox_access_token)
                )

                MapView(context).apply {
                    getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
                    }
                }
            },
            update = { mapView ->
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
                        currentPosition = it
                        Log.d(
                            TAG,
                            "onIndicatorPositionChangedListener: ${it.latitude()} - ${it.longitude()}"
                        )
                        mapView.getMapboxMap()
                            .setCamera(CameraOptions.Builder().center(it).zoom(14.0).build())
                        mapView.gestures.focalPoint =
                            mapView.getMapboxMap().pixelForCoordinate(it)

                        if (saveLocal) {
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
            }
        )

        IconBack {
            navigation?.popBackStack()
        }

        CardInfo(modifier = Modifier.align(Alignment.BottomCenter))
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
fun CardInfo(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = modifier
            .padding(32.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = "",
            tint = Color.LightGray,
        )
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Running time", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "01:09:44", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(40.dp)
                        .clickable(true) { },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayCircleOutline,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRunning()
        }
    }
}

@Composable
fun InfoRunning() {
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
            ItemInfoRunning(Icons.Outlined.DirectionsRun, 20.0, "km")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(Icons.Outlined.DirectionsRun, 20.0, "km")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(Icons.Outlined.DirectionsRun, 20.0, "km")
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ItemInfoRunning(icon: ImageVector, number: Double, unit: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = "")
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = number.toString(), color = Color.Gray)
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
        ItemInfoRunning(Icons.Outlined.DirectionsRun, 20.0, "km")
    }
}