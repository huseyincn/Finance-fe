package com.huseyincan.financemobile.view.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.adapter.PortfolioAdapter
import com.huseyincan.financemobile.databinding.FragmentPortfolioBinding
import com.huseyincan.financemobile.viewModel.PortfolioViewModel

class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PortfolioAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val portfolioViewModel =
            ViewModelProvider(this).get(PortfolioViewModel::class.java)

        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        portfolioViewModel.makeApiCall()
        adapter = PortfolioAdapter()
        adapter.setOnItemClickListener(object : PortfolioAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val item = adapter.getItem(position)
                val bundle = Bundle()
                bundle.putSerializable(
                    "port",
                    item
                )  // Assuming your SymbolPrice class is Serializable
                val fragmentToGo = PortfolioViewer()
                fragmentToGo.arguments = bundle
//                val container =
//                    requireActivity().findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
//                container.removeAllViews()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragmentToGo)
                    .addToBackStack("a")
                    .commit()
            }
        })
        val recyclerView: RecyclerView = binding.portfoliorecycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        portfolioViewModel.portfoliolist.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
        binding.createPortfoliobutton.setOnClickListener {
            val fragmentToGo = CreatePortfolio()
//            val container =
//                requireActivity().findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
//            container.removeAllViews()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragmentToGo)
                .addToBackStack("x")
                .commit()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}