package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var homeFragment = HomeFragment()
    private var categoriesFragment: CategoriesFragment = CategoriesFragment()
    private var progressFragment: ProgressFragment = ProgressFragment()
    private var configurationFragment: ConfigurationFragment = ConfigurationFragment()
    private val fm by lazy { supportFragmentManager }
    private var currentFragment: Fragment = homeFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fm.beginTransaction().add(R.id.container, configurationFragment, "4").hide(configurationFragment).commit()
        fm.beginTransaction().add(R.id.container, progressFragment, "3").hide(progressFragment).commit()
        fm.beginTransaction().add(R.id.container, categoriesFragment, "2").hide(categoriesFragment).commit()
        fm.beginTransaction().add(R.id.container, homeFragment, "1").commit()
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    fm.beginTransaction().hide(currentFragment).show(homeFragment).commit()
                    currentFragment = homeFragment
                    return@setOnNavigationItemSelectedListener true
//                    fm.beginTransaction().replace(R.id.container, HomeFragment()).commit()
                }
                R.id.action_categories -> {
                    fm.beginTransaction().hide(currentFragment).show(categoriesFragment).commit()
                    currentFragment = categoriesFragment
                    return@setOnNavigationItemSelectedListener true
//                    fm.beginTransaction().replace(R.id.container, categoriesFragment)
//                            .commit()
                }
                R.id.action_progress -> {
                    fm.beginTransaction().hide(currentFragment).show(progressFragment).commit()
                    currentFragment = progressFragment
                    return@setOnNavigationItemSelectedListener true
//                    fm.beginTransaction()
//                            .replace(R.id.container, progressFragment)
//                            .commit()
                }
                R.id.action_profile -> {
                    fm.beginTransaction().hide(currentFragment).show(configurationFragment).commit()
                    currentFragment = configurationFragment
                    return@setOnNavigationItemSelectedListener true
//                    fm.beginTransaction()
//                            .replace(R.id.container, configurationFragment)
//                            .commit()
                }
                else -> {
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }
}
