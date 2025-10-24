package com.tt.invoicecreator.data

import com.tt.invoicecreator.data.roomV2.entities.InvoiceItemV2
import com.tt.invoicecreator.data.roomV2.entities.InvoiceV2
import com.tt.invoicecreator.data.roomV2.entities.PaidV2

data class AppUiState(
    val rewardedAppLoaded: Boolean = false,
    val rewardedAdWatched: Boolean = false,
    val modePro: Boolean = false,
    val invoiceV2: InvoiceV2 = InvoiceV2(),
    val paymentDay: String = "",
    val invoiceState: Enum<InvoiceStatus> = InvoiceStatus.ALL
)