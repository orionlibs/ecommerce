package de.hybris.platform.scripting.engine.content;

import java.util.Map;

public interface ScriptContent
{
    String getEngineName();


    String getContent();


    Map<String, Object> getCustomContext();
}
