package com.example.bocatas2.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bocatas2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        val emailText: EditText = findViewById(R.id.email)
        val pwText: EditText = findViewById(R.id.password)
        val login: Button = findViewById(R.id.loginBtn)

        login.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = pwText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Debes completar todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserRole()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getUserRole() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val role = snapshot.child("role").value.toString()
                        when(role) {
                            "admin" -> Toast.makeText(this@LoginActivity, "Soy Admin", Toast.LENGTH_SHORT).show()
                            "alumno" -> Toast.makeText(this@LoginActivity, "Soy un alumno", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this@LoginActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this@LoginActivity, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}