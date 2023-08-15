package com.hqnguyen.syl_v2

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hqnguyen.syl_v2.ui.page.achievement.AchievementScreen
import com.hqnguyen.syl_v2.ui.page.home.HomeScreen
import com.hqnguyen.syl_v2.ui.page.noti.NotificationScreen
import com.hqnguyen.syl_v2.ui.page.profile.ProfileScreen

sealed class BottomNavigation(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : BottomNavigation("home", R.string.home, Icons.Outlined.FormatListBulleted)
    object Achievement :
        BottomNavigation("achievement", R.string.achievement, Icons.Outlined.StarOutline)

    object Notification :
        BottomNavigation("notification", R.string.notification, Icons.Outlined.Notifications)

    object Profile : BottomNavigation("profile", R.string.profile, Icons.Outlined.Person)
}

@Composable
fun SYLApp(modifier: Modifier = Modifier) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.primary
    )

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigation.Home,
        BottomNavigation.Achievement,
        BottomNavigation.Notification,
        BottomNavigation.Profile,
    )

    Scaffold(bottomBar = { BottomNavigationApp(bottomNavigationItems, navController) }) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(it)
        ) {
            composable(route = "home") {
                HomeScreen()
            }

            composable(route = "achievement") {
                AchievementScreen()

            }

            composable(route = "notification") {
                NotificationScreen()
            }

            composable(route = "profile") {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun BottomNavigationApp(
    bottomNavigationItems: List<BottomNavigation>,
    navController: NavHostController
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                clip = true
            }
            .background(Color.White),
        tonalElevation = 12.dp,
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        bottomNavigationItems.forEach {
            val currentRoute = currentRoute(navController)
            val selected = currentRoute == it.route
            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(id = it.resourceId),
                        fontSize = 10.sp,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(it.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = "",
                        tint = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray
                    )
                },
            )
        }
    }
}
