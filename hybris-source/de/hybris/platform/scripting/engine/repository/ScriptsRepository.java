package de.hybris.platform.scripting.engine.repository;

import de.hybris.platform.scripting.engine.content.ScriptContent;
import java.util.Collection;

public interface ScriptsRepository
{
    ScriptContent lookupScript(String paramString1, String paramString2);


    Collection<String> getSupportedProtocols();
}
