package com.tt.invoicecreator.data.roomV2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemV2 (

    @PrimaryKey(autoGenerate = true)
    val itemId:Int = 0,
    var itemName: String = "",
    var itemValue: Double = 0.0,
)