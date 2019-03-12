package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var homeFragment = HomeFragment()
    private var categoriesFragment: CategoriesFragment = CategoriesFragment()
    private var progressFragment: ProgressFragment = ProgressFragment()
    private var configurationFragment: ConfigurationFragment = ConfigurationFragment()
    private val fm by lazy { supportFragmentManager }
    private var currentFragment: Fragment = homeFragment

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(Constants.EXTRAS_CURRENT_FRAGMENT, currentFragment.tag)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragments(savedInstanceState)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    fm.beginTransaction().hide(currentFragment).show(homeFragment).commit()
                    homeFragment.resume()
                    currentFragment = homeFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_categories -> {
                    updateFragments()
                    fm.beginTransaction().hide(currentFragment).show(categoriesFragment).commit()
                    currentFragment = categoriesFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_progress -> {
                    updateFragments()
                    fm.beginTransaction().hide(currentFragment).show(progressFragment).commit()
                    progressFragment.resume()
                    currentFragment = progressFragment
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    updateFragments()
                    fm.beginTransaction().hide(currentFragment).show(configurationFragment).commit()
                    currentFragment = configurationFragment
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun updateFragments() {
        when (currentFragment) {
            is HomeFragment -> homeFragment.pause()
            else -> {
            }
        }
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        validateFragment(homeFragment, HomeFragment.TAG)?.let { homeFragment = it as HomeFragment }
        validateFragment(categoriesFragment, CategoriesFragment.TAG)?.let { categoriesFragment = it as CategoriesFragment }
        validateFragment(progressFragment, ProgressFragment.TAG)?.let { progressFragment = it as ProgressFragment }
        validateFragment(configurationFragment, ConfigurationFragment.TAG)?.let { configurationFragment = it as ConfigurationFragment }
        if (savedInstanceState == null)
            fm.beginTransaction().show(homeFragment).commit()
        else {
            currentFragment = fm.findFragmentByTag(savedInstanceState.getString(Constants.EXTRAS_CURRENT_FRAGMENT)) ?: currentFragment
        }
    }

    private fun validateFragment(fragment: Fragment, tag: String): Fragment? {
        return if (fm.findFragmentByTag(tag) == null) {
            fm.beginTransaction().add(R.id.container, fragment, tag).hide(fragment).commit()
            null
        } else
            fm.findFragmentByTag(tag)
    }
}
