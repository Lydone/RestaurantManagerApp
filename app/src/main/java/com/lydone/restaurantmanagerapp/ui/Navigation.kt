package com.lydone.restaurantmanagerapp.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lydone.restaurantmanagerapp.R
import com.lydone.restaurantmanagerapp.ui.category.CategoriesRoute
import com.lydone.restaurantmanagerapp.ui.dish.DishesRoute
import com.lydone.restaurantmanagerapp.ui.table.TablesRoute

private const val ROUTE_DISHES = "dishes"
private const val ROUTE_CATEGORIES = "categories"
private const val ROUTE_TABLES = "tables"

@Composable
fun NavHost(navController: NavHostController, modifier: Modifier = Modifier) =
    NavHost(navController, ROUTE_DISHES, modifier) {
        composable(ROUTE_DISHES) { DishesRoute() }
        composable(ROUTE_CATEGORIES) { CategoriesRoute() }
        composable(ROUTE_TABLES) { TablesRoute() }
    }

enum class TopLevelDestination(
    val route: String,
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
) {
    DISHES(ROUTE_DISHES, R.drawable.baseline_restaurant_menu_24, R.string.destination_dishes),
    CATEGORIES(ROUTE_CATEGORIES, R.drawable.baseline_category_24, R.string.destination_categories),
    TABLES(ROUTE_TABLES, R.drawable.baseline_table_restaurant_24, R.string.destination_tables)
}