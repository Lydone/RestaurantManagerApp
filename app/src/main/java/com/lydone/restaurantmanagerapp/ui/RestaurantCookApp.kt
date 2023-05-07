package com.lydone.restaurantmanagerapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCookApp() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val destinations = listOf(
        TopLevelDestination.DISHES,
        TopLevelDestination.CATEGORIES,
        TopLevelDestination.TABLES,
        TopLevelDestination.ACCOUNTS,
        TopLevelDestination.ORDERS,
    )
    Scaffold {
        Row(Modifier.padding(it)) {
            NavigationRail {
                destinations.forEach { destination ->
                    NavigationRailItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(destination.iconId),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(destination.titleId)) },
                    )
                }
            }
            NavHost(navController)
        }
    }
}