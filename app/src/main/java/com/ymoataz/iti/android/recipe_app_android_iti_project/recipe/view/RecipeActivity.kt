import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ymoataz.iti.android.recipe_app_android_iti_project.R
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.AuthHelper
import com.ymoataz.iti.android.recipe_app_android_iti_project.auth.view.AuthActivity

class RecipeActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

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
                R.id.aboutFragment, R.id.mealsByCategoryFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottomNavigationView.visibility = BottomNavigationView.GONE
                }

                R.id.detailsFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottomNavigationView.visibility = BottomNavigationView.VISIBLE
                }
                R.id.mealsByCategoryFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottomNavigationView.visibility = BottomNavigationView.GONE
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
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to sign out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        AuthHelper.logout(this)
                        val bundle = Bundle()
                        bundle.putBoolean("isFromLogout", true)
                        NavDeepLinkBuilder(this)
                            .setGraph(R.navigation.auth_navigation)
                            .setDestination(R.id.loginFragment)
                            .setComponentName(AuthActivity::class.java)
                            .setArguments(bundle)
                            .createPendingIntent().send()
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
