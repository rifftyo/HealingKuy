package com.rifftyo.healingkuy.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifftyo.core.data.Resource
import com.rifftyo.core.ui.CityDestinationsAdapter
import com.rifftyo.healingkuy.databinding.FragmentBookmarkBinding
import com.rifftyo.healingkuy.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var adapter: CityDestinationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeBookmarks()

        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Boolean>("refresh_bookmark")?.observe(viewLifecycleOwner) { shouldRefresh ->
            if (shouldRefresh == true) {
                viewModel.refreshBookmarks()
                savedStateHandle.remove<Boolean>("refresh_bookmark")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshBookmarks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = CityDestinationsAdapter()
        binding.rvCity.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCity.setHasFixedSize(true)
        binding.rvCity.adapter = adapter

        adapter.onItemClick = { selectedData ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, selectedData.id)
            startActivity(intent)
        }
    }

    private fun observeBookmarks() {
        viewModel.bookmarkDestinations.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvCity.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = resource.data

                    if (data.isNullOrEmpty()) {
                        binding.tvTitle.visibility = View.GONE
                        binding.rvCity.visibility = View.GONE
                        binding.imgNotFound.visibility = View.VISIBLE
                        binding.tvNotFound.visibility = View.VISIBLE
                    } else {
                        adapter.submitList(data)
                        binding.tvTitle.visibility = View.VISIBLE
                        binding.rvCity.visibility = View.VISIBLE
                        binding.imgNotFound.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvCity.visibility = View.VISIBLE
                }
            }
        }
    }
}