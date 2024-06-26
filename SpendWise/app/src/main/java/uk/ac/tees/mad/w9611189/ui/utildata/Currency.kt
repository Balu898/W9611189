package uk.ac.tees.mad.w9611189.ui.utildata

object CurrencyList {
    fun getCurrencyList(): MutableList<Currency> {
        val list = mutableListOf<Currency>().apply {
            add(Currency("British Pound Sterling (GBP)", "£"))
            add(Currency("United States Dollar (USD)", "$"))
            add(Currency("Euro (EUR)", "€"))
            add(Currency("Japanese Yen (JPY)", "¥"))
            add(Currency("Australian Dollar (AUD)", "A$"))
            add(Currency("Canadian Dollar (CAD)", "C$"))
            add(Currency("Swiss Franc (CHF)", "Fr."))
            add(Currency("Chinese Yuan (CNY)", "¥"))
            add(Currency("Swedish Krona (SEK)", "kr"))
            add(Currency("New Zealand Dollar (NZD)", "NZ$"))
            add(Currency("Mexican Peso (MXN)", "Mex$"))
            add(Currency("Singapore Dollar (SGD)", "S$"))
            add(Currency("Hong Kong Dollar (HKD)", "HK$"))
            add(Currency("Norwegian Krone (NOK)", "kr"))
            add(Currency("South Korean Won (KRW)", "₩"))
            add(Currency("Turkish Lira (TRY)", "₺"))
            add(Currency("Russian Ruble (RUB)", "₽"))
            add(Currency("Indian Rupee (INR)", "₹"))
            add(Currency("Brazilian Real (BRL)", "R$"))
            add(Currency("South African Rand (ZAR)", "R"))
            add(Currency("Danish Krone (DKK)", "kr"))
            add(Currency("Polish Zloty (PLN)", "zł"))
            add(Currency("Israeli New Shekel (ILS)", "₪"))
            add(Currency("Saudi Riyal (SAR)", "SR"))
            add(Currency("United Arab Emirates Dirham (AED)", "د.إ"))
            add(Currency("Czech Koruna (CZK)", "Kč"))
            add(Currency("Chilean Peso (CLP)", "$"))
            add(Currency("Malaysian Ringgit (MYR)", "RM"))
            add(Currency("Colombian Peso (COP)", "$"))
            add(Currency("Philippine Peso (PHP)", "₱"))
            add(Currency("Indonesian Rupiah (IDR)", "Rp"))
            add(Currency("Egyptian Pound (EGP)", "£"))
            add(Currency("Argentine Peso (ARS)", "$"))
            add(Currency("Thai Baht (THB)", "฿"))
            add(Currency("Pakistani Rupee (PKR)", "Rs"))
            add(Currency("Bangladeshi Taka (BDT)", "৳"))
            add(Currency("Vietnamese Dong (VND)", "₫"))
            add(Currency("Nigerian Naira (NGN)", "₦"))
            add(Currency("Iraqi Dinar (IQD)", "ع.د"))
            add(Currency("Kenyan Shilling (KES)", "KSh"))
            add(Currency("Swiss Franc (XOF)", "CFA"))
            add(Currency("Algerian Dinar (DZD)", "د.ج"))
            add(Currency("Tunisian Dinar (TND)", "د.ت"))
            add(Currency("Ukrainian Hryvnia (UAH)", "₴"))
            add(Currency("Ghanaian Cedi (GHS)", "GH₵"))
            add(Currency("Peruvian Nuevo Sol (PEN)", "S/."))
            add(Currency("Moroccan Dirham (MAD)", "د.م."))
            add(Currency("Ethiopian Birr (ETB)", "Br"))
            add(Currency("Sudanese Pound (SDG)", "£"))
            add(Currency("Angolan Kwanza (AOA)", "Kz"))
            add(Currency("Tanzanian Shilling (TZS)", "TSh"))
            add(Currency("Uzbekistani Som (UZS)", "soʻm"))
            add(Currency("Bolivian Boliviano (BOB)", "Bs."))
            add(Currency("Azerbaijani Manat (AZN)", "₼"))
            add(Currency("Dominican Peso (DOP)", "RD$"))
            add(Currency("Uruguayan Peso (UYU)", "\$U"))
            add(Currency("Croatian Kuna (HRK)", "kn"))
            add(Currency("Costa Rican Colón (CRC)", "₡"))
            add(Currency("Bahraini Dinar (BHD)", ".د.ب"))
            add(Currency("Lebanese Pound (LBP)", "£"))
            add(Currency("Sri Lankan Rupee (LKR)", "Rs"))
            add(Currency("Guatemalan Quetzal (GTQ)", "Q"))
            add(Currency("Armenian Dram (AMD)", "֏"))
            add(Currency("Namibian Dollar (NAD)", "N$"))
            add(Currency("Omani Rial (OMR)", "ر.ع."))
            add(Currency("Mozambican Metical (MZN)", "MT"))
            add(Currency("Paraguayan Guarani (PYG)", "₲"))
            add(Currency("Rwandan Franc (RWF)", "FRw"))
            add(Currency("Honduran Lempira (HNL)", "L"))
            add(Currency("Bulgarian Lev (BGN)", "лв"))
            add(Currency("Haitian Gourde (HTG)", "G"))
            add(Currency("Moldovan Leu (MDL)", "L"))
            add(Currency("Guinean Franc (GNF)", "FG"))
            add(Currency("Mauritian Rupee (MUR)", "Rs"))
            add(Currency("Nicaraguan Córdoba (NIO)", "C$"))
            add(Currency("Albanian Lek (ALL)", "L"))
            add(Currency("Malawian Kwacha (MWK)", "MK"))
            add(Currency("Congolese Franc (CDF)", "FC"))
            add(Currency("Macedonian Denar (MKD)", "ден"))
            add(Currency("Burundian Franc (BIF)", "FBu"))
            add(Currency("Mongolian Tugrik (MNT)", "₮"))
            add(Currency("Lao Kip (LAK)", "₭"))
            add(Currency("Cape Verdean Escudo (CVE)", "Esc"))
            add(Currency("East Caribbean Dollar (XCD)", "EC$"))
            add(Currency("Fiji Dollar (FJD)", "FJ$"))
            add(Currency("Surinamese Dollar (SRD)", "$"))
            add(Currency("Malagasy Ariary (MGA)", "Ar"))
            add(Currency("Guyanese Dollar (GYD)", "$"))
            add(Currency("Zimbabwean Dollar (ZWL)", "Z$"))
            add(Currency("Bhutanese Ngultrum (BTN)", "Nu."))
            add(Currency("Qatari Rial (QAR)", "ر.ق"))
            add(Currency("Trinidad and Tobago Dollar (TTD)", "TT$"))
            add(Currency("Djiboutian Franc (DJF)", "Fdj"))
            add(Currency("Brunei Dollar (BND)", "B$"))
            add(Currency("Bahamian Dollar (BSD)", "B$"))
            add(Currency("Barbadian Dollar (BBD)", "Bds$"))
            add(Currency("Comorian Franc (KMF)", "CF"))
            add(Currency("Saint Helena Pound (SHP)", "£"))
            add(Currency("Solomon Islands Dollar (SBD)", "SI$"))
            add(Currency("Saint Kitts and Nevis Dollar (XCD)", "EC$"))
        }

        return list
    }


    fun getCurrencyBySymbol(symbol: String): Currency? {
        return getCurrencyList().find { it.symbol == symbol }
    }
}

data class Currency(
    val name:String,
    val symbol:String
)
