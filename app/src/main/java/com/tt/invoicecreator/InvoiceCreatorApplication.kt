package com.tt.invoicecreator

import android.app.Application
import com.tt.invoicecreator.data.room.Database

class InvoiceCreatorApplication: Application() {
    val database: Database by lazy { Database.getDatabase(this) }
}