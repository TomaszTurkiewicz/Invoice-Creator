package com.tt.invoicecreator.data.roomV2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PaidV2 (
    @PrimaryKey(autoGenerate = true)
    val paidId:Int = 0,
    var invoiceId:Int = 0,
    var amountPaid:Double = 0.0,
    var time:Long = 0L,
    var notes:String = ""
)