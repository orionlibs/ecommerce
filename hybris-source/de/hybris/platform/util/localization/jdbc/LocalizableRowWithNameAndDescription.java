package de.hybris.platform.util.localization.jdbc;

public abstract class LocalizableRowWithNameAndDescription extends LocalizableRowWithName
{
    public LocalizableRowWithNameAndDescription(String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue)
    {
        super(lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
    }


    public abstract String getDescriptionPropertyKey();
}
