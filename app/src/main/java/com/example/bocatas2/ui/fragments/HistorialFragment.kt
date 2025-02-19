package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.adapters.PedidosAdapter
import com.example.bocatas2.databinding.FragmentHistorialBinding
import com.example.bocatas2.models.Pedido
import com.google.firebase.database.*

class HistorialFragment : Fragment() {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidosAdapter
    private val pedidos = mutableListOf<Pedido>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerHistorial
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PedidosAdapter(pedidos)
        recyclerView.adapter = adapter

        cargarPedidos()

        return binding.root
    }

    private fun cargarPedidos() {
        database = FirebaseDatabase.getInstance().reference.child("pedidos")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pedidos.clear()
                for (pedidoSnapshot in snapshot.children) {
                    val pedido = pedidoSnapshot.getValue(Pedido::class.java)
                    if (pedido != null) {
                        pedidos.add(pedido)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al cargar los pedidos: ${error.message}")
            }
        })
    }
}