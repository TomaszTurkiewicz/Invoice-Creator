package com.tt.invoicecreator.helpers

import java.text.DecimalFormatSymbols

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    private val decimalSeparator = symbols.decimalSeparator

    fun cleanup(
        input: String,
        needSeparator: Boolean = true
    ):String{
        if (input.matches("\\D".toRegex())) return ""
        if (input.matches("0+".toRegex())) return "0"

        val sb = StringBuilder()

        var hasDecimalSep = false

        for(char in input){
            if(char.isDigit()){
                sb.append(char)
                continue
            }
            if(char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty() && needSeparator){
                sb.append(char)
                hasDecimalSep = true
            }
        }
        return sb.toString()
    }
}