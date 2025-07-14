package com.tt.invoicecreator

import androidx.annotation.StringRes

enum class InvoiceCreatorScreen(@StringRes val title:Int) {
    Invoices(title = R.string.invoices),
    AddItem(title = R.string.add_item)
}