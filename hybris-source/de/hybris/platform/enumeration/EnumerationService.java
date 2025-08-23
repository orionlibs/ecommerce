package de.hybris.platform.enumeration;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.List;
import java.util.Locale;

public interface EnumerationService
{
    <T extends HybrisEnumValue> List<T> getEnumerationValues(String paramString);


    <T extends HybrisEnumValue> List<T> getEnumerationValues(Class<T> paramClass);


    <T extends HybrisEnumValue> T getEnumerationValue(String paramString1, String paramString2);


    <T extends HybrisEnumValue> T getEnumerationValue(Class<T> paramClass, String paramString);


    String getEnumerationName(HybrisEnumValue paramHybrisEnumValue);


    void setEnumerationName(HybrisEnumValue paramHybrisEnumValue, String paramString);


    String getEnumerationName(HybrisEnumValue paramHybrisEnumValue, Locale paramLocale);


    void setEnumerationName(HybrisEnumValue paramHybrisEnumValue, String paramString, Locale paramLocale);
}
