package de.hybris.platform.impex.jalo;

import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractScriptingEngineCodeLine extends AbstractCodeLine
{
    private final String scriptingEngineName;
    private final ScriptingLanguagesService service;
    private ScriptExecutable scriptExecutable = null;


    public AbstractScriptingEngineCodeLine(Map<Integer, String> csvLine, String marker, String scriptContent, int lineNumber, String location, String scriptingEngineName, ScriptingLanguagesService service)
    {
        super(csvLine, marker, scriptContent, lineNumber, location);
        this.scriptingEngineName = scriptingEngineName;
        this.service = service;
    }


    public void executeAndSetResult(Map<String, Object> ctx)
    {
        if(shouldCreateExecutable())
        {
            String content = enhanceScriptContentWithImports(getExecutableCode());
            SimpleScriptContent scriptContent = new SimpleScriptContent(this.scriptingEngineName, content);
            this.scriptExecutable = this.service.getExecutableByContent((ScriptContent)scriptContent);
        }
        if(this.scriptExecutable != null)
        {
            setResult(this.scriptExecutable.execute(ctx).getScriptResult());
        }
    }


    private boolean shouldCreateExecutable()
    {
        return (this.scriptExecutable == null && StringUtils.isNotBlank(getExecutableCode()));
    }


    private String enhanceScriptContentWithImports(String scriptContent)
    {
        StringBuilder sb = new StringBuilder();
        for(String importLine : getStandardImports())
        {
            sb.append(importLine).append("\n");
        }
        sb.append(scriptContent);
        return sb.toString();
    }


    public String getScriptingEngineName()
    {
        return this.scriptingEngineName;
    }


    protected abstract List<String> getStandardImports();
}
