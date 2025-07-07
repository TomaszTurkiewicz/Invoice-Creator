package com.tt.invoicecreator.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var itemName: String = "",
    var itemValue: Double = 0.0
)