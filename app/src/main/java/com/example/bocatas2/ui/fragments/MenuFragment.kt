package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.adapters.BocadillosAdapter
import com.example.bocatas2.databinding.FragmentMenuBinding
import com.example.bocatas2.models.Bocadillo
import com.google.firebase.database.*

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BocadillosAdapter
    private val bocadillos = mutableListOf<Bocadillo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerMenu
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BocadillosAdapter(bocadillos)
        recyclerView.adapter = adapter

        cargarBocadillos()

        return view
    }

    private fun cargarBocadillos() {
        database = FirebaseDatabase.getInstance().reference.child("bocadillos")
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
}