package com.example.bocatas2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.models.Bocadillo

class BocadillosAdapter(private val bocadillos: List<Bocadillo>) : RecyclerView.Adapter<BocadillosAdapter.BocadilloViewHolder>() {

    class BocadilloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre_bocadillo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tv_descripcion)
        val tvCoste: TextView = itemView.findViewById(R.id.tv_coste)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bocadillo, parent, false)
        return BocadilloViewHolder(view)
    }

    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]
        holder.tvNombre.text = bocadillo.nombre
        holder.tvDescripcion.text = bocadillo.descripcion
        holder.tvCoste.text = "€${bocadillo.coste}"
    }

    override fun getItemCount(): Int = bocadillos.size
}