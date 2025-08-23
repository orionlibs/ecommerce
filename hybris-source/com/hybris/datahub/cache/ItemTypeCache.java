package com.hybris.datahub.cache;

import java.util.Set;

public interface ItemTypeCache
{
    Set<String> getTypes();


    boolean typeExists(String paramString);


    void addType(String paramString);


    void clearCache();
}
