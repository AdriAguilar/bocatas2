package com.example.bocatas2.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bocatas2.R
import com.example.bocatas2.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var emailText: EditText
    private lateinit var pwText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users")

        emailText = binding.email
        pwText = binding.password
        loginButton = binding.loginBtn

        loginButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val password = pwText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Debes completar todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email, password)
        }

        binding.biometricBtn.setOnClickListener {
            if (verificarDisponibilidadBiometrica(requireContext())) {
                mostrarPromptBiometrico()
            } else {
                Toast.makeText(requireContext(), "La autenticación biométrica no está disponible.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        emailText.text.clear()
        pwText.text.clear()
    }

    private fun verificarDisponibilidadBiometrica(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun mostrarPromptBiometrico() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                println("Autenticación biométrica exitosa")
                iniciarSesionConFirebase()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(), "Error de autenticación: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(requireContext(), "Autenticación fallida", Toast.LENGTH_SHORT).show()
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación Biométrica")
            .setSubtitle("Inicia sesión con tu huella digital")
            .setDescription("Coloca tu dedo en el lector de huellas para continuar.")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun iniciarSesionConFirebase() {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)
        val password = sharedPref.getString("password", null)

        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        getUserRole()
                    } else {
                        Toast.makeText(context, "Error al iniciar sesión con Firebase: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "No se encontraron credenciales almacenadas.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    guardarCredenciales(email, password)
                    getUserRole()
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun guardarCredenciales(email: String, password: String) {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    private fun getUserRole() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val role = snapshot.child("role").value.toString()
                        navigateToFragment(role)
                    } else {
                        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToFragment(role: String) {
        when (role) {
            "admin" -> findNavController().navigate(R.id.adminFragment)
            "alumno" -> findNavController().navigate(R.id.alumnoFragment)
            else -> {
                Toast.makeText(requireContext(), "Rol no reconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
