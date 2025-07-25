package com.rifftyo.healingkuy.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rifftyo.core.data.Resource
import com.rifftyo.healingkuy.R
import com.rifftyo.healingkuy.databinding.ActivityDetailBinding
import com.rifftyo.healingkuy.databinding.DialogRatingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var titleDestination: String
    private lateinit var rating: String
    private var isBookmarked: Boolean = false

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            fetchDetail(id)
            getRateDetail(id)
            binding.imgBackgroundRatingNow.setOnClickListener {
                showRatingDialog(id, titleDestination)
            }
        }

        binding.bgBack.setOnClickListener {
            finish()
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.imgBookmark.setOnClickListener {
            if (isBookmarked) {
                deleteBookmark(id.toString())
            } else {
                addBookmark(id.toString())
            }
            handleBookmarkIcon(isBookmarked)
        }

        binding.bgBookmark.setOnClickListener {
            if (isBookmarked) {
                deleteBookmark(id.toString())
            } else {
                addBookmark(id.toString())
            }
            handleBookmarkIcon(isBookmarked)
        }
    }

    private fun fetchDetail(id: String) {
        detailViewModel.detailDestination(id).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvName.text = resource.data?.name
                    titleDestination = resource.data?.name.toString()
                    binding.tvDescription.text = resource.data?.description
                    Glide.with(this@DetailActivity)
                        .load(resource.data?.image)
                        .into(binding.imgDestination)
                    binding.tvRating.text = resource.data?.rating.toString()
                    binding.tvLocation.text = resource.data?.city
                    isBookmarked = resource.data?.isBookmark == true
                    handleBookmarkIcon(isBookmarked)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun getRateDetail(id: String) {
        detailViewModel.getRateDestination(id).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    rating = resource.data.toString()
                    binding.tvRatingValue.text = rating
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun postRateDestination(id: String, rating: Double, progressBar: ProgressBar, textView: TextView) {
        detailViewModel.postRateDestination(id, rating).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    textView.text = ""
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    textView.text = "Kirim"
                    getRateDetail(id)
                    fetchDetail(id)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    textView.text = "Kirim"
                }
            }
        }
    }

    private fun updateRateDestination(id: String, rating: Double, progressBar: ProgressBar, textView: TextView) {
        detailViewModel.putRateDestination(id, rating).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    textView.text = ""
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    textView.text = "Update"
                    getRateDetail(id)
                    fetchDetail(id)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    textView.text = "Update"
                }
            }
        }
    }

    private fun handleBookmarkIcon(isBookmark: Boolean) {
        if (isBookmark) {
            binding.imgBookmark.setImageResource(R.drawable.bookmark_on)
        } else {
            binding.imgBookmark.setImageResource(R.drawable.bookmark_icon)
        }
    }

    private fun addBookmark(id: String) {
        detailViewModel.addBookmark(id).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    isBookmarked = true
                    handleBookmarkIcon(true)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun deleteBookmark(id: String) {
        detailViewModel.deleteBookmark(id).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    isBookmarked = false
                    handleBookmarkIcon(false)
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showRatingDialog(id: String, name: String) {
        val dialogBinding = DialogRatingBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()


        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        if (rating == "0.0") {
            dialogBinding.btnSubmitRating.text = "Kirim"
        } else {
            dialogBinding.btnSubmitRating.text = "Update"
        }

        dialogBinding.tvTitle.text = name
        dialogBinding.btnSubmitRating.setOnClickListener {
            val ratingValue = dialogBinding.ratingBar.rating.toDouble()
            if (ratingValue > 0) {
                if (rating == "0.0") {
                    postRateDestination(id, ratingValue, dialogBinding.progressBar, dialogBinding.btnSubmitRating)
                } else {
                    updateRateDestination(id, ratingValue, dialogBinding.progressBar, dialogBinding.btnSubmitRating)
                }
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Rating tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

}