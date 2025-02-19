package com.example.bocatas2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.models.Bocadillo
import com.example.bocatas2.models.Pedido
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.Locale

class PedidosAdapter(private val pedidos: List<Pedido>): RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPedidoId: TextView = itemView.findViewById(R.id.tv_pedido_id)
        val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
        val tvSandwich: TextView = itemView.findViewById(R.id.tv_sandwich)
        val tvCosteTotal: TextView = itemView.findViewById(R.id.tv_coste_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvPedidoId.text = "Pedido ID: ${pedido.id}"
        holder.tvFecha.text = "Fecha: ${pedido.fecha}"
        obtenerBocata(pedido.bocadillo_id) { bocata ->
            if (bocata != null) {
                holder.tvSandwich.text = "Bocadillo: ${bocata.nombre}"
            }
        }
        holder.tvCosteTotal.text = "Coste Total: ${pedido.coste_total.toEuroFormat()}"
    }

    override fun getItemCount(): Int = pedidos.size

    private fun obtenerBocata(bocataId: String, callback: (Bocadillo?) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("bocadillos").child(bocataId)
        database.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bocadillo = snapshot.getValue(Bocadillo::class.java)
                callback(bocadillo)
            } else {
                callback(null)
            }
        }.addOnFailureListener { e ->
            callback(null)
            println("Error al obtener el bocadillo: ${e.message}")
        }
    }

    private fun Double.toEuroFormat(): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "ES"))
        return numberFormat.format(this)
    }
}