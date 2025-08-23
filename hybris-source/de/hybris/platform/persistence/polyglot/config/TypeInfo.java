package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.model.Identity;

public interface TypeInfo
{
    int getTypeCode();


    Identity getIdentity();


    TypeInfo getMoreSpecificTypeInfo(String paramString);


    default TypeInfo getMoreSpecificTypeInfo(String qualifier, boolean allowMissingAttribute)
    {
        return getMoreSpecificTypeInfo(qualifier);
    }


    default Object getTypeItem()
    {
        return null;
    }


    default boolean isMoreSpecificUnpredictable()
    {
        return false;
    }
}
