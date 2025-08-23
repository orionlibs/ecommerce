package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;

public class LocalizableMapTypeRowsQuery extends LocalizableTypeRowsQuery
{
    public LocalizableMapTypeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    protected String getMetaTypeCode()
    {
        return "MapType";
    }
}
