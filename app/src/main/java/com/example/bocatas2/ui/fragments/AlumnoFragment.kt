package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentAlumnoBinding
import com.google.firebase.auth.FirebaseAuth

class AlumnoFragment : Fragment() {

    private var _binding: FragmentAlumnoBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val navController = childFragmentManager.findFragmentById(binding.navHostAlumno.id)
            ?.let { it as NavHostFragment }?.navController

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }

        binding.pedirBocataBtn.setOnClickListener { navController?.navigate(R.id.pedirBocataFragment) }
        binding.historialBtn.setOnClickListener { navController?.navigate(R.id.historialFragment) }
        binding.menuBtn.setOnClickListener { navController?.navigate(R.id.menuFragment) }
        binding.perfilBtn.setOnClickListener { navController?.navigate(R.id.perfilFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}