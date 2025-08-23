package de.hybris.platform.servicelayer.internal.converter.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.locking.ItemLockedForProcessingException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.directpersistence.selfhealing.ItemToHeal;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.directpersistence.selfhealing.impl.DefaultSelfHealingService;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;
import de.hybris.platform.servicelayer.exceptions.ModelForItemAlreadyBeingLoadedException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.ReadParams;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.internal.converter.util.PrimitiveDefaults;
import de.hybris.platform.servicelayer.internal.model.LocMap;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.internal.model.attribute.impl.DefaultDynamicAttributesProvider;
import de.hybris.platform.servicelayer.internal.model.impl.AttributeProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.internal.polyglot.PolyglotPersistenceServiceLayerSupport;
import de.hybris.platform.servicelayer.internal.polyglot.ServiceLayerPersistenceInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemContextBuilder;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.model.strategies.DefaultFetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.FetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.Key;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ReflectionUtils;

public class ItemModelConverter implements Cloneable, UpdateableModelConverter, TypeSystemAwareModelConverter
{
    private final UUID uuid = UUID.randomUUID();
    private static final Logger LOG = Logger.getLogger(ItemModelConverter.class);
    private static final String LEGACYPERSISTENCE = "legacyPersistence";
    private final Class<? extends AbstractItemModel> modelClass;
    private transient Constructor<? extends AbstractItemModel> modelClassConstructor;
    private final String defaultType;
    private static final String PK = GeneratedCoreConstants.Attributes.Item.PK;
    private static final String ITEMTYPE = GeneratedCoreConstants.Attributes.Item.ITEMTYPE;
    private volatile TypeMetaInfo metaInfo;
    private volatile Set<String> writablePartOfAttributes;
    private final ModelService modelService;
    private final I18NService i18nService;
    private final CommonI18NService commonI18NService;
    private final SourceTransformer sourceTransformer;
    private final LocaleProvider localeProvider;
    private final SerializationStrategy serializationStrategy;
    private final LocalizedAttributesProcessor localizedAttributesProcessor;
    private final SelfHealingService selfHealingService;
    private final AttributePrefetchMode ATTRIBUTE_PREFETCH_MODE = readPrefetchSettings();


    private static boolean isForceInsertPk()
    {
        return Config.getBoolean("impex.force.insert.pk", false);
    }


    private static ItemModelContextImpl getItemModelContextImpl(Object model)
    {
        return (ItemModelContextImpl)ModelContextUtils.getItemModelContext((AbstractItemModel)model);
    }


    protected AttributePrefetchMode readPrefetchSettings()
    {
        return (new PrefetchModeResolver(getConfig().getParameter("servicelayer.prefetch"))).getPrefetchMode();
    }


    protected ConfigIntf getConfig()
    {
        return Registry.getCurrentTenant().getConfig();
    }


    public ItemModelConverter(ModelService modelService, I18NService i18nService, CommonI18NService commonI18NService, String type, Class<? extends AbstractItemModel> modelClass, SerializationStrategy serializationStrategy)
    {
        this(modelService, i18nService, commonI18NService, type, modelClass, serializationStrategy,
                        (SourceTransformer)Registry.getApplicationContext().getBean("sourceTransformer", SourceTransformer.class),
                        (SelfHealingService)Registry.getApplicationContext().getBean("defaultSelfHealingService", DefaultSelfHealingService.class));
    }


    public ItemModelConverter(ModelService modelService, I18NService i18nService, CommonI18NService commonI18NService, String type, Class<? extends AbstractItemModel> modelClass, SerializationStrategy serializationStrategy, SourceTransformer sourceTransformer, SelfHealingService selfHealingService)
    {
        ServicesUtil.validateParameterNotNull(modelClass, "modelClass was null");
        ServicesUtil.validateParameterNotNull(type, "type was null");
        ServicesUtil.validateParameterNotNull(i18nService, "i18nService was null");
        ServicesUtil.validateParameterNotNull(commonI18NService, "commonI18NService was null");
        ServicesUtil.validateParameterNotNull(sourceTransformer, "sourceTransformer was null");
        ServicesUtil.validateParameterNotNull(selfHealingService, "selfHealingService was null");
        this.modelClass = modelClass;
        this.modelService = modelService;
        this.defaultType = type;
        this.i18nService = i18nService;
        this.sourceTransformer = sourceTransformer;
        this.commonI18NService = commonI18NService;
        this.localeProvider = (LocaleProvider)new DefaultLocaleProvider(i18nService);
        this.serializationStrategy = serializationStrategy;
        this.localizedAttributesProcessor = new LocalizedAttributesProcessor(i18nService);
        this.selfHealingService = selfHealingService;
    }


    private static final Class[] MODEL_CONSTR_SIG = new Class[] {ItemModelContext.class};


    private Constructor<? extends AbstractItemModel> getModelConstructorForContext()
    {
        if(this.modelClassConstructor == null)
        {
            try
            {
                this.modelClassConstructor = this.modelClass.getDeclaredConstructor(MODEL_CONSTR_SIG);
                if(!this.modelClassConstructor.isAccessible())
                {
                    this.modelClassConstructor.setAccessible(true);
                }
            }
            catch(NoSuchMethodException e)
            {
                throw new ModelCreationException("Error while creating new instance of model type " + this.modelClass.getName(), e);
            }
        }
        return this.modelClassConstructor;
    }


    private TypeMetaInfo getTypeMetaInfo()
    {
        TypeMetaInfo ret = this.metaInfo;
        if(ret == null)
        {
            synchronized(this)
            {
                ret = this.metaInfo;
                if(ret == null)
                {
                    ComposedType ct = TypeManager.getInstance().getComposedType(getDefaultType());
                    ret = assembleMetaInfo(ct, calculateTypeAttributeInfos(ct));
                    this.metaInfo = ret;
                }
            }
        }
        return ret;
    }


    public List<ModelAttributeInfo> getInitialModelAttributes()
    {
        return (getTypeMetaInfo())._modelAttributesInitial;
    }


    public List<ModelAttributeInfo> getAllModelAttributes()
    {
        return (getTypeMetaInfo())._modelAttributesAll;
    }


    public ModelAttributeInfo getInfo(String qualfier)
    {
        return (ModelAttributeInfo)(getTypeMetaInfo())._modelAttributeLookupMap.get(qualfier);
    }


    public AbstractItemModel create(String type)
    {
        return createModelInstance(createContextForNewModel(type));
    }


    public boolean isConfiguredForLegacyPersistence()
    {
        return (getTypeMetaInfo())._legacyPersistence;
    }


    protected AbstractItemModel createModelInstance(ItemModelInternalContext ctx)
    {
        try
        {
            return getModelConstructorForContext().newInstance(new Object[] {ctx});
        }
        catch(InvocationTargetException | IllegalAccessException | InstantiationException e)
        {
            throw new ModelCreationException("Error while creating new instance of model type " + this.modelClass.getName(), e);
        }
    }


    protected ItemModelInternalContext createContextForNewModel(String type)
    {
        ItemContextBuilder builder = new ItemContextBuilder();
        builder.setItemType((type != null) ? type : getDefaultType());
        builder.setTenantID(getTenantId());
        builder.setLocaleProvider(getLocaleProvider());
        builder.setDynamicAttributesProvider(getDynamicAttributesProvider());
        builder.setSerializationStrategy(getSerializationStrategy());
        builder.setFetchStrategy((FetchStrategy)new DefaultFetchStrategy());
        builder.setValueHistory(new ModelValueHistory());
        return builder.build();
    }


    protected ItemModelInternalContext createContextForExistingModel(String type, PK pk, long persistenceVersion, AttributeProvider attributeProvider)
    {
        ItemContextBuilder builder = new ItemContextBuilder();
        builder.setPk(pk);
        builder.setItemType((type != null) ? type : getDefaultType());
        builder.setTenantID(getTenantId());
        builder.setAttributeProvider(attributeProvider);
        builder.setLocaleProvider(getLocaleProvider());
        builder.setDynamicAttributesProvider(getDynamicAttributesProvider());
        builder.setSerializationStrategy(getSerializationStrategy());
        builder.setFetchStrategy((FetchStrategy)new DefaultFetchStrategy());
        builder.setValueHistory(new ModelValueHistory(attributeProvider));
        builder.getValueHistory().setPersistenceVersion(persistenceVersion);
        return builder.build();
    }


    String getTenantId()
    {
        Tenant currentTenantNoFallback = Registry.getCurrentTenantNoFallback();
        return (currentTenantNoFallback == null) ? null : currentTenantNoFallback.getTenantID();
    }


    public final AbstractItemModel load(Object source)
    {
        ServicesUtil.validateParameterNotNull(source, "Parameter 'source' was null!");
        PersistenceObject persistenceObject = this.sourceTransformer.transformSource(source);
        AbstractItemModel model = createModelInstance(
                        createContextForExistingModel(persistenceObject
                                        .getTypeCode(), persistenceObject
                                        .getPK(), persistenceObject
                                        .getPersistenceVersion(), (AttributeProvider)new ItemAttributeProvider(persistenceObject, this)));
        AttributesToLoad toLoad = new AttributesToLoad(this);
        for(ModelAttributeInfo info : getInitialModelAttributes())
        {
            if(info.getAttributeInfo().isLocalized())
            {
                if(toLoad.getStdLocale() != null)
                {
                    toLoad.addLocalized(info.getQualifier(), toLoad.getStdLocale());
                }
                continue;
            }
            toLoad.addUnlocalizedAttribute(info.getQualifier());
        }
        if(!toLoad.isEmptyExceptPK())
        {
            fillModel(model, toLoad, persistenceObject);
        }
        return model;
    }


    public void addModelModificationListener(Object model, ModelModificationListener listener)
    {
        AbstractItemModel m = (AbstractItemModel)model;
        ItemModelContextImpl ctx = getItemModelContextImpl(m);
        if(!ctx.isNew())
        {
            ctx.getValueHistory().setListener((ModelValueHistory.HistoryListener)new MyHistoryListenerAdapter(m, this, listener));
        }
    }


    public void removeModelModificationListener(Object model, ModelModificationListener listener)
    {
        ItemModelContextImpl ctx = getItemModelContextImpl(model);
        if(!ctx.isNew())
        {
            ModelValueHistory valueHistory = ctx.getValueHistory();
            MyHistoryListenerAdapter l = (MyHistoryListenerAdapter)valueHistory.getListener();
            if(l != null && l.targetListener == listener)
            {
                valueHistory.setListener(null);
            }
        }
    }


    public void beforeAttach(Object model, ModelContext ctx)
    {
        if(model != null)
        {
            AbstractItemModel m = (AbstractItemModel)model;
            mergeNonDataLocales(m);
            getItemModelContextImpl(m).beforeAttach(getLocaleProvider());
        }
    }


    protected void mergeNonDataLocales(AbstractItemModel model)
    {
        ItemModelContextImpl ctx = getItemModelContextImpl(model);
        if(ctx.isDirty())
        {
            ModelValueHistory valueHistory = ctx.getValueHistory();
            Map<Locale, Set<String>> dirtyLocalesAndAttributes = valueHistory.getDirtyLocalizedAttributes();
            if(MapUtils.isNotEmpty(dirtyLocalesAndAttributes))
            {
                Map<Locale, Set<Locale>> dataLocale2LocaleMappings = this.localizedAttributesProcessor.getLocaleOrder(dirtyLocalesAndAttributes.keySet());
                if(hasNonDataLocales(dataLocale2LocaleMappings))
                {
                    Set<String> dirtyAttributesWithNonDataLocales = getDirtyLocalizedAttributesWithNonDataLocales(dirtyLocalesAndAttributes, dataLocale2LocaleMappings);
                    Map<String, LocMap<Locale, Object>> allCurrentValues = getCombinedLocalizedAttributesValues(model);
                    Map<String, LocMap<Locale, Object>> allNewValues = new HashMap<>((int)(allCurrentValues.size() / 0.75F) + 1);
                    for(Map.Entry<String, LocMap<Locale, Object>> localizedAttributeValuesEntry : allCurrentValues.entrySet())
                    {
                        String qualifier = localizedAttributeValuesEntry.getKey();
                        LocMap<Locale, Object> oldLocValues = localizedAttributeValuesEntry.getValue();
                        if(!dirtyAttributesWithNonDataLocales.contains(qualifier))
                        {
                            allNewValues.put(qualifier, oldLocValues);
                            continue;
                        }
                        allNewValues.put(qualifier,
                                        mergeOldValuesIntoDataLocales(dataLocale2LocaleMappings, qualifier, oldLocValues));
                    }
                    setCombinedLocalizedAttributesValues(model, allNewValues);
                    valueHistory.mergeDirty(dataLocale2LocaleMappings);
                }
            }
        }
    }


    public LocMap<Locale, Object> mergeOldValuesIntoDataLocales(Map<Locale, Set<Locale>> dataLocale2LocaleMappings, String qualifier, LocMap<Locale, Object> oldLocValues)
    {
        LocMap<Locale, Object> newLocValues = new LocMap();
        for(Map.Entry<Locale, Set<Locale>> e : dataLocale2LocaleMappings.entrySet())
        {
            Locale dataLocale = e.getKey();
            Set<Locale> nonDataLocales = e.getValue();
            if(nonDataLocales != null)
            {
                for(Locale nonDataLoc : nonDataLocales)
                {
                    if(oldLocValues.containsKey(nonDataLoc))
                    {
                        if(!newLocValues.containsKey(dataLocale))
                        {
                            newLocValues.put(dataLocale, oldLocValues.get(nonDataLoc));
                            continue;
                        }
                        LOG.warn("skipped attribute " + qualifier + " , field" + qualifier + "[" + nonDataLoc + "]=" + oldLocValues
                                        .get(nonDataLoc) + " since there is more specific value! (attach)");
                    }
                }
            }
            if(oldLocValues.containsKey(dataLocale))
            {
                if(!newLocValues.containsKey(dataLocale))
                {
                    newLocValues.put(dataLocale, oldLocValues.get(dataLocale));
                    continue;
                }
                LOG.warn("skipped attribute " + qualifier + " , field" + qualifier + "[" + dataLocale + "]=" + oldLocValues
                                .get(dataLocale) + " since there is more specific value! (attach)");
            }
        }
        return newLocValues;
    }


    protected Set<String> getDirtyLocalizedAttributesWithNonDataLocales(Map<Locale, Set<String>> dirtyLocalizedAttributes, Map<Locale, Set<Locale>> dataLocale2NonDataLocales)
    {
        Set<String> ret = null;
        for(Map.Entry<Locale, Set<String>> dirty : dirtyLocalizedAttributes.entrySet())
        {
            Locale l = dirty.getKey();
            if(!dataLocale2NonDataLocales.containsKey(l))
            {
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                ret.addAll(dirty.getValue());
            }
        }
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    protected boolean hasNonDataLocales(Map<Locale, Set<Locale>> dataLocale2LocaleMappings)
    {
        for(Map.Entry<Locale, Set<Locale>> entry : dataLocale2LocaleMappings.entrySet())
        {
            if(CollectionUtils.isNotEmpty(entry.getValue()))
            {
                return true;
            }
        }
        return false;
    }


    public void afterDetach(Object model, ModelContext ctx)
    {
    }


    public Set<String> getWritablePartOfAttributes(TypeService typeService)
    {
        if(this.writablePartOfAttributes == null)
        {
            synchronized(this)
            {
                if(this.writablePartOfAttributes == null)
                {
                    ImmutableSet.Builder<String> ret = null;
                    Collection<AttributeDescriptorModel> allDescriptors = typeService.getAttributeDescriptors(this.defaultType);
                    for(AttributeDescriptorModel adm : allDescriptors)
                    {
                        if(Boolean.TRUE.equals(adm.getPartOf()) && Boolean.TRUE.equals(adm.getWritable()))
                        {
                            if(ret == null)
                            {
                                ret = new ImmutableSet.Builder();
                            }
                            ret.add(adm.getQualifier());
                        }
                    }
                    this.writablePartOfAttributes = (ret == null) ? Collections.<String>emptySet() : (Set<String>)ret.build();
                }
            }
        }
        return this.writablePartOfAttributes;
    }


    public Set<String> getPartOfAttributes(TypeService typeService)
    {
        return typeService.getPartOfAttributes(this.defaultType);
    }


    public void reload(Object modelOriginal)
    {
        ServicesUtil.validateParameterNotNull(modelOriginal, "Parameter 'modelOriginal' was null!");
        PersistenceObject src = getPersistenceSource(modelOriginal);
        AbstractItemModel model = (AbstractItemModel)modelOriginal;
        src.refresh();
        fillModel(model, getAttributesToReload(model, null, false), src);
        ItemModelContextImpl ctx = getItemModelContextImpl(model);
        ctx.afterReload(src.getPersistenceVersion());
    }


    public void remove(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " can only handle source objects of type Item!!");
        }
        ItemModelContextImpl ctx = getItemModelContextImpl(model);
        Item item = (Item)getSource(model);
        if(item != null)
        {
            getPersistenceInterceptor().removeFromServiceLayer(item.getPK(), () -> {
                try
                {
                    item.remove();
                    ModelValueHistory h = ctx.getValueHistory();
                    if(h != null)
                    {
                        h.setListener(null);
                    }
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Removed " + getType(model) + " item");
                    }
                }
                catch(ItemLockedForProcessingException e)
                {
                    throw e;
                }
                catch(Exception e)
                {
                    throw new ModelRemovalException(e.getMessage(), e);
                }
            });
        }
    }


    public void save(Object model, Collection<String> excludedAttributes)
    {
        ModifiedAttributeValues modified;
        boolean createMode;
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " can only handle objects of type AbstractItemModel");
        }
        AbstractItemModel itemModel = (AbstractItemModel)model;
        ItemModelContextImpl ctx = getItemModelContextImpl(itemModel);
        PersistenceObject persistenceSource = ctx.getPersistenceSource();
        if(!ctx.isNew())
        {
            Item item = this.sourceTransformer.getItemFromPersistenceObject(persistenceSource);
            if(item == null)
            {
                throw new IllegalStateException("persistent item for model " + model + " does not exist any more");
            }
        }
        if(persistenceSource == null)
        {
            createMode = true;
            String type = getType(itemModel);
            ItemModelInternalContext ictx = (ItemModelInternalContext)ModelContextUtils.getItemModelContext(itemModel);
            modified = getModifiedAttributeValues(itemModel, true, getExcluded(excludedAttributes));
            PK newPK = (PK)modified.getModifiedValues().get(Item.PK);
            if(newPK == null)
            {
                newPK = ictx.getNewPK();
                if(newPK != null)
                {
                    modified.addValue(Item.PK, newPK);
                }
            }
            PersistenceObject createdObject = (PersistenceObject)getPersistenceInterceptor().createFromServiceLayer(() -> createNewItem(itemModel, modified));
            ctx.afterCreate(
                            getLocaleProvider(), (AttributeProvider)new ItemAttributeProvider(createdObject, this),
                            getDynamicAttributesProvider(), createdObject
                                            .getPK(), createdObject
                                            .getPersistenceVersion(), type);
        }
        else
        {
            createMode = false;
            Item item = this.sourceTransformer.getItemFromPersistenceObject(persistenceSource);
            modified = getModifiedAttributeValues(itemModel, false, getExcluded(excludedAttributes));
            getPersistenceInterceptor().updateFromServiceLayer(item.getPK(), () -> storeAttributes(item, modified));
            ctx.afterUpdate(item.getPersistenceVersion());
        }
        fillModel((AbstractItemModel)model, getAttributesToReload((AbstractItemModel)model, modified, createMode),
                        getPersistenceSource(model));
    }


    private ServiceLayerPersistenceInterceptor getPersistenceInterceptor()
    {
        return PolyglotPersistenceServiceLayerSupport.getServiceLayerPersistenceInterceptor();
    }


    public void afterModification(AbstractItemModel model, PK pk, long persistenceVersion)
    {
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).afterCreate(
                        getLocaleProvider(), (AttributeProvider)new ItemAttributeProvider(pk, this),
                        getDynamicAttributesProvider(), pk, persistenceVersion,
                        getType(model));
        unloadAttributes(model);
    }


    public void unloadAttributes(AbstractItemModel model)
    {
        ItemModelContextImpl ctx = (ItemModelContextImpl)ModelContextUtils.getItemModelContext(model);
        ImmutableSet immutableSet = ImmutableSet.copyOf(ctx.getDirtyLocalizedAttributes().keySet());
        for(ModelAttributeInfo info : getAllModelAttributes())
        {
            if(!info.getAttributeInfo().isLocalized())
            {
                clearAttribute(model, info.getQualifier());
                continue;
            }
            for(Locale locale : immutableSet)
            {
                clearLocAttribute(model, info.getQualifier(), locale);
            }
        }
    }


    protected Set<ModelAttributeInfo> getExcluded(Collection<String> excluded)
    {
        if(excluded == null || excluded.isEmpty())
        {
            return null;
        }
        Set<ModelAttributeInfo> ret = new HashSet<>(excluded.size());
        for(String q : excluded)
        {
            ret.add(getInfo(q));
        }
        return ret;
    }


    @Deprecated(since = "5.7.0", forRemoval = true)
    public Object getSource(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        ItemModelContext ctx = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
        if(ctx.isNew())
        {
            throw new IllegalStateException("model " + model + " has not been persisted");
        }
        Item i = (Item)ctx.getSource();
        if(i == null)
        {
            throw new IllegalStateException("persistent item does not exist any more for model " + model);
        }
        return i;
    }


    public PersistenceObject getPersistenceSource(Object model)
    {
        return getItemModelContextImpl(model).getPersistenceSource();
    }


    public boolean isModified(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        ItemModelContext ctx = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
        return (ctx.isNew() || ctx.isDirty());
    }


    public boolean isModified(Object model, String attribute)
    {
        return isModified(model, attribute, getStdLocale());
    }


    public boolean isModified(Object model, String attribute, Locale loc)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        if(PK.equalsIgnoreCase(attribute) || ITEMTYPE.equalsIgnoreCase(attribute))
        {
            return false;
        }
        ModelAttributeInfo info = getInfo(attribute);
        if(info != null)
        {
            ItemModelContext ctx = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
            if(ctx.isNew())
            {
                return true;
            }
            if(info.getAttributeInfo().isLocalized())
            {
                return ctx.isDirty(attribute, loc);
            }
            return ctx.isDirty(attribute);
        }
        throw new AttributeNotSupportedException("Cannot check modified for non-SL attribute '" + attribute + "'", attribute);
    }


    public boolean exists(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        return ModelContextUtils.getItemModelContext((AbstractItemModel)model).exists();
    }


    public boolean isRemoved(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        return ModelContextUtils.getItemModelContext((AbstractItemModel)model).isRemoved();
    }


    public boolean isNew(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        return ModelContextUtils.getItemModelContext((AbstractItemModel)model).isNew();
    }


    public boolean isUpToDate(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        return ModelContextUtils.getItemModelContext((AbstractItemModel)model).isUpToDate();
    }


    public Object getAttributeValue(Object model, String attributeQualifier)
    {
        return getLocalizedAttributeValue(model, attributeQualifier, null);
    }


    public Object getDirtyAttributeValue(Object model, String attributeQualifier)
    {
        if(PK.equalsIgnoreCase(attributeQualifier))
        {
            return ModelContextUtils.getItemModelContext((AbstractItemModel)model).getPK();
        }
        if(ITEMTYPE.equalsIgnoreCase(attributeQualifier))
        {
            return getTypeFromModelContext(model);
        }
        ModelAttributeInfo info = getInfo(attributeQualifier);
        if(info == null)
        {
            throw new AttributeNotSupportedException("cannot find attribute " + attributeQualifier, attributeQualifier);
        }
        return getFieldValue(model, info);
    }


    public Object getLocalizedAttributeValue(Object model, String attributeQualifier, Locale locale)
    {
        if(PK.equalsIgnoreCase(attributeQualifier))
        {
            return ModelContextUtils.getItemModelContext((AbstractItemModel)model).getPK();
        }
        if(ITEMTYPE.equalsIgnoreCase(attributeQualifier))
        {
            return getTypeFromModelContext(model);
        }
        ModelAttributeInfo info = getInfo(attributeQualifier);
        checkAttributeIsNotReadable(attributeQualifier, info);
        if(info.getAttributeInfo().isRuntime())
        {
            if(locale == null)
            {
                return ((AbstractItemModel)model).getProperty(attributeQualifier);
            }
            return ((AbstractItemModel)model).getProperty(attributeQualifier, locale);
        }
        if(locale == null)
        {
            return invokeMethod(attributeQualifier, getGetter(model, info), model);
        }
        return invokeMethod(attributeQualifier, getLocalizedGetter(model, info), model, new Object[] {locale});
    }


    public Object getDirtyLocalizedAttributeValue(Object model, String attributeQualifier, Locale locale)
    {
        if(locale == null)
        {
            throw new IllegalArgumentException("missing locale");
        }
        ModelAttributeInfo info = getInfo(attributeQualifier);
        checkAttributeIsNotReadable(attributeQualifier, info);
        Map<String, LocMap<Locale, Object>> allLocalizedAttributes = fetchAndVerifyCollectionLocFieldValue((AbstractItemModel)model,
                        Collections.singleton(info));
        LocMap<Locale, Object> locMap = allLocalizedAttributes.get(info.getQualifier());
        if(locMap != null && locMap.containsKey(locale))
        {
            return locMap.get(locale);
        }
        throw new IllegalStateException("localized attribute " + info + " doesn't value for locale " + locale + " - not dirty ?");
    }


    public void setAttributeValue(Object model, String attributeQualifier, Object value)
    {
        if(PK.equalsIgnoreCase(attributeQualifier))
        {
            throw new IllegalArgumentException("PK cannot be set manually");
        }
        if(ITEMTYPE.equalsIgnoreCase(attributeQualifier))
        {
            throw new IllegalArgumentException("itemtype cannot be set manually");
        }
        ModelAttributeInfo info = getInfo(attributeQualifier);
        TypeAttributeInfo attributeInfo = checkAttributeIsNotWritable(attributeQualifier, info);
        if(!isNew(model) && attributeInfo.isInitial() && !attributeInfo.isWritableFlag())
        {
            throw new AttributeNotSupportedException("attribute " + attributeQualifier + " is initial only", attributeQualifier, AttributeNotSupportedException.AttributeError.INITIAL_ONLY_ATTRIBUTE);
        }
        if(attributeInfo.isLocalized() && isCombinedLocalizedValuesMap(value))
        {
            Map<Object, Object> locValueMap = (Map<Object, Object>)value;
            for(Map.Entry<Object, Object> mapEntry : locValueMap.entrySet())
            {
                Locale loc;
                Object key = mapEntry.getKey();
                Object locValue = mapEntry.getValue();
                if(key instanceof Locale)
                {
                    loc = (Locale)key;
                }
                else
                {
                    loc = getCommonI18NService().getLocaleForLanguage((LanguageModel)key);
                }
                if(info.getAttributeInfo().isRuntime() && model instanceof ItemModel)
                {
                    ((ItemModel)model).setProperty(attributeInfo.getQualifier(), loc, locValue);
                    continue;
                }
                invokeMethod(attributeQualifier, getLocalizedSetter(model, info, locValue), model, new Object[] {locValue, loc});
            }
        }
        else if(value instanceof PK && model instanceof ItemModel && isForceInsertPk())
        {
            try
            {
                Field primaryKey = ItemModel.class.getDeclaredField("_pk");
                primaryKey.setAccessible(true);
                primaryKey.set(model, value);
                Method privateMethod = AbstractItemModel.class.getDeclaredMethod("markDirty", new Class[] {String.class});
                privateMethod.setAccessible(true);
                privateMethod.invoke(model, new Object[] {"pk"});
            }
            catch(Exception e)
            {
                throw new ModelCreationException("Unable to set primary key on entity " + model.getClass(), e);
            }
        }
        else if(attributeInfo.isRuntime() && model instanceof ItemModel)
        {
            ((ItemModel)model).setProperty(attributeInfo.getQualifier(), value);
        }
        else
        {
            Method setter = null;
            try
            {
                setter = getSetter(model, info, value);
                invokeMethod(attributeQualifier, setter, model, new Object[] {value});
            }
            catch(IllegalArgumentException iae)
            {
                if(shouldResetPrimitiveToDefault(value, setter))
                {
                    try
                    {
                        invokeMethod(attributeQualifier, setter, model, new Object[] {PrimitiveDefaults.getDefaultValue(setter.getParameterTypes()[0])});
                    }
                    catch(IllegalArgumentException iae2)
                    {
                        throw new AttributeNotSupportedException(iae2.getMessage(), iae2, attributeQualifier);
                    }
                }
                else
                {
                    throw new AttributeNotSupportedException(iae.getMessage(), iae, attributeQualifier);
                }
            }
        }
    }


    private TypeAttributeInfo checkAttributeIsNotReadable(String attributeQualifier, ModelAttributeInfo info)
    {
        AttributeNotSupportedException.AttributeError attributeError = AttributeNotSupportedException.AttributeError.NOT_READABLE_ATTRIBUTE;
        TypeAttributeInfo attributeInfo = checkAttributeIsNotNullOrPrivate(attributeQualifier, info, attributeError);
        if(!attributeInfo.isReadable())
        {
            throw new AttributeNotSupportedException("attribute " + attributeQualifier + " is not readable", attributeQualifier, attributeError);
        }
        return attributeInfo;
    }


    private TypeAttributeInfo checkAttributeIsNotWritable(String attributeQualifier, ModelAttributeInfo info)
    {
        AttributeNotSupportedException.AttributeError attributeError = AttributeNotSupportedException.AttributeError.NOT_WRITABLE_ATTRIBUTE;
        TypeAttributeInfo attributeInfo = checkAttributeIsNotNullOrPrivate(attributeQualifier, info, attributeError);
        if(!attributeInfo.isWritable())
        {
            throw new AttributeNotSupportedException("attribute " + attributeQualifier + " is not writable", attributeQualifier, attributeError);
        }
        return attributeInfo;
    }


    private TypeAttributeInfo checkAttributeIsNotNullOrPrivate(String attributeQualifier, ModelAttributeInfo info, AttributeNotSupportedException.AttributeError attributeError)
    {
        if(info == null)
        {
            throw new AttributeNotSupportedException("cannot find attribute " + attributeQualifier, attributeQualifier);
        }
        TypeAttributeInfo attributeInfo = info.getAttributeInfo();
        if(attributeInfo.isPrivate())
        {
            throw new AttributeNotSupportedException("attribute " + attributeQualifier + " is private", attributeQualifier, attributeError);
        }
        return attributeInfo;
    }


    private boolean shouldResetPrimitiveToDefault(Object value, Method setter)
    {
        return (value == null && setter != null && (setter.getParameterTypes()).length > 0 &&
                        PrimitiveDefaults.isPrimitive(setter.getParameterTypes()[0]));
    }


    private boolean isCombinedLocalizedValuesMap(Object value)
    {
        if(!(value instanceof Map) || ((Map)value).isEmpty())
        {
            return false;
        }
        Map<Object, Object> map = (Map<Object, Object>)value;
        for(Map.Entry<Object, Object> e : map.entrySet())
        {
            Object key = e.getKey();
            if(!(key instanceof Locale) && !(key instanceof LanguageModel))
            {
                return false;
            }
        }
        return true;
    }


    private Object invokeMethod(String attributeQualifier, Method method, Object object)
    {
        return invokeMethod(attributeQualifier, method, object, new Object[0]);
    }


    private Object invokeMethod(String attributeQualifier, Method method, Object object, Object... args)
    {
        checkMethodExist(attributeQualifier, method);
        return ReflectionUtils.invokeMethod(method, object, args);
    }


    private void checkMethodExist(String attributeQualifier, Method method)
    {
        if(method == null)
        {
            throw new AttributeNotSupportedException("Attribute qualifier '" + attributeQualifier + "' does not have correspoinding setter/getter", attributeQualifier);
        }
    }


    public Class<? extends AbstractItemModel> getModelClass()
    {
        return this.modelClass;
    }


    public String getDefaultType()
    {
        return this.defaultType;
    }


    public String getType(Object model)
    {
        String explicitType = getTypeFromModelContext(model);
        return (explicitType == null) ? getDefaultType() : explicitType;
    }


    protected String getTypeFromModelContext(Object model)
    {
        return ModelContextUtils.getItemModelContext((AbstractItemModel)model).getItemType();
    }


    protected ComposedType getComposedType(AbstractItemModel model)
    {
        try
        {
            return TypeManager.getInstance().getComposedType(getType(model));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("unknown type " + getType(model) + " for model " + model);
        }
    }


    protected Object readSingleAttribute(Locale dataLoc, PersistenceObject item, String qualifier)
    {
        ReadParams params;
        ReadParams.Builder paramsBuilder = ReadParams.builderForSingleQualifier(qualifier, getI18nService()).useModelConverter((ModelConverter)this);
        if(dataLoc != null)
        {
            params = paramsBuilder.localized(dataLoc).build();
        }
        else
        {
            params = paramsBuilder.build();
        }
        Object rawValue = item.readRawValue(params);
        return convertObjectsToModels(item, qualifier, rawValue);
    }


    public boolean jaloOnlyAttribute(String qualifier)
    {
        ModelAttributeInfo attrInfo = getInfo(qualifier);
        return attrInfo.getAttributeInfo().isJaloOnly();
    }


    protected Collection<TypeAttributeInfo> calculateTypeAttributeInfos(ComposedType ct)
    {
        List<TypeAttributeInfo> result = new LinkedList<>();
        if(ct != null)
        {
            for(AttributeDescriptor desc : ct.getAttributeDescriptorsIncludingPrivate())
            {
                TypeAttributeInfo info = calculateInfo(desc);
                if(info != null)
                {
                    result.add(info);
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("model converter calculated type settings " + ct.getCode() + " : " + result);
            }
        }
        return result;
    }


    private TypeAttributeInfo calculateInfo(AttributeDescriptor desc)
    {
        TypeAttributeInfo info = null;
        try
        {
            if(!"ExtensibleItem".equalsIgnoreCase(desc.getDeclaringEnclosingType().getCode()))
            {
                if(desc instanceof RelationDescriptor)
                {
                    RelationTypeAttributeInfo relationTypeAttributeInfo = new RelationTypeAttributeInfo(desc.getQualifier(), desc.getEnclosingType().getCode());
                    adjustRelationInfo((RelationDescriptor)desc, relationTypeAttributeInfo);
                }
                else
                {
                    info = new TypeAttributeInfo(desc.getQualifier(), desc.getEnclosingType().getCode());
                }
                info.setNonLazyLoading(isNonLazyLoadedAttribute(desc));
                info.setReadable(desc.isReadable());
                info.setPartOf(desc.isPartOf());
                info.setWritable((desc.isWritable() || desc.isInitial()));
                info.setWritableFlag(desc.isWritable());
                info.setLocalized(desc.isLocalized());
                info.setReference(isReferenceAttribute(desc));
                info.setUnique(desc.isUnique());
                info.setAttributeHandler(desc.getAttributeHandler());
                info.setOptional(desc.isOptional());
                info.setRedeclared(desc.isRedeclared());
                info.setRequiredForCreation((!desc.isOptional() || (!desc.isWritable() && desc.isInitial())));
                info.setInitial(desc.isInitial());
                info.setMandatory((!info.isOptional() && info.isWritable()));
                info.setMandatoryForCreation((!info.isOptional() && (info.isWritable() || desc.isInitial())));
                info.setJaloOnly((desc.getPersistenceType() == null));
                info.setExtensionName(desc.getExtensionName());
                info.setEncrypted(desc.isEncrypted());
                info.setPrivate(desc.isPrivate());
                info.setRuntime(desc.isRuntime().booleanValue());
                info.setAttributeType(desc.getAttributeType());
            }
        }
        catch(YNoSuchEntityException | de.hybris.platform.jalo.JaloObjectNoLongerValidException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Ignoring attribute, no longer valid", e);
            }
            return null;
        }
        return info;
    }


    private void adjustRelationInfo(RelationDescriptor desc, RelationTypeAttributeInfo info)
    {
        if(desc.isRedeclared() && desc.getRelationType().isOneToMany())
        {
            fillRedeclaredRelationEnds(desc, info);
        }
        info.setRelationType(desc.getRelationType());
        info.setRelationName(desc.getRelationName());
        info.setSource(desc.isSource());
    }


    private void fillRedeclaredRelationEnds(RelationDescriptor desc, RelationTypeAttributeInfo info)
    {
        ComposedType concreteManyType, concreteOneType;
        if(desc.getAttributeType() instanceof CollectionType)
        {
            concreteManyType = (ComposedType)((CollectionType)desc.getAttributeType()).getElementType();
            RelationDescriptor otherSideAttrDescriptor = getOtherSideAttrDescriptor(desc);
            String referenceToOneSide = otherSideAttrDescriptor.getQualifier();
            concreteOneType = (ComposedType)concreteManyType.getAttributeDescriptor(referenceToOneSide).getAttributeType();
        }
        else
        {
            concreteOneType = desc.getRelationType().getSourceType();
            RelationDescriptor otherSideAttrDescriptor = getOtherSideAttrDescriptor(desc);
            String referenceToManySide = otherSideAttrDescriptor.getQualifier();
            concreteManyType = (ComposedType)((CollectionType)concreteOneType.getAttributeDescriptor(referenceToManySide).getAttributeType()).getElementType();
        }
        RelationType relationType = desc.getRelationType();
        boolean isTarget = !desc.isSource();
        boolean isTargetTypeMany = !relationType.isTargetTypeOne();
        if((desc.isSource() && relationType.isSourceTypeOne()) || (isTarget && isTargetTypeMany))
        {
            info.setConcreteSourceSideType(concreteOneType);
            info.setConcreteTargetSideType(concreteManyType);
        }
        else
        {
            info.setConcreteSourceSideType(concreteManyType);
            info.setConcreteTargetSideType(concreteOneType);
        }
    }


    private RelationDescriptor getOtherSideAttrDescriptor(RelationDescriptor desc)
    {
        if(desc.isSource())
        {
            return desc.getRelationType().getTargetAttributeDescriptor();
        }
        return desc.getRelationType().getSourceAttributeDescriptor();
    }


    protected boolean isReferenceAttribute(AttributeDescriptor desc)
    {
        Type type = desc.getAttributeType();
        boolean result = mustConvert(type);
        if(result && type instanceof MapType)
        {
            ComposedType languageType = TypeManager.getInstance().getComposedType(Language.class);
            MapType mapType = (MapType)type;
            result = ((mustConvert(mapType.getArgumentType()) && !languageType.isAssignableFrom(mapType.getArgumentType())) || mustConvert(mapType.getReturnType()));
        }
        return result;
    }


    protected TypeMetaInfo assembleMetaInfo(ComposedType ct, Collection<TypeAttributeInfo> attributes)
    {
        CaseInsensitiveMap<String, ModelAttributeInfo> caseInsensitiveMap = new CaseInsensitiveMap((int)(attributes.size() / 0.75F) + 1);
        CaseInsensitiveMap<String, DynamicAttributeHandler> caseInsensitiveMap1 = new CaseInsensitiveMap();
        List<ModelAttributeInfo> all = new ArrayList<>(attributes.size());
        List<ModelAttributeInfo> initial = new ArrayList<>(attributes.size());
        for(TypeAttributeInfo info : attributes)
        {
            if(PK.equalsIgnoreCase(info.getQualifier()) || ITEMTYPE.equalsIgnoreCase(info.getQualifier()))
            {
                continue;
            }
            if(info.isDynamic())
            {
                try
                {
                    DynamicAttributeHandler attributeHandler = (DynamicAttributeHandler)Registry.getApplicationContext().getBean(info.getAttributeHandler(), DynamicAttributeHandler.class);
                    caseInsensitiveMap1.put(info.getQualifier(), attributeHandler);
                    ModelAttributeInfo modelInfo = new ModelAttributeInfo(info.getQualifier());
                    modelInfo.setAttributeInfo(info);
                    caseInsensitiveMap.put(modelInfo.getQualifier(), modelInfo);
                }
                catch(NoSuchBeanDefinitionException nsde)
                {
                    StringBuilder helpfulInfo = new StringBuilder();
                    helpfulInfo.append("cannot find spring bean [");
                    helpfulInfo.append(info.getAttributeHandler());
                    helpfulInfo.append("] configured for dynamic attribute [");
                    helpfulInfo.append(info.getEnclosingTypeQualifier()).append(".").append(info.getQualifier()).append("]");
                    String extensionName = info.getExtensionName();
                    if(extensionName != null)
                    {
                        helpfulInfo.append(" from extension [").append(extensionName).append("]");
                    }
                    throw new SystemException(helpfulInfo.toString(), nsde);
                }
                continue;
            }
            if(ModelUtils.existsField(this.modelClass, info.getQualifier().toUpperCase(LocaleHelper.getPersistenceLocale())) || info.isRuntime())
            {
                ModelAttributeInfo modelInfo = createModelAttributeInfo(info);
                all.add(modelInfo);
                caseInsensitiveMap.put(modelInfo.getQualifier(), modelInfo);
                if(info.isPreFetched() && info.isReadable())
                {
                    initial.add(modelInfo);
                }
                continue;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Skipping attribute " + info.getQualifier() + " for model " + this.modelClass.getName());
            }
        }
        return new TypeMetaInfo(
                        Collections.unmodifiableMap((Map<? extends String, ? extends ModelAttributeInfo>)caseInsensitiveMap),
                        Collections.unmodifiableList(all),
                        Collections.unmodifiableList(initial), (DynamicAttributesProvider)new DefaultDynamicAttributesProvider(
                        Collections.unmodifiableMap((Map<? extends String, ? extends DynamicAttributeHandler>)caseInsensitiveMap1)),
                        isConfiguredForLegacyPersistence(ct));
    }


    private boolean isConfiguredForLegacyPersistence(ComposedType composedType)
    {
        String extensionName = composedType.getExtensionName();
        boolean forcePerExtensionLegacyPers = Config.getBoolean("persistence." + extensionName + ".legacy.mode", false);
        return (forcePerExtensionLegacyPers || Boolean.TRUE.equals(composedType.getProperty("legacyPersistence")));
    }


    protected ModelAttributeInfo createModelAttributeInfo(TypeAttributeInfo info)
    {
        ModelAttributeInfo modelInfo = new ModelAttributeInfo(info.getQualifier());
        if(!info.isLocalized() && !info.isRuntime())
        {
            modelInfo.setPrimitive(ModelUtils.isPrimitiveField(this.modelClass, modelInfo.getFieldQualifier()));
            modelInfo.setFieldType(ModelUtils.getFieldType(this.modelClass, modelInfo.getFieldQualifier()));
        }
        modelInfo.setAttributeInfo(info);
        return modelInfo;
    }


    protected LoadedAttributeValues readAttributes(Locale dataLoc, PersistenceObject item, Set<String> qualifiers)
    {
        ReadParams params;
        if(qualifiers.isEmpty())
        {
            return new LoadedAttributeValues(1);
        }
        LoadedAttributeValues ret = new LoadedAttributeValues(qualifiers.size());
        ReadParams.Builder paramsBuilder = ReadParams.builderForMultipleQualifiers(qualifiers, getI18nService()).useModelConverter((ModelConverter)this);
        if(dataLoc != null)
        {
            params = paramsBuilder.localized(dataLoc).build();
        }
        else
        {
            params = paramsBuilder.build();
        }
        Map<String, Object> rawValues = item.readRawValues(params);
        for(Map.Entry<String, Object> entry : rawValues.entrySet())
        {
            String qualifier = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Exception)
            {
                ret.addError(qualifier, (Exception)value);
                continue;
            }
            try
            {
                ret.put(qualifier, convertObjectsToModels(item, qualifier, value));
            }
            catch(ModelForItemAlreadyBeingLoadedException modelForItemAlreadyBeingLoadedException)
            {
            }
        }
        return ret;
    }


    protected void storeAttributes(Item item, ModifiedAttributeValues values)
    {
        JaloSession currentSession = JaloSession.getCurrentSession();
        currentSession.createLocalSessionContext();
        try
        {
            currentSession.setAttribute("save.from.service.layer", Boolean.TRUE);
            item.setAllAttributes(values.getModifiedValues());
            for(Map.Entry<Locale, Map<String, Object>> e : (Iterable<Map.Entry<Locale, Map<String, Object>>>)values.getAdditionalLocValues().entrySet())
            {
                getI18nService().setCurrentLocale(e.getKey());
                item.setAllAttributes(e.getValue());
            }
        }
        catch(ItemLockedForProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new ModelSavingException(e.getMessage(), e);
        }
        finally
        {
            currentSession.removeLocalSessionContext();
        }
    }


    protected boolean isNonLazyLoadedAttribute(AttributeDescriptor desc)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("prefetch mode " + this.ATTRIBUTE_PREFETCH_MODE + " for " + desc.getEnclosingType().getCode());
        }
        return this.ATTRIBUTE_PREFETCH_MODE.isPrefetched(desc);
    }


    protected boolean mustConvert(Type type)
    {
        boolean ret = false;
        if(type instanceof ComposedType)
        {
            ret = true;
        }
        else if(type instanceof AtomicType)
        {
            ret = false;
        }
        else if(type instanceof CollectionType)
        {
            ret = mustConvert(((CollectionType)type).getElementType());
        }
        else if(type instanceof MapType)
        {
            ret = (mustConvert(((MapType)type).getArgumentType()) || mustConvert(((MapType)type).getReturnType()));
        }
        return ret;
    }


    private ModifiedAttributeValues getModifiedAttributeValues(AbstractItemModel model, boolean create, Set<ModelAttributeInfo> excluded)
    {
        ItemModelContext ctx = ModelContextUtils.getItemModelContext(model);
        ModifiedAttributeValues ret = new ModifiedAttributeValues(this, model, ((ItemModelContextImpl)ctx).getValueHistory(), create);
        List<ModelAttributeInfo> localizedInfos = null;
        for(ModelAttributeInfo info : getAllModelAttributes())
        {
            if((excluded == null || !excluded.contains(info)) && (ctx
                            .isDirty(info.getAttributeInfo().getQualifier()) || info.getAttributeInfo().isWritable()))
            {
                if(isAttributeLocalized(info, model))
                {
                    if(localizedInfos == null)
                    {
                        localizedInfos = new ArrayList<>();
                    }
                    localizedInfos.add(info);
                    continue;
                }
                verifyCollectionFieldValue(model, info);
                ret.addValue(info.getQualifier(), getFieldValue(model, info));
            }
        }
        if(localizedInfos != null)
        {
            ret.addAllLocValues(localizedInfos, fetchAndVerifyCollectionLocFieldValue(model, localizedInfos));
        }
        return ret;
    }


    private Map<String, LocMap<Locale, Object>> fetchAndVerifyCollectionLocFieldValue(AbstractItemModel model, Collection<ModelAttributeInfo> localizedAttrInofs)
    {
        Map<String, LocMap<Locale, Object>> fvaluemap = getCombinedLocalizedAttributesValues(model);
        for(ModelAttributeInfo info : localizedAttrInofs)
        {
            if(fvaluemap.containsKey(info.getQualifier()))
            {
                LocMap<Locale, Object> singleLocAttrValues = fvaluemap.get(info.getQualifier());
                for(Object value : singleLocAttrValues.values())
                {
                    if(value instanceof Collection && ((Collection)value).contains(null))
                    {
                        throw new ModelSavingException("Attribute '" + info.getQualifier() + "' for '" + model + "' is a collection and contains at least one NULL value.");
                    }
                }
            }
        }
        return fvaluemap;
    }


    private void verifyCollectionFieldValue(AbstractItemModel model, ModelAttributeInfo info)
    {
        Object fvalue = getFieldValue(model, info);
        if(fvalue instanceof Collection && ((Collection)fvalue).stream().anyMatch(Objects::isNull))
        {
            throw new ModelSavingException("Attribute '" + info.getQualifier() + "' for '" + model + "' is a collection and contains at least one NULL value.");
        }
    }


    private boolean isAttributeLocalized(ModelAttributeInfo info, AbstractItemModel model)
    {
        return (info.getAttributeInfo().isLocalized() || (model instanceof AttributeDescriptorModel && Boolean.TRUE
                        .equals(((AttributeDescriptorModel)model).getLocalized()) && info
                        .getQualifier().equalsIgnoreCase("defaultValue")));
    }


    protected Locale getStdLocale()
    {
        if(JaloSession.getCurrentSession().getSessionContext() == null ||
                        JaloSession.getCurrentSession().getSessionContext().getLanguage() == null)
        {
            return null;
        }
        return this.i18nService.getCurrentLocale();
    }


    protected PersistenceObject createNewItem(AbstractItemModel model, ModifiedAttributeValues values)
    {
        JaloSession currentSession = JaloSession.getCurrentSession();
        currentSession.createLocalSessionContext();
        try
        {
            currentSession.setAttribute("save.from.service.layer", Boolean.TRUE);
            Item ret = getComposedType(model).newInstance(values.getModifiedValues());
            for(Map.Entry<Locale, Map<String, Object>> e : (Iterable<Map.Entry<Locale, Map<String, Object>>>)values.getAdditionalLocValues().entrySet())
            {
                getI18nService().setCurrentLocale(e.getKey());
                ret.setAllAttributes(e.getValue());
            }
            return this.sourceTransformer.transformSource(ret);
        }
        catch(Exception e)
        {
            throw new ModelSavingException(e.getMessage(), e);
        }
        finally
        {
            currentSession.removeLocalSessionContext();
        }
    }


    public String toString()
    {
        return getClass().getSimpleName() + "[" + getClass().getSimpleName() + "<->" + getModelClass() + "]";
    }


    protected void fillModel(AbstractItemModel model, AttributesToLoad attributesToLoad, PersistenceObject item)
    {
        Locale stdLoc = attributesToLoad.getStdLocale();
        for(String attribute : attributesToLoad.getStdAttributesToClear())
        {
            ModelAttributeInfo info = getInfo(attribute);
            if(info == null)
            {
                throw new AttributeNotSupportedException("cannot find attribute " + attribute, attribute);
            }
            if(info.getAttributeInfo().isLocalized())
            {
                clearLocAttribute(model, info.getQualifier(), stdLoc);
                continue;
            }
            clearAttribute(model, info.getQualifier());
        }
        for(Map.Entry<Locale, Set<String>> e : (Iterable<Map.Entry<Locale, Set<String>>>)attributesToLoad.getAdditionalLocAttributesToClear().entrySet())
        {
            Locale loc = e.getKey();
            Set<String> additionAttributes = e.getValue();
            for(String attribute : additionAttributes)
            {
                clearLocAttribute(model, attribute, loc);
            }
        }
        LoadedAttributeValues loadedAttributes = readAttributes(stdLoc, item, attributesToLoad.getStdAttributes());
        Map<String, LocMap<Locale, Object>> allLocalizedAttrsMap = null;
        for(String attribute : attributesToLoad.getStdAttributes())
        {
            if(!loadedAttributes.containsKey(attribute))
            {
                continue;
            }
            Object loadedAttributeValue = loadedAttributes.get(attribute);
            ModelAttributeInfo info = getInfo(attribute);
            if(info == null)
            {
                throw new AttributeNotSupportedException("cannot find attribute " + attribute, attribute);
            }
            if(loadedAttributeValue == null && info.isPrimitive())
            {
                loadedAttributeValue = ConvertUtils.convert((String)null, info.getFieldType());
            }
            if(info.getAttributeInfo().isLocalized())
            {
                if(allLocalizedAttrsMap == null)
                {
                    allLocalizedAttrsMap = getCombinedLocalizedAttributesValues(model);
                }
                preLoadLocAttribute(allLocalizedAttrsMap, model, info.getQualifier(), stdLoc, loadedAttributeValue);
                if(loadedAttributes.hadError(attribute))
                {
                    setLocAttributeLoadError(model, info.getQualifier(), stdLoc, new ModelLoadingException("error loading '" + attribute + "'[" + stdLoc + "] of " + item
                                    .getPK(), loadedAttributes
                                    .getError(attribute)));
                }
                continue;
            }
            preLoadAttribute(model, info.getQualifier(), loadedAttributeValue);
            if(loadedAttributes.hadError(attribute))
            {
                setAttributeLoadError(model, info.getQualifier(), new ModelLoadingException("error loading '" + attribute + "' of " + item
                                .getPK(), loadedAttributes.getError(attribute)));
            }
        }
        if(attributesToLoad.hasExtraLocalizations())
        {
            for(Map.Entry<Locale, Set<String>> e : (Iterable<Map.Entry<Locale, Set<String>>>)attributesToLoad.getAdditionalLocAttributes().entrySet())
            {
                Locale loc = e.getKey();
                Set<String> additionAttributes = e.getValue();
                loadedAttributes = readAttributes(loc, item, additionAttributes);
                for(String attribute : additionAttributes)
                {
                    Object loadedAttributeValue = loadedAttributes.get(attribute);
                    ModelAttributeInfo info = getInfo(attribute);
                    if(loadedAttributeValue == null && info.isPrimitive())
                    {
                        loadedAttributeValue = ConvertUtils.convert((String)null, info.getFieldType());
                    }
                    if(info.getAttributeInfo().isLocalized())
                    {
                        if(allLocalizedAttrsMap == null)
                        {
                            allLocalizedAttrsMap = getCombinedLocalizedAttributesValues(model);
                        }
                        preLoadLocAttribute(allLocalizedAttrsMap, model, info.getQualifier(), loc, loadedAttributeValue);
                        if(loadedAttributes.hadError(attribute))
                        {
                            setLocAttributeLoadError(model, info.getQualifier(), loc, new ModelLoadingException("error loading '" + attribute + "'[" + loc + "] of " + item
                                            .getPK(), loadedAttributes
                                            .getError(attribute)));
                        }
                        continue;
                    }
                    preLoadAttribute(model, info.getQualifier(), loadedAttributeValue);
                    if(loadedAttributes.hadError(attribute))
                    {
                        setAttributeLoadError(model, info.getQualifier(), new ModelLoadingException("error loading '" + attribute + "' of " + item
                                        .getPK(), loadedAttributes
                                        .getError(attribute)));
                    }
                }
            }
        }
    }


    protected void setLocAttributeLoadError(AbstractItemModel model, String qualifier, Locale loc, ModelLoadingException error)
    {
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().setLocAttributeLoadingError(qualifier, loc, error);
    }


    protected void setAttributeLoadError(AbstractItemModel model, String qualifier, ModelLoadingException error)
    {
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().setAttributeLoadingError(qualifier, error);
    }


    private synchronized LocMap<Locale, Object> ensureLocAttributeMap(Map<String, LocMap<Locale, Object>> locMap, String qualifier)
    {
        LocMap<Locale, Object> lazyLocMap = locMap.get(qualifier);
        if(lazyLocMap == null)
        {
            locMap.put(qualifier, lazyLocMap = new LocMap());
        }
        return lazyLocMap;
    }


    protected void preLoadLocAttribute(Map<String, LocMap<Locale, Object>> localizedAttributesFieldValue, AbstractItemModel model, String qualifier, Locale loc, Object value)
    {
        ensureLocAttributeMap(localizedAttributesFieldValue, qualifier).put(loc, value);
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().loadOriginalValue(qualifier, loc, value);
    }


    protected void clearLocAttribute(AbstractItemModel model, String qualifier, Locale loc)
    {
        Map<Locale, Object> locMap = (Map<Locale, Object>)getCombinedLocalizedAttributesValues(model).get(qualifier);
        if(locMap != null)
        {
            locMap.remove(loc);
        }
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().clearOriginalValue(qualifier, loc);
    }


    protected void preLoadAttribute(AbstractItemModel model, String qualifier, Object value)
    {
        setFieldValue(model, getInfo(qualifier), value);
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().loadOriginalValue(qualifier, value);
    }


    protected void clearAttribute(AbstractItemModel model, String qualifier)
    {
        ModelAttributeInfo info = getInfo(qualifier);
        setFieldValue(model, info, null);
        ((ItemModelContextImpl)ModelContextUtils.getItemModelContext(model)).getValueHistory().clearOriginalValue(qualifier);
    }


    protected Object convertModelsToObjects(AbstractItemModel model, String qualifier, Object value)
    {
        Object ret;
        if(value == null)
        {
            ret = null;
        }
        else if("itemtype".equalsIgnoreCase(qualifier))
        {
            ret = TypeManager.getInstance().getComposedType((String)value);
        }
        else
        {
            ret = getModelService().toPersistenceLayer(value);
        }
        return ret;
    }


    protected Object convertObjectsToModels(PersistenceObject item, String qualifier, Object value)
    {
        Object ret;
        if(value == null)
        {
            ret = null;
        }
        else if("itemtype".equalsIgnoreCase(qualifier))
        {
            ret = ((ComposedType)value).getCode();
        }
        else if(item.isEnumerationType() && "values".equalsIgnoreCase(qualifier))
        {
            Collection src = (Collection)value;
            ret = src.isEmpty() ? src : getModelService().getAll(src, new LinkedHashSet(src.size()), "EnumerationValue");
        }
        else if("modifiedItem".equalsIgnoreCase(qualifier) && isEnumValue(value))
        {
            ret = getModelService().get(value, "EnumerationValue");
        }
        else if(value instanceof ItemPropertyValueCollection)
        {
            ItemPropertyValueCollection itemProperties = (ItemPropertyValueCollection)value;
            int size = itemProperties.size();
            Collection<Object> resultCollection = itemProperties.createNewWrappedCollection();
            BitSet nonValidIndexies = null;
            for(int i = 0; i < size; i++)
            {
                ItemPropertyValue propValue = (ItemPropertyValue)itemProperties.get(i);
                PersistenceObject persistenceObject = this.sourceTransformer.transformSourceOrReturnNull(propValue);
                if(persistenceObject != null)
                {
                    Object model = getModelService().toModelLayer(persistenceObject);
                    if(model != null)
                    {
                        resultCollection.add(model);
                    }
                }
                else
                {
                    if(nonValidIndexies == null)
                    {
                        nonValidIndexies = new BitSet();
                    }
                    nonValidIndexies.set(i);
                }
            }
            if(nonValidIndexies != null && !nonValidIndexies.isEmpty())
            {
                this.selfHealingService.addItemToHeal(new ItemToHeal(item.getPK(), item.getTypeCode(), qualifier, item
                                .getPersistenceVersion(), itemProperties
                                .cloneWithoutInvalid(nonValidIndexies)));
            }
            ret = resultCollection;
        }
        else if(value instanceof ItemPropertyValue)
        {
            PersistenceObject persistenceObject = this.sourceTransformer.transformSourceOrReturnNull(value);
            ret = getModelService().toModelLayer(persistenceObject);
            if(ret == null)
            {
                this.selfHealingService
                                .addItemToHeal(new ItemToHeal(item
                                                .getPK(), item.getTypeCode(), qualifier, item.getPersistenceVersion(), null));
            }
        }
        else
        {
            ret = isCorrectRuntimeAttributeAtomicType(qualifier, value) ? getModelService().toModelLayer(value) : null;
        }
        return ret;
    }


    private boolean isCorrectRuntimeAttributeAtomicType(String qualifier, Object value)
    {
        if(Config.getBoolean("should.check.runtime.attributes.type.match", true) && getInfo(qualifier) != null &&
                        getInfo(qualifier).getAttributeInfo() != null && getInfo(qualifier).getAttributeInfo().isRuntime())
        {
            Type attributeType = getInfo(qualifier).getAttributeInfo().getAttributeType();
            if(attributeType instanceof AtomicType)
            {
                AtomicType atomicType = (AtomicType)attributeType;
                if(!atomicType.getJavaClass().isAssignableFrom(value.getClass()))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Can't read parameter " + qualifier + " " + atomicType.getJavaClass() + " is not assignable from " + value
                                        .getClass());
                    }
                    return false;
                }
            }
        }
        return true;
    }


    protected boolean isEnumValue(Object value)
    {
        if(value instanceof de.hybris.platform.jalo.enumeration.EnumerationValue)
        {
            return true;
        }
        if(value instanceof ItemPropertyValue)
        {
            return (((ItemPropertyValue)value).getPK().getTypeCode() == 91);
        }
        if(value instanceof PK)
        {
            return (((PK)value).getTypeCode() == 91);
        }
        return false;
    }


    protected AttributesToLoad getAttributesToReload(AbstractItemModel model, ModifiedAttributeValues savedValues, boolean forCreation)
    {
        AttributesToLoad ret = new AttributesToLoad(this);
        if(forCreation)
        {
            ret.addUnlocalizedAttribute("creationtime");
        }
        ret.addUnlocalizedAttribute("modifiedtime");
        for(ModelAttributeInfo info : getAllModelAttributes())
        {
            if(info.getAttributeInfo().isPreFetched())
            {
                if(info.getAttributeInfo().isLocalized())
                {
                    if(ret.getStdLocale() != null)
                    {
                        ret.addLocalized(info.getQualifier(), ret.getStdLocale());
                    }
                    continue;
                }
                ret.addUnlocalizedAttribute(info.getQualifier());
            }
        }
        ItemModelContext ctx = ModelContextUtils.getItemModelContext(model);
        ModelValueHistory valueHistory = ((ItemModelContextImpl)ctx).getValueHistory();
        for(String q : valueHistory.getLoadedAttributes())
        {
            if(!ctx.isDirty(q))
            {
                ret.clearUnlocalizedAttribute(q);
            }
        }
        for(Key<Locale, String> localizedKey : (Iterable<Key<Locale, String>>)valueHistory.getLoadedLocAttributes())
        {
            Locale locale = (Locale)localizedKey.getKey();
            String qualifier = (String)localizedKey.getValue();
            if(!ctx.isDirty(qualifier, locale))
            {
                ret.clearLocalized(qualifier, locale);
            }
        }
        for(String q : valueHistory.getDirtyAttributes())
        {
            if(savedValues == null || savedValues.contains(q))
            {
                ret.clearUnlocalizedAttribute(q);
            }
        }
        for(Map.Entry<Locale, Set<String>> e : (Iterable<Map.Entry<Locale, Set<String>>>)valueHistory.getDirtyLocalizedAttributes().entrySet())
        {
            Locale loc2 = e.getKey();
            for(String quali : e.getValue())
            {
                if(savedValues == null || savedValues.containsLocalized(loc2, quali))
                {
                    ret.clearLocalized(quali, loc2);
                }
            }
        }
        return ret;
    }


    public void init(ConverterRegistry registry)
    {
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected I18NService getI18nService()
    {
        return this.i18nService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    protected LocaleProvider getLocaleProvider()
    {
        return this.localeProvider;
    }


    protected DynamicAttributesProvider getDynamicAttributesProvider()
    {
        return (getTypeMetaInfo())._dynamicAttributesProvider;
    }


    SourceTransformer getSourceTransformer()
    {
        return this.sourceTransformer;
    }


    protected Method getGetter(Object model, ModelAttributeInfo info)
    {
        Class<?> cl = model.getClass();
        Method ret = info.getGetter(cl);
        if(ret == null)
        {
            ret = findGetterForAttribute(cl, info.getQualifier(), false);
            if(ret != null)
            {
                info.registerGetter(cl, ret);
            }
        }
        return ret;
    }


    protected Method getLocalizedGetter(Object model, ModelAttributeInfo info)
    {
        Class<?> cl = model.getClass();
        Method ret = info.getLocalizedGetter(cl);
        if(ret == null)
        {
            ret = findGetterForAttribute(cl, info.getQualifier(), true);
            if(ret != null)
            {
                info.registerLocalizedGetter(cl, ret);
            }
        }
        return ret;
    }


    protected Method getSetter(Object model, ModelAttributeInfo info, Object value)
    {
        Class<?> cl = model.getClass();
        Method ret = info.getSetter(cl);
        if(ret == null)
        {
            ret = findSetterForAttribute(cl, info.getQualifier(), value, false);
            if(ret != null)
            {
                info.registerSetter(cl, ret);
            }
        }
        return ret;
    }


    protected Method getLocalizedSetter(Object model, ModelAttributeInfo info, Object value)
    {
        Class<?> cl = model.getClass();
        Method ret = info.getLocalizedSetter(cl);
        if(ret == null)
        {
            ret = findSetterForAttribute(cl, info.getQualifier(), value, true);
            if(ret != null)
            {
                info.registerLocalizedSetter(cl, ret);
            }
        }
        return ret;
    }


    private Method findGetterForAttribute(Class modelClass, String attributeName, boolean localized)
    {
        MatchMethodCallback matchMethodCallback = new MatchMethodCallback(this);
        MatchGetterFilter matchMethodFilter = new MatchGetterFilter(this, attributeName, localized);
        ReflectionUtils.doWithMethods(modelClass, (ReflectionUtils.MethodCallback)matchMethodCallback, (ReflectionUtils.MethodFilter)matchMethodFilter);
        Method foundMethod = matchMethodCallback.getFoundMethod();
        return foundMethod;
    }


    private Method findSetterForAttribute(Class modelClass, String attributeName, Object attributeValue, boolean localized)
    {
        MatchMethodCallback matchMethodCallback = new MatchMethodCallback(this);
        MatchSetterFilter matchMethodFilter = new MatchSetterFilter(this, attributeName, localized, attributeValue);
        ReflectionUtils.doWithMethods(modelClass, (ReflectionUtils.MethodCallback)matchMethodCallback, (ReflectionUtils.MethodFilter)matchMethodFilter);
        Method foundMethod = matchMethodCallback.getFoundMethod();
        return foundMethod;
    }


    public SerializationStrategy getSerializationStrategy()
    {
        return this.serializationStrategy;
    }


    protected final Object getFieldValue(Object model, ModelAttributeInfo info)
    {
        return getItemModelContextImpl(model).getRawPropertyValue(info.getQualifier());
    }


    private Map<String, LocMap<Locale, Object>> getCombinedLocalizedAttributesValues(Object model)
    {
        ItemModelContext ctx = ModelContextUtils.getItemModelContext((AbstractItemModel)model);
        return ((ItemModelContextImpl)ctx).getCombinedLocalizedValuesMap();
    }


    private void setCombinedLocalizedAttributesValues(Object model, Map<String, LocMap<Locale, Object>> allValues)
    {
        Map<String, LocMap<Locale, Object>> allCombinedValues = getCombinedLocalizedAttributesValues(model);
        allCombinedValues.clear();
        allCombinedValues.putAll(allValues);
    }


    private void setFieldValue(Object model, ModelAttributeInfo info, Object value)
    {
        getItemModelContextImpl(model).setRawPropertyValue(info.getQualifier(), value);
    }


    public Map<String, Set<Locale>> getDirtyAttributes(Object model)
    {
        if(!(model instanceof AbstractItemModel))
        {
            throw new IllegalArgumentException("ItemModelConverter can only handle source objects of type Item!!");
        }
        return ((ItemModelContextImpl)ModelContextUtils.getItemModelContext((AbstractItemModel)model)).getValueHistory().getAllDirtyAttributes();
    }


    public Set<String> getUniqueAttributes()
    {
        return (getTypeMetaInfo())._modelAttributesUnique;
    }


    public Set<String> getMandatoryAttributes()
    {
        return (getTypeMetaInfo())._modelAttributesMandatory;
    }


    public Set<String> getMandatoryAttributesForCreation()
    {
        return (getTypeMetaInfo())._modelAttributesMandatoryForCreation;
    }


    public void typeSystemChanged()
    {
        synchronized(this)
        {
            this.metaInfo = null;
        }
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        ItemModelConverter that = (ItemModelConverter)o;
        return this.uuid.equals(that.uuid);
    }


    public int hashCode()
    {
        return this.uuid.hashCode();
    }
}
