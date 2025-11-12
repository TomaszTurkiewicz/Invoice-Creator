package com.tt.invoicecreator.helpers


enum class Currency(val symbol: String, val prefix: Boolean = true) {


    /** Euro, used in the Eurozone countries. */
    EUR("€",true),
    /** Pound Sterling, the official currency of the United Kingdom. */
    GBP("£",true),
    /** United States Dollar, the official currency of the United States. */
    USD("$", true),
    /** United Arab Emirates Dirham, the official currency of the UAE. */
    AED("د.إ", true),
    /** Australian Dollar, the official currency of Australia. */
    AUD("A$", true),
    /** Canadian Dollar, the official currency of Canada. */
    CAD("CA$", true),
    /** Swiss Franc, the official currency of Switzerland and Liechtenstein. */
    CHF("CHF", true),
    /** Chinese Renminbi, the official currency of the People's Republic of China. */
    CNY("¥",true),
    /** Czech Koruna, the official currency of the Czech Republic. */
    CZK("Kč", false),
    /** Danish Krone, the official currency of Denmark. */
    DKK("kr", false),
    /** Hong Kong Dollar, the official currency of Hong Kong. */
    HKD("HK$", true),
    /** Japanese Yen, the official currency of Japan. */
    JPY("¥", true),
    /** Norwegian Krone, the official currency of Norway. */
    NOK("kr", false),
    /** New Zealand Dollar, the official currency of New Zealand. */
    NZD("NZ$", true),
    /** Polish Złoty, the official currency of Poland. */
    PLN("zł", false),
    /** Swedish Krona, the official currency of Sweden. */
    SEK("kr", false),
    /** Singapore Dollar, the official currency of Singapore. */
    SGD("S$", true),
    /** South African Rand, the official currency of South Africa. */
    ZAR("R", true)
}