package com.huseyincan.financemobile.view.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import com.huseyincan.financemobile.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class LoginFragment : Fragment() {

    private val TAG = "EmailPassword"
    val emailRegex: String
        get() = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val mail = binding.loginMail.text.toString()
            val password = binding.loginPassword.text.toString()
            if (checkTextBoxes(mail, password)) {
                signIn(mail, password)
            }
        }
        binding.registerButton.setOnClickListener {
            val mail = binding.loginMail.text.toString()
            val password = binding.loginPassword.text.toString()
            if (checkTextBoxes(mail, password)) {
                createAccount(mail, password)
            }
        }
    }

    private fun checkTextBoxes(mail: String, password: String): Boolean {
        return if (mail.isNotBlank() && password.isNotBlank() && password.length > 8) { // && mail.matches(emailRegex.toRegex())
            true
        } else {
            false
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        lifecycleScope.launch {
            val requestBody: RequestBody = createRequestBody(email, password)
            val response = BackendRetrofitInstance.instance.postLogin(requestBody)
            if (response.isSuccessful) {
                response.body()?.token?.let { TokenData.changeToken(it) }
                updateUI()
            } else {
                println("login is not correct")
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        lifecycleScope.launch {
            val requestBody: RequestBody = createRequestBody(email, password)
            val response = BackendRetrofitInstance.instance.postRegister(requestBody)
            if (response.isSuccessful) {
                response.body()?.token?.let { TokenData.changeToken(it) }
                updateUI()
            } else {
                println("Register is not correct")
            }
        }
    }

    private fun updateUI() {
        if (TokenData.token != null) {
            val fragmentToGo: Fragment = ProfileFragment()
            val container =
                requireActivity().findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
            container.removeAllViews()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragmentToGo)
                .commit()
        } else {
            Toast.makeText(requireContext(), "giriş failed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createRequestBody(username: String, password: String): RequestBody {
        val json = JSONObject()
        json.put("email", username)
        json.put("password", password)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        return RequestBody.create(mediaType, json.toString())
    }

}