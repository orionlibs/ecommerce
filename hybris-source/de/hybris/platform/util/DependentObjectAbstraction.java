package de.hybris.platform.util;

import java.util.Map;

public interface DependentObjectAbstraction
{
    Map getTransientObjectMap();


    void setTransientObject(String paramString, Object paramObject);


    Object getTransientObject(String paramString);
}
