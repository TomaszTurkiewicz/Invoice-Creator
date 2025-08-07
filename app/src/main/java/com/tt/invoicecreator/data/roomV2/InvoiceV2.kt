package com.tt.invoicecreator.data.roomV2

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoiceV2 (
    @PrimaryKey(autoGenerate = true)
    val invoiceId:Int = 0,
    @Embedded
    var client:ClientV2 = ClientV2(),
    var time:Long = 0L,
    var invoiceNumber:Int = 0

)