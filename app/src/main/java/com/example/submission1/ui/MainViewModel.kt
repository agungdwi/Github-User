package com.example.submission1.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1.remote.response.GithubResponse
import com.example.submission1.remote.response.ItemsItem
import com.example.submission1.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _user = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "agung dwi"
    }

    init {
        findUser(USERNAME)
    }

    fun findUser(user: String) {
        _isError.postValue(false)
        _isEmpty.postValue(false)
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getUser(user)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    if (response.body()?.items.isNullOrEmpty()) {
                        _isEmpty.postValue(true)
                    }
                    _user.postValue(response.body()?.items)
                } else {
                    _isError.postValue(true)
                    Log.e(TAG, "onFailure: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
