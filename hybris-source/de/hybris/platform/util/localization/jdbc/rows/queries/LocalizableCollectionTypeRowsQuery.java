package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;

public class LocalizableCollectionTypeRowsQuery extends LocalizableTypeRowsQuery
{
    public LocalizableCollectionTypeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    protected String getMetaTypeCode()
    {
        return "CollectionType";
    }
}
