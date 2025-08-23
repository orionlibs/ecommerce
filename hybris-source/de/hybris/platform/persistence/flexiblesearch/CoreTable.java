package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;

class CoreTable extends Table
{
    private static final String CORETABLE_PREFIX = "ITEM";


    CoreTable(ParsedType type)
    {
        super(type);
    }


    protected String createTableAlias()
    {
        return ("ITEM_T" + Integer.toString(getType().getIndex())).toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    final String getTableName()
    {
        return getType().getCoreTableName();
    }
}
