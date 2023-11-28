package com.hqnguyen.syl_v2.persentation.page.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.persentation.page.map_record.MapState
import com.hqnguyen.syl_v2.ui.theme.SYLTheme

private const val TAG = "HomeScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedStated: MapState = MapState(),
    navigationToMapScreen: () -> Unit = {},
    ) {

    val homeUiState by viewModel.state.collectAsStateWithLifecycle()

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.primary
    )
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        AppBarHome()
        if (sharedStated.isRecord) {
            Spacer(modifier = Modifier.height(20.dp))
            BoxRunningInfo(sharedStated.infoTracking.distance,sharedStated.infoTracking.kCal,sharedStated.countTime) {
                navigationToMapScreen()
            }
        }
        Recent(modifier = Modifier.padding(16.dp, 0.dp), homeUiState.listRecord) {
            navigationToMapScreen()
        }
    }
}

@Composable
fun AppBarHome() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    ConstraintLayout {
        val (backgroundAppBarRef, itemHeadRef, spacingRef, cardRef) = createRefs()

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 4)
                .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
                .constrainAs(backgroundAppBarRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = MaterialTheme.colorScheme.primary,
        ) {}

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 12)
            .constrainAs(spacingRef) {
                top.linkTo(backgroundAppBarRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        ItemHeaderAppBar(
            Modifier
                .fillMaxWidth()
                .constrainAs(itemHeadRef) {
                    top.linkTo(backgroundAppBarRef.top, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

        CardAppBar(modifier = Modifier.constrainAs(cardRef) {
            bottom.linkTo(spacingRef.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

@Composable
fun ItemHeaderAppBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.avatar_default),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Hello, Nguyen", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Beginner",
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CardAppBar(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row {
                Text(text = "Week goal", color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "50 km",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(text = "35 km done", color = Color.Black, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "15 km left", color = Color.Black, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                LinearProgressIndicator(
                    progress = 0.2f, color = MaterialTheme.colorScheme.primary, modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)), trackColor = Color.LightGray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    SYLTheme {
        HomeScreen()
    }
}