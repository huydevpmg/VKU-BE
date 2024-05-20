package com.dacs.vku.ui.fragments.Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dacs.vku.R
import com.dacs.vku.ui.viewModels.NotificationDaoTaoViewModel
import com.dacs.vku.ui.viewModels.profileViewModel.shareProfileViewModel

private lateinit var notificationDaoTaoViewModel: NotificationDaoTaoViewModel

class Profile_fragment : Fragment() {

    private lateinit var shareProfileViewModel: shareProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareProfileViewModel = ViewModelProvider(requireActivity()).get(shareProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_fragment, container, false)

        val emailTextView: TextView = view.findViewById(R.id.profileEmail)
        val nameTextView: TextView = view.findViewById(R.id.profileName)
        val profileImageView: ImageView = view.findViewById(R.id.profileURL)

        shareProfileViewModel.userData.observe(viewLifecycleOwner, { user ->
            user?.let {
                emailTextView.text = it.email
                nameTextView.text = it.name
            }
        })

        notificationDaoTaoViewModel.profileImageUriLiveData.observe(viewLifecycleOwner, { profileImageUri ->
            profileImageUri?.let {
                // Load profile image using Glide or Picasso
                Glide.with(requireContext())
                    .load(profileImageUri)
                    .into(profileImageView)
            } ?: run {
                // Handle when profile image URI is null
            }
        })

        return view
    }
}