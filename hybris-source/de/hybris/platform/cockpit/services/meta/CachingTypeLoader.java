package de.hybris.platform.cockpit.services.meta;

public interface CachingTypeLoader
{
    void removeCachedType(String paramString);


    void removeDefaultCachedTypes();


    void removeAllCachedTypes();
}
