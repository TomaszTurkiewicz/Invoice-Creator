package com.tt.invoicecreator

import androidx.annotation.StringRes

enum class InvoiceCreatorScreen(@StringRes val title:Int) {
    AddClientV2(title = R.string.add_client_v2),
    ChooseItemV2(title = R.string.choose_item_v2),
    AddItemV2(title = R.string.add_item_V2),
    ChooseMode(title = R.string.choose_mode),
    InvoicesV2(title = R.string.invoicesV2),
    AddInvoiceV2(title = R.string.add_invoiceV2),
    ChooseClientV2(title = R.string.choose_client_v2),

    Settings(title = R.string.settings),

    InvoiceInfoV2(title = R.string.info),

    FilteredInvoicesV2(title = R.string.filtered_invoices_v2),

    InvoicesByClient(title = R.string.invoices_by_client),

    FilteredInvoicesByClient(title = R.string.filtered_invoices_by_client)
}