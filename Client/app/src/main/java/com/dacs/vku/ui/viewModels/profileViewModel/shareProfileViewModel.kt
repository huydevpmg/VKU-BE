package com.dacs.vku.ui.viewModels.profileViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dacs.vku.db.User

class shareProfileViewModel: ViewModel() {
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

    fun setUserData(user: User) {
        _userData.value = user
    }
}