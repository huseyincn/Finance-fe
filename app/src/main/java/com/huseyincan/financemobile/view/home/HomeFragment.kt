package com.huseyincan.financemobile.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.data.adapter.HomeAdapter
import com.huseyincan.financemobile.data.model.SymbolPrice
import com.huseyincan.financemobile.databinding.FragmentHomeBinding
import com.huseyincan.financemobile.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HomeAdapter
    private lateinit var sharedViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searcher: SearchView = binding.searchView
        val queryListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! >= 3) {
                    adapter.setSearchModeOn(newText)
                } else {
                    adapter.setSearchModeOf()
                }
                return true
            }
        }

        searcher.setOnQueryTextListener(queryListener)


        sharedViewModel.startUpdatingPrices()
        adapter = HomeAdapter()

        val recyclerView: RecyclerView = binding.recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        sharedViewModel.averagePriceList.observe(viewLifecycleOwner) {
            adapter.updateItems(it.toList())
        }
        return root
    }

    private fun filterList(newText: String) {
        val filteredList = arrayListOf<SymbolPrice>()
        for (symbol in adapter.getItemsForFilteration()) {
            if (symbol.symbol.startsWith(newText, true)) {
                filteredList.add(symbol)
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No data found.", Toast.LENGTH_LONG).show()
        } else {
            adapter.updateItems(filteredList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}