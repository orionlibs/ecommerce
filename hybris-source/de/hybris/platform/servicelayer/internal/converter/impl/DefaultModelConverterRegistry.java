package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.bootstrap.codegenerator.model.ModelNameUtils;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.event.events.AfterTenantRestartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.exceptions.ModelTypeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.logging.Logs;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModelConverterRegistry extends AbstractEventListener<AfterTenantRestartEvent> implements ConverterRegistry
{
    private static final Logger LOG = Logger.getLogger(DefaultModelConverterRegistry.class);
    private I18NService i18nService;
    private CommonI18NService commonI18NService;
    private ModelService modelService;
    private SerializationStrategy defaultItemModelSerializationStrategy;
    private SourceTransformer sourceTransformer;
    private SelfHealingService selfHealingService;
    private final Map<String, ModelConverter> typeToConverterMap = new ConcurrentHashMap<>();
    private final Map<Class, MappedConverter> modelToConverterMap = (Map)new ConcurrentHashMap<>();
    private volatile boolean loaded = false;
    private Collection<ModelConverterMapping> configuredMappings;
    private static final Object NONE = new Object();
    private final Map<Class, Object> modelToConverterCache = (Map)new ConcurrentHashMap<>();
    private final Map<String, Object> typeToConverterCache = new ConcurrentHashMap<>();


    protected void onEvent(AfterTenantRestartEvent event)
    {
        LOG.info("Clearing the ConverterRegistry after tenant <<" + event.getTenantId() + ">> restart ");
        this.loaded = false;
        assertDefaultsLoaded();
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setModelConverterMappings(Collection<ModelConverterMapping> configuredMappings)
    {
        this.configuredMappings = configuredMappings;
    }


    public String getMappedType(Class<?> modelClass)
    {
        ServicesUtil.validateParameterNotNull(modelClass, "model class was null");
        assertDefaultsLoaded();
        MappedConverter conv = null;
        for(Class cl : getAllClasses(modelClass))
        {
            conv = this.modelToConverterMap.get(cl);
            if(conv != null)
            {
                break;
            }
        }
        return (conv == null) ? null : conv.type;
    }


    private ModelConverter getModelConverterByModelTypeInternal(Class<?> modelClass)
    {
        ServicesUtil.validateParameterNotNull(modelClass, "model class was null");
        assertDefaultsLoaded();
        MappedConverter conv = null;
        for(Class cl : getAllClasses(modelClass))
        {
            conv = this.modelToConverterMap.get(cl);
            if(conv != null)
            {
                break;
            }
        }
        if(conv == null)
        {
            throw new ModelTypeNotSupportedException("No converter registered for model class " + modelClass, modelClass
                            .getName());
        }
        return conv.converter;
    }


    public ModelConverter getModelConverterByModelType(Class<?> modelClass)
    {
        Object ret = this.modelToConverterCache.get(modelClass);
        if(ret == null)
        {
            try
            {
                ret = getModelConverterByModelTypeInternal(modelClass);
            }
            catch(ModelTypeNotSupportedException e)
            {
                ret = NONE;
            }
            this.modelToConverterCache.put(modelClass, ret);
        }
        if(ret == NONE)
        {
            throw new ModelTypeNotSupportedException("No converter registered for model class " + modelClass, modelClass
                            .getName());
        }
        return (ModelConverter)ret;
    }


    public ModelConverter getModelConverterBySourceType(String key)
    {
        String lowerKey = PlatformStringUtils.toLowerCaseCached(key);
        Object ret = this.typeToConverterCache.get(lowerKey);
        if(ret == null)
        {
            try
            {
                ret = getModelConverterBySourceTypeInternal(lowerKey);
            }
            catch(ModelTypeNotSupportedException e)
            {
                ret = NONE;
            }
            this.typeToConverterCache.put(lowerKey, ret);
        }
        if(ret == NONE)
        {
            throw new ModelTypeNotSupportedException("No converter registered for source type " + key, key);
        }
        return (ModelConverter)ret;
    }


    private ModelConverter getModelConverterBySourceTypeInternal(String lowerKey)
    {
        assertDefaultsLoaded();
        ModelConverter converter = this.typeToConverterMap.get(lowerKey);
        if(converter == null)
        {
            for(String superType : getAllSuperTypes(lowerKey))
            {
                String stKey = PlatformStringUtils.toLowerCaseCached(superType);
                converter = this.typeToConverterMap.get(stKey);
                if(converter != null)
                {
                    this.typeToConverterCache.put(lowerKey, converter);
                    break;
                }
            }
            if(converter == null)
            {
                throw new ModelTypeNotSupportedException("No converter registered for source type " + lowerKey, lowerKey);
            }
        }
        return converter;
    }


    public ModelConverter removeModelConverterBySourceType(String type)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("type", type);
        String key = PlatformStringUtils.toLowerCaseCached(type);
        ModelConverter converter = this.typeToConverterMap.remove(key);
        this.typeToConverterCache.remove(key);
        return converter;
    }


    public void registerModelConverter(String type, Class modelClass, ModelConverter converter)
    {
        ServicesUtil.validateParameterNotNull(type, "type class was null");
        ServicesUtil.validateParameterNotNull(modelClass, "modelClass class was null");
        if(!isValidTypeCode(type))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("code " + type + " does not belong to known type. Maybe you have added a new type without updating your system.");
            }
            return;
        }
        MappedConverter mapped = new MappedConverter(type, modelClass, (converter == null) ? createDefaultConverter(type, modelClass) : converter);
        synchronized(this)
        {
            mapped.converter.init(this);
            this.typeToConverterMap.put(PlatformStringUtils.toLowerCaseCached(mapped.type), mapped.converter);
            this.typeToConverterCache.clear();
            if(mapped.modelClass.getSimpleName().equalsIgnoreCase(type) || mapped.modelClass
                            .getSimpleName().equalsIgnoreCase(type + "model"))
            {
                this.modelToConverterMap.put(mapped.modelClass, mapped);
                this.modelToConverterCache.clear();
            }
        }
    }


    protected synchronized void loadDefaults(boolean doLogError)
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant == null || !Utilities.isSystemInitialized(tenant.getDataSource()))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("System not initialized - cannot load model converters");
            }
        }
        else
        {
            try
            {
                if(!this.loaded)
                {
                    LOG.info("loading model converters: ");
                    String prefetchMode = Config.getParameter("servicelayer.prefetch");
                    LOG.info("\tpre-fetch mode:" + (StringUtils.isEmpty(prefetchMode) ? "(default)" : prefetchMode));
                    loadConfiguredMappings();
                    loadDefaultMappings();
                    this.loaded = true;
                }
            }
            catch(Exception e)
            {
                if(RedeployUtilities.isShutdownInProgress())
                {
                    Logs.debug(LOG, () -> "Unexpected error loading model converters: " + e.getMessage() + " - will try later", e);
                }
                else if(doLogError)
                {
                    LOG.error("Unexpected error loading model converters: " + e.getMessage() + " - will try later", e);
                }
            }
        }
    }


    protected void assertDefaultsLoaded()
    {
        if(!this.loaded)
        {
            loadDefaults(true);
        }
    }


    protected void loadConfiguredMappings()
    {
        if(this.configuredMappings != null)
        {
            for(ModelConverterMapping mapping : this.configuredMappings)
            {
                registerModelConverter(mapping.getTypeCode(), mapping.getModelClass(), mapping.getConverter());
            }
            this.configuredMappings = null;
        }
    }


    protected void loadDefaultMappings()
    {
        YTypeSystem typeSystem = TypeSystemUtils.loadViaClassLoader(Registry.getCurrentTenant().getTenantSpecificExtensionNames());
        for(YComposedType ct : typeSystem.getComposedTypes())
        {
            Class<?> modelClass;
            if(!ct.isGenerateModel() || ct instanceof de.hybris.bootstrap.typesystem.YRelation || "ExtensibleItem".equalsIgnoreCase(ct.getCode()) || "LocalizableItem"
                            .equalsIgnoreCase(ct.getCode()) || "GenericItem".equalsIgnoreCase(ct.getCode()) || this.typeToConverterMap
                            .containsKey(PlatformStringUtils.toLowerCaseCached(ct.getCode())))
            {
                continue;
            }
            ExtensionInfo info = Utilities.getExtensionInfo(((YExtension)ct.getNamespace()).getExtensionName());
            if(info == null)
            {
                LOG.error("cannot find extension info for '" + ((YExtension)ct.getNamespace()).getExtensionName() + " - skipping type " + ct
                                .getCode());
                continue;
            }
            if(info.getCoreModule() == null)
            {
                LOG.error("extension info for '" + ((YExtension)ct.getNamespace()).getExtensionName() + " got no core module - skipping type " + ct
                                .getCode());
                continue;
            }
            String modelClassName = ModelNameUtils.getModel(ct, info.getCoreModule().getPackageRoot());
            try
            {
                modelClass = Class.forName(modelClassName);
            }
            catch(Exception e)
            {
                LOG.error("cannot load model class '" + modelClassName + "' due to " + e.getMessage() + " - skipping type " + ct
                                .getCode());
                continue;
            }
            registerModelConverter(ct.getCode(), modelClass, null);
        }
        for(YEnumType ct : typeSystem.getEnumTypes())
        {
            Class<?> modelClass;
            if(this.typeToConverterMap.containsKey(PlatformStringUtils.toLowerCaseCached(ct.getCode())))
            {
                continue;
            }
            ExtensionInfo info = Utilities.getExtensionInfo(((YExtension)ct.getNamespace()).getExtensionName());
            if(info == null)
            {
                LOG.error("cannot find extension info for '" + ((YExtension)ct.getNamespace()).getExtensionName() + " - skipping enum " + ct
                                .getCode());
                continue;
            }
            if(info.getCoreModule() == null)
            {
                LOG.error("extension info for '" + ((YExtension)ct.getNamespace()).getExtensionName() + " got no core module - skipping enum " + ct
                                .getCode());
                continue;
            }
            String className = ModelNameUtils.getEnumModel(ct, info
                            .getCoreModule().getPackageRoot());
            try
            {
                modelClass = Class.forName(className);
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("cannot load enum model class '" + className + "' due to " + e.getMessage() + " - skipping type " + ct
                                .getCode());
                continue;
            }
            registerModelConverter(ct.getCode(), modelClass, (ModelConverter)new EnumValueModelConverter(modelClass, getSourceTransformer()));
        }
    }


    protected ModelConverter createDefaultConverter(String code, Class modelClass)
    {
        return (ModelConverter)new ItemModelConverter(getModelService(), getI18nService(), getCommonI18NService(), code, modelClass,
                        getDefaulItemModelSerializationStrategy(), getSourceTransformer(), getSelfHealingService());
    }


    protected TypeManager getJaloTypeManager()
    {
        return TypeManager.getInstance();
    }


    protected Set<String> getAllTypes(String type)
    {
        try
        {
            Set<String> ret = new HashSet<>();
            ret.add(type);
            for(ComposedType ct : getJaloTypeManager().getComposedType(type).getAllSubTypes())
            {
                ret.add(ct.getCode());
            }
            return ret;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("unknown type " + type, e);
        }
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


    protected boolean isAssignableFrom(String superType, String type)
    {
        return getJaloTypeManager().getComposedType(superType).isAssignableFrom((Type)getJaloTypeManager().getComposedType(type));
    }


    protected List<Class> getAllClasses(Class<?> clazz)
    {
        List<Class<?>> ret = new ArrayList<>();
        for(Class<?> c = clazz; c != null; c = c.getSuperclass())
        {
            ret.add(c);
        }
        return ret;
    }


    protected List<String> getAllSuperTypes(String type)
    {
        ComposedType cType = getJaloTypeManager().getComposedType(type);
        List<ComposedType> allSuper = cType.getAllSuperTypes();
        if(allSuper.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        String[] codes = new String[allSuper.size()];
        int index = 0;
        for(ComposedType t : allSuper)
        {
            codes[index++] = t.getCode();
        }
        return Arrays.asList(codes);
    }


    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public SourceTransformer getSourceTransformer()
    {
        return this.sourceTransformer;
    }


    @Required
    public void setSourceTransformer(SourceTransformer sourceTransformer)
    {
        this.sourceTransformer = sourceTransformer;
    }


    public SelfHealingService getSelfHealingService()
    {
        return this.selfHealingService;
    }


    @Required
    public void setSelfHealingService(SelfHealingService selfHealingService)
    {
        this.selfHealingService = selfHealingService;
    }


    public SerializationStrategy getDefaulItemModelSerializationStrategy()
    {
        return this.defaultItemModelSerializationStrategy;
    }


    public void setDefaulItemModelSerializationStrategy(SerializationStrategy defaulItemModelSerializationStrategy)
    {
        this.defaultItemModelSerializationStrategy = defaulItemModelSerializationStrategy;
    }


    public boolean hasModelConverterForModelType(Class<?> clazz)
    {
        Object ret = this.modelToConverterCache.get(clazz);
        if(ret == null)
        {
            try
            {
                ret = getModelConverterByModelType(clazz);
            }
            catch(ModelTypeNotSupportedException e)
            {
                ret = NONE;
            }
            this.modelToConverterCache.put(clazz, ret);
        }
        return (ret != NONE);
    }


    public boolean hasModelConverterForSourceType(String key)
    {
        String lowerKey = key.toLowerCase(LocaleHelper.getPersistenceLocale());
        Object ret = this.typeToConverterCache.get(lowerKey);
        if(ret == null)
        {
            try
            {
                ret = getModelConverterBySourceType(key);
            }
            catch(ModelTypeNotSupportedException e)
            {
                ret = NONE;
            }
            this.typeToConverterCache.put(lowerKey, ret);
        }
        return (ret != NONE);
    }


    public Collection<ModelConverter> getModelConverters()
    {
        return Collections.unmodifiableCollection(this.typeToConverterMap.values());
    }


    public void clearModelConverters()
    {
        this.typeToConverterMap.clear();
        this.typeToConverterCache.clear();
    }


    public ModelConverter getModelConverterByModel(Object model)
    {
        String type = getSourceTypeFromModel(model);
        ModelConverter ret = null;
        if(type != null)
        {
            ret = getModelConverterBySourceType(type);
            if(model instanceof de.hybris.platform.servicelayer.model.AbstractItemModel && ret instanceof EnumValueModelConverter)
            {
                ret = null;
            }
        }
        if(ret == null)
        {
            ret = getModelConverterByModelType(model.getClass());
        }
        return ret;
    }


    protected String getSourceTypeFromModel(Object model)
    {
        return getModelConverterByModelType(model.getClass()).getType(model);
    }
}
