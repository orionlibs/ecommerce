package de.hybris.platform.util.localization.jdbc.rows.queries;

import de.hybris.platform.util.localization.jdbc.DbInfo;

public class LocalizableAtomicTypeRowsQuery extends LocalizableTypeRowsQuery
{
    public LocalizableAtomicTypeRowsQuery(DbInfo dbInfo)
    {
        super(dbInfo);
    }


    protected String getMetaTypeCode()
    {
        return "AtomicType";
    }
}
