package com.tt.invoicecreator

import androidx.annotation.StringRes

enum class InvoiceCreatorScreen(@StringRes val title:Int) {
    Invoices(title = R.string.invoices),
    AddInvoice(title = R.string.add_invoice),
    ChooseClient(title = R.string.choose_client),
    AddClient(title = R.string.add_client),
    AddClientV2(title = R.string.add_client_v2),
    ChooseItem(title = R.string.choose_item),
    AddItem(title = R.string.add_item),
    ChooseMode(title = R.string.choose_mode),
    InvoicesV2(title = R.string.invoicesV2),
    AddInvoiceV2(title = R.string.add_invoiceV2),
    ChooseClientV2(title = R.string.choose_client_v2)
}