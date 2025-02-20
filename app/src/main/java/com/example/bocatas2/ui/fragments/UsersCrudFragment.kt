package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.adapters.UsersCrudAdapter
import com.example.bocatas2.databinding.FragmentUsersCrudBinding
import com.example.bocatas2.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersCrudFragment : Fragment() {

    private var _binding: FragmentUsersCrudBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsersCrudAdapter
    private val users = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersCrudBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference.child("users")

        recyclerView = binding.recyclerUsersCrud
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UsersCrudAdapter(users, ::onEditClick, ::onDeleteClick)
        recyclerView.adapter = adapter

        cargarUsuarios()
        println(users.toString())
        binding.fabAddUser.setOnClickListener {
            navigateToAddUsuario()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargarUsuarios() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (userSnapshot in snapshot.children) {
                    val usuario = userSnapshot.getValue(User::class.java)
                    if (usuario != null) {
                        users.add(usuario)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al cargar usuarios: ${error.message}")
            }
        })
    }

    private fun navigateToAddUsuario() {
        findNavController().navigate(R.id.addUserFragment)
    }

    private fun onEditClick(usuario: User) {
        val action = UsersCrudFragmentDirections.actionUsersCrudFragmentToEditUserFragment(usuario)
        findNavController().navigate(action)
    }

    private fun onDeleteClick(usuario: User) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarUsuario(usuario)
            }
            .show()
    }

    private fun eliminarUsuario(usuario: User) {
        val userId = usuario.id

        database.child(userId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario eliminado exitósamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al eliminar datos del usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}