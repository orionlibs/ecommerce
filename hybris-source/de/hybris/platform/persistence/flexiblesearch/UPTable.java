package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.persistence.property.JDBCValueMappings;

class UPTable extends Table
{
    private static final String UNLOCALIZEDTABLE_PREFIX = JDBCValueMappings.UNLOC_TABLE_POSTFIX;


    UPTable(ParsedType type)
    {
        super(type);
    }


    protected String createTableAlias()
    {
        return (UNLOCALIZEDTABLE_PREFIX + "_T" + UNLOCALIZEDTABLE_PREFIX).toLowerCase();
    }


    final String getTableName()
    {
        return getType().getUPTableName();
    }
}
