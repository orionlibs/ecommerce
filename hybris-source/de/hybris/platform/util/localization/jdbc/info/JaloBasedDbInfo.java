package de.hybris.platform.util.localization.jdbc.info;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.localization.jdbc.DbInfo;

public class JaloBasedDbInfo implements DbInfo
{
    public String getTableNameFor(String typeCode)
    {
        Preconditions.checkNotNull(typeCode, "typeCode can't be null");
        try
        {
            return TypeManager.getInstance().getComposedType(typeCode).getTable();
        }
        catch(JaloSystemException jaloException)
        {
            throw new IllegalArgumentException("Can't find table name for " + typeCode);
        }
    }


    public String getColumnNameFor(String typeCode, String attributeQualifier)
    {
        Preconditions.checkNotNull(typeCode, "typeCode can't be null");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier can't be null");
        try
        {
            return TypeManager.getInstance().getComposedType(typeCode).getAttributeDescriptor(attributeQualifier)
                            .getDatabaseColumn();
        }
        catch(JaloSystemException jaloException)
        {
            throw new IllegalArgumentException("Can't find column name for " + typeCode + "." + attributeQualifier, jaloException);
        }
    }
}
