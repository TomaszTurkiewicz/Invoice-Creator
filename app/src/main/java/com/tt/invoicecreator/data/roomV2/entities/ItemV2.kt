package com.tt.invoicecreator.data.roomV2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tt.invoicecreator.helpers.Currency

@Entity
data class ItemV2 (

    @PrimaryKey(autoGenerate = true)
    val itemId:Int = 0,
    var itemName: String = "",
    var itemValue: Double = 0.0,
    var itemCurrency: Currency = Currency.GBP
)