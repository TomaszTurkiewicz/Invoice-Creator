package com.tt.invoicecreator.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,
    var itemName: String = "",
    var itemCount: Double = 0.0,
    var itemValue: Double = 0.0,
)