package com.tt.invoicecreator.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val invoiceId: Int = 0,
    @Embedded
    var client: Client = Client(),
    @Embedded
    var item: Item = Item(),
    var itemCount: Double = 1.00,
    var itemDiscount: Double = 0.00,
    var comment: String = "",
    var time:Long = 0L,
    var invoiceNumber:Int = 0
)
