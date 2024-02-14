package com.example.submission1.di

import android.content.Context
import com.example.submission1.data.entity.FavoriteRepo
import com.example.submission1.data.room.FavoriteRoomDatabase
import com.example.submission1.ui.SettingPreferences
import com.example.submission1.ui.dataStore
import com.example.submission1.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepo {
        val database = FavoriteRoomDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()

        return FavoriteRepo.getInstance(dao)
    }


    fun providePrefences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }


}