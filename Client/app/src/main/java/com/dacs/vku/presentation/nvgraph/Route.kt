package com.dacs.vku.presentation.nvgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen: Route(route= "OnBoardingScreen")
    object DaotaoScreen: Route(route= "DaotaoScreen")
    object KHTCScreen: Route(route= "KHTCScreen")
    object KTDBCLScreen: Route(route = "KTDBCLScreen")
    object CTSVScreen: Route(route = "CTSVScreen")
// màn hình onboarding
    object AppStartNavigation: Route(route = "appStartNavigation")

//    MH Vào
    object VKUNavigation: Route(route = "VKUNavigation")


    object VKUNavigatorScreen: Route(route ="VKUNavigator")

    object DetailsScreen: Route(route = "detailsScreen")
    object VKULoginViaGmail: Route(route= "LoginViaGmail")
}