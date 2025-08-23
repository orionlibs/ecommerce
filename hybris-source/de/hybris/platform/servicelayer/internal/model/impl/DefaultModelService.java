package de.hybris.platform.servicelayer.internal.model.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.cache.DefaultSLDDataContainerProvider;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemRemovalEvent;
import de.hybris.platform.servicelayer.exceptions.ModelForItemAlreadyBeingLoadedException;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.ModelTypeNotSupportedException;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObjectNotFoundException;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.ModelCloningStrategy;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.ModelPersister;
import de.hybris.platform.servicelayer.internal.model.ModelSearchStrategy;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import de.hybris.platform.servicelayer.internal.model.extractor.ChangeSetBuilder;
import de.hybris.platform.servicelayer.internal.model.extractor.ModelExtractor;
import de.hybris.platform.servicelayer.internal.model.extractor.PersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapperContext;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.persistence.PersistenceUtils;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class DefaultModelService extends AbstractModelService
{
    private static final Logger LOG = Logger.getLogger(DefaultModelService.class);
    public static final String ENABLE_TRANSACTIONAL_SAVES = "enableTransactionalSaves";
    private ModelContext modelContext;
    private ModelPersister modelPersister;
    private ModelExtractor modelExtractor;
    private TransactionTemplate transactionTemplate;
    private ChangeSetBuilder changeSetBuilder;
    private EventService eventService;
    private PersistenceTypeService persistenceTypeService;
    private Cascader cascader;
    private SourceTransformer sourceTransformer;
    private DefaultSLDDataContainerProvider sldDataContainerProvider;
    private boolean defaultTransactionalSave;
    private final Object _converterRegistryLock = new Object();
    private volatile ConverterRegistry _converterRegistry;
    private final Object _interceptorRegistryLock = new Object();
    private volatile InterceptorRegistry _interceptorRegistry;
    private final Object _modelCloningStrategyLock = new Object();
    private volatile ModelCloningStrategy _modelCloningStrategy;
    private final Object _modelCloningContextLock = new Object();
    private volatile ModelCloningContext _modelCloningContext;
    private final Object _modelSearchStrategyLock = new Object();
    private volatile ModelSearchStrategy _modelSearchStrategy;
    private final Object sessionServiceLock = new Object();
    private volatile SessionService sessionService;
    private final Object writePersistenceGatewayLock = new Object();
    private volatile WritePersistenceGateway writePersistenceGateway;


    public void attach(Object model)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("model", model);
        ModelConverter conv = getModelConverterByModel(model);
        doAttach(model, conv.isNew(model) ? null : conv.getPersistenceSource(model), conv, getModelContext());
    }


    public void clearTransactionsSettings()
    {
        getSessionService().removeAttribute("enableTransactionalSaves");
    }


    public <T> T clone(T original)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("original", original);
        return clone(original, null, null);
    }


    public <T> T clone(Object original, Class<T> targetType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("original", original);
        return clone(original, targetType, null);
    }


    public <T> T clone(T original, ModelCloningContext ctx)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("original", original);
        return (T)getModelCloningStrategy().clone(original, (ctx != null) ? ctx : getModelCloningContext());
    }


    public <T> T clone(Object original, Class<T> targetType, ModelCloningContext ctx)
    {
        return (T)getModelCloningStrategy().clone(original,
                        (targetType != null) ? getConverterRegistry().getMappedType(targetType) : null,
                        (ctx != null) ? ctx : getModelCloningContext());
    }


    public <T> T create(Class modelClass)
    {
        ServicesUtil.validateParameterNotNull(modelClass, "Parameter 'modelClass' is null!");
        ModelConverter conv = getConverterRegistry().getModelConverterByModelType(modelClass);
        if(conv instanceof de.hybris.platform.servicelayer.internal.converter.impl.EnumValueModelConverter)
        {
            conv = getConverterRegistry().getModelConverterBySourceType("EnumerationValue");
        }
        return doCreate(conv, null);
    }


    public <T> T create(String typeCode)
    {
        ServicesUtil.validateParameterNotNull(typeCode, "Parameter 'typeCode' is null!");
        ModelConverter conv = getConverterRegistry().getModelConverterBySourceType(typeCode);
        if(conv instanceof de.hybris.platform.servicelayer.internal.converter.impl.EnumValueModelConverter)
        {
            conv = getConverterRegistry().getModelConverterBySourceType("EnumerationValue");
        }
        return doCreate(conv, typeCode);
    }


    public void detach(Object modelPkOrItem)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("modelPkOrItem", modelPkOrItem);
        try
        {
            ModelConverter conv = getModelConverterByModel(modelPkOrItem);
            if(!conv.isRemoved(modelPkOrItem))
            {
                doDetach(modelPkOrItem, conv.isNew(modelPkOrItem) ? null : conv.getPersistenceSource(modelPkOrItem), conv);
            }
        }
        catch(ModelTypeNotSupportedException ex)
        {
            if(modelPkOrItem instanceof PK)
            {
                detach((PK)modelPkOrItem);
            }
            else
            {
                detachViaSource(transformSourceToPersistenceObject(modelPkOrItem));
            }
        }
    }


    public void detach(PK sourcePK)
    {
        ServicesUtil.validateParameterNotNull(sourcePK, "Parameter 'sourcePK' is null!");
        try
        {
            detachViaSource(transformSourceToPersistenceObject(sourcePK));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new ModelLoadingException("No item found for given pk " + sourcePK, e);
        }
    }


    public void detachAll()
    {
        getModelContext().clear();
    }


    public void disableTransactions()
    {
        getSessionService().setAttribute("enableTransactionalSaves", Boolean.FALSE);
    }


    public void enableTransactions()
    {
        getSessionService().setAttribute("enableTransactionalSaves", Boolean.TRUE);
    }


    public <T> T get(Object source)
    {
        ServicesUtil.validateParameterNotNull(source, "Parameter 'source' is null!");
        PersistenceObject persistenceObject = transformSourceToPersistenceObject(source);
        return doLoad(getModelConverterBySource(persistenceObject), persistenceObject);
    }


    public <T> T get(Object source, String conversionType)
    {
        ServicesUtil.validateParameterNotNull(source, "Parameter 'source' is null!");
        ServicesUtil.validateParameterNotNull(conversionType, "Parameter 'conversionType' is null!");
        PersistenceObject persistenceObject = transformSourceToPersistenceObject(source);
        return doLoad(getConverterRegistry().getModelConverterBySourceType(conversionType), persistenceObject);
    }


    public <T> T get(PK sourcePK)
    {
        ServicesUtil.validateParameterNotNull(sourcePK, "Parameter 'sourcePK' is null!");
        try
        {
            return get(transformSourceToPersistenceObject(sourcePK));
        }
        catch(PersistenceObjectNotFoundException e)
        {
            throw new ModelLoadingException("No item found for given pk " + sourcePK, e);
        }
    }


    private PersistenceObject transformSourceToPersistenceObject(Object src)
    {
        return this.sourceTransformer.transformSource(src);
    }


    public <T> T getAttributeValue(Object model, String attributeQualifier)
    {
        Preconditions.checkNotNull(model, "model is required to obtain a value");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier is required to obtain a value");
        return (T)getModelConverterByModel(model).getAttributeValue(model, attributeQualifier);
    }


    public <T> T getAttributeValue(Object model, String attributeQualifier, Locale locale)
    {
        Preconditions.checkNotNull(model, "model is required to obtain a value");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier is required to obtain a value");
        Preconditions.checkNotNull(locale, "locale is required to obtain a value");
        return (T)getModelConverterByModel(model).getLocalizedAttributeValue(model, attributeQualifier, locale);
    }


    public <T> Map<Locale, T> getAttributeValues(Object model, String attributeQualifier, Locale... locales)
    {
        Preconditions.checkNotNull(model, "model is required to obtain a value");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier is required to obtain a value");
        Preconditions.checkNotNull(locales, "locales cannot be null");
        ImmutableMap.Builder<Locale, T> builder = ImmutableMap.builder();
        for(Locale locale : locales)
        {
            T value = getAttributeValueQuietly(model, attributeQualifier, locale);
            putValueInMapBuilderIfNotNull(builder, locale, value);
        }
        return (Map<Locale, T>)builder.build();
    }


    private static <T> void putValueInMapBuilderIfNotNull(ImmutableMap.Builder<Locale, T> builder, Locale locale, T value)
    {
        if(value != null)
        {
            builder.put(locale, value);
        }
    }


    private <T> T getAttributeValueQuietly(Object model, String attributeQualifier, Locale locale)
    {
        try
        {
            return getAttributeValue(model, attributeQualifier, locale);
        }
        catch(IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("value for localized attribute '" + attributeQualifier + "' in locale '" + locale + "' not found [reason: " + e
                                .getMessage() + "]");
            }
            return null;
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public <T> T getByExample(T example)
    {
        return (T)getModelSearchStrategy().getModelByExample(getModelConverterByModel(example), example);
    }


    public String getModelType(Class modelClass)
    {
        return getConverterRegistry().getMappedType(modelClass);
    }


    public String getModelType(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        String type = getSourceTypeFromModel(model);
        return (type == null) ? getModelType(model.getClass()) : type;
    }


    public Class getModelTypeClass(Class modelClass)
    {
        if(getConverterRegistry().hasModelConverterForModelType(modelClass))
        {
            return TypeManager.getInstance().getComposedType(getConverterRegistry().getMappedType(modelClass)).getJaloClass();
        }
        return modelClass;
    }


    private PersistenceObject getPersistenceSource(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        PersistenceObject src = getModelConverterByModel(model).getPersistenceSource(model);
        if(src == null)
        {
            throw new IllegalStateException("model " + model + " got no source");
        }
        return src;
    }


    public <T> T getSource(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        T ret = (T)getModelConverterByModel(model).getSource(model);
        if(ret == null)
        {
            throw new IllegalStateException("model " + model + " got no source");
        }
        return ret;
    }


    public void initDefaults(Object model) throws ModelInitializationException
    {
        DefaultModelServiceInterceptorContext ctx = null;
        for(InitDefaultsInterceptor inter : getInterceptorRegistry().getInitDefaultsInterceptors(getModelType(model)))
        {
            if(ctx == null)
            {
                ModelWrapperContext wrapperContext = createModelWrapperContext();
                ctx = new DefaultModelServiceInterceptorContext(this, Collections.singletonList(model), wrapperContext);
            }
            try
            {
                inter.onInitDefaults(model, (InterceptorContext)ctx);
            }
            catch(InterceptorException e)
            {
                throw new ModelInitializationException("[" + inter + "]:" + e.getMessage(), e);
            }
            catch(Exception e)
            {
                throw new ModelInitializationException("[" + inter + "]:unexpected load init default error: " + e.getMessage(), e);
            }
        }
    }


    public boolean preloadItems(List<PK> pks)
    {
        if(isSLDPersistence())
        {
            this.sldDataContainerProvider.preload(pks);
            return true;
        }
        return false;
    }


    static boolean isSLDPersistence()
    {
        return !PersistenceUtils.isPersistenceLegacyModeEnabled();
    }


    public boolean isModified(Object model)
    {
        return getModelConverterByModel(model).isModified(model);
    }


    public boolean isNew(Object model)
    {
        return getModelConverterByModel(model).isNew(model);
    }


    public boolean isRemoved(Object model)
    {
        return getModelConverterByModel(model).isRemoved(model);
    }


    public boolean isUpToDate(Object model)
    {
        return getModelConverterByModel(model).isUpToDate(model);
    }


    public void refresh(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        getModelConverterByModel(model).reload(model);
        notifyLoadInterceptors(model, getPersistenceSource(model));
    }


    public void remove(Object model) throws ModelRemovalException
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        removeAll(Collections.singleton(model));
    }


    public void remove(PK pk) throws ModelRemovalException
    {
        try
        {
            remove(get(pk));
        }
        catch(ModelForItemAlreadyBeingLoadedException e)
        {
            LOG.error(e.getMessage());
        }
    }


    public void removeAll(Collection<? extends Object> models) throws ModelRemovalException
    {
        ServicesUtil.validateParameterNotNull(models, "Parameter 'models' is null!");
        removeAll(models, isTransactional());
    }


    public void removeAll(Object... models) throws ModelRemovalException
    {
        ServicesUtil.validateParameterNotNull(models, "Parameter 'models' is null!");
        removeAll(Arrays.asList(models), isTransactional());
    }


    protected void removeAll(Collection<? extends Object> models, boolean transactional)
    {
        if(transactional)
        {
            try
            {
                this.transactionTemplate.execute((TransactionCallback)new Object(this, models));
            }
            catch(ModelRemovalException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new ModelRemovalException(e.getMessage(), e);
            }
        }
        else
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Executing remove operation for models: " + models + " in non-transaction mode");
            }
            removeAllInternal(models);
        }
    }


    private void removeAllInternal(Collection<? extends Object> models)
    {
        DefaultModelServiceInterceptorContext ctx = new DefaultModelServiceInterceptorContext(this, PersistenceOperation.DELETE, models, createModelWrapperContext());
        performPersistenceOperations(ctx);
    }


    public boolean isUniqueConstraintErrorAsRootCause(Exception e)
    {
        return (Utilities.getRootCauseOfType(e, SQLIntegrityConstraintViolationException.class) != null ||
                        isSpringDuplicateKeyException(e) || isSpringConcurrencyException(e) || isHanaConstraintViolation(e) ||
                        isSQLServerConstraintViolation(e));
    }


    protected boolean isSQLServerConstraintViolation(Exception e)
    {
        Throwable root = Utilities.getRootCauseOfName(e, "com.microsoft.sqlserver.jdbc.SQLServerException");
        return (root != null && root.getMessage().contains("unique"));
    }


    protected boolean isHanaConstraintViolation(Exception e)
    {
        Throwable root = Utilities.getRootCauseOfName(e, "com.sap.db.jdbc.exceptions.JDBCDriverException");
        return (root != null && root.getMessage().contains("check_unique"));
    }


    protected boolean isSpringDuplicateKeyException(Exception e)
    {
        return (Utilities.getRootCauseOfType(e, DuplicateKeyException.class) != null);
    }


    protected boolean isSpringConcurrencyException(Exception e)
    {
        return (Utilities.getRootCauseOfType(e, ConcurrencyFailureException.class) != null);
    }


    public void save(Object model)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' is null!");
        saveAll(Collections.singletonList(model), isTransactional());
    }


    public void saveAll() throws ModelSavingException
    {
        Set<Object> models = prepareObjectsToSave();
        saveAll(models, isTransactional());
    }


    public void saveAll(Collection<? extends Object> models) throws ModelSavingException
    {
        ServicesUtil.validateParameterNotNull(models, "Parameter 'models' is null!");
        saveAll(models, isTransactional());
    }


    public void saveAll(Object... models) throws ModelSavingException
    {
        saveAll(Arrays.asList(models), isTransactional());
    }


    protected void saveAll(Collection<? extends Object> models, boolean transactional)
    {
        try
        {
            if(transactional)
            {
                this.transactionTemplate.execute((TransactionCallback)new Object(this, models));
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Executing save operation for models: " + models + " in non-transaction mode");
                }
                saveAllInternal(models);
            }
        }
        catch(ModelSavingException | de.hybris.platform.core.suspend.SystemIsSuspendedException | de.hybris.platform.core.locking.ItemLockedForProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new ModelSavingException(e.getMessage(), e);
        }
    }


    private void saveAllInternal(Collection<? extends Object> models)
    {
        DefaultModelServiceInterceptorContext ctx = new DefaultModelServiceInterceptorContext(this, PersistenceOperation.SAVE, models, createModelWrapperContext());
        performPersistenceOperations(ctx);
    }


    private void performPersistenceOperations(DefaultModelServiceInterceptorContext ctx)
    {
        List<ModelWrapper> wrappers = this.modelExtractor.process(ctx, getInterceptorRegistry(), getConverterRegistry());
        persistWrappers(wrappers);
    }


    private ModelWrapperContext createModelWrapperContext()
    {
        return new ModelWrapperContext(getConverterRegistry(), getInterceptorRegistry(), this.cascader);
    }


    private void doJaloPersistence(Collection<ModelWrapper> wrappers)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Jalo persistence [wrappers:" + wrappers + "]");
        }
        try
        {
            List<Object[]> modelsAndSourcesForDetach = collectWrappersToRemove(wrappers);
            List<ModelWrapper> wrappersToSave = collectWrappersToSave(wrappers);
            HJMPUtils.registerVersionsForPks(collectOptimisticLockingVersionsForWrappers(wrappersToSave));
            Collection<ModelWrapper> saved = saveViaJalo(wrappersToSave);
            removeViaJalo(modelsAndSourcesForDetach);
            postProcessJaloSavedItems(wrappersToSave, saved);
        }
        finally
        {
            HJMPUtils.clearVersionsForPks();
        }
    }


    private static Map<PK, Long> collectOptimisticLockingVersionsForWrappers(List<ModelWrapper> wrappers)
    {
        Map<PK, Long> result = new HashMap<>(wrappers.size());
        for(ModelWrapper wrapper : wrappers)
        {
            Object model = wrapper.getModel();
            if(model instanceof AbstractItemModel)
            {
                ItemModelContext itemModelContext = ((AbstractItemModel)model).getItemModelContext();
                if(itemModelContext != null && wrapper.getPk() != null)
                {
                    result.put(wrapper.getPk(), Long.valueOf(itemModelContext.getPersistenceVersion()));
                }
            }
        }
        return result;
    }


    private static List<ModelWrapper> collectWrappersToSave(Collection<ModelWrapper> wrappers)
    {
        List<ModelWrapper> result = new ArrayList<>(wrappers.size());
        for(ModelWrapper wrapper : wrappers)
        {
            if(wrapper.getOperationToPerform() == PersistenceOperation.SAVE)
            {
                result.add(wrapper);
            }
        }
        return result;
    }


    private static List<Object[]> collectWrappersToRemove(Collection<ModelWrapper> wrappers)
    {
        List<Object[]> modelsAndSourcesForDetach = new ArrayList(wrappers.size());
        for(ModelWrapper wrapper : wrappers)
        {
            if(wrapper.getOperationToPerform() != PersistenceOperation.DELETE)
            {
                continue;
            }
            Object model = wrapper.getModel();
            ModelConverter conv = wrapper.getConverter();
            modelsAndSourcesForDetach.add(new Object[] {model, conv});
        }
        return modelsAndSourcesForDetach;
    }


    private void removeViaJalo(List<Object[]> modelsAndSourcesForDetach)
    {
        for(Object[] modelRecord : modelsAndSourcesForDetach)
        {
            Object model = modelRecord[0];
            ModelConverter conv = (ModelConverter)modelRecord[1];
            if(!isRemoved(model))
            {
                conv.remove(model);
            }
            doDetach(model, conv.getPersistenceSource(model), conv);
        }
    }


    protected Set<Object> prepareObjectsToSave()
    {
        Set<Object> toSave, newOnes = getModelContext().getNew();
        Set<Object> modifiedOnes = getModelContext().getModified();
        if(newOnes.isEmpty())
        {
            toSave = modifiedOnes;
        }
        else if(modifiedOnes.isEmpty())
        {
            toSave = newOnes;
        }
        else
        {
            toSave = new LinkedHashSet(newOnes.size() + modifiedOnes.size());
            toSave.addAll(newOnes);
            toSave.addAll(modifiedOnes);
        }
        return toSave;
    }


    public void setAttributeValue(Object model, String attributeQualifier, Object value)
    {
        Preconditions.checkNotNull(model, "model is required to set a value");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier is required to set a value");
        getModelConverterByModel(model).setAttributeValue(model, attributeQualifier, value);
    }


    public <T> void setAttributeValue(Object model, String attributeQualifier, Map<Locale, T> value)
    {
        Preconditions.checkNotNull(model, "model is required to set a value");
        Preconditions.checkNotNull(attributeQualifier, "attributeQualifier is required to set a value");
        getModelConverterByModel(model).setAttributeValue(model, attributeQualifier, value);
    }


    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate)
    {
        this.transactionTemplate = transactionTemplate;
    }


    @Required
    public void setModelContext(ModelContext modelContext)
    {
        this.modelContext = modelContext;
    }


    @Required
    public void setModelPersister(ModelPersister modelPersister)
    {
        this.modelPersister = modelPersister;
    }


    @Required
    public void setModelExtractor(ModelExtractor factory)
    {
        this.modelExtractor = factory;
    }


    @Required
    public void setChangeSetBuilder(ChangeSetBuilder builder)
    {
        this.changeSetBuilder = builder;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setCacheInvalidator(CacheInvalidator invalidator)
    {
        LOG.warn("You are using deprecated setCacheInvalidator method");
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    public void setTransactional(boolean transactional)
    {
        this.defaultTransactionalSave = transactional;
    }


    protected void detachViaSource(PersistenceObject persistenceObject)
    {
        ModelConverter conv = getModelConverterBySource(persistenceObject);
        Object model = getModelContext().getAttached(persistenceObject, conv);
        if(model != null && !conv.isRemoved(model))
        {
            doDetach(model, conv.isNew(model) ? null : persistenceObject, conv);
        }
    }


    protected void doAttach(Object model, PersistenceObject source, ModelConverter conv, ModelContext ctx)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' was null!");
        ServicesUtil.validateParameterNotNull(conv, "Parameter 'conv' was null!");
        ServicesUtil.validateParameterNotNull(ctx, "Parameter 'ctx' was null!");
        conv.beforeAttach(model, ctx);
        ctx.attach(model, source, conv);
    }


    protected <T> T doCreate(ModelConverter conv, String type)
    {
        T model = (T)conv.create(type);
        initDefaults(model);
        doAttach(model, null, conv, getModelContext());
        return model;
    }


    protected void doDetach(Object model, Object source, ModelConverter conv)
    {
        this.modelContext.detach(model, source, conv);
        conv.afterDetach(model, this.modelContext);
    }


    protected <T> T doLoad(ModelConverter conv, PersistenceObject source)
    {
        ModelContext ctx = getModelContext();
        T model = (T)getModelContext().getAttached(source, conv);
        if(model == null)
        {
            if(isLoading(ctx, source))
            {
                throw new ModelForItemAlreadyBeingLoadedException(source);
            }
            markLoading(ctx, source);
            try
            {
                model = (T)conv.load(source);
                notifyLoadInterceptors(model, source);
                doAttach(model, source, conv, getModelContext());
            }
            finally
            {
                clearLoading(ctx, source);
            }
        }
        return model;
    }


    private static void clearLoading(ModelContext ctx, PersistenceObject source)
    {
        ctx.clearLoading(source);
    }


    private static void markLoading(ModelContext ctx, PersistenceObject source)
    {
        ctx.markLoading(source);
    }


    private static boolean isLoading(ModelContext ctx, PersistenceObject source)
    {
        return ctx.isLoading(source);
    }


    protected ModelConverter getModelConverterByModel(Object model)
    {
        return getConverterRegistry().getModelConverterByModel(model);
    }


    protected ModelConverter getModelConverterBySource(PersistenceObject persistenceObject)
    {
        return getConverterRegistry().getModelConverterBySourceType(getSourceTypeFromSource(persistenceObject));
    }


    protected Object getModelForPersistentValue(Object persistentValue)
    {
        PersistenceObject persistenceObject = this.sourceTransformer.transformSourceOrReturnNull(persistentValue);
        if(persistenceObject == null)
        {
            return null;
        }
        if(getConverterRegistry().hasModelConverterForSourceType(getSourceTypeFromSource(persistenceObject)))
        {
            return get(persistentValue);
        }
        return null;
    }


    protected Object getPersistentValueForModel(Object model)
    {
        if(model != null && getConverterRegistry().hasModelConverterForModelType(model.getClass()))
        {
            try
            {
                return getSource(model);
            }
            catch(IllegalArgumentException e)
            {
                if(model instanceof AbstractItemModel)
                {
                    throw e;
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Can not get an item from given model", e);
                }
                return null;
            }
        }
        return null;
    }


    protected String getSourceTypeFromModel(Object model)
    {
        return getConverterRegistry().getModelConverterByModelType(model.getClass()).getType(model);
    }


    protected String getSourceTypeFromSource(PersistenceObject persistenceObject)
    {
        ServicesUtil.validateParameterNotNull(persistenceObject, "Parameter 'persistenceObject' is null!");
        return persistenceObject.getTypeCode();
    }


    protected void notifyLoadInterceptors(Object model, PersistenceObject source)
    {
        DefaultModelServiceInterceptorContext ctx = null;
        for(LoadInterceptor i : getInterceptorRegistry().getLoadInterceptors(getSourceTypeFromSource(source)))
        {
            if(ctx == null)
            {
                ModelWrapperContext wrapperContext = createModelWrapperContext();
                ctx = new DefaultModelServiceInterceptorContext(this, Collections.singletonList(model), wrapperContext);
                ctx.registerElement(model, source);
            }
            try
            {
                i.onLoad(model, (InterceptorContext)ctx);
            }
            catch(InterceptorException e)
            {
                throw new ModelLoadingException("[" + i + "]:" + e.getMessage(), e);
            }
            catch(Exception e)
            {
                throw new ModelLoadingException("[" + i + "]: unexpected load error: " + e.getMessage(), e);
            }
        }
    }


    protected boolean isTransactional()
    {
        Object sAttr = getSessionService().getAttribute("enableTransactionalSaves");
        if(sAttr != null)
        {
            return Boolean.TRUE.equals(sAttr);
        }
        return this.defaultTransactionalSave;
    }


    private void persistWrappers(List<ModelWrapper> wrappers)
    {
        if(this.persistenceTypeService.getPersistenceType(wrappers) == PersistenceType.DIRECT)
        {
            ChangeSet fullChangeSet = prepareChangeSet(wrappers);
            if(fullChangeSet == null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Jalo persistence [models to save: " + wrappers + " - dynamic due to jalo attribute");
                }
                doJaloPersistence(wrappers);
            }
            else
            {
                doDirectPersistence(wrappers, fullChangeSet);
            }
        }
        else
        {
            doJaloPersistence(wrappers);
        }
    }


    private ChangeSet prepareChangeSet(List<ModelWrapper> initialAndInterceptorGeneratedWrappers)
    {
        ChangeSet fullChangeSet = this.changeSetBuilder.build(initialAndInterceptorGeneratedWrappers);
        if(fullChangeSet.isJaloWayRecommended())
        {
            return null;
        }
        return fullChangeSet;
    }


    private void doDirectPersistence(List<ModelWrapper> allWrappers, ChangeSet fullChangeSet)
    {
        Collection<PersistResult> results;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Direct persistence [changeset: " + fullChangeSet + "]");
        }
        try
        {
            results = getWritePersistenceGateway().persist(fullChangeSet);
        }
        catch(ModelPersistenceException e)
        {
            throw new ModelSavingException(e.getMessage(), e);
        }
        processPersistResults(allWrappers, results);
    }


    private Collection<ModelWrapper> saveViaJalo(Collection<ModelWrapper> toSave)
    {
        if(CollectionUtils.isEmpty(toSave))
        {
            return Collections.EMPTY_LIST;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Jalo persistence [models to save: " + toSave + "]");
        }
        return this.modelPersister.persist(toSave);
    }


    private void postProcessJaloSavedItems(Collection<ModelWrapper> toSave, Collection<ModelWrapper> saved)
    {
        reloadModels(toSave);
        this.modelContext.afterPersist(saved);
    }


    private static void detachSource(ModelContext ctx, ModelWrapper wrapper)
    {
        Object model = wrapper.getModel();
        ModelConverter conv = wrapper.getConverter();
        PersistenceObject src = conv.getPersistenceSource(model);
        ctx.detach(model, src, conv);
    }


    private void processPersistResults(Collection<ModelWrapper> wrappers, Collection<PersistResult> results)
    {
        if(CollectionUtils.isEmpty(results))
        {
            for(ModelWrapper wrapper : wrappers)
            {
                ((ItemModelConverter)wrapper.getConverter()).unloadAttributes((AbstractItemModel)wrapper.getModel());
            }
            return;
        }
        Map<PK, PersistResult> resultMap = buildResultMap(results);
        ModelContext ctx = getModelContext();
        for(ModelWrapper wrapper : wrappers)
        {
            PK pk = wrapper.isNew() ? wrapper.getGeneratedPk() : wrapper.getPk();
            PersistResult persistResult = resultMap.get(pk);
            validateNotNull(persistResult);
            CrudEnum operation = persistResult.getOperation();
            if(operation == CrudEnum.UPDATE || operation == CrudEnum.CREATE)
            {
                notifyItemModelConverter(wrapper, pk, persistResult);
                this.modelContext.afterDirectPersist(wrapper);
            }
            if(operation == CrudEnum.DELETE)
            {
                detachSource(ctx, wrapper);
            }
        }
        notifyPersistenceListeners(results);
        publishEvents(results);
    }


    private void publishEvents(Collection<PersistResult> results)
    {
        if(!RedeployUtilities.isShutdownInProgress())
        {
            for(PersistResult result : results)
            {
                publishEvents(result);
            }
        }
    }


    private static void validateNotNull(PersistResult persistResult)
    {
        if(persistResult == null)
        {
            throw new ModelSavingException("Fatal error during save");
        }
    }


    private static void notifyPersistenceListeners(Collection<PersistResult> results)
    {
        for(PersistResult r : results)
        {
            if(CrudEnum.CREATE == r.getOperation())
            {
                Registry.getCurrentTenantNoFallback().getPersistencePool().notifyEntityCreation(r.getPk());
            }
        }
    }


    private void publishEvents(PersistResult res)
    {
        AfterItemCreationEvent evt;
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$CrudEnum[res.getOperation().ordinal()])
        {
            case 1:
                evt = new AfterItemCreationEvent((Serializable)res.getPk());
                evt.setTypeCode(res.getTypeCode());
                this.eventService.publishEvent((AbstractEvent)evt);
                break;
            case 3:
                this.eventService.publishEvent((AbstractEvent)new AfterItemRemovalEvent((Serializable)res.getPk()));
                break;
        }
    }


    private static void notifyItemModelConverter(ModelWrapper wrapper, PK pk, PersistResult result)
    {
        ((ItemModelConverter)wrapper.getConverter()).afterModification((AbstractItemModel)wrapper.getModel(), pk, result
                        .getPersistenceVersion().longValue());
    }


    private static Map<PK, PersistResult> buildResultMap(Collection<PersistResult> results)
    {
        Preconditions.checkNotNull(results);
        if(results.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<PK, PersistResult> resultMap = new LinkedHashMap<>();
        for(PersistResult result : results)
        {
            resultMap.put(result.getPk(), result);
        }
        return resultMap;
    }


    private void reloadModels(Collection<ModelWrapper> wrappers)
    {
        if(wrappers.size() > 1)
        {
            for(ModelWrapper wrapper : wrappers)
            {
                Object model = wrapper.getModel();
                if(isRemoved(model))
                {
                    continue;
                }
                wrapper.getConverter().reload(model);
            }
        }
    }


    public void lock(PK itemPK)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("itemPK", itemPK);
        Transaction.current().lock(JaloSession.getCurrentSession().getItem(itemPK));
    }


    public void lock(Object source)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("source", source);
        if(!(source instanceof Item))
        {
            throw new IllegalArgumentException("parameter source must be instance of Item. Source is: " + source);
        }
        Transaction.current().lock((Item)source);
    }


    public <T> T getWithLock(Object source)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("source", source);
        if(!(source instanceof Item))
        {
            throw new IllegalArgumentException("parameter source must be instance of Item. Source is: " + source);
        }
        Item itemSource = (Item)source;
        Transaction.current().lock(itemSource);
        return get(itemSource);
    }


    public boolean isAttached(Object model)
    {
        return getModelContext().isAttached(model, getModelConverterByModel(model));
    }


    public boolean isSourceAttached(Object source)
    {
        return (getModelContext().getAttached(source,
                        getModelConverterBySource(transformSourceToPersistenceObject(source))) != null);
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ConverterRegistry getConverterRegistry()
    {
        if(this._converterRegistry == null)
        {
            synchronized(this._converterRegistryLock)
            {
                if(this._converterRegistry == null)
                {
                    this._converterRegistry = lookupConverterRegistry();
                }
            }
        }
        return this._converterRegistry;
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public InterceptorRegistry getInterceptorRegistry()
    {
        if(this._interceptorRegistry == null)
        {
            synchronized(this._interceptorRegistryLock)
            {
                if(this._interceptorRegistry == null)
                {
                    this._interceptorRegistry = lookupInterceptorRegistry();
                }
            }
        }
        return this._interceptorRegistry;
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ModelCloningContext getModelCloningContext()
    {
        if(this._modelCloningContext == null)
        {
            synchronized(this._modelCloningContextLock)
            {
                if(this._modelCloningContext == null)
                {
                    this._modelCloningContext = lookupModelCloningContext();
                }
            }
        }
        return this._modelCloningContext;
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ModelCloningStrategy getModelCloningStrategy()
    {
        if(this._modelCloningStrategy == null)
        {
            synchronized(this._modelCloningStrategyLock)
            {
                if(this._modelCloningStrategy == null)
                {
                    this._modelCloningStrategy = lookupModelCloningStrategy();
                }
            }
        }
        return this._modelCloningStrategy;
    }


    public ModelContext getModelContext()
    {
        return this.modelContext;
    }


    @SuppressWarnings(value = {"DMI_UNSUPPORTED_METHOD"}, justification = "because I know better")
    public ModelSearchStrategy getModelSearchStrategy()
    {
        if(this._modelSearchStrategy == null)
        {
            synchronized(this._modelSearchStrategyLock)
            {
                if(this._modelSearchStrategy == null)
                {
                    this._modelSearchStrategy = lookupModelSearchStrategy();
                }
            }
        }
        return this._modelSearchStrategy;
    }


    public SessionService getSessionService()
    {
        if(this.sessionService == null)
        {
            synchronized(this.sessionServiceLock)
            {
                if(this.sessionService == null)
                {
                    this.sessionService = lookupSessionService();
                }
            }
        }
        return this.sessionService;
    }


    public WritePersistenceGateway getWritePersistenceGateway()
    {
        if(this.writePersistenceGateway == null)
        {
            synchronized(this.writePersistenceGatewayLock)
            {
                if(this.writePersistenceGateway == null)
                {
                    this.writePersistenceGateway = lookupWritePersistenceGateway();
                }
            }
        }
        return this.writePersistenceGateway;
    }


    public ConverterRegistry lookupConverterRegistry()
    {
        throw new UnsupportedOperationException("please override DefaultModelService.lookupConverterRegistry() or use <lookup-method>");
    }


    public InterceptorRegistry lookupInterceptorRegistry()
    {
        throw new UnsupportedOperationException("please override DefaultModelService.lookupInterceptorRegistry() or use <lookup-method>");
    }


    public ModelCloningContext lookupModelCloningContext()
    {
        throw new UnsupportedOperationException("please override DefaultModelService.lookupModelCloningContext() or use <lookup-method>");
    }


    public ModelCloningStrategy lookupModelCloningStrategy()
    {
        throw new UnsupportedOperationException("please override DefaultModelService.lookupModelCloningStrategy() or use <lookup-method>");
    }


    public ModelSearchStrategy lookupModelSearchStrategy()
    {
        throw new UnsupportedOperationException("please override DefaultModelService.lookupModelSearchStrategy() or use <lookup-method>");
    }


    public SessionService lookupSessionService()
    {
        throw new UnsupportedOperationException("please override #lookupSessionService() or use <lookup-method>");
    }


    public WritePersistenceGateway lookupWritePersistenceGateway()
    {
        throw new UnsupportedOperationException("please override #lookupPersistenceGateway() or use <lookup-method>");
    }


    @Required
    public void setPersistenceTypeService(PersistenceTypeService persistenceTypeService)
    {
        this.persistenceTypeService = persistenceTypeService;
    }


    @Required
    public void setCascader(Cascader cascader)
    {
        this.cascader = cascader;
    }


    public void setSourceTransformer(SourceTransformer sourceTransformer)
    {
        this.sourceTransformer = sourceTransformer;
    }


    @Required
    public void setSldDataContainerProvider(DefaultSLDDataContainerProvider sldDataContainerProvider)
    {
        this.sldDataContainerProvider = sldDataContainerProvider;
    }
}
