package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;

final class PropsTable extends LocalizedTable
{
    private static final String OLDSTYLETABLE_PREFIX = "PROP";
    private final String propertyName;
    private final int propsTableIndex;


    static PropsTable createUnlocalized(ParsedType type, String propertyName)
    {
        return new PropsTable(type, propertyName, false, false, null);
    }


    static PropsTable createDefaultLocalized(ParsedType type, String propertyName)
    {
        return new PropsTable(type, propertyName, false, true, null);
    }


    static PropsTable createLocalizedIgnoringLanguage(ParsedType type, String propertyName)
    {
        return new PropsTable(type, propertyName, true, false, null);
    }


    static PropsTable createCustomLocalized(ParsedType type, String propertyName, PK customLanguagePK)
    {
        return new PropsTable(type, propertyName, false, false, customLanguagePK);
    }


    private PropsTable(ParsedType type, String propertyName, boolean ignoreLanguage, boolean useDefaultLanguage, PK customLanguagePK)
    {
        super(type, useDefaultLanguage, ignoreLanguage, (ignoreLanguage || useDefaultLanguage) ? null : (
                        (customLanguagePK != null) ? customLanguagePK : PK.NULL_PK));
        this.propertyName = propertyName;
        this.propsTableIndex = type.getPropsTableCount();
    }


    protected String createTableAlias()
    {
        return ("PROP_T" + Integer.toString(getType().getIndex()) + "_P" + Integer.toString(this.propsTableIndex))
                        .toLowerCase();
    }


    String getTableName()
    {
        return getType().getPropsTableName();
    }


    String getPropertyName()
    {
        return this.propertyName;
    }
}
