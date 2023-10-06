package com.example.mytasksactivity.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mytasksactivity.R
import com.example.mytasksactivity.databinding.ActivityMainBinding
import com.example.mytasksactivity.prefs.AppSharedPreferencesController
import com.example.mytasksactivity.repository.UserRepository
import com.example.mytasksactivity.view_model.login_view_model.UserViewModel
import com.example.mytasksactivity.view_model.login_view_model.UsersViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = UserRepository()
        val viewModelProvider = UsersViewModelProviderFactory(application, userRepository)
        viewModel = ViewModelProvider(this, viewModelProvider)[UserViewModel::class.java]


    }

    override fun onStart() {
        super.onStart()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_login, R.id.navigation_home
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        val toLogin = AppSharedPreferencesController.getInstance(this).isLoggedIn()
        if (toLogin) {
            navController.navigate(R.id.navigation_home)
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            navGraph.setStartDestination(R.id.navigation_home)
            navController.graph = navGraph
        } else {
            navController.navigate(R.id.navigation_login);
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

