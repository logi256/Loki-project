package com.example.smartbikepass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartbikepass.ui.screens.ApplyScreen
import com.example.smartbikepass.ui.screens.ApprovedPassScreen
import com.example.smartbikepass.ui.screens.HomeScreen
import com.example.smartbikepass.ui.screens.LoginScreen
import com.example.smartbikepass.ui.screens.PrincipalScreen
import com.example.smartbikepass.ui.screens.StatusScreen
import com.example.smartbikepass.ui.screens.TransportScreen
import com.example.smartbikepass.ui.theme.SmartBikePassTheme
import com.example.smartbikepass.viewmodel.BikePassViewModel
import com.example.smartbikepass.viewmodel.BikePassViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: BikePassViewModel by viewModels {
        BikePassViewModelFactory((application as SmartBikePassApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartBikePassTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SmartBikePassNavHost(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SmartBikePassNavHost(
    viewModel: BikePassViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
        },
        exitTransition = {
            fadeOut(tween(250)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
        },
        popEnterTransition = {
            fadeIn(tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
        },
        popExitTransition = {
            fadeOut(tween(250)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
        }
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onStudentLoginSuccess = {
                    navController.navigate("home")
                },
                onAdminLoginSuccess = { role ->
                    val target = when (role.trim().lowercase()) {
                        "principal" -> "principal"
                        else -> "transport"
                    }
                    navController.navigate(target)
                },
                onExploreAsGuest = {
                    navController.navigate("home")
                },
                onNavigateBack = if (navController.previousBackStackEntry != null) {
                    { navController.popBackStack() }
                } else null
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToApply = { navController.navigate("apply") },
                onNavigateToStatus = { passId ->
                    if (!passId.isNullOrBlank()) {
                        navController.navigate("status/$passId")
                    } else {
                        navController.navigate("status")
                    }
                },
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToDashboard = { role ->
                    when (role.lowercase().trim()) {
                        "principal" -> navController.navigate("principal")
                        "transport" -> navController.navigate("transport")
                        else -> navController.navigate("transport")
                    }
                }
            )
        }

        composable("apply") {
            ApplyScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStatus = { passId ->
                    navController.navigate("status/$passId") {
                        popUpTo("home")
                    }
                }
            )
        }

        composable("status") {
            StatusScreen(
                viewModel = viewModel,
                initialPassId = null,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToApprovedPass = { passId ->
                    navController.navigate("approved/$passId")
                }
            )
        }

        composable(
            route = "status/{passId}",
            arguments = listOf(navArgument("passId") { type = NavType.StringType })
        ) { backStackEntry ->
            val passId = backStackEntry.arguments?.getString("passId")
            StatusScreen(
                viewModel = viewModel,
                initialPassId = passId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToApprovedPass = { pid ->
                    navController.navigate("approved/$pid")
                }
            )
        }

        composable(
            route = "approved/{passId}",
            arguments = listOf(navArgument("passId") { type = NavType.StringType })
        ) { backStackEntry ->
            val passId = backStackEntry.arguments?.getString("passId") ?: ""
            ApprovedPassScreen(
                passId = passId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("transport") {
            TransportScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPrincipal = { navController.navigate("principal") }
            )
        }

        composable("principal") {
            PrincipalScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransport = { navController.navigate("transport") },
                onNavigateToApprovedPass = { passId ->
                    navController.navigate("approved/$passId")
                }
            )
        }
    }
}
