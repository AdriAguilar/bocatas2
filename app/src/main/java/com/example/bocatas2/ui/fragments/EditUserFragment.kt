package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentEditUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditUserFragment : Fragment() {

    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val args: EditUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference.child("users")

        val user = args.user

        val roles = resources.getStringArray(R.array.rol)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, roles)

        binding.etNombre.setText(user.name)
        binding.etEmail.setText(user.email)
        binding.spRol.setText(user.role)
        binding.spRol.setAdapter(adapter)

        binding.btnGuardar.setOnClickListener {
            actualizarUser(user.id)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actualizarUser(id: String) {
        val nombre = binding.etNombre.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val rol = binding.spRol.text.toString().trim()

        if (nombre.isEmpty() || email.isEmpty() || rol.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mapOf(
            "name" to nombre,
            "email" to email,
            "role" to rol
        )
        database.child(id).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario actualizado exitósamente", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al actualizar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}