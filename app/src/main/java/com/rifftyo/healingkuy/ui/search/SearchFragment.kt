package com.rifftyo.healingkuy.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Category
import com.rifftyo.core.ui.CityAdapter
import com.rifftyo.healingkuy.R
import com.rifftyo.healingkuy.databinding.FragmentSearchBinding
import com.rifftyo.healingkuy.ui.category.CategoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels()

    private var hasNavigatedToSearchValue = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            handleCategory(categories)
            binding.progressBar.visibility = View.GONE
        }

        val cityAdapter = CityAdapter()
        setupCityRecyclerView(cityAdapter)

        cityAdapter.onItemClick = { selectedData ->
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(CategoryActivity.EXTRA_CITY, selectedData.name)
            intent.putExtra(CategoryActivity.TYPE, "City")
            startActivity(intent)
        }

        viewModel.cities.observe(viewLifecycleOwner) {
            cityAdapter.submitList(it)
        }

        binding.svDestinations.addTextChangedListener { editable ->
            val query = editable.toString()
            viewModel.queryChannel.value = query
            Log.d("SearchFragment", "Query changed: $query")

            if (query.isNotBlank()) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, SearchValueFragment())
                    .commit()
                hasNavigatedToSearchValue = true
                binding.defaultSearch.visibility = View.GONE
            } else {
                val fragment = childFragmentManager.findFragmentById(R.id.main_frame)
                if (fragment is SearchValueFragment) {
                    childFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()
                }

                binding.defaultSearch.visibility = View.VISIBLE
                hasNavigatedToSearchValue = false
            }
        }

        handleSearch()

    }

    override fun onResume() {
        super.onResume()
        hasNavigatedToSearchValue = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCityRecyclerView(adapter: CityAdapter) {
        binding.rvCity.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
        }

        adapter.onItemClick = { selectedData ->
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(CategoryActivity.EXTRA_CITY, selectedData.name)
            intent.putExtra(CategoryActivity.TYPE, "City")
            startActivity(intent)
        }
    }

    private fun handleCategory(categories: List<Category>) {
        val mainCategory = categories[0]
        val otherCategories = categories.drop(1).take(6)

        binding.tvTitleMainCategory.text = mainCategory.name

        Glide.with(requireContext())
            .load(mainCategory.image)
            .into(binding.imgMainCategory)

        val intent = Intent(context, CategoryActivity::class.java).apply {
            putExtra(CategoryActivity.EXTRA_CITY, mainCategory.name)
            putExtra(CategoryActivity.TYPE, "Category")
        }

        binding.imgMainCategory.setOnClickListener {
            startActivity(intent)
        }

        binding.bgMainCategory.setOnClickListener {
            startActivity(intent)
        }

        val imageViews = listOf(
            binding.imgFirstCategory,
            binding.imgSecondCategory,
            binding.imgThirdCategory,
            binding.imgFourCategory,
            binding.imgFiveCategory,
            binding.imgSixCategory
        )

        val backgroundViews = listOf(
            binding.bgFirstCategory,
            binding.bgSecondCategory,
            binding.bgThirdCategory,
            binding.bgFourCategory,
            binding.bgFiveCategory,
            binding.bgSixCategory
        )

        val textViews = listOf(
            binding.tvFirstCategory,
            binding.tvSecondCategory,
            binding.tvThirdCategory,
            binding.tvFourCategory,
            binding.tvFiveCategory,
            binding.tvSixCategory
        )

        otherCategories.forEachIndexed { index, category ->
            textViews[index].text = category.name
            Glide.with(requireContext())
                .load(category.image)
                .into(imageViews[index])

            val intent = Intent(context, CategoryActivity::class.java).apply {
                putExtra(CategoryActivity.EXTRA_CITY, category.name)
                putExtra(CategoryActivity.TYPE, "Category")
            }

            imageViews[index].setOnClickListener { startActivity(intent) }
            backgroundViews[index].setOnClickListener { startActivity(intent) }
        }
    }

    private fun handleSearch() {
        viewModel.searchResultLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}