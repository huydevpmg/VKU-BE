package com.loc.newsapp.presentation.news_navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dacs.vku.R
import com.dacs.vku.presentation.notificationCTSV.CTSVViewModel
import com.dacs.vku.presentation.notificationDaoTao.NotiScreen
import com.dacs.vku.presentation.notificationDaoTao.NotificationViewModel
import com.dacs.vku.presentation.nvgraph.Route
import com.loc.newsapp.presentation.news_navigator.components.BottomNavigationItem
import com.loc.newsapp.presentation.news_navigator.components.NewsBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = "Dao Tao"),
            BottomNavigationItem(icon = R.drawable.ic_search, text = "CTSV"),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "KHTC"),
            BottomNavigationItem(icon = R.drawable.ic_network, text = "DBKTCL")

            )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }

    selectedItem = when (backStackState?.destination?.route) {
        Route.DaotaoScreen.route -> 0
        Route.CTSVScreen.route -> 1
        Route.KHTCScreen.route -> 2
        Route.KTDBCLScreen.route -> 3
        else -> 0
    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NewsBottomNavigation(
            items = bottomNavigationItems,
            selectedItem = selectedItem,
            onItemClick = { index ->
                when (index) {
                    0 -> navigateToTab(
                        navController = navController,
                        route = Route.DaotaoScreen.route
                    )

                    1 -> navigateToTab(
                        navController = navController,
                        route = Route.CTSVScreen.route
                    )

                    2 -> navigateToTab(
                        navController = navController,
                        route = Route.KHTCScreen.route
                    )

                    3 -> navigateToTab(
                        navController = navController,
                        route = Route.KTDBCLScreen.route
                    )
                }
            }
        )
    }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.DaotaoScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.DaotaoScreen.route) {
                val viewModel: NotificationViewModel = hiltViewModel()
                val articles = viewModel.noti.collectAsLazyPagingItems()
                NotiScreen(
                    notis = articles,
                    navigate = { navigateToTab(navController = navController, route = it) },
                    )
            }
            composable(route = Route.CTSVScreen.route) {
                val viewModel: CTSVViewModel = hiltViewModel()
                val articles = viewModel.noti.collectAsLazyPagingItems()
                NotiScreen(
                    notis = articles,
                    navigate = { navigateToTab(navController = navController, route = it) },
                )

            }

            composable(route = Route.KHTCScreen.route) {
                val viewModel: CTSVViewModel = hiltViewModel()
                val articles = viewModel.noti.collectAsLazyPagingItems()
                NotiScreen(
                    notis = articles,
                    navigate = { navigateToTab(navController = navController, route = it) },
                )

            }

            composable(route = Route.KTDBCLScreen.route) {
                val viewModel: CTSVViewModel = hiltViewModel()
                val articles = viewModel.noti.collectAsLazyPagingItems()
                NotiScreen(
                    notis = articles,
                    navigate = { navigateToTab(navController = navController, route = it) },
                )

            }

        }
    }
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(
            navController = navController,
            route = Route.DaotaoScreen.route
        )
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}