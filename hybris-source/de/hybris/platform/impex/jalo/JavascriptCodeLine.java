package de.hybris.platform.impex.jalo;

import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JavascriptCodeLine extends AbstractScriptingEngineCodeLine
{
    public JavascriptCodeLine(Map<Integer, String> csvLine, String marker, String scriptContent, int lineNumber, String location, String scriptingEngineName, ScriptingLanguagesService service)
    {
        super(csvLine, marker, scriptContent, lineNumber, location, scriptingEngineName, service);
    }


    protected List<String> getStandardImports()
    {
        return Collections.emptyList();
    }
}
