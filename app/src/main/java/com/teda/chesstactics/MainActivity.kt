package com.teda.chesstactics

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.teda.chesstactics.ui.CategoriesFragment
import com.teda.chesstactics.ui.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_categories -> {
                    var ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, CategoriesFragment.newInstance())
                    ft.commit()
                }
                R.id.action_home -> {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, HomeFragment.newInstance())
                    ft.commit()
                }
                R.id.action_profile -> {
                }
                else -> {
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
