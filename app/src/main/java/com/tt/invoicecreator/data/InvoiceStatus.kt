package com.tt.invoicecreator.data

enum class InvoiceStatus(var string: String) {
    ALL("ALL"),
    PAID("PAID"),
    OVERDUE("OVERDUE"),
    NOT_PAID("NOT PAID"),

    CANCELLED("CANCELLED");

    override fun toString(): String {
        return string
    }
}

