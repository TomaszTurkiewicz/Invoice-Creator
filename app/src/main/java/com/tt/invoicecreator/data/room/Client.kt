package com.tt.invoicecreator.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true)
    val clientId: Int = 0,
    var clientName: String = ""
)
