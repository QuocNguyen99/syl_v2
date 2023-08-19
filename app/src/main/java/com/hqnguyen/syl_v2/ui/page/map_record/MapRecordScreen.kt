package com.hqnguyen.syl_v2.ui.page.map_record

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hqnguyen.syl_v2.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.logD
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapRecordScreen(navigation: ((uri: String) -> Unit)? = null) {
    val TAG = "MapRecordScreen"
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false
    var currentBearing = 0.0
    var currentPosition: Point = Point.fromLngLat(0.0, 0.0)
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

                val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
                    if (it != currentBearing) {
                        currentBearing = it
                        Log.d(TAG, "onIndicatorBearingChangedListener: $it")
                        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).zoom(14.0).build())
                    }
                }

                val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
                    if (currentPosition.latitude() != it.latitude() || currentPosition.longitude() != it.longitude()) {
                        currentPosition = it
                        Log.d(TAG, "onIndicatorPositionChangedListener: ${it.latitude()} - ${it.longitude()}")
                        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).zoom(14.0).build())
                        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
                    }
                }

                // Pass the user's location to camera
                mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
                mapView.location.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
            }
        )
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(color = Color.White)
        ) {
            IconButton(onClick = { /* Handle icon click event */ }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "User Icon",
                    tint = Color.Black
                )
            }
        }
    }
}