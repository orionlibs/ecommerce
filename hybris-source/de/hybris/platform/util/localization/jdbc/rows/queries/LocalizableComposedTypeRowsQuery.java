package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;

public class LocalizableComposedTypeRowsQuery extends LocalizableTypeRowsQuery
{
    public LocalizableComposedTypeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    protected String getMetaTypeCode()
    {
        return "ComposedType";
    }
}
