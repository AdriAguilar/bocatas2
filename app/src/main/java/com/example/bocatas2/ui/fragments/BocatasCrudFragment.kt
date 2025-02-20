package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.adapters.BocadillosCrudAdapter
import com.example.bocatas2.databinding.FragmentBocatasCrudBinding
import com.example.bocatas2.models.Bocadillo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*


class BocatasCrudFragment : Fragment() {

    private var _binding: FragmentBocatasCrudBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BocadillosCrudAdapter
    private val bocadillos = mutableListOf<Bocadillo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBocatasCrudBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference.child("bocadillos")

        recyclerView = binding.recyclerBocadillosCrud
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BocadillosCrudAdapter(bocadillos, ::onEditClick, ::onDeleteClick)
        recyclerView.adapter = adapter

        cargarBocadillos()

        binding.fabAddBocadillo.setOnClickListener {
            navigateToAddBocadillo()
        }

        return binding.root
    }

    private fun navigateToAddBocadillo() {
        findNavController().navigate(R.id.addBocataFragment)
    }

    private fun cargarBocadillos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bocadillos.clear()
                for (bocadilloSnapshot in snapshot.children) {
                    val bocadillo = bocadilloSnapshot.getValue(Bocadillo::class.java)
                    if (bocadillo != null) {
                        bocadillos.add(bocadillo)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al cargar los bocadillos: ${error.message}")
            }
        })
    }

    private fun onEditClick(bocadillo: Bocadillo) {
//        findNavController().navigate()
    }

    private fun onDeleteClick(bocadillo: Bocadillo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este bocadillo?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarBocadillo(bocadillo)
            }
            .show()
    }

    private fun eliminarBocadillo(bocadillo: Bocadillo) {
        val key = bocadillo.id
        database.child(key).removeValue()
            .addOnSuccessListener {
                println("Bocadillo eliminado exitosamente")
            }
            .addOnFailureListener { e ->
                println("Error al eliminar el bocadillo: ${e.message}")
            }
    }
}