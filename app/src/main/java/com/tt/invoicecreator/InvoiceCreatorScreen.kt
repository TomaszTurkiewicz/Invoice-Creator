package com.tt.invoicecreator

import androidx.annotation.StringRes

enum class InvoiceCreatorScreen(@StringRes val title:Int) {
    Invoices(title = R.string.invoices),
    AddInvoice(title = R.string.add_invoice),
    ChooseClient(title = R.string.choose_client),
    AddClient(title = R.string.add_client),
    ChooseItem(title = R.string.choose_item),
    AddItem(title = R.string.add_item)
}