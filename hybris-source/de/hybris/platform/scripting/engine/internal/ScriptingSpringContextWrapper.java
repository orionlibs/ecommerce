package de.hybris.platform.scripting.engine.internal;

public interface ScriptingSpringContextWrapper
{
    Object getBean(String paramString);


    <T> T getBean(Class<T> paramClass);


    <T> T getBean(String paramString, Class<T> paramClass);
}
