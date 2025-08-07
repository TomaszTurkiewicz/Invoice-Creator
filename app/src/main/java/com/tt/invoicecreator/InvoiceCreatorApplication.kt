package com.tt.invoicecreator

import android.app.Application
import com.tt.invoicecreator.data.room.Database
import com.tt.invoicecreator.data.roomV2.DatabaseV2

class InvoiceCreatorApplication: Application() {
    val database: Database by lazy { Database.getDatabase(this) }

    val databaseV2:DatabaseV2 by lazy { DatabaseV2.getDatabaseV2(this) }
}