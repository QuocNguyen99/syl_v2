package com.hqnguyen.syl_v2

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hqnguyen.syl_v2.ui.page.home.HomeScreen

sealed class BottomNavigation(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : BottomNavigation("home", R.string.home, Icons.Outlined.Home)
    object Achievement : BottomNavigation("achievement", R.string.achievement, Icons.Outlined.Star)
    object Notification :
        BottomNavigation("notification", R.string.notification, Icons.Outlined.Notifications)

    object Profile : BottomNavigation("profile", R.string.profile, Icons.Outlined.Person)
}

@Composable
fun SYLApp(modifier: Modifier = Modifier) {

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

            }

            composable(route = "notification") {

            }

            composable(route = "profile") {

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
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
            .background(Color.White)
            .shadow(4.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        elevation = 10.dp
    ) {
        bottomNavigationItems.forEach {
            val currentRoute = currentRoute(navController)
            BottomNavigationItem(
                label = { Text(text = stringResource(id = it.resourceId), fontSize = 10.sp) },
                selected = currentRoute == it.route,
                onClick = {
                    if (currentRoute != it.route) {
                        navController.navigate(it.route)
                    }
                },
                icon = { Icon(imageVector = it.icon, contentDescription = "") },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray
            )
        }
    }
}
