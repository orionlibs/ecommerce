package de.hybris.platform.servicelayer.internal.model.impl.wrapper;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidationConfigurationException;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.extractor.CascadingDependenciesResolver;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelServiceInterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.InterceptorContextSnapshot;
import de.hybris.platform.servicelayer.internal.model.impl.Schedule;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModelWrapper
{
    private final Object model;
    private final ModelConverter converter;
    private final String persistenceType;
    private final boolean newFlag;
    private boolean legacyPersistence;
    private final PersistenceOperation operationToPerform;
    private final ModelWrapperContext wrapperContext;
    private final List<ModelWrapper> childWrappers;
    private Map<String, Set<ModelWrapper>> dependencies;
    private boolean validateInvoked;


    public ModelWrapper(Object model, PersistenceOperation mode, ModelWrapperContext wrapperContext)
    {
        this.converter = wrapperContext.getConverterRegistry().getModelConverterByModel(model);
        Preconditions.checkArgument((this.converter != null), "model converter is required");
        this.wrapperContext = wrapperContext;
        this.model = model;
        this.persistenceType = this.converter.getType(model);
        this.newFlag = this.converter.isNew(model);
        this.legacyPersistence = determineIfConfigureForLegacyPersistence();
        this.operationToPerform = mode;
        this.childWrappers = new ArrayList<>();
    }


    private boolean determineIfConfigureForLegacyPersistence()
    {
        if(isModelConverterConfiguredForLegacyPersistence(this.converter))
        {
            return true;
        }
        boolean sldSafeForWrite = this.wrapperContext.getJaloAccessorsService().isSLDSafeForWrite(this.persistenceType);
        return !sldSafeForWrite;
    }


    private boolean isModelConverterConfiguredForLegacyPersistence(ModelConverter converter)
    {
        return (!(converter instanceof ItemModelConverter) || ((ItemModelConverter)converter).isConfiguredForLegacyPersistence());
    }


    public void executeInterceptorsAndCascade(DefaultModelServiceInterceptorContext ctx)
    {
        if(ctx.isAlreadyNotified(getModel()))
        {
            return;
        }
        InterceptorContextSnapshot ctxSnapshotBefore = ctx.createSnapshot();
        if(this.operationToPerform == PersistenceOperation.SAVE)
        {
            invokePrepareInterceptors((InterceptorContext)ctx);
        }
        else if(this.operationToPerform == PersistenceOperation.DELETE)
        {
            invokeRemoveInterceptors((InterceptorContext)ctx);
        }
        ctx.markAsNotified(getModel());
        InterceptorContextSnapshot ctxSnapshotAfter = ctx.createSnapshot();
        Collection<ModelWrapper> modelsToCreate = getModelsToCreate(ctx, ctxSnapshotBefore, ctxSnapshotAfter);
        Collection<ModelWrapper> modelsToUpdate = getModelsToUpdate(ctx, ctxSnapshotBefore, ctxSnapshotAfter);
        Collection<ModelWrapper> modelsToDelete = getModelsToDelete(ctx, ctxSnapshotBefore, ctxSnapshotAfter);
        ctx.registerWrappedElementsFor(modelsToCreate, PersistenceOperation.SAVE);
        this.childWrappers.addAll(modelsToCreate);
        this.childWrappers.addAll(modelsToUpdate);
        this.childWrappers.addAll(modelsToDelete);
        for(ModelWrapper childWrapper : this.childWrappers)
        {
            childWrapper.executeInterceptorsAndCascade(ctx);
            ctx.schedule(childWrapper);
        }
    }


    public void resolveDependencies(CascadingDependenciesResolver dependenciesResolver, WrapperRegistry wrapperRegistry)
    {
        dependenciesResolver.resolveDependencies(this, wrapperRegistry);
    }


    private Collection<ModelWrapper> getModelsToCreate(DefaultModelServiceInterceptorContext ctx, InterceptorContextSnapshot ctxBefore, InterceptorContextSnapshot ctxAfter)
    {
        Collection<ModelWrapper> result = new ArrayList<>();
        Collection<ModelWrapper> cascadedModels = this.wrapperContext.getCascader().getNewModels(this, ctxAfter, ctx
                        .getWrapperRegistry());
        Collection<ModelWrapper> registeredForCreation = getModelsRegisteredForCreation(ctx, ctxBefore, ctxAfter);
        result.addAll(cascadedModels);
        result.addAll(registeredForCreation);
        return result;
    }


    private Collection<ModelWrapper> getModelsRegisteredForCreation(DefaultModelServiceInterceptorContext ctx, InterceptorContextSnapshot ctxBefore, InterceptorContextSnapshot ctxSnapshotAfter)
    {
        Collection<AbstractItemModel> result = filterElements(ctx, ctxBefore, ctxSnapshotAfter, PersistenceOperation.SAVE, ModelState.NEW);
        return ctx.wrap(result, PersistenceOperation.SAVE);
    }


    private Collection<ModelWrapper> getModelsToUpdate(DefaultModelServiceInterceptorContext ctx, InterceptorContextSnapshot ctxBefore, InterceptorContextSnapshot ctxAfter)
    {
        Collection<AbstractItemModel> result = filterElements(ctx, ctxBefore, ctxAfter, PersistenceOperation.SAVE, ModelState.MODIFIED);
        return ctx.wrap(result, PersistenceOperation.SAVE);
    }


    private Collection<ModelWrapper> getModelsToDelete(DefaultModelServiceInterceptorContext ctx, InterceptorContextSnapshot ctxBefore, InterceptorContextSnapshot ctxAfter)
    {
        Collection<AbstractItemModel> result = filterElements(ctx, ctxBefore, ctxAfter, PersistenceOperation.DELETE, ModelState.DELETED);
        return ctx.wrap(result, PersistenceOperation.DELETE);
    }


    private Collection<AbstractItemModel> filterElements(DefaultModelServiceInterceptorContext ctx, InterceptorContextSnapshot ctxBefore, InterceptorContextSnapshot ctxAfter, PersistenceOperation operation, ModelState state)
    {
        Collection<?> elementsBefore = new LinkedHashSet(ctxBefore.getElementsFor(operation));
        Collection elementsAfter = new LinkedHashSet(ctxAfter.getElementsFor(operation));
        Collection<AbstractItemModel> result = new ArrayList<>(elementsAfter.size());
        if(elementsAfter.size() > elementsBefore.size())
        {
            elementsAfter.removeAll(elementsBefore);
            for(Object el : elementsAfter)
            {
                if(this.model.equals(el) && state.getPersistenceOperation() == this.operationToPerform)
                {
                    continue;
                }
                if(!(el instanceof AbstractItemModel))
                {
                    continue;
                }
                Schedule schedule = ctx.getSchedule();
                if((state == ModelState.NEW && ctx.isNew(el) && !schedule.containsWrapperFor(el, PersistenceOperation.SAVE)) || (state == ModelState.MODIFIED &&
                                !ctx.isNew(el) &&
                                !schedule.containsWrapperFor(el, PersistenceOperation.SAVE)) || (state == ModelState.DELETED &&
                                !ctx.isNew(el) &&
                                !schedule.containsWrapperFor(el, PersistenceOperation.DELETE)))
                {
                    result.add((AbstractItemModel)el);
                }
            }
        }
        return result;
    }


    public void validate(InterceptorContext ctx)
    {
        if(this.validateInvoked)
        {
            return;
        }
        this.validateInvoked = true;
        for(ModelWrapper childWrapper : this.childWrappers)
        {
            childWrapper.validate(ctx);
        }
        if(this.operationToPerform == PersistenceOperation.SAVE)
        {
            invokeValidateInterceptors(ctx);
        }
    }


    public Object getModel()
    {
        return this.model;
    }


    public ModelConverter getConverter()
    {
        return this.converter;
    }


    public String getPersistenceType()
    {
        return this.persistenceType;
    }


    public boolean isNew()
    {
        return this.newFlag;
    }


    private void invokePrepareInterceptors(InterceptorContext ctx)
    {
        for(PrepareInterceptor preparer : this.wrapperContext.getPreparers(getPersistenceType()))
        {
            try
            {
                preparer.onPrepare(getModel(), ctx);
            }
            catch(InterceptorException e)
            {
                e.setInterceptor((Interceptor)preparer);
                throw new ModelSavingException(e.getMessage(), e);
            }
            catch(Exception e)
            {
                throw new ModelSavingException("[" + preparer + "]: unexpected preparer error: " + e.getMessage(), e);
            }
        }
    }


    private void invokeValidateInterceptors(InterceptorContext ctx) throws ModelSavingException
    {
        ModelSavingException validationErrors = null;
        for(ValidateInterceptor validator : this.wrapperContext.getValidators(getPersistenceType()))
        {
            try
            {
                validator.onValidate(getModel(), ctx);
            }
            catch(ValidationConfigurationException e)
            {
                throw new ModelSavingException("[" + validator + "]: " + e.getMessage(), e);
            }
            catch(InterceptorException e)
            {
                e.setInterceptor((Interceptor)validator);
                if(validationErrors == null)
                {
                    validationErrors = new ModelSavingException(e.getMessage(), (Throwable)e);
                    continue;
                }
                validationErrors.setNextException(new ModelSavingException(e.getMessage(), (Throwable)e));
            }
            catch(SystemIsSuspendedException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new ModelSavingException("[" + validator + "]: unexpected validator error: " + e.getMessage(), e);
            }
        }
        if(validationErrors != null)
        {
            throw validationErrors;
        }
    }


    private void invokeRemoveInterceptors(InterceptorContext ctx)
    {
        for(RemoveInterceptor removeInter : this.wrapperContext.getRemovers(getPersistenceType()))
        {
            try
            {
                removeInter.onRemove(this.model, ctx);
            }
            catch(InterceptorException e)
            {
                e.setInterceptor((Interceptor)removeInter);
                throw new ModelRemovalException(e.getMessage(), e);
            }
            catch(Exception e)
            {
                throw new ModelRemovalException("[" + removeInter + "]: unexpected removal error: " + e.getMessage(), e);
            }
        }
    }


    public boolean save(Set<ModelWrapper> done, boolean doPartialSave)
    {
        if(doPartialSave)
        {
            Collection<String> excluded = getPartialSaveExcludeAttributes(done);
            if(excluded != null)
            {
                getConverter().save(getModel(), excluded);
                return true;
            }
            return false;
        }
        if(canSave(done))
        {
            getConverter().save(getModel(), null);
            return true;
        }
        return false;
    }


    public void addDependent(String attribute, Collection<ModelWrapper> otherModels)
    {
        if(otherModels != null && !otherModels.isEmpty())
        {
            if(this.dependencies == null)
            {
                this.dependencies = new HashMap<>();
            }
            Set<ModelWrapper> dep = this.dependencies.get(attribute);
            if(dep == null)
            {
                this.dependencies.put(attribute, dep = new HashSet<>());
            }
            for(ModelWrapper otherModelWrapper : otherModels)
            {
                if(!wrappedModelAlreadyInDependencies(otherModelWrapper, dep))
                {
                    dep.add(otherModelWrapper);
                }
            }
        }
    }


    protected boolean wrappedModelAlreadyInDependencies(ModelWrapper otherModelWrapper, Set<ModelWrapper> dep)
    {
        Object model = otherModelWrapper.getModel();
        for(ModelWrapper dependencyWrappers : dep)
        {
            if(model.equals(dependencyWrappers.getModel()))
            {
                return true;
            }
        }
        return false;
    }


    protected Collection<String> getPartialSaveExcludeAttributes(Set<ModelWrapper> done)
    {
        Collection<String> ret = null;
        if(this.dependencies != null && !this.dependencies.isEmpty())
        {
            for(Map.Entry<String, Set<ModelWrapper>> e : this.dependencies.entrySet())
            {
                if(!done.containsAll(e.getValue()))
                {
                    if(getConverter() instanceof ItemModelConverter)
                    {
                        ItemModelConverter conv = (ItemModelConverter)getConverter();
                        ItemModelConverter.TypeAttributeInfo attributeInfo = conv.getInfo(e.getKey()).getAttributeInfo();
                        if(!attributeInfo.isRequiredForCreation() && !isUsedInPolyglotRepoConfig(attributeInfo))
                        {
                            if(ret == null)
                            {
                                ret = new ArrayList<>(20);
                            }
                            ret.add(e.getKey());
                            continue;
                        }
                        return null;
                    }
                    continue;
                }
                ((Set)e.getValue()).clear();
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    private boolean isUsedInPolyglotRepoConfig(ItemModelConverter.TypeAttributeInfo attributeInfo)
    {
        if(!(this.model instanceof AbstractItemModel))
        {
            return false;
        }
        int itemTypeCode = Registry.getPersistenceManager().getPersistenceInfo(((AbstractItemModel)this.model).getItemtype()).getItemTypeCode();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(itemTypeCode);
        return PolyglotPersistence.isKeyUsedInConfig(typeInfo, attributeInfo.getQualifier());
    }


    protected boolean canSave(Set<ModelWrapper> done)
    {
        if(this.dependencies != null && !this.dependencies.isEmpty())
        {
            for(Map.Entry<String, Set<ModelWrapper>> e : this.dependencies.entrySet())
            {
                if(!done.containsAll(e.getValue()))
                {
                    return false;
                }
                ((Set)e.getValue()).clear();
            }
        }
        return true;
    }


    public String toString()
    {
        return "<" + getModel().toString() + ":configured for legacy persistence -> " + this.legacyPersistence + ">";
    }


    public PK getPk()
    {
        if(this.model instanceof AbstractItemModel)
        {
            return ((AbstractItemModel)this.model).getPk();
        }
        throw new IllegalStateException("There is not AbstractItemModel available for getPk operation");
    }


    public PK getGeneratedPk()
    {
        ItemModelInternalContext ictx = (ItemModelInternalContext)ModelContextUtils.getItemModelContext((AbstractItemModel)this.model);
        PK newPK = ictx.getNewPK();
        return (newPK == null) ? ictx.generateNewPK() : newPK;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ModelWrapper wrapper = (ModelWrapper)o;
        if(!this.model.equals(wrapper.model))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        return this.model.hashCode();
    }


    public PK getResolvedPk()
    {
        return isNew() ? getGeneratedPk() : getPk();
    }


    public boolean isConfiguredForLegacyPersistence()
    {
        return this.legacyPersistence;
    }


    public PersistenceOperation getOperationToPerform()
    {
        return this.operationToPerform;
    }


    public ModelWrapperContext getWrapperContext()
    {
        return this.wrapperContext;
    }
}
