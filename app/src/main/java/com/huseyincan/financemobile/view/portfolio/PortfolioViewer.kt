package com.huseyincan.financemobile.view.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.data.adapter.ContentAdapter
import com.huseyincan.financemobile.data.model.Portfolio
import com.huseyincan.financemobile.databinding.FragmentPortfolioViewerBinding

class PortfolioViewer : Fragment() {

    private var _binding: FragmentPortfolioViewerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPortfolioViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = ContentAdapter()
        val recyclerView: RecyclerView = binding.portfoliocontent
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getSerializable("port") as? Portfolio
        if (item != null) {
            adapter.updateItems(item.elements)
            binding.contentowner.text = item.userName
            binding.contentrevenue.text = String.format("%.6f", item.revenue) + "\$"
        }
    }

}