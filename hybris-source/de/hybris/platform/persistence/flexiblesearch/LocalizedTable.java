package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;

abstract class LocalizedTable extends Table
{
    private final boolean useDefaultLanguageFlag;
    private final boolean ignoreLanguageFlag;
    private final PK languagePK;


    LocalizedTable(ParsedType type, boolean useDefaultLanguage, boolean ignoreLanguage, PK languagePK)
    {
        super(type);
        if(useDefaultLanguage && ignoreLanguage)
        {
            throw new IllegalArgumentException("cannot set UseDefaultLanguage and IgnoreLanguage to TRUE both");
        }
        if(!useDefaultLanguage && !ignoreLanguage && languagePK == null)
        {
            throw new IllegalArgumentException("cannot set LanguagePK to NULL without having one of UseDefaultLanguage or IgnoreLanguage TRUE");
        }
        this.useDefaultLanguageFlag = useDefaultLanguage;
        this.ignoreLanguageFlag = ignoreLanguage;
        this.languagePK = languagePK;
    }


    boolean useDefaultLanguage()
    {
        return this.useDefaultLanguageFlag;
    }


    boolean ignoreLanguage()
    {
        return this.ignoreLanguageFlag;
    }


    PK getCustomLanguagePK()
    {
        return this.languagePK;
    }
}
