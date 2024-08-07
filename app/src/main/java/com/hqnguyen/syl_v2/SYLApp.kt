package com.hqnguyen.syl_v2

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hqnguyen.syl_v2.persentation.page.achievement.AchievementScreen
import com.hqnguyen.syl_v2.persentation.page.home.HomeScreen
import com.hqnguyen.syl_v2.persentation.page.map_record.MapRecordScreen
import com.hqnguyen.syl_v2.persentation.page.map_record.MapRecordViewModel
import com.hqnguyen.syl_v2.persentation.page.noti.NotificationScreen
import com.hqnguyen.syl_v2.persentation.page.profile.ProfileScreen

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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SYLApp() {

    val systemUiController = rememberSystemUiController()

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigation.Home,
        BottomNavigation.Achievement,
        BottomNavigation.Notification,
        BottomNavigation.Profile,
    )

    val bottomBarRoutes = bottomNavigationItems.map { it.route }
    val shouldShowBottomBar: Boolean = navController
        .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes
    val shouldShowFAB: Boolean = navController
        .currentBackStackEntryAsState().value?.destination?.route == bottomBarRoutes.first()
    systemUiController.isNavigationBarVisible = false

    val sharedViewModel: MapRecordViewModel = hiltViewModel()
    val uiStateShared by sharedViewModel.uiState.collectAsState()
    Scaffold(floatingActionButton = {
        if (shouldShowFAB)
            FloatingButtonCustom {
                navController.navigate("map_record")
            }
    }, bottomBar = {
        if (shouldShowBottomBar)
            BottomNavigationApp(bottomNavigationItems, navController)
    }) {
        NavHost(
            navController = navController,
            startDestination = "home",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            modifier = Modifier.padding(it)
        ) {
            composable(
                route = "home",
                enterTransition = null,
                exitTransition = null,
                popExitTransition = null,
                popEnterTransition = null
            ) {
                HomeScreen(
                    navigationToMapScreen = { navController.navigate("map_record") },
                    sharedStated = uiStateShared
                )
            }

            composable(
                route = "achievement",
                enterTransition = null,
                exitTransition = null,
                popExitTransition = null,
                popEnterTransition = null
            ) {
                AchievementScreen(navController::navigate)
            }

            composable(
                route = "notification",
                enterTransition = null,
                exitTransition = null,
                popExitTransition = null,
                popEnterTransition = null
            ) {
                NotificationScreen(navController::navigate)
            }

            composable(
                route = "profile",
                enterTransition = null,
                exitTransition = null,
                popExitTransition = null,
                popEnterTransition = null
            ) {
                ProfileScreen(navController::navigate)
            }

            composable(
                route = "map_record", enterTransition = null,
                exitTransition = null,
                popExitTransition = null,
                popEnterTransition = null
            ) {
                MapRecordScreen(viewModel = sharedViewModel) { navController.popBackStack() }
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
            .padding(0.dp)
            .background(Color.White),
        containerColor = Color.White,
    ) {
        bottomNavigationItems.forEach {
            val currentRoute = currentRoute(navController)
            val selected = currentRoute == it.route
            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(id = it.resourceId),
                        fontSize = 10.sp,
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
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun FloatingButtonCustom(onClick: () -> Unit) {
    androidx.compose.material.FloatingActionButton(
        shape = CircleShape,
        onClick = { onClick() },
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(imageVector = Icons.Outlined.Add, contentDescription = "", tint = Color.White)
    }
}
