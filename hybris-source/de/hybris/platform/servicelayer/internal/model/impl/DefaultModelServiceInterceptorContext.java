package de.hybris.platform.servicelayer.internal.model.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapperContext;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DefaultModelServiceInterceptorContext implements InterceptorContext
{
    private final DefaultModelService defaultModelService;
    private final OperationOnModelInternal defaultOperationOnModel;
    private final RegisteredElements initialElements;
    private final Map<OperationOnModelInternal, RegisteredElements> registeredElementsPerMode;
    private final Map<Object, Object> notified = new IdentityHashMap<>();
    private final WrapperRegistry wrapperRegistry;
    private final Schedule schedule;
    private final InterceptorContext.TransientStorage transientStorage = (InterceptorContext.TransientStorage)new DefaultTransientStorage();
    private Map<Object, Object> sourceMappings;
    private Map<String, Object> attributes;


    public DefaultModelServiceInterceptorContext(DefaultModelService parent, PersistenceOperation defaultPersistenceOperation, Collection<? extends Object> initialElements, ModelWrapperContext wrapperContext)
    {
        this.defaultModelService = parent;
        this.wrapperRegistry = new WrapperRegistry(wrapperContext);
        if(defaultPersistenceOperation != null)
        {
            this.defaultOperationOnModel = OperationOnModelInternal.from(defaultPersistenceOperation);
        }
        else
        {
            this.defaultOperationOnModel = OperationOnModelInternal.OTHER;
        }
        this.registeredElementsPerMode = new HashMap<>();
        this.initialElements = new RegisteredElements(initialElements);
        this.schedule = new Schedule(initialElements.size());
    }


    public DefaultModelServiceInterceptorContext(DefaultModelService parent, Collection<Object> initialElements, ModelWrapperContext wrapperContext)
    {
        this(parent, null, initialElements, wrapperContext);
    }


    public void registerElement(Object model, Object source)
    {
        if(this.sourceMappings == null)
        {
            this.sourceMappings = new HashMap<>();
        }
        this.sourceMappings.put(model, source);
        elementsRegisteredFor(this.defaultOperationOnModel).add(model);
    }


    public void registerElement(Object model)
    {
        registerElement(model, null);
    }


    public void registerElementFor(Object model, PersistenceOperation operation)
    {
        elementsRegisteredFor(OperationOnModelInternal.from(operation)).add(model);
    }


    public RegisteredElements elementsRegisteredFor(PersistenceOperation operation)
    {
        return elementsRegisteredFor(OperationOnModelInternal.from(operation));
    }


    private RegisteredElements elementsRegisteredFor(OperationOnModelInternal operation)
    {
        if(!this.registeredElementsPerMode.containsKey(operation))
        {
            this.registeredElementsPerMode.put(operation, new RegisteredElements());
        }
        return this.registeredElementsPerMode.get(operation);
    }


    public Set<Object> getAllRegisteredElements()
    {
        HashSet<Object> result = Sets.newHashSet(this.initialElements.unmodifiableSet());
        result.addAll(elementsRegisteredFor(this.defaultOperationOnModel).unmodifiableSet());
        return result;
    }


    public Set<Object> getElementsRegisteredFor(PersistenceOperation operation)
    {
        return elementsRegisteredFor(operation).unmodifiableSet();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public boolean contains(Object model)
    {
        return getAllRegisteredElements().contains(model);
    }


    public boolean contains(Object model, PersistenceOperation operation)
    {
        return elementsRegisteredFor(operation).contains(model);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Object getSource(Object model)
    {
        Object ret = (this.sourceMappings != null) ? this.sourceMappings.get(model) : null;
        if(ret != null)
        {
            ret = this.defaultModelService.getSource(model);
            this.sourceMappings.put(model, ret);
        }
        return ret;
    }


    public boolean isNew(Object model)
    {
        return this.defaultModelService.getModelConverterByModel(model).isNew(model);
    }


    public boolean exists(Object model)
    {
        return this.defaultModelService.getModelConverterByModel(model).exists(model);
    }


    public boolean isModified(Object model)
    {
        return this.defaultModelService.getModelConverterByModel(model).isModified(model);
    }


    public boolean isModified(Object model, String attribute)
    {
        return this.defaultModelService.getModelConverterByModel(model).isModified(model, attribute);
    }


    public boolean isRemoved(Object model)
    {
        return this.defaultModelService.getModelConverterByModel(model).isRemoved(model);
    }


    public ModelService getModelService()
    {
        return (ModelService)this.defaultModelService;
    }


    public Object getAttribute(String key)
    {
        return (this.attributes != null) ? this.attributes.get(key) : null;
    }


    public void setAttribute(String key, Object value)
    {
        if(this.attributes == null)
        {
            if(value != null)
            {
                this.attributes = new HashMap<>();
                this.attributes.put(key, value);
            }
        }
        else if(value == null)
        {
            this.attributes.remove(key);
        }
        else
        {
            this.attributes.put(key, value);
        }
    }


    public Map<String, Set<Locale>> getDirtyAttributes(Object model)
    {
        return this.defaultModelService.getModelConverterByModel(model).getDirtyAttributes(model);
    }


    public InterceptorContext.TransientStorage getTransientStorage()
    {
        return this.transientStorage;
    }


    public void registerWrappedElementsFor(Collection<ModelWrapper> wrappers, PersistenceOperation operation)
    {
        for(ModelWrapper wrapper : wrappers)
        {
            registerElementFor(wrapper.getModel(), operation);
        }
    }


    public InterceptorContextSnapshot createSnapshot()
    {
        return new InterceptorContextSnapshot(this);
    }


    public RegisteredElements getInitialElements()
    {
        return this.initialElements;
    }


    public void markAsNotified(Object model)
    {
        this.notified.put(model, model);
    }


    public boolean isAlreadyNotified(Object model)
    {
        return this.notified.containsKey(model);
    }


    public Collection<ModelWrapper> getInitialWrappers()
    {
        if(this.defaultOperationOnModel != OperationOnModelInternal.SAVE && this.defaultOperationOnModel != OperationOnModelInternal.DELETE)
        {
            throw new IllegalStateException("This method can only be called during save and delete operations");
        }
        return this.wrapperRegistry.wrap(this.initialElements.unmodifiableSet(), this.defaultOperationOnModel.toOperationOnModelExternal());
    }


    public void schedule(ModelWrapper wrapper)
    {
        if(!this.schedule.contains(wrapper))
        {
            this.schedule.add(wrapper);
        }
    }


    public Schedule getSchedule()
    {
        return this.schedule;
    }


    public Collection<ModelWrapper> wrap(Collection<? extends Object> models, PersistenceOperation operation)
    {
        return this.wrapperRegistry.wrap(models, operation);
    }


    public WrapperRegistry getWrapperRegistry()
    {
        return this.wrapperRegistry;
    }
}
