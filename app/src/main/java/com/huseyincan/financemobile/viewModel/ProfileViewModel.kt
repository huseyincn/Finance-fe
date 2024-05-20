package com.huseyincan.financemobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huseyincan.financemobile.data.model.UserResponse
import com.huseyincan.financemobile.data.repository.BackendRetrofitInstance
import com.huseyincan.financemobile.view.profile.TokenData
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private var _user: MutableLiveData<UserResponse> = MutableLiveData()
    val user: LiveData<UserResponse> get() = _user


    fun makeApiCall() {
        viewModelScope.launch {
            if (TokenData.token != null) {
                Log.i("token", TokenData.token.toString())
                val response =
                    BackendRetrofitInstance.instance.fetchUserData("Bearer " + TokenData.token!!)
                handleResponse(response)
            }
        }
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                _user.value = it
            }
        } else {
            response.errorBody()?.let { Log.e("", it.string()) }
        }
    }
}