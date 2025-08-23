package de.hybris.platform.scripting.engine.internal;

import java.util.Set;

public interface ScriptEnginesRegistry
{
    ScriptEngineType getScriptEngineType(String paramString);


    Set<ScriptEngineType> getRegisteredEngineTypes();
}
