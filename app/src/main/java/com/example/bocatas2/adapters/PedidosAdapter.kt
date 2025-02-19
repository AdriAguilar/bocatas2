package com.example.bocatas2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.models.Pedido

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
        holder.tvSandwich.text = "Bocadillo: ${pedido.bocadillo_id}"
        holder.tvCosteTotal.text = "Coste Total: €${pedido.coste_total}"
    }

    override fun getItemCount(): Int = pedidos.size
}