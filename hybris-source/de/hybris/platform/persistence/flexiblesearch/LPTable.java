package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.JDBCValueMappings;

final class LPTable extends LocalizedTable
{
    private static final String LOCALIZEDTABLE_PREFIX = JDBCValueMappings.LOC_TABLE_POSTFIX;
    private final int locTableIndex;


    static LPTable createDefaultLocalized(ParsedType type)
    {
        return new LPTable(type, false, true, null);
    }


    static LPTable createIgnoringLanguage(ParsedType type)
    {
        return new LPTable(type, true, false, null);
    }


    static LPTable createCustomLocalized(ParsedType type, PK customLanguagePK)
    {
        return new LPTable(type, false, false, customLanguagePK);
    }


    private LPTable(ParsedType type, boolean ignoreLanguage, boolean useDefaultLanguage, PK customLanguagePK)
    {
        super(type, useDefaultLanguage, ignoreLanguage, (ignoreLanguage || useDefaultLanguage) ? null : customLanguagePK);
        this.locTableIndex = type.getLocTableCount();
    }


    protected String createTableAlias()
    {
        return (LOCALIZEDTABLE_PREFIX + "_T" + LOCALIZEDTABLE_PREFIX + Integer.toString(getType().getIndex()))
                        .toLowerCase();
    }


    String getTableName()
    {
        return getType().getLPTableName();
    }
}
