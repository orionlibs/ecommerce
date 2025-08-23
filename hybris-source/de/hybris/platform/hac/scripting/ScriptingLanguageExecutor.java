package de.hybris.platform.hac.scripting;

import java.util.Map;

public interface ScriptingLanguageExecutor
{
    Map<String, Object> executeScript(String paramString1, String paramString2, boolean paramBoolean);
}
