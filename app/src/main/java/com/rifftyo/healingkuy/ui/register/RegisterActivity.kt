package com.rifftyo.healingkuy.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rifftyo.core.data.Resource
import com.rifftyo.healingkuy.MainActivity
import com.rifftyo.healingkuy.databinding.ActivityRegisterBinding
import com.rifftyo.healingkuy.ui.login.LoginActivity
import com.rifftyo.healingkuy.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            handleImage(uri)
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            goToLogin()
        }

        binding.imgProfile.setOnClickListener {
            startGallery()
        }

        binding.btnEditProfile.setOnClickListener {
            startGallery()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handleImage(uri: Uri) {
        val fileName = ImageUtils.getFileNameFromUri(contentResolver, uri)
        val fileBytes = ImageUtils.getBytesFromUri(contentResolver, uri)

        if (fileBytes != null) {
            viewModel.setProfileImage(fileName, fileBytes)
            binding.imgProfile.setImageURI(uri)
            Log.d("Image", "File Name: $fileName")
        } else {
            Toast.makeText(this, "Failed to read image data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val profileName = viewModel.profileName.value
        val profileBytes = viewModel.profileBytes.value

        if (profileName == null || profileBytes == null) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.register(username, email, password).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnRegister.text = ""
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.text = "Daftar"
                    goToHome()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.text = "Daftar"
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}