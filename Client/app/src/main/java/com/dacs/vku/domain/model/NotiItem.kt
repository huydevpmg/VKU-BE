package com.dacs.vku.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NotiItem(
    val href: String,
    @PrimaryKey val id: String,
    val spanText: String,
    val title: String,
)