package de.hybris.platform.hac.scripting;

import de.hybris.platform.hac.scripting.exceptions.ScriptCompilationException;
import java.util.Map;

public interface ScriptingLanguageShell
{
    Object eval(Map<String, Object> paramMap, String paramString) throws ScriptCompilationException;
}
