package de.hybris.platform.scripting.engine.repository.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ResourceScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.internal.cache.impl.SimpleScriptCacheKey;
import de.hybris.platform.scripting.engine.repository.CacheableScriptsRepository;
import javax.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ClasspathScriptsRepository extends AbstractScriptsRepository implements CacheableScriptsRepository
{
    private static final String PROTOCOL = "classpath";
    protected String tenantId;


    @PostConstruct
    public void init()
    {
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
    }


    public ScriptContent lookupScript(String protocol, String path)
    {
        Preconditions.checkNotNull(path, "path is required");
        ClassPathResource resource = new ClassPathResource(path);
        validateScriptResource(resource);
        return (ScriptContent)new ResourceScriptContent((Resource)resource);
    }


    private void validateScriptResource(ClassPathResource resource)
    {
        if(!resource.exists())
        {
            throw new ScriptNotFoundException("Script resource: " + resource + " not found in the classpath");
        }
    }


    public ScriptContent lookupScript(CacheKey cacheKey)
    {
        Preconditions.checkNotNull(cacheKey, "cacheKey is required");
        Preconditions.checkState(cacheKey instanceof SimpleScriptCacheKey, "cacheKey must be instance of ModelScriptCacheKey.class");
        return lookupScript("classpath", ((SimpleScriptCacheKey)cacheKey).getPath());
    }


    public CacheKey createCacheKey(String protocol, String path)
    {
        Preconditions.checkNotNull(path, "path is required");
        return (CacheKey)new SimpleScriptCacheKey("classpath", path, this.tenantId);
    }
}
