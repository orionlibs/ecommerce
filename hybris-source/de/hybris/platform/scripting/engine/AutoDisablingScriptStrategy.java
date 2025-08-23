package de.hybris.platform.scripting.engine;

import java.util.Map;

public interface AutoDisablingScriptStrategy
{
    void onException(Exception paramException, ScriptExecutable paramScriptExecutable, Map<String, Object> paramMap);


    boolean isDisabled(ScriptExecutable paramScriptExecutable);
}
