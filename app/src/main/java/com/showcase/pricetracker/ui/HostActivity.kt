package com.showcase.pricetracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.showcase.pricetracker.R
import kotlinx.android.synthetic.main.activity_main.*


class HostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }
}