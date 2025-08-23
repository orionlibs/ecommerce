package de.hybris.platform.util.localization.jdbc.rows;

import com.google.common.base.Preconditions;
import de.hybris.platform.util.localization.jdbc.LocalizableRowWithName;

public class LocalizableEnumerationValueRow extends LocalizableRowWithName
{
    private final String typeCode;
    private final String valueCode;


    public LocalizableEnumerationValueRow(String typeCode, String valueCode, String lpTableName, long itemPKValue, long itemTypePKValue, Long languagePKValue)
    {
        super(lpTableName, itemPKValue, itemTypePKValue, languagePKValue);
        Preconditions.checkNotNull(typeCode, "typeCode can't be null");
        Preconditions.checkNotNull(valueCode, "valueCode can't be null");
        this.typeCode = typeCode;
        this.valueCode = valueCode;
    }


    public String getNamePropertyKey()
    {
        return "type." + this.typeCode + "." + this.valueCode + ".name";
    }
}
