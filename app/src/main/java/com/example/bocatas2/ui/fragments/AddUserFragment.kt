package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentAddBocataBinding
import com.example.bocatas2.databinding.FragmentAddUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        val roles = resources.getStringArray(R.array.rol)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, roles)

        binding.spRol.setAdapter(adapter)

        binding.btnGuardar.setOnClickListener {
            guardarUser()
        }

        return binding.root
    }

    private fun guardarUser() {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rol = binding.spRol.text.toString().trim()

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener

                val usuario = mapOf(
                    "id" to userId,
                    "name" to nombre,
                    "email" to email,
                    "role" to rol
                )

                database.child(userId).setValue(usuario)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error al guardar datos del usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al registrar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}