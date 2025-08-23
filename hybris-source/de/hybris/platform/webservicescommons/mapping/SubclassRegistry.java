package de.hybris.platform.webservicescommons.mapping;

import java.util.Set;

public interface SubclassRegistry
{
    Set<Class> getSubclasses(Class paramClass);


    Set<Class> getAllSubclasses(Class paramClass);


    void registerSubclass(Class paramClass1, Class paramClass2);


    void registerSubclasses(Class paramClass, Set<Class> paramSet);


    void unregisterSubclass(Class paramClass1, Class paramClass2);


    void unregisterSubclasses(Class paramClass, Set<Class> paramSet);


    void unregisterSubclasses(Class paramClass);
}
