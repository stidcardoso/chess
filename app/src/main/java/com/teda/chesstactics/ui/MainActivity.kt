package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, HomeFragment.newInstance())
                    ft.commit()
                }
                R.id.action_categories -> {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, CategoriesFragment.newInstance())
                    ft.commit()
                }
                R.id.action_progress -> {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, ProgressFragment())
                    ft.commit()
                }
                R.id.action_profile -> {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, ConfigurationFragment())
                    ft.commit()
                }
                else -> {
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
