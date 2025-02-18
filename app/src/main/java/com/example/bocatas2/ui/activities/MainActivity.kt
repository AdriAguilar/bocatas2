package com.example.bocatas2.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bocatas2.R
import com.example.bocatas2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostRole.id) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = navGraph
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(binding.navHostRole.id)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}