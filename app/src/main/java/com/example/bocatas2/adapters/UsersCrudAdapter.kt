package com.example.bocatas2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocatas2.R
import com.example.bocatas2.models.User

class UsersCrudAdapter(
    private val usuarios: List<User>,
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
): RecyclerView.Adapter<UsersCrudAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        val tvRole: TextView = itemView.findViewById(R.id.tv_role)
        val tvId: TextView = itemView.findViewById(R.id.tv_id)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btn_editar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btn_eliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_crud, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.tvNombre.text = usuario.name
        holder.tvEmail.text = usuario.email
        holder.tvRole.text = "Rol: ${usuario.role}"
        holder.tvId.text = "ID: ${usuario.id}"

        holder.btnEditar.setOnClickListener {
            onEditClick(usuario)
        }

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(usuario)
        }
    }

    override fun getItemCount(): Int = usuarios.size
}