package de.hybris.platform.util.localization.jdbc.rows;

import de.hybris.platform.util.localization.jdbc.LocalizableRowWithNameAndDescription;

public class LocalizableAttributeRow extends LocalizableRowWithNameAndDescription
{
    private final String qualifier;
    private final String ownerCode;
    private final String declarerCode;
    private final String inheritancePath;


    public LocalizableAttributeRow(String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue, String qualifier, String ownerCode, String declarerCode, String inheritancePath)
    {
        super(lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
        this.qualifier = qualifier;
        this.ownerCode = ownerCode;
        this.declarerCode = declarerCode;
        this.inheritancePath = inheritancePath;
    }


    public String getOwnerCode()
    {
        return this.ownerCode;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String getInheritancePath()
    {
        return this.inheritancePath;
    }


    public String getDescriptionPropertyKey()
    {
        return "type." + getLocalizationOwnerCode() + "." + getQualifier() + ".description";
    }


    public String getNamePropertyKey()
    {
        return "type." + getLocalizationOwnerCode() + "." + getQualifier() + ".name";
    }


    private String getLocalizationOwnerCode()
    {
        return (this.declarerCode != null) ? this.declarerCode : this.ownerCode;
    }
}
