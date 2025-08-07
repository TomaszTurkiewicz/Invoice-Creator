package com.tt.invoicecreator.data.roomV2

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoiceItemV2 (
    @PrimaryKey(autoGenerate = true)
    val invoiceItemId:Int = 0,
    @Embedded
    var itemV2: ItemV2 = ItemV2(),
    var itemCount: Double = 1.0,
    var itemDiscount: Double = 0.0,
    var comment: String = "",

)