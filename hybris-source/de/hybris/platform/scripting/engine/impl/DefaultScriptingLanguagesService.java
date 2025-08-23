package de.hybris.platform.scripting.engine.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.AutoDisablingScriptContent;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptCompilationException;
import de.hybris.platform.scripting.engine.exception.ScriptURIException;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;
import de.hybris.platform.scripting.engine.internal.cache.ScriptExecutablesCacheService;
import de.hybris.platform.scripting.engine.repository.CacheableScriptsRepository;
import de.hybris.platform.scripting.engine.repository.ScriptRepositoriesRegistry;
import de.hybris.platform.scripting.engine.repository.ScriptsRepository;
import de.hybris.platform.util.logging.Logs;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class DefaultScriptingLanguagesService implements ScriptingLanguagesService
{
    private static final Logger LOG = Logger.getLogger(DefaultScriptingLanguagesService.class);
    private ScriptEngineManager engineManager;
    private ScriptExecutablesCacheService cacheService;
    private ScriptEnginesRegistry scriptEnginesRegistry;
    private ScriptRepositoriesRegistry scriptRepositoriesRegistry;
    private final Map<String, EngineLoadingStrategy> engine2loadingStrategyMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init()
    {
        this.engineManager = new ScriptEngineManager();
    }


    public ScriptExecutable getExecutableByContent(ScriptContent scriptContent)
    {
        return buildExecutableFromContent(scriptContent);
    }


    private ScriptEngine getEngine(String engineName)
    {
        String engineKey = generateEngineKey(engineName);
        EngineLoadingStrategy strat = this.engine2loadingStrategyMap.get(engineKey);
        return (strat == null) ? createLoadStrategyAndLoad(engineName, engineKey) : strat.load();
    }


    private String generateEngineKey(String engineName)
    {
        return engineName + "_" + engineName;
    }


    private ScriptEngine createLoadStrategyAndLoad(String engineName, String engineKey)
    {
        ScriptEngineType engineType = this.scriptEnginesRegistry.getScriptEngineType(engineName);
        EngineLoadingStrategy newStrategy = () -> this.engineManager.getEngineByName(engineType.getName());
        ScriptEngine engine = newStrategy.load();
        if(engine == null)
        {
            newStrategy = (() -> this.engineManager.getEngineByExtension(engineType.getFileExtension()));
            engine = newStrategy.load();
        }
        if(engine == null)
        {
            newStrategy = (() -> this.engineManager.getEngineByMimeType(engineType.getMime()));
            engine = newStrategy.load();
        }
        if(engine != null)
        {
            CachedEngineLoadingStrategy cachedEngineLoadingStrategy;
            if("Graal.js".equalsIgnoreCase(engine.getFactory().getEngineName()))
            {
                GraalEngineLoadingStrategy graalEngineLoadingStrategy = new GraalEngineLoadingStrategy();
                engine = graalEngineLoadingStrategy.load();
            }
            if(engineType.canBeCached())
            {
                cachedEngineLoadingStrategy = new CachedEngineLoadingStrategy(engine);
            }
            this.engine2loadingStrategyMap.put(engineKey, cachedEngineLoadingStrategy);
            return engine;
        }
        throw new IllegalStateException("No such engine identified by: " + engineType + " (lookup order by 'name', 'file extension', 'mime')");
    }


    public ScriptExecutable getExecutableByURI(String scriptURI)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Getting executable by scriptURI [scriptURI: " + scriptURI + "]");
        }
        String[] uriParts = splitScriptURI(scriptURI);
        String protocol = uriParts[0];
        String path = uriParts[1];
        ScriptsRepository repository = this.scriptRepositoriesRegistry.getRepositoryByProtocol(protocol);
        if(repository == null)
        {
            throw new IllegalStateException("Repository must not be null");
        }
        CacheableScriptsRepository cachingRepository = asCachingRepository(repository);
        if(cachingRepository != null)
        {
            CacheKey cacheKey = cachingRepository.createCacheKey(protocol, path);
            URIScriptExecutableLoader loader = new URIScriptExecutableLoader(this, cachingRepository, scriptURI);
            ScriptExecutablesCacheService.ScriptDTO scriptDTO = this.cacheService.putOrGetFromCache(cacheKey, (CacheValueLoader)loader);
            return create(scriptDTO.getScript(), scriptDTO.getGlobalContext(), scriptDTO.getEngine().getFactory(), scriptDTO
                            .getStrategy());
        }
        return buildExecutableFromContent(repository.lookupScript(protocol, path));
    }


    private ScriptExecutable buildExecutableFromContent(ScriptContent scriptContent)
    {
        ScriptEngine engine = getEngine(scriptContent.getEngineName());
        Object scriptBody = precompileOrGetRaw(engine, scriptContent);
        return create(scriptBody, scriptContent.getCustomContext(), engine.getFactory(),
                        getAutoDisablingScriptStrategy(scriptContent));
    }


    private ScriptExecutable create(Object scriptBody, Map<String, Object> globalContext, ScriptEngineFactory factory, AutoDisablingScriptStrategy strategy)
    {
        if(scriptBody instanceof CompiledScript)
        {
            return (ScriptExecutable)new PrecompiledExecutable((CompiledScript)scriptBody, globalContext, getApplicationContext(), strategy);
        }
        return (ScriptExecutable)new InterpretedScriptExecutable((String)scriptBody, factory, getApplicationContext(), globalContext, strategy);
    }


    ApplicationContext getApplicationContext()
    {
        return Registry.getApplicationContext();
    }


    private Object precompileOrGetRaw(ScriptEngine engine, ScriptContent scriptContent)
    {
        if(isCompilable(engine))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Precompiling scriptContent: " + scriptContent);
            }
            try
            {
                return ((Compilable)engine).compile(scriptContent.getContent());
            }
            catch(ScriptException e)
            {
                throw new ScriptCompilationException(e.getMessage(), e);
            }
            catch(Throwable e)
            {
                Logs.debug(LOG, () -> "Compilation of script has failed. Will try Interpreted version. [engine: " + engine + ", message: " + e.getMessage() + "]");
            }
        }
        return scriptContent.getContent();
    }


    private boolean isCompilable(ScriptEngine engine)
    {
        return engine instanceof Compilable;
    }


    private CacheableScriptsRepository asCachingRepository(ScriptsRepository repository)
    {
        try
        {
            return (CacheableScriptsRepository)repository;
        }
        catch(ClassCastException e)
        {
            return null;
        }
    }


    private String[] splitScriptURI(String scriptURI)
    {
        try
        {
            String[] uriParts = scriptURI.split("://");
            validateUriParts(uriParts);
            uriParts[0] = uriParts[0].trim();
            uriParts[1] = uriParts[1].trim();
            return uriParts;
        }
        catch(Exception e)
        {
            throw new ScriptURIException(e.getMessage(), e);
        }
    }


    private void validateUriParts(String[] uriParts)
    {
        Preconditions.checkNotNull(uriParts, "Split result for URI cannot be null");
        Preconditions.checkState((uriParts.length == 2), "Split result for URI must have 2 parts - protocol and path (ie. classpath://foo/bar)");
        Preconditions.checkState(StringUtils.isNotBlank(uriParts[0]), "Protocol part must not be empty");
        Preconditions.checkState(StringUtils.isNotBlank(uriParts[1]), "Path part must not be empty");
    }


    @Required
    public void setCacheService(ScriptExecutablesCacheService cacheService)
    {
        this.cacheService = cacheService;
    }


    @Required
    public void setScriptEnginesRegistry(ScriptEnginesRegistry scriptEnginesRegistry)
    {
        this.scriptEnginesRegistry = scriptEnginesRegistry;
    }


    @Required
    public void setScriptRepositoriesRegistry(ScriptRepositoriesRegistry scriptRepositoriesRegistry)
    {
        this.scriptRepositoriesRegistry = scriptRepositoriesRegistry;
    }


    private AutoDisablingScriptStrategy getAutoDisablingScriptStrategy(ScriptContent scriptContent)
    {
        if(scriptContent instanceof AutoDisablingScriptContent)
        {
            return ((AutoDisablingScriptContent)scriptContent).getAutoDisablingScriptStrategy();
        }
        return null;
    }
}
