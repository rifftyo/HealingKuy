package com.rifftyo.healingkuy.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rifftyo.core.data.Resource
import com.rifftyo.core.utils.TokenManager
import com.rifftyo.healingkuy.databinding.DialogCommingSoonBinding
import com.rifftyo.healingkuy.databinding.DialogEditProfileBinding
import com.rifftyo.healingkuy.databinding.DialogLogoutBinding
import com.rifftyo.healingkuy.databinding.FragmentProfileBinding
import com.rifftyo.healingkuy.ui.login.LoginActivity
import com.rifftyo.healingkuy.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var username: String
    private lateinit var profileUrl: String

    private var selectedImageUri: Uri? = null

    private var dialogBinding: DialogEditProfileBinding? = null
    private var alertDialog: AlertDialog? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri

            val fileName = ImageUtils.getFileNameFromUri(requireContext().contentResolver, uri)
            val fileBytes = ImageUtils.getBytesFromUri(requireContext().contentResolver, uri)

            if (fileBytes != null) {
                viewModel.setProfileImage(fileName, fileBytes)
                dialogBinding?.imgEditProfile?.setImageURI(selectedImageUri)
            }
        } else {
            Toast.makeText(requireContext(), "Tidak ada gambar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()

        binding.btnLogout.setOnClickListener {
            logout()
        }

        setupComingSoonButtons()

        setupEditProfile()

        setupSettingApplication()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dialogBinding = null
        alertDialog = null
    }

    private fun getUser() {
        viewModel.user.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvUsername.text = resource.data?.username
                    Glide.with(requireContext())
                        .load(resource.data?.profile)
                        .into(binding.imgProfile)

                    username = resource.data?.username ?: ""
                    profileUrl = resource.data?.profile ?: ""
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Terjadi kesalahan" + resource.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun logout() {
        val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            lifecycleScope.launch {
                val tokenManager = TokenManager(requireContext())
                tokenManager.deleteToken()
                viewModel.deleteUser()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        alertDialog.show()
    }

    private fun showComingSoon() {
        val dialogBinding = DialogCommingSoonBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog.show()
    }

    private fun setupComingSoonButtons() {
        listOf(
            binding.tvChangeEmail,
            binding.imgChangeEmail,
            binding.tvChangePassword,
            binding.imgChangePassword,
            binding.tvChangeLanguage,
            binding.imgChangeLanguage,
            binding.tvChangeTheme,
            binding.imgChangeTheme
        ).forEach { view ->
            view.setOnClickListener { showComingSoon() }
        }
    }

    private fun showUpdateProfile() {
        dialogBinding = DialogEditProfileBinding.inflate(layoutInflater)

        alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding!!.root)
            .setCancelable(true)
            .create()

        alertDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog?.setOnDismissListener {
            dialogBinding = null
            alertDialog = null
        }

        dialogBinding!!.usernameEditText.setText(username)

        if (selectedImageUri != null) {
            dialogBinding!!.imgEditProfile.setImageURI(selectedImageUri)
        } else if (profileUrl.isNotEmpty()) {
            Glide.with(requireContext())
                .load(profileUrl)
                .into(dialogBinding!!.imgEditProfile)
        }

        val pickImage = {
            launcherGallery.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        dialogBinding!!.imgEditProfile.setOnClickListener { pickImage() }

        dialogBinding!!.btnEditProfile.setOnClickListener { pickImage() }

        dialogBinding!!.btnUpdate.setOnClickListener {
            val newUsername = dialogBinding!!.usernameEditText.text.toString()
            val profileName = viewModel.profileName.value
            val profileBytes = viewModel.profileBytes.value

            if (newUsername.isNotEmpty()) {
                viewModel.updateProfile(newUsername, profileName, profileBytes).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            dialogBinding!!.btnUpdate.text = ""
                            dialogBinding!!.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            dialogBinding!!.progressBar.visibility = View.GONE
                            dialogBinding!!.btnUpdate.text = "Perbarui Profil"
                            alertDialog!!.dismiss()
                            getUser()
                        }
                        is Resource.Error -> {
                            dialogBinding!!.progressBar.visibility = View.GONE
                            dialogBinding!!.btnUpdate.text = "Perbarui Profil"
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        alertDialog!!.show()
    }

    private fun setupEditProfile() {
        listOf(
            binding.tvChangeProfile,
            binding.imgChangeProfile
        ).forEach {
            it.setOnClickListener { showUpdateProfile() }
        }
    }

    private fun showSettingApplication() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun setupSettingApplication() {
        listOf(
            binding.tvAppSetting,
            binding.imgChangeSetting
        ).forEach {
            it.setOnClickListener { showSettingApplication() }
        }
    }
}