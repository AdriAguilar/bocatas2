package com.example.bocatas2.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.bocatas2.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val userRole = intent.getStringExtra("userRole") ?: "alumno"

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_role) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        when (userRole) {
            "admin" -> navGraph.setStartDestination(R.id.adminFragment)
            "alumno" -> navGraph.setStartDestination(R.id.alumnoFragment)
            else -> {
                Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        navController.graph = navGraph
    }
}