package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Key;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;

public class DefaultInterceptorRegistry implements InterceptorRegistry
{
    private static final Logger LOG = Logger.getLogger(DefaultInterceptorRegistry.class.getName());
    private volatile boolean mappingsLoaded = false;
    private Collection<InterceptorMapping> configuredMappings;
    private final ConcurrentMap<String, MappedInterceptors> typeToInterceptorsMap = new ConcurrentHashMap<>(256, 0.75F, 32);
    private final Map<String, List<String>> allTypesCache = new ConcurrentHashMap<>(1000, 0.75F, 64);
    private final Map<Key, Collection<? extends Interceptor>> allInterceptorsCache = new ConcurrentHashMap<>(1024, 0.75F, 4);
    private InterceptorExecutionPolicy interceptorExecutionPolicy;
    private ApplicationContext applicationContext;
    private final Map<String, Class> beanClassMapping = (Map)new HashMap<>();


    public DefaultInterceptorRegistry()
    {
        init();
    }


    public Collection<LoadInterceptor> getLoadInterceptors(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        return getInterceptors(type, InterceptorExecutionPolicy.InterceptorType.LOAD);
    }


    public Collection<PrepareInterceptor> getPrepareInterceptors(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        return getInterceptors(type, InterceptorExecutionPolicy.InterceptorType.PREPARE);
    }


    public Collection<RemoveInterceptor> getRemoveInterceptors(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        return getInterceptors(type, InterceptorExecutionPolicy.InterceptorType.REMOVE);
    }


    public Collection<ValidateInterceptor> getValidateInterceptors(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        return getInterceptors(type, InterceptorExecutionPolicy.InterceptorType.VALIDATE);
    }


    public Collection<InitDefaultsInterceptor> getInitDefaultsInterceptors(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        return getInterceptors(type, InterceptorExecutionPolicy.InterceptorType.INIT_DEFAULTS);
    }


    public void registerInterceptor(String type, Interceptor interceptor, Collection<Interceptor> replacements)
    {
        InterceptorMapping mapping = new InterceptorMapping();
        mapping.setTypeCode(type);
        mapping.setInterceptor(interceptor);
        mapping.setReplacedInterceptors(replacements);
        registerInterceptor(mapping);
    }


    public void registerInterceptor(InterceptorMapping mapping)
    {
        String type = mapping.getTypeCode();
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        if(!isValidTypeCode(type))
        {
            LOG.warn("typecode '" + type + "' does not belong to known type! Ignoring Interceptor: " + mapping.getInterceptor());
            return;
        }
        if(mapping.getInterceptor() == null)
        {
            LOG.warn("Mapping for " + type + " has no defined interceptor - skipping registration. Use VoidInterceptor instead of null in bean definition.");
            return;
        }
        this.allInterceptorsCache.clear();
        MappedInterceptors newOne = new MappedInterceptors();
        MappedInterceptors mapped = this.typeToInterceptorsMap.putIfAbsent(PlatformStringUtils.toLowerCaseCached(type), newOne);
        mapped = (mapped == null) ? newOne : mapped;
        Class<?> interceptorClass = mapping.getInterceptor().getClass();
        String[] beanNamesForType = this.applicationContext.getBeanNamesForType(interceptorClass);
        for(String bean : beanNamesForType)
        {
            this.beanClassMapping.put(bean, interceptorClass);
        }
        mapped.addInterceptor(mapping);
    }


    public void unregisterInterceptor(InterceptorMapping mapping)
    {
        String type = mapping.getTypeCode();
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null!");
        if(!isValidTypeCode(type))
        {
            LOG.warn("typecode '" + type + "' does not belong to known type! Ignoring Interceptor: " + mapping.getInterceptor());
            return;
        }
        this.allInterceptorsCache.clear();
        MappedInterceptors mapped = this.typeToInterceptorsMap.get(PlatformStringUtils.toLowerCaseCached(type));
        if(mapped != null)
        {
            mapped.removeInterceptor(mapping);
        }
    }


    protected void init()
    {
        addTypeInvalidationListener();
    }


    protected void addTypeInvalidationListener()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new TypeInvalidationListener(this));
    }


    private <T extends Interceptor> Collection<T> getEnabledInterceptors(String itemType, InterceptorExecutionPolicy.InterceptorType interceptorType)
    {
        InterceptorExecutionPolicy.InterceptorExecutionContext<T> ctx = createInterceptorExecutionContext(itemType, interceptorType);
        return this.interceptorExecutionPolicy.getEnabledInterceptors(ctx);
    }


    private <T extends Interceptor> InterceptorExecutionPolicy.InterceptorExecutionContext<T> createInterceptorExecutionContext(String itemType, InterceptorExecutionPolicy.InterceptorType interceptorType)
    {
        Collection<? extends Interceptor> availableInterceptors = findInterceptors(itemType, interceptorType
                        .getBaseInterceptorClass());
        List<String> typesHierarchy = getAssignableTypes(itemType);
        return new InterceptorExecutionPolicy.InterceptorExecutionContext(itemType, this.beanClassMapping, availableInterceptors, interceptorType, typesHierarchy);
    }


    private <T extends Interceptor> Collection<T> getInterceptors(String itemType, InterceptorExecutionPolicy.InterceptorType interceptorType)
    {
        assertLoaded();
        return getEnabledInterceptors(itemType, interceptorType);
    }


    private <T extends Interceptor> Collection<T> findInterceptors(String type, Class<T> interceptorType)
    {
        Key<String, Class<T>> key = Key.get(type, interceptorType);
        Collection<T> result = (Collection<T>)this.allInterceptorsCache.get(key);
        if(result == null)
        {
            List<InterceptorMapping> mappings = null;
            List<String> types = getAssignableTypes(type);
            for(String assignableType : types)
            {
                MappedInterceptors mappedPerType = this.typeToInterceptorsMap.get(assignableType);
                if(mappedPerType != null)
                {
                    List<InterceptorMapping> mappingsPerType = mappedPerType.getInterceptorMappings(interceptorType);
                    if(!mappingsPerType.isEmpty())
                    {
                        if(mappings == null)
                        {
                            mappings = new ArrayList<>(types.size() * 5);
                        }
                        mappings.addAll(mappingsPerType);
                    }
                }
            }
            if(mappings != null)
            {
                result = new ArrayList<>(mappings.size());
                OrderComparator.sort(mappings);
                for(InterceptorMapping mapping : mappings)
                {
                    result.add((T)mapping.getInterceptor());
                }
                for(InterceptorMapping mapping : mappings)
                {
                    result.removeAll(mapping.getReplacedInterceptors());
                }
                result = Collections.unmodifiableCollection(result);
            }
            else
            {
                result = Collections.emptyList();
            }
            this.allInterceptorsCache.put(Key.create(type, interceptorType), result);
        }
        return result;
    }


    protected List<String> getAssignableTypes(String type)
    {
        List<String> ret = this.allTypesCache.get(type);
        if(ret == null)
        {
            List<ComposedType> ctList = getJaloTypeManager().getComposedType(type).getAllSuperTypes();
            ret = new ArrayList<>(ctList.size() + 1);
            ret.add(PlatformStringUtils.toLowerCaseCached(type));
            for(ComposedType ct : ctList)
            {
                ret.add(PlatformStringUtils.toLowerCaseCached(ct.getCode()));
            }
            Collections.reverse(ret);
            this.allTypesCache.put(type, ret);
        }
        return ret;
    }


    public void setInterceptorMappings(Collection<InterceptorMapping> configuredMappings)
    {
        this.configuredMappings = configuredMappings;
    }


    protected void assertLoaded()
    {
        if(!this.mappingsLoaded)
        {
            synchronized(this)
            {
                if(!this.mappingsLoaded)
                {
                    if(this.configuredMappings != null)
                    {
                        for(InterceptorMapping mapping : this.configuredMappings)
                        {
                            registerInterceptor(mapping);
                        }
                    }
                    this.mappingsLoaded = true;
                }
            }
        }
    }


    protected TypeManager getJaloTypeManager()
    {
        return TypeManager.getInstance();
    }


    protected boolean isValidTypeCode(String code)
    {
        try
        {
            return (getJaloTypeManager().getComposedType(code) != null);
        }
        catch(JaloItemNotFoundException e)
        {
            return false;
        }
    }


    @Required
    public void setInterceptorExecutionPolicy(InterceptorExecutionPolicy interceptorExecutionPolicy)
    {
        this.interceptorExecutionPolicy = interceptorExecutionPolicy;
    }


    @Required
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
