package com.huseyincan.financemobile.view.portfolio

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
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.adapter.CreatePortfolioAdapter
import com.huseyincan.financemobile.databinding.FragmentCreatePortfolioBinding
import com.huseyincan.financemobile.view.profile.TokenData
import com.huseyincan.financemobile.viewModel.CreatePortfolioViewModel
import com.huseyincan.financemobile.viewModel.HomeViewModel

class CreatePortfolio : Fragment() {

    private var _binding: FragmentCreatePortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CreatePortfolioAdapter
    private lateinit var sharedViewModel: HomeViewModel
    private lateinit var portCreateVM: CreatePortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePortfolioBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        portCreateVM =
            ViewModelProvider(requireActivity()).get(CreatePortfolioViewModel::class.java)
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
        adapter = CreatePortfolioAdapter()
        adapter.setOnItemClickListener(object : CreatePortfolioAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItem(position)
                val bottomSheetDialog = BottomSheetDialog()
                val bundle = Bundle()
                bundle.putSerializable(
                    "item",
                    item
                )  // Assuming your SymbolPrice class is Serializable
                bottomSheetDialog.arguments = bundle
                bottomSheetDialog.show(childFragmentManager, "")
            }
        })
        binding.sendpfolio.setOnClickListener {
            if (TokenData.token != null) {
                portCreateVM.makeApiCall(TokenData.token!!)
                val fragmentToGo = PortfolioFragment()
                val container =
                    requireActivity().findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
                container.removeAllViews()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_fragment_activity_main, fragmentToGo)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "User must be login", Toast.LENGTH_LONG).show()
            }
        }
        val recyclerView: RecyclerView = binding.recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        sharedViewModel.averagePriceList.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
        return root
    }
}