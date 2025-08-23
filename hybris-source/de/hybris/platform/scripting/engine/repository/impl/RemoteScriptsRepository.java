package de.hybris.platform.scripting.engine.repository.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ResourceScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.exception.ScriptURIException;
import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class RemoteScriptsRepository extends AbstractScriptsRepository
{
    public ScriptContent lookupScript(String protocol, String path)
    {
        Preconditions.checkNotNull(protocol, "protocol is required");
        Preconditions.checkNotNull(path, "path is required");
        UrlResource urlResource = getUrlResource(protocol, path);
        validateScriptResource(urlResource);
        return (ScriptContent)new ResourceScriptContent((Resource)urlResource);
    }


    private UrlResource getUrlResource(String protocol, String path)
    {
        try
        {
            return new UrlResource(protocol + "://" + protocol);
        }
        catch(MalformedURLException e)
        {
            throw new ScriptURIException(e.getMessage(), e);
        }
    }


    private void validateScriptResource(UrlResource resource)
    {
        if(!resource.exists())
        {
            throw new ScriptNotFoundException("Script resource: " + resource + " not found in the classpath");
        }
    }
}
