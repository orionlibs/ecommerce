package de.hybris.platform.util.localization.jdbc.rows;

import com.google.common.base.Preconditions;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithNameAndDescription;

public class LocalizableTypeRow extends LocalizableRowWithNameAndDescription
{
    private final String typeCode;


    public LocalizableTypeRow(String typeCode, String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue)
    {
        super(lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
        Preconditions.checkNotNull(typeCode, "typeCode can't be null");
        this.typeCode = typeCode;
    }


    public String getDescriptionPropertyKey()
    {
        return "type." + this.typeCode + ".description";
    }


    public String getNamePropertyKey()
    {
        return "type." + this.typeCode + ".name";
    }
}
