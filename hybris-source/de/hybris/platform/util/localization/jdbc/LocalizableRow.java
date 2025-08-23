package de.hybris.platform.util.localization.jdbc;

public class LocalizableRow
{
    private final String lpTableName;
    private final long itemPKValue;
    private final long itemTypePKValue;
    private final Long languagePKValue;


    public LocalizableRow(String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue)
    {
        this.lpTableName = lpTableName;
        this.itemPKValue = itemPKValue;
        this.itemTypePKValue = itemTypePKValue;
        this.languagePKValue = languagePKValue;
    }


    public long getItemPKValue()
    {
        return this.itemPKValue;
    }


    public long getItemTypePKValue()
    {
        return this.itemTypePKValue;
    }


    public Long getLanguagePKValue()
    {
        return this.languagePKValue;
    }


    public String getLpTableName()
    {
        return this.lpTableName;
    }
}
