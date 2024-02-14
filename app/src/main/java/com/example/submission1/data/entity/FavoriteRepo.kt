package com.example.submission1.data.entity

import androidx.lifecycle.LiveData
import com.example.submission1.data.room.FavoriteUserDao
import com.example.submission1.utils.AppExecutors

class FavoriteRepo private constructor(
    private val favoriteUserDao: FavoriteUserDao
) {
    fun getAllFavorite(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavorite()

    fun getFavoriteUserByUsername(username:String): LiveData<FavoriteUser> = favoriteUserDao.getFavoriteUserByUsername(username)

    suspend fun insert(favoriteUser: FavoriteUser) {
        favoriteUserDao.insert(favoriteUser)

    }

    suspend fun delete(favoriteUser: FavoriteUser) {
        favoriteUserDao.delete(favoriteUser)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepo? = null
        fun getInstance(
            favoriteUserDao: FavoriteUserDao,
        ): FavoriteRepo =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepo(favoriteUserDao)
            }.also { instance = it }
    }
}