package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view.AuthActivity
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view.AuthActivityDirections

class RecipeActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.searchFragment,
                R.id.favouritesFragment
            )
        )

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        var badge = bottomNavigationView.getOrCreateBadge(R.id.favouritesFragment)
        badge.isVisible = true
        badge.number = 99

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.aboutFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottomNavigationView.visibility = BottomNavigationView.GONE
                }
                R.id.detailsFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottomNavigationView.visibility = BottomNavigationView.VISIBLE
                }
                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    bottomNavigationView.visibility = BottomNavigationView.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.right_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about_us -> {
                navController.navigate(R.id.action_global_aboutFragment)
                true
            }
            R.id.action_sign_out -> {
                AuthHelper.logout(this)
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.auth_navigation)
                    .setDestination(R.id.loginFragment)
                    .setComponentName(AuthActivity::class.java)
                    .createPendingIntent().send()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
