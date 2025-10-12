package com.tt.invoicecreator.data

enum class InvoiceStatus(var string: String) {
    ALL("All"),
    PAID("Paid"),
    OVERDUE("Overdue"),
    NOT_PAID("Not paid");

    override fun toString(): String {
        return string
    }
}

