package de.hybris.platform.scripting.engine.repository.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ModelScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.impl.DefaultAutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.impl.DoNotExecuteWhenDisabledStrategy;
import de.hybris.platform.scripting.engine.internal.cache.ScriptExecutablesCacheService;
import de.hybris.platform.scripting.engine.internal.cache.impl.ModelScriptCacheKey;
import de.hybris.platform.scripting.engine.repository.CacheableScriptsRepository;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class ModelScriptsRepository extends AbstractScriptsRepository implements CacheableScriptsRepository, InitializingBean
{
    public static final String REVISION_SEPARATOR = "/";
    public static final String PROTOCOL = "model";
    private static final Splitter REV_SPLITTER = Splitter.on("/");
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;
    private SessionService sessionService;
    private DefaultGenericDao<ScriptModel> dao;
    private ScriptExecutablesCacheService cacheService;
    private ModelService modelService;
    private String tenantId;
    private final InvalidationListener invalidationListener = (InvalidationListener)new Object(this);


    @PostConstruct
    public void init()
    {
        this.tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this.invalidationListener);
    }


    public ScriptContent lookupScript(String protocol, String path)
    {
        Preconditions.checkNotNull(path, "path is required");
        ScriptModel scriptModel = findScript(path);
        if(scriptModel == null)
        {
            throw new ScriptNotFoundException("ScriptModel for path: " + path + " not found");
        }
        return (ScriptContent)new ModelScriptContent(scriptModel);
    }


    private ScriptModel findScript(String path)
    {
        if(path.contains("/"))
        {
            return findScriptForRevision(path);
        }
        return findActiveScript(path);
    }


    private ScriptModel findScriptForRevision(String path)
    {
        String[] parts = splitScriptPath(path);
        ImmutableMap immutableMap = ImmutableMap.builder().put("code", parts[0]).put("version", parseVersionNumber(parts[1])).build();
        List<ScriptModel> scriptModels = this.dao.find((Map)immutableMap);
        if(scriptModels.size() != 1)
        {
            throw new ScriptNotFoundException("ScriptModel with code: " + parts[0] + "and version" + parts[1] + " not found");
        }
        return scriptModels.iterator().next();
    }


    private Long parseVersionNumber(String version)
    {
        try
        {
            return Long.valueOf(version);
        }
        catch(NumberFormatException e)
        {
            throw new NumberFormatException("ScriptModel version is not a number");
        }
    }


    public ScriptModel findActiveScript(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("code", code).put("active", Boolean.TRUE).build();
        List<ScriptModel> scriptModels = this.dao.find((Map)immutableMap);
        if(scriptModels.isEmpty())
        {
            throw new ScriptNotFoundException("ScriptModel with code: " + code + " not found");
        }
        if(scriptModels.size() == 1)
        {
            return scriptModels.get(0);
        }
        throw new IllegalArgumentException("Found multiple active scripts for code " + code);
    }


    public boolean scriptExists(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("code", code).put("active", Boolean.TRUE).build();
        List<ScriptModel> scriptModels = this.dao.find((Map)immutableMap);
        return !scriptModels.isEmpty();
    }


    public ScriptContent lookupScript(CacheKey cacheKey)
    {
        DoNotExecuteWhenDisabledStrategy doNotExecuteWhenDisabledStrategy;
        Preconditions.checkNotNull(cacheKey, "cacheKey is required");
        Preconditions.checkState(cacheKey instanceof ModelScriptCacheKey, "cacheKey must be instance of ModelScriptCacheKey.class");
        ScriptModel scriptModel = (ScriptModel)this.modelService.get(((ModelScriptCacheKey)cacheKey).getPk());
        if(scriptModel.isAutodisabling())
        {
            DefaultAutoDisablingScriptStrategy defaultAutoDisablingScriptStrategy = new DefaultAutoDisablingScriptStrategy(scriptModel.getPk(), this.modelService, this.sessionService);
        }
        else
        {
            doNotExecuteWhenDisabledStrategy = new DoNotExecuteWhenDisabledStrategy(scriptModel.getPk(), this.modelService);
        }
        return (ScriptContent)new ModelScriptContent(scriptModel, (AutoDisablingScriptStrategy)doNotExecuteWhenDisabledStrategy);
    }


    public CacheKey createCacheKey(String protocol, String path)
    {
        Preconditions.checkNotNull(path, "path is required");
        ScriptModel script = findScript(path);
        return (CacheKey)new ModelScriptCacheKey(script.getPk(), this.tenantId);
    }


    private String[] splitScriptPath(String path)
    {
        Iterable<String> result = REV_SPLITTER.split(path);
        Preconditions.checkState((Iterables.size(result) == 2), "splitting model path should contain 2 elements");
        return (String[])Iterables.toArray(result, String.class);
    }


    public List<ScriptModel> findAllActiveScriptsForType(ScriptType scriptType)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("scriptType", scriptType).put("active", Boolean.TRUE).build();
        return this.dao.find((Map)immutableMap);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCacheService(ScriptExecutablesCacheService cacheService)
    {
        this.cacheService = cacheService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public List<ScriptModel> findAllRevisionsForCode(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("code", code).build();
        SortParameters sortParameters = new SortParameters();
        sortParameters.addSortParameter("version", SortParameters.SortOrder.ASCENDING);
        return this.dao.find((Map)immutableMap, sortParameters);
    }


    public void afterPropertiesSet() throws Exception
    {
        this.dao = new DefaultGenericDao("Script");
        this.dao.setFlexibleSearchService(this.flexibleSearchService);
    }
}
