package com.example.bocatas2.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bocatas2.databinding.FragmentPedirBocataBinding
import com.example.bocatas2.models.Bocadillo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PedirBocataFragment : Fragment() {

    private var _binding: FragmentPedirBocataBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var bCaliente: Bocadillo
    private lateinit var bFrio: Bocadillo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPedirBocataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dia = getDiaSemana()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val bocatasRef = database.child("bocadillos")
        obtenerBocadillos(bocatasRef) { bocatas ->
            bCaliente = filtrarBocatas(bocatas, dia, "caliente")
            bFrio = filtrarBocatas(bocatas, dia, "frio")
        }

        val userId = auth.currentUser?.uid
        binding.calienteBtn.setOnClickListener {
            if (userId != null) {
                hacerPedido(userId, bCaliente)
            }
        }
        binding.frioBtn.setOnClickListener {
            if (userId != null) {
                hacerPedido(userId, bFrio)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verificarPedidoExistente(userId: String, onComplete: (exists: Boolean) -> Unit) {
        val pedidosRef = database.child("pedidos")
        val fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val query = pedidosRef.orderByChild("user_id").equalTo(userId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val pedidoFecha = snapshot.child("fecha").value.toString()

                    val pedidoFechaDate = LocalDate.parse(pedidoFecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val fechaActualDate = LocalDate.parse(fechaActual, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    if (fechaActualDate.isEqual(pedidoFechaDate)) {
                        Log.d("Funciona", "fecha iguales")
                        onComplete(true)
                    } else {
                        Log.d("No funciona", "No coinciden fechas")
                        onComplete(false)
                    }
                } else {
                    Log.d("No funciona", "No se encontro pedido")
                    onComplete(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al verificar el pedido existente: ${error.message}")
                onComplete(false)
            }
        })
    }

    private fun hacerPedido(userId: String, bocata: Bocadillo) {
        verificarPedidoExistente(userId) { exists ->
            if (exists) {
                Toast.makeText(requireContext(), "Ya has realizado un pedido para hoy", Toast.LENGTH_SHORT).show()
            } else {
                val pedidosRef = database.child("pedidos")
                val pedidoId = pedidosRef.push().key

                if (pedidoId != null) {
                    val pedidoData = mapOf(
                        "user_id" to userId,
                        "bocata_id" to bocata.id,
                        "coste_total" to bocata.coste,
                        "fecha" to LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )

                    pedidosRef.child(pedidoId).setValue(pedidoData)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Pedido realizado exitosamente.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error al realizar el pedido: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "No se pudo generar el pedido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerBocadillos(database: DatabaseReference, onComplete: (List<Bocadillo>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bocatas = mutableListOf<Bocadillo>()

                for (bocadilloSnapshot in snapshot.children) {
                    val uid = bocadilloSnapshot.key

                    val bocadilloData = bocadilloSnapshot.getValue(Bocadillo::class.java)

                    if (uid != null && bocadilloData != null) {
                        val bocadillo = bocadilloData.copy(id = uid)
                        bocatas.add(bocadillo)
                    }
                }

                onComplete(bocatas)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al obtener los bocadillos: ${error.message}")
            }
        })
    }

    private fun getDiaSemana(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE", Locale("es", "ES"))
        return today.format(formatter).lowercase().replaceFirstChar { it.titlecase() }
    }

    private fun filtrarBocatas(bocadillos: List<Bocadillo>, dia: String, tipo: String): Bocadillo {
        return bocadillos.filter { it.dia.equals(dia, ignoreCase = true) && it.tipo.equals(tipo, ignoreCase = true) }[0]
    }
}