package com.tt.invoicecreator.data.roomV2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClientV2 (
    @PrimaryKey(autoGenerate = true)
    val clientId: Int = 0,
    var clientName: String = "",
    var clientAddress1: String = "",
    var clientAddress2: String = "",
    var clientCity: String = ""
)