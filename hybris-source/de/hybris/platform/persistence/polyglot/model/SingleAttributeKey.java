package de.hybris.platform.persistence.polyglot.model;

import de.hybris.platform.util.typesystem.PlatformStringUtils;

public abstract class SingleAttributeKey extends Key
{
    final String qualifier;


    SingleAttributeKey(String qualifier)
    {
        this.qualifier = PlatformStringUtils.toLowerCaseCached(qualifier);
    }


    public String getQualifier()
    {
        return this.qualifier;
    }
}
