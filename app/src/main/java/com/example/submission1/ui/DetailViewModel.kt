package com.example.submission1.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1.data.entity.FavoriteRepo
import com.example.submission1.data.entity.FavoriteUser
import com.example.submission1.remote.response.DetailUserResponse
import com.example.submission1.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val favoriteRepo: FavoriteRepo) : ViewModel() {

    private val _detailUsername = MutableLiveData<DetailUserResponse>()
    val detailUsername: LiveData<DetailUserResponse> = _detailUsername

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getFavorite() = favoriteRepo.getAllFavorite()

    fun getFavoriteByUsername(username: String) = favoriteRepo.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteRepo.insert(favoriteUser)
        }

    }

    fun delete(favoriteUser: FavoriteUser) {
        viewModelScope.launch{
            favoriteRepo.delete(favoriteUser)
        }

    }

    fun setUserDetail(username: String) {
        _isError.postValue(false)
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _detailUsername.postValue(response.body())


                } else {
                    _isError.postValue(true)
                    Log.e(TAG, "onFailure: ${response.message()}")


                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isError.postValue(true)
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}