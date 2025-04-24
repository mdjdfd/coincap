package com.example.coincap.ui.compose.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coincap.ui.compose.route.Navigation.Args.ASSET_ID
import com.example.coincap.ui.compose.screen.AssetListScreen
import com.example.coincap.ui.compose.screen.AssetPreviewScreen
import com.example.coincap.util.EventHandler
import com.example.coincap.vm.AssetListContract
import com.example.coincap.vm.AssetPreviewContract

/**
 * Function is responsible for control navigation throughout the application.
 */
@Composable
fun CoincapNavigation(eventHandler: EventHandler) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Navigation.Routes.ASSETS_LIST) {

        composable(
            route = Navigation.Routes.ASSETS_LIST
        ) {
            AssetListScreen(eventHandler, onPreviewRequest = {
                if (it is AssetListContract.Effect.Preview.ToPreview) {
                    navController.navigateToPreview(it.id)
                }
            })
        }

        composable(
            route = Navigation.Routes.ASSET_DETAILS,
            arguments = listOf(navArgument(name = ASSET_ID) {
                type = NavType.StringType
            })
        ) { backStack ->
            val id = requireNotNull(backStack.arguments?.getString(ASSET_ID)){"User id is required"}
            AssetPreviewScreen(id, eventHandler, onBackRequest = {
                if (it is AssetPreviewContract.Effect.Back.ToList) {
                    navController.popBackStack()
                }
            })
        }
    }

}

/**
 * Object provides all the navigation parameters used by nav controller to track navigation stack.
 */
object Navigation {
    object Args {
        const val ASSET_ID = "asset_id"
    }

    object Routes {
        const val ASSETS_LIST = "assets"
        const val ASSET_DETAILS = "$ASSETS_LIST/{$ASSET_ID}"
    }
}

/**
 * Go back to previous screen by navigation backstack call.
 */
fun NavController.navigateToPreview(id: String) {
    navigate(route = "${Navigation.Routes.ASSETS_LIST}/$id")
}