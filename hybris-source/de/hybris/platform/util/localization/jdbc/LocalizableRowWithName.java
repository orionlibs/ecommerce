package de.hybris.platform.util.localization.jdbc;

public abstract class LocalizableRowWithName extends LocalizableRow
{
    public LocalizableRowWithName(String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue)
    {
        super(lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
    }


    public abstract String getNamePropertyKey();
}
