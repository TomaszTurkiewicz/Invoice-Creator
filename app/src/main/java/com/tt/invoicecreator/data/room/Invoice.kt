package com.tt.invoicecreator.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded
    val client: Client?,
    @Embedded
    val item: Item?
)
