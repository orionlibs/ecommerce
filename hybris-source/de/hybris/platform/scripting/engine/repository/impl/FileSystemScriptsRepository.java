package de.hybris.platform.scripting.engine.repository.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ResourceScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileSystemScriptsRepository extends AbstractScriptsRepository
{
    public ScriptContent lookupScript(String protocol, String path)
    {
        Preconditions.checkNotNull(path, "path is required");
        FileSystemResource resource = new FileSystemResource(path);
        validateScriptResource(resource);
        return (ScriptContent)new ResourceScriptContent((Resource)resource);
    }


    private void validateScriptResource(FileSystemResource resource)
    {
        if(!resource.exists())
        {
            throw new ScriptNotFoundException("Script resource: " + resource + " not found in the classpath");
        }
    }
}
