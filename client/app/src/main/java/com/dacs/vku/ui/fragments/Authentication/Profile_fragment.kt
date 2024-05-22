package com.dacs.vku.ui.fragments.Authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dacs.vku.R
import com.dacs.vku.api.UserData
import com.dacs.vku.databinding.FragmentNotificationBinding
import com.dacs.vku.databinding.FragmentProfileFragmentBinding
import com.dacs.vku.ui.fragments.NotificationFragmentArgs
import com.dacs.vku.ui.viewModels.NotificationDaoTaoViewModel


class Profile_fragment : Fragment(R.layout.fragment_profile_fragment) {
    private var _binding: FragmentProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userData = arguments?.getSerializable("userData") as? UserData // Nhận đối tượng UserData từ Bundle
        Log.e("VKUUUUUU", userData.toString())
        _binding = FragmentProfileFragmentBinding.bind(view)

        binding.profileEmail.text = userData?.email
        binding.profileName.text = userData?.name
        Glide.with(this)
            .load(userData?.profilePictureUrl)
            .into(binding.profileURL) // Thay "profileImage" bằng ID của ImageView trong layout của bạn
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentProfileFragmentBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        if (args.profile != null) {
//            val userdata = args.profile
//            // Thiết lập dữ liệu người dùng trên giao diện
//            binding.profileEmail.text = userdata.email
//            binding.profileName.text = userdata.userId
//        } else {
//
//            // Toast.makeText(requireContext(), "Missing profile data", Toast.LENGTH_SHORT).show()
//        }
//}
}


