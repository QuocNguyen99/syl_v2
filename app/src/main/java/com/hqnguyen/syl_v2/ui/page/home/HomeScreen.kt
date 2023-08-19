package com.hqnguyen.syl_v2.ui.page.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.data.Record
import com.hqnguyen.syl_v2.ui.theme.SYLTheme
import com.hqnguyen.syl_v2.ui.theme.colorBackgroundColor

@Composable
fun HomeScreen(navigation: ((uri: String) -> Unit)? = null) {
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
        Spacer(modifier = Modifier.height(20.dp))
        BoxRunningInfo()
        Recent(modifier = Modifier.padding(16.dp, 0.dp)) {
            if (navigation != null) {
                navigation("map_record")
            }
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

@Composable
fun BoxRunningInfo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp)
            .height(60.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp, 0.dp)
        ) {
            ConstraintLayout {
                val (backgroundIconRef, iconRef) = createRefs()
                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .constrainAs(backgroundIconRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    color = colorBackgroundColor,
                ) {}
                Image(
                    painter = painterResource(id = R.drawable.ic_running),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(0.dp, 12.dp)
                        .constrainAs(iconRef) {
                            top.linkTo(backgroundIconRef.top)
                            end.linkTo(backgroundIconRef.end)
                            bottom.linkTo(backgroundIconRef.bottom)
                            start.linkTo(backgroundIconRef.start)
                        }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Current jogging",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "01:09:44",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = "10,9 km",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "538 kcal",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun Recent(modifier: Modifier = Modifier, navigation: (() -> Unit)?) {
    val fakeData = listOf(
        Record(time = System.currentTimeMillis()),
        Record(time = System.currentTimeMillis() + 1),
        Record(time = System.currentTimeMillis() + 2),
        Record(time = System.currentTimeMillis() + 3),
        Record(time = System.currentTimeMillis() + 4),
        Record(time = System.currentTimeMillis() + 5),
        Record(time = System.currentTimeMillis() + 6),
        Record(time = System.currentTimeMillis() + 7),
        Record(time = System.currentTimeMillis() + 8),
        Record(time = System.currentTimeMillis() + 9),
    )

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text(
                text = "Recent Activity",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add",
                modifier = modifier.clickable(true) {
                    navigation?.let { it() }
                },
                color = MaterialTheme.colorScheme.primary, fontSize = 13.sp
            )
            Text(text = "All", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (fakeData.size >= 4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            ) {
                LazyColumn {
                    itemsIndexed(
                        items = fakeData,
                        key = { _: Int, item: Record -> item.time!! }) { index, item ->
                        ItemRecent(record = item)
                        if (index < fakeData.lastIndex)
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(74.dp, 0.dp, 20.dp, 0.dp)
                            )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            ) {
                LazyColumn {
                    itemsIndexed(
                        items = fakeData,
                        key = { _: Int, item: Record -> item.time!! }) { index, item ->
                        ItemRecent(record = item)
                        if (index < fakeData.lastIndex)
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(74.dp, 0.dp, 20.dp, 0.dp)
                            )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ItemRecent(record: Record) {
    Row(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 8.dp)
            .height(IntrinsicSize.Max)
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_running),
            contentDescription = null,
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "November 26",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "01:09:44",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row {
                Text(
                    text = "701 kcal",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "11.2 km/h",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    SYLTheme {
        HomeScreen()
    }
}