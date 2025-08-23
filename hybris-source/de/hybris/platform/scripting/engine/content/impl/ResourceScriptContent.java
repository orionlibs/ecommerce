package de.hybris.platform.scripting.engine.content.impl;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.exception.ScriptURIException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

public class ResourceScriptContent implements ScriptContent
{
    private final String engineName;
    private final Resource resource;


    public ResourceScriptContent(Resource resource)
    {
        Preconditions.checkNotNull(resource, "path is required");
        this.engineName = getFileExtension(resource);
        this.resource = resource;
    }


    private String getFileExtension(Resource classPathResource)
    {
        try
        {
            String path = classPathResource.getURL().getPath();
            String extension = FilenameUtils.getExtension(path);
            if(StringUtils.isBlank(extension))
            {
                throw new ScriptURIException("Cannot determine script type from file extension [resource: " + path + "]");
            }
            return extension;
        }
        catch(IOException e)
        {
            throw new ScriptURIException(e.getMessage(), e);
        }
    }


    public String getEngineName()
    {
        return this.engineName.toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public String getContent()
    {
        try
        {
            Reader reader = new InputStreamReader(this.resource.getInputStream());
            try
            {
                String str = CharStreams.toString(reader);
                reader.close();
                return str;
            }
            catch(Throwable throwable)
            {
                try
                {
                    reader.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new ScriptNotFoundException(e.getMessage(), e);
        }
    }


    public Map<String, Object> getCustomContext()
    {
        return Collections.emptyMap();
    }
}
