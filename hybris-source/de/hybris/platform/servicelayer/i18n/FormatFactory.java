package de.hybris.platform.servicelayer.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;

public interface FormatFactory
{
    NumberFormat createCurrencyFormat();


    DateFormat createDateTimeFormat(int paramInt1, int paramInt2);


    DateFormat createDateTimeFormat(String paramString);


    NumberFormat createIntegerFormat();


    NumberFormat createNumberFormat(String paramString);


    NumberFormat createNumberFormat();


    NumberFormat createPercentFormat();
}
