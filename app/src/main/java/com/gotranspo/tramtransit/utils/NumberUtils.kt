package com.gotranspo.tramtransit.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object NumberUtils {

    fun formatNumberWithLocaleToString(value: Double): String {
        val currentLocale = Locale.getDefault()
        val numberFormat = NumberFormat.getNumberInstance(currentLocale)

        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2

        return numberFormat.format(value)
    }

    fun formatNumberWithLocale(value: Double): String {
        val formatter = DecimalFormat.getInstance() as DecimalFormat
        val symbols = formatter.decimalFormatSymbols
        val isCommaDecimalSeparator = symbols.decimalSeparator == ','
        symbols.decimalSeparator = if (isCommaDecimalSeparator) '.' else ','
        formatter.decimalFormatSymbols = symbols

        return formatter.format(value)
    }

    fun formatNumberWithLocale(value: Double, locale: Locale): String {
        val formatter = DecimalFormat.getInstance(locale) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols
        val isCommaDecimalSeparator = symbols.decimalSeparator == ','
        symbols.decimalSeparator = if (isCommaDecimalSeparator) '.' else ','
        formatter.decimalFormatSymbols = symbols

        return formatter.format(value)
    }
}