package com.example.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submission1.R
import com.example.submission1.adapter.SectionPagerAdapter
import com.example.submission1.data.entity.FavoriteUser
import com.example.submission1.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var name: String
    private lateinit var avatarUrl: String
    private val detailViewModel by viewModels<DetailViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("key_user").toString()
        avatarUrl = intent.getStringExtra("key_avatar").toString()

        val data = FavoriteUser(name, avatarUrl)

        detailViewModel.setUserDetail(name)

        supportActionBar?.title = name

        detailViewModel.detailUsername.observe(this) {
            binding.tvDetailName.text = it.name
            binding.tvLoginName.text = it.login
            binding.tvFollowers.text = resources.getString(R.string.followers, it.followers)
            binding.tvFollowing.text = resources.getString(R.string.following, it.following)
            Glide.with(this)
                .load(it.avatarUrl)
                .into(binding.imgDetailPhoto)

        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.apply {
            username = name
        }

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detailViewModel.isError.observe(this) {
            showError(it)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getFavoriteByUsername(username = name).observe(this) {
            if (it == null) {
                binding.favBtn.text = resources.getString(R.string.favorite)
                binding.favBtn.setOnClickListener {
                    detailViewModel.insert(data)
                }
            } else {
                binding.favBtn.text = resources.getString(R.string.unfavorite)
                binding.favBtn.setOnClickListener {
                    detailViewModel.delete(data)
                }

            }
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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}