package com.tt.invoicecreator.data.roomV2.backups

import com.tt.invoicecreator.data.roomV2.entities.ClientV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.ItemV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2
import com.tt.invoicecreator.helpers.User

data class BackupData(
    val timestamp: Long = System.currentTimeMillis(),
    val version: Int = 1,
    val invoices: List<InvoiceV2>,
    val invoiceItems: List<InvoiceItemV2>,
    val items: List<ItemV2>,
    val clients: List<ClientV2>,
    val paid: List<PaidV2>,
    val user: User,
    val paymentMethod: String?,
    val signatureBase64: String?
)