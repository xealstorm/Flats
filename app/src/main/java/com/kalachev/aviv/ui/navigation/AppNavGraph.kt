package com.kalachev.aviv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kalachev.aviv.layer.presentation.details.DetailsScreen
import com.kalachev.aviv.layer.presentation.feed.FeedScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = "feedScreen"
    ) {
        composable("feedScreen") {
            FeedScreen(
                modifier = modifier,
                navController = navHostController,
            )
        }

        composable("detailsScreen/{detailsId}", arguments = listOf(navArgument("detailsId") { type = NavType.LongType })) { backStackEntry ->
            val detailsId = backStackEntry.arguments?.getLong("detailsId")
            DetailsScreen(
                modifier = modifier,
                detailsId = detailsId
            )
        }
    }
}