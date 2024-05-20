package com.dacs.vku.ui

import AuthenticationRepository
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dacs.vku.R
import com.dacs.vku.api.AuthenticationAPI
import com.dacs.vku.databinding.ActivityVkuBinding
import com.dacs.vku.db.NotificationDatabase
import com.dacs.vku.repository.NotificationRepository
import com.dacs.vku.ui.viewModels.NotificationDaoTaoViewModel
import com.dacs.vku.ui.viewModels.NotificationDaoTaoViewModelproviderFactory

class VkuActivity : AppCompatActivity() {
    lateinit var notificationViewModel: NotificationDaoTaoViewModel
    lateinit var authenticationAPI: AuthenticationAPI
    lateinit var binding: ActivityVkuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityVkuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationRepository = NotificationRepository(NotificationDatabase(this))
        val authenticationRepository = AuthenticationRepository(authenticationAPI)
        val viewModelproviderFactory= NotificationDaoTaoViewModelproviderFactory(application, notificationRepository,authenticationRepository)

        notificationViewModel = ViewModelProvider(this, viewModelproviderFactory)[NotificationDaoTaoViewModel::class.java]

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.notificationNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}