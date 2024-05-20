package com.huseyincan.financemobile.view.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huseyincan.financemobile.R
import com.huseyincan.financemobile.data.adapter.PortfolioAdapter
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import com.huseyincan.financemobile.databinding.FragmentProfileBinding
import com.huseyincan.financemobile.view.portfolio.PortfolioViewer
import com.huseyincan.financemobile.viewModel.ProfileViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: ProfileViewModel
    private lateinit var adapter: PortfolioAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.singoutbutton.setOnClickListener {
            signOutFromAuth()
        }
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
        val recyclerView: RecyclerView = binding.profilerecycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        binding.profileImage.setOnClickListener {
            photoPhoto()
        }
        sharedViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.nameText.text = it.user.email
                if (it.user.photo != null) {
                    val byteArray = Base64.decode(it.user.photo, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    binding.profileImage.setImageBitmap(bitmap)
                }
                var reven: Double = 0.0
                for (x in it.portfolios) {
                    reven += x.revenue
                }
                binding.userrevenue.text = "User's Revenue= ${String.format("%.6f", reven)} \$"
                adapter.updateItems(it.portfolios)
            }
        }
        return root
    }

    var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode === RESULT_OK) {
                val image_uri: Uri? = it.data?.data
                binding.profileImage.setImageURI(image_uri)
                sendPhoto()
            }
        }
    )

    private fun photoPhoto() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private fun sendPhoto() {
        lifecycleScope.launch {
            val bitmap = (binding.profileImage.drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray)
            val filePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
            val response =
                BackendRetrofitInstance.instance.savePhoto("Bearer " + TokenData.token!!, filePart)
            if (response.isSuccessful) {
                sharedViewModel.makeApiCall()
                Log.i("FMobile", "Now photo comes from disk.")
            } else {
                println("Photo sending is not correct")
            }
        }
    }

    private fun signOutFromAuth() {
        TokenData.changeToken(null)
        val container =
            requireActivity().findViewById<ViewGroup>(R.id.nav_host_fragment_activity_main)
        container.removeAllViews()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, LoginFragment())
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.makeApiCall()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}