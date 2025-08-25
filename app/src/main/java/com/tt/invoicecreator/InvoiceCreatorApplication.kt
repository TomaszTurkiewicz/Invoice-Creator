package com.tt.invoicecreator

import android.app.Application
import com.tt.invoicecreator.data.roomV2.DatabaseV2

class InvoiceCreatorApplication: Application() {
    val databaseV2:DatabaseV2 by lazy { DatabaseV2.getDatabaseV2(this) }
}