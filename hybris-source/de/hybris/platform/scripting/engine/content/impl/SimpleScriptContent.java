package de.hybris.platform.scripting.engine.content.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import java.util.Collections;
import java.util.Map;

public class SimpleScriptContent implements ScriptContent
{
    private final String engineName;
    private final String content;


    public SimpleScriptContent(String engineName, String content)
    {
        Preconditions.checkNotNull(engineName, "engine name is required");
        Preconditions.checkNotNull(content, "content is required");
        this.engineName = engineName;
        this.content = content;
    }


    public String getEngineName()
    {
        return this.engineName.toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public String getContent()
    {
        return this.content;
    }


    public Map<String, Object> getCustomContext()
    {
        return Collections.emptyMap();
    }
}
