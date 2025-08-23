package de.hybris.platform.scripting.engine.internal;

public interface ScriptEngineType
{
    String getName();


    String getFileExtension();


    String getMime();


    default boolean canBeCached()
    {
        return false;
    }
}
