package com.example.bocatas2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentAdminBinding
import com.example.bocatas2.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val navController = childFragmentManager.findFragmentById(binding.navHostAdmin.id)
            ?.let { it as NavHostFragment }?.navController

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }

        binding.bocatasBtn.setOnClickListener { navController?.navigate(R.id.bocatasCrudFragment) }
        binding.usersBtn.setOnClickListener { navController?.navigate(R.id.usersCrudFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}