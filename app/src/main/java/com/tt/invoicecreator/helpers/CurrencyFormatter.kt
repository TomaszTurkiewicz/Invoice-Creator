package com.tt.invoicecreator.helpers

class CurrencyFormatter {
    fun format(
        value: Double,
        currency: Currency
    ): String {
        val formattedValue = String.format("%.2f", value)
        return if (currency.prefix) {
            "${currency.symbol} $formattedValue"
        } else {
            "$formattedValue ${currency.symbol}"
        }
    }
}