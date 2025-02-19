package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        val tvNombreUsuario = binding.tvNombreUsuario
        val tvEmailUsuario = binding.tvEmailUsuario
        val tvRolUsuario = binding.tvRolUsuario
        val tvUltimoLogin = binding.tvUltimoLogin

        obtenerInformacionUsuario(tvNombreUsuario, tvEmailUsuario, tvRolUsuario)
        tvUltimoLogin.text = "Último inicio de sesión: ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}"

        return binding.root
    }

    private fun obtenerInformacionUsuario(tvNombreUsuario: TextView, tvEmailUsuario: TextView, tvRolUsuario: TextView) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("name").value.toString()
                        val email = snapshot.child("email").value.toString()
                        val rol = snapshot.child("role").value.toString()

                        tvNombreUsuario.text = nombre
                        tvEmailUsuario.text = email
                        tvRolUsuario.text = "Rol: $rol"

                    } else {
                        tvNombreUsuario.text = "Usuario no encontrado"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    tvNombreUsuario.text = "Error al cargar los datos"
                }
            })
        } else {
            tvNombreUsuario.text = "No has iniciado sesión"
        }
    }
}