package com.tt.invoicecreator.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val invoiceId: Int = 0,
    @Embedded
    val client: Client,
    @Embedded
    val item: Item,
    val comment: String = "",
    var time:Long = 0L,
    var invoiceNumber:Int = 0
)
