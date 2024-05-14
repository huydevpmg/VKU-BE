package com.dacs.vku.presentation.nvgraph

//import com.dacs.vku.presentation.notification.NotificationListScreen
//import com.dacs.vku.presentation.notification.NotificationViewModel
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.dacs.vku.presentation.MainViewModel.MainViewModel
import com.dacs.vku.presentation.SignInWithGoogle.GoogleAuthClient
import com.dacs.vku.presentation.SignInWithGoogle.NavGoogle
import com.dacs.vku.presentation.onboarding.OnBoardingScreen
import com.dacs.vku.presentation.onboarding.OnBoardingViewModel
import com.loc.newsapp.presentation.news_navigator.NewsNavigator

@Composable
fun NavGraph(
    mainViewModel: MainViewModel,
    startDestination: String,
    googleAuthClient: GoogleAuthClient,

) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    event = viewModel::onEvent
                )
            }
        }

        navigation(
            route = Route.VKUNavigation.route,
            startDestination = Route.DaotaoScreen.route
        ) {
            composable(
                route = Route.DaotaoScreen.route
            ) {
                NewsNavigator(mainViewModel, googleAuthClient )
            }
        }


    }
}