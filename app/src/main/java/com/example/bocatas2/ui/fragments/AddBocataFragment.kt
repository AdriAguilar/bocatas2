package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentAddBocataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddBocataFragment : Fragment() {

    private var _binding: FragmentAddBocataBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBocataBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference.child("bocadillos")

        val dias = resources.getStringArray(R.array.dias)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, dias)

        binding.spDia.setAdapter(adapter)

        binding.btnGuardar.setOnClickListener {
            guardarBocadillo()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun guardarBocadillo() {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val coste = binding.etCoste.text.toString().toDoubleOrNull() ?: 0.0
        val tipo = if (binding.rbFrio.isChecked) "frio" else "caliente"
        val dia = binding.spDia.text.toString()

        if (nombre.isEmpty() || descripcion.isEmpty() || coste <= 0 || dia.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val key = database.push().key ?: return

        val bocadillo = mapOf(
            "id" to key,
            "nombre" to nombre,
            "descripcion" to descripcion,
            "coste" to coste,
            "tipo" to tipo,
            "dia" to dia
        )

        database.child(key).setValue(bocadillo)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Bocadillo creado exitosamente", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al crear el bocadillo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}