package com.example.bocatas2.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bocatas2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var emailText: EditText
    private lateinit var pwText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        emailText = rootView.findViewById(R.id.email)
        pwText = rootView.findViewById(R.id.password)
        loginButton = rootView.findViewById(R.id.loginBtn)

        loginButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = pwText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Debes completar todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email, password)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        emailText.text.clear()
        pwText.text.clear()
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    getUserRole()
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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
                        navigateToFragment(role)
                    } else {
                        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToFragment(role: String) {
        when (role) {
            "admin" -> findNavController().navigate(R.id.adminFragment)
            "alumno" -> findNavController().navigate(R.id.alumnoFragment)
            else -> {
                Toast.makeText(requireContext(), "Rol no reconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
