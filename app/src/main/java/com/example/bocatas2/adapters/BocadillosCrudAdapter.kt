package com.example.bocatas2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.models.Bocadillo

class BocadillosCrudAdapter(
    private val bocadillos: List<Bocadillo>,
    private val onEditClick: (Bocadillo) -> Unit,
    private val onDeleteClick: (Bocadillo) -> Unit
) : RecyclerView.Adapter<BocadillosCrudAdapter.BocadilloViewHolder>() {

    class BocadilloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tv_descripcion)
        val tvId: TextView = itemView.findViewById(R.id.tv_id)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btn_editar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btn_eliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bocadillo_crud, parent, false)
        return BocadilloViewHolder(view)
    }

    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]

        holder.tvNombre.text = bocadillo.nombre
        holder.tvDescripcion.text = bocadillo.descripcion
        holder.tvId.text = "ID: ${bocadillo.id}"

        holder.btnEditar.setOnClickListener {
            onEditClick(bocadillo)
        }

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(bocadillo)
        }
    }

    override fun getItemCount(): Int = bocadillos.size
}