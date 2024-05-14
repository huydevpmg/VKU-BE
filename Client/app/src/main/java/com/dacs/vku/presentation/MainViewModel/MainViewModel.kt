package com.dacs.vku.presentation.MainViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dacs.vku.domain.usecases.app_entry.AppEntryUseCases
import com.dacs.vku.presentation.nvgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    private val _splashCondition = mutableStateOf(true)
    var splashCondition: State<Boolean> = _splashCondition
    private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    val lifecycleScope: LifecycleCoroutineScope
        get() = viewModelScope as LifecycleCoroutineScope

    init {
        appEntryUseCases.readAppEntry().onEach { shouldStartFromHomeScreen ->
            if(shouldStartFromHomeScreen) {
                startDestination = Route.VKUNavigation.route
            } else {
                startDestination = Route.AppStartNavigation.route

            }

            delay(300)

            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }

}