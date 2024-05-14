package com.dacs.vku.presentation.SignInWithGoogle

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dacs.vku.presentation.nvgraph.Route
import com.dacs.vku.presentation.onboarding.OnBoardingViewModel
import com.loc.newsapp.presentation.news_navigator.NewsNavigator
import kotlinx.coroutines.launch
import kotlin.contracts.contract


@Composable
fun NavGoogle(
    lifecycleScope: LifecycleCoroutineScope,
    GoogleAuthClient: GoogleAuthClient

) {
    val viewModel: SignInViewModel = hiltViewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "signIn") {
        composable(

            "signIn"

        ) {
            val state by viewModel.state.collectAsState()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->

                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch{
                            val SignInResult = GoogleAuthClient.signInWithIntent(
                                intent = result.data?: return@launch

                            )
                            viewModel.onSignInResult(SignInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSucessful ) {
                if (state.isSignInSucessful) {
                    Toast.makeText(
                        GoogleAuthClient.context,
                        "Sign In Successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            SignInScreen(state = state, onSignInClick = {
                lifecycleScope.launch {
                    val signInIntentSender = GoogleAuthClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender?: return@launch
                        ).build()
                    )
                }
            })


        }


    }
}

