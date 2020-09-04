package com.showcase.pricetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.showcase.pricetracker.R
import com.showcase.pricetracker.di.CompositionRoot
import kotlinx.android.synthetic.main.activity_main.*

/**
 * This is a single activity application with very limited
 * dependencies and hence, the composition root is attached to this activity.
 *
 * In-case of extending to multiple activity, the dependency graph must also adjust.
 */
class HostActivity : AppCompatActivity() {

    private lateinit var compositionRoot: CompositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositionRoot = CompositionRoot()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUpNavController()
    }

    private fun setUpNavController() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.watchListFragment -> title = getString(R.string.watchList_fragment)
                R.id.historyFragment -> title = getString(R.string.history_fragment)
            }
        }
    }

    fun getCompositionRoot(): CompositionRoot {
        return compositionRoot
    }

}