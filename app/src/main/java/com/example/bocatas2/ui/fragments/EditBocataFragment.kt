package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentEditBocataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditBocataFragment : Fragment() {

    private var _binding: FragmentEditBocataBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val args: EditBocataFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBocataBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance().reference.child("bocadillos")

        val bocadillo = args.bocadillo

        val dias = resources.getStringArray(R.array.dias)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, dias)

        binding.etNombre.setText(bocadillo.nombre)
        binding.etDescripcion.setText(bocadillo.descripcion)
        binding.etCoste.setText(bocadillo.coste.toString())
        binding.spDia.setText(bocadillo.dia)
        binding.spDia.setAdapter(adapter)

        if (bocadillo.tipo == "frio") {
            binding.rbFrio.isChecked = true
        } else {
            binding.rbCaliente.isChecked = true
        }

        binding.btnGuardarCambios.setOnClickListener {
            actualizarBocadillo(bocadillo.id)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actualizarBocadillo(id: String) {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val coste = binding.etCoste.text.toString().toDoubleOrNull() ?: 0.0
        val tipo = if (binding.rbFrio.isChecked) "frio" else "caliente"
        val dia = binding.spDia.text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty() || coste <= 0 || dia.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val updates = mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "coste" to coste,
            "tipo" to tipo,
            "dia" to dia
        )
        database.child(id).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Bocadillo actualizado exitósamente", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al actualizar el bocadillo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}