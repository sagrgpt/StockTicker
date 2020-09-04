package com.showcase.pricetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.showcase.pricetracker.R
import com.showcase.pricetracker.di.CompositionRoot
import kotlinx.android.synthetic.main.activity_main.*


class HostActivity : AppCompatActivity() {

    private lateinit var compositionRoot: CompositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositionRoot = CompositionRoot()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.watchListFragment -> getString(R.string.watchList_fragment)
                R.id.historyFragment -> getString(R.string.history_fragment)
                else -> ""
            }
        }
    }

    fun getCompositionRoot(): CompositionRoot {
        return compositionRoot
    }

}