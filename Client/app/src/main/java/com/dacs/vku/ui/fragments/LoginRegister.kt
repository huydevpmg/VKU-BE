package com.dacs.vku.ui.fragments

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dacs.vku.R
import com.dacs.vku.api.GoogleAuthUiClient
import com.dacs.vku.api.RetrofitInstance
import com.dacs.vku.api.UserData
import com.dacs.vku.databinding.FragmentLoginRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRegister : Fragment() {


    private var _binding: FragmentLoginRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleAuthUiClient = GoogleAuthUiClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            try {
                val intentSender = googleAuthUiClient.signIn()
                intentSender?.let {
                    startIntentSenderForResult(
                        it, RC_SIGN_IN, null, 0, 0, 0, null
                    )
                }
            } catch (e: Exception) {
                Log.e("VKUUUUU", "Error starting sign-in", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && data != null) {
            lifecycleScope.launch {
                val signInResult = googleAuthUiClient.signInWithIntent(data)
                signInResult.user?.let {
                    sendUserDataToServer(it)
                } ?: run {
                    Toast.makeText(requireContext(), signInResult.errorMessage ?: "Sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendUserDataToServer(userData: UserData) {
        val apiService = RetrofitInstance.api
        val call = apiService.verifyUser(userData)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.e("VKUUUUU", "User data sent successfully $userData")
                    val bundle = Bundle().apply {
                        putSerializable("userData", userData)
                    }
                    try {
                        Log.e("VKUUUUU", "Bundle $bundle")

                        findNavController().navigate(R.id.action_AuthenticationFragment_to_ProfileFragment, bundle)
                    } catch (e: IllegalArgumentException) {
                        Log.e("NavigationError", "Navigation failed: ${e.message}")
                    }
                } else {
                    Log.e("VKUUUUU", "Failed to send user data: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to send user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("VKUUUUU", "Error sending user data", t)
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
