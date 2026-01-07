package com.tt.invoicecreator

import android.app.Application
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionConfig
import com.qonversion.android.sdk.dto.QLaunchMode
import com.tt.invoicecreator.data.roomV2.DatabaseV2

class InvoiceCreatorApplication: Application() {
    val databaseV2:DatabaseV2 by lazy { DatabaseV2.getDatabaseV2(this) }

    override fun onCreate() {
        super.onCreate()
        val qonversionConfig = QonversionConfig
            .Builder(
                this,
                "HNRwq5lfMg7DmA7BthIDI72hiWMAHrSe",
                QLaunchMode.SubscriptionManagement)
            .build()

        Qonversion.initialize(qonversionConfig)
    }
}