package de.hybris.platform.scripting.engine;

import de.hybris.platform.scripting.engine.content.ScriptContent;

public interface ScriptingLanguagesService
{
    ScriptExecutable getExecutableByContent(ScriptContent paramScriptContent);


    ScriptExecutable getExecutableByURI(String paramString);
}
