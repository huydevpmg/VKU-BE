package com.dacs.vku.domain.usecases.app_entry

import com.dacs.vku.domain.manager.LocalUserManager

class SaveAppEntry(private val localUserManager:LocalUserManager
) {
    suspend operator fun invoke() {
        localUserManager.saveAppEntry()
    }
}