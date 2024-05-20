package com.huseyincan.financemobile

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.auth
import com.huseyincan.financemobile.databinding.ActivityMainBinding
import com.huseyincan.financemobile.view.home.HomeFragment
import com.huseyincan.financemobile.view.portfolio.PortfolioFragment
import com.huseyincan.financemobile.view.profile.LoginFragment
import com.huseyincan.financemobile.view.profile.ProfileFragment
import com.huseyincan.financemobile.view.profile.TokenData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        setNavBar(navView)
    }

    private fun setNavBar(navView: BottomNavigationView) {
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> sendToFragment(HomeFragment())
                R.id.navigation_portfolio -> sendToFragment(PortfolioFragment())
                R.id.navigation_login -> if (TokenData.token == null) sendToFragment(LoginFragment()) else sendToFragment(ProfileFragment())
            }
            true
        }
    }

    private fun sendToFragment(fragmentToGo: Fragment) {
        val container = this.findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
        container.removeAllViews()
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragmentToGo)
            .commit()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
    }
}