package com.example.submission1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1.adapter.ListUserAdapter
import com.example.submission1.databinding.FragmentFollowBinding
import com.example.submission1.remote.response.ItemsItem
import kotlin.properties.Delegates

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()
    private var position by Delegates.notNull<Int>()
    private lateinit var username: String
    private var adapterFollow: ListUserAdapter? = null // Deklarasikan adapter sebagai nullable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        // Inisialisasi adapter di sini
        adapterFollow = ListUserAdapter()

        binding.rvFollow.adapter = adapterFollow // Mengatur adapter pada RecyclerView

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.getFollowers(username)
        followViewModel.getFollowing(username)

        if (position == 1) {
            followViewModel.followers.observe(viewLifecycleOwner) {
                setFollowData(it)

            }
        } else {
            followViewModel.following.observe(viewLifecycleOwner) {
                setFollowData(it)
            }
        }

        followViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun setFollowData(list: List<ItemsItem>) {
        adapterFollow?.submitList(list)
        binding.apply {
            rvFollow.setHasFixedSize(true)
            rvFollow.adapter = adapterFollow
        }
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(requireActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val ARG_USERNAME = "extra_name"
        const val ARG_POSITION = "extra_position"

    }


}