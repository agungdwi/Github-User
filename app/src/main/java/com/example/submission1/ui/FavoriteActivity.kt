package com.example.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.R
import com.example.submission1.adapter.ListUserAdapter
import com.example.submission1.databinding.ActivityFavoriteBinding
import com.example.submission1.remote.response.ItemsItem

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager = LinearLayoutManager(this)
        binding.rvView.layoutManager = layoutManager

        supportActionBar?.title = "Favorite Users"

        detailViewModel.getFavorite().observe(this) { it ->
            val items = arrayListOf<ItemsItem>()
            it.map {
                val item =
                    it.avatarUrl?.let { it1 -> ItemsItem(login = it.username, avatarUrl = it1) }
                if (item != null) {
                    items.add(item)
                }
            }
            val adapter = ListUserAdapter()
            adapter.submitList(items)
            binding.rvView.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        menu.findItem(R.id.favorite).isVisible = false
        menu.findItem(R.id.search).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                Intent(this, SettingActitvity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}