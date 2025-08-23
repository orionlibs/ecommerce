/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.impl;

import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of ObjectFacade. Delegates to a matching {@link ObjectFacadeStrategy}.
 */
public class DefaultObjectFacade implements ObjectFacade
{
    public static final String CTX_PARAM_UPDATED_OBJECT_IS_NEW = "updatedObjectIsNew";
    public static final String COCKPITNG_CRUD_COCKPIT_EVENT_NOTIFICATION = "cockpitng.crud.cockpit.event.notification";
    private static final String MSG_CANNOT_FIND_OBJECT_STRATEGY_FOR = "Cannot find object strategy for: {}";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultObjectFacade.class);
    protected TypeFacade typeFacade;
    private AbstractStrategyRegistry<ObjectFacadeStrategy, Object> strategyRegistry;
    /**
     * @deprecated since 6.5
     * @see #eventPublisher
     */
    @Deprecated(since = "6.5", forRemoval = true)
    private CockpitEventQueue eventQueue;
    private CockpitGlobalEventPublisher eventPublisher;
    private CockpitProperties cockpitProperties;


    @Override
    public <T> T load(final String id) throws ObjectNotFoundException
    {
        return this.load(id, null);
    }


    @Override
    public <T> T load(final String id, final Context ctx) throws ObjectNotFoundException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(id);
        if(strategy == null)
        {
            throw new ObjectNotFoundException(id);
        }
        return strategy.load(id, ctx);
    }


    @Override
    public <T> void delete(final T object) throws ObjectDeletionException
    {
        this.delete(object, null);
    }


    @Override
    public <T> void delete(final T object, final Context ctx) throws ObjectDeletionException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            throw new ObjectDeletionException(String.valueOf(object));
        }
        strategy.delete(object, ctx);
        if(ctx == null || BooleanUtils.isNotTrue((Boolean)ctx.getAttribute(CTX_PARAM_SUPPRESS_EVENT)))
        {
            publishEvent(OBJECTS_DELETED_EVENT, Collections.singletonList(object), ctx);
        }
    }


    @Override
    public <T> ObjectFacadeOperationResult<T> delete(final Collection<T> objects, final Context ctx)
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(objects);
        ObjectFacadeOperationResult<T> result = new ObjectFacadeOperationResult<>();
        if(strategy == null)
        {
            return result;
        }
        result = strategy.delete(objects, ctx);
        if(ctx == null || BooleanUtils.isNotTrue((Boolean)ctx.getAttribute(CTX_PARAM_SUPPRESS_EVENT)))
        {
            publishEvent(OBJECTS_DELETED_EVENT, Collections.unmodifiableCollection(result.getSuccessfulObjects()), ctx);
        }
        return result;
    }


    @Override
    public <T> T create(final String typeId) throws ObjectCreationException
    {
        return this.create(typeId, null);
    }


    @Override
    public <T> T create(final String typeId, final Context ctx) throws ObjectCreationException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(typeId);
        if(strategy == null)
        {
            throw new ObjectCreationException(typeId);
        }
        final T ret = strategy.create(typeId, ctx);
        if(ctx == null || BooleanUtils.isNotTrue((Boolean)ctx.getAttribute(CTX_PARAM_SUPPRESS_EVENT)))
        {
            publishEvent(OBJECT_CREATED_EVENT, ret, ctx);
        }
        return ret;
    }


    @Override
    public <T> T save(final T objectToSave) throws ObjectSavingException
    {
        return this.save(objectToSave, null);
    }


    protected <T> T saveSingleObject(final T object, final Context ctx) throws ObjectSavingException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            throw new ObjectSavingException(String.valueOf(object), null);
        }
        ctx.addAttribute(CTX_PARAM_UPDATED_OBJECT_IS_NEW, isNew(object));
        return strategy.save(object, ctx);
    }


    @Override
    public <T> T save(final T object, final Context ctx) throws ObjectSavingException
    {
        final Context internalContext = ctx == null ? new DefaultContext.Builder().build()
                        : new DefaultContext.Builder(ctx).build();
        final T savedObject = saveSingleObject(object, internalContext);
        if(BooleanUtils.isNotTrue((Boolean)internalContext.getAttribute(CTX_PARAM_SUPPRESS_EVENT)))
        {
            publishEvent(OBJECTS_UPDATED_EVENT, Collections.singletonList(savedObject), internalContext);
        }
        return savedObject;
    }


    /**
     * Persist objects by given collection data. Since 1808 this method is transactional.
     *
     * @param objects
     *           the data for updating objects in system. Each object is saved separately in a dedicated transaction.
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return ObjectFacadeOperationResult object
     */
    @Override
    public <T> ObjectFacadeOperationResult<T> save(final Collection<T> objects, final Context ctx)
    {
        final Context internalContext = ctx == null ? new DefaultContext.Builder().build()
                        : new DefaultContext.Builder(ctx).build();
        final Set<Pair<T, Boolean>> isNewObjects = objects.stream().map(object -> Pair.of(object, isNew(object))).collect(Collectors.toSet());
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(objects, ctx);
        final ObjectFacadeOperationResult<T> result = strategy.save(objects, ctx);
        if(ctx != null)
        {
            final Map<Object, Boolean> isNewObjectsMap = new HashMap<>();
            result.getSuccessfulObjects().forEach(successfulObject -> {
                isNewObjects.stream().filter(pair -> pair.getLeft().equals(successfulObject)).findFirst()
                                .ifPresent(pair -> isNewObjectsMap.put(successfulObject, pair.getRight()));
            });
            internalContext.addAttribute(CTX_PARAM_UPDATED_OBJECT_IS_NEW, isNewObjectsMap);
        }
        if(BooleanUtils.isNotTrue((Boolean)internalContext.getAttribute(CTX_PARAM_SUPPRESS_EVENT)))
        {
            publishEvent(OBJECTS_UPDATED_EVENT, result.getSuccessfulObjects(), internalContext);
        }
        return result;
    }


    @Override
    public <T> T reload(final T object, final Context ctx) throws ObjectNotFoundException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            throw new ObjectNotFoundException(String.valueOf(object));
        }
        return strategy.reload(object, ctx);
    }


    @Override
    public <T> T reload(final T object) throws ObjectNotFoundException
    {
        return reload(object, null);
    }


    @Override
    public <T> boolean isNew(final T object)
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            LOG.error(MSG_CANNOT_FIND_OBJECT_STRATEGY_FOR, object);
            return ObjectFacade.super.isNew(object);
        }
        return strategy.isNew(object);
    }


    @Override
    public <T> boolean isModified(final T object)
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            LOG.error(MSG_CANNOT_FIND_OBJECT_STRATEGY_FOR, object);
            return ObjectFacade.super.isModified(object);
        }
        return strategy.isModified(object);
    }


    @Override
    public <T> boolean isDeleted(final T object)
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            LOG.error(MSG_CANNOT_FIND_OBJECT_STRATEGY_FOR, object);
            return ObjectFacade.super.isDeleted(object);
        }
        return strategy.isDeleted(object);
    }


    @Override
    public <T> T clone(final T objectToClone, final Context ctx) throws ObjectCloningException
    {
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(objectToClone);
        if(strategy == null)
        {
            return ObjectFacade.super.clone(objectToClone, ctx);
        }
        return strategy.clone(objectToClone, ctx);
    }


    @Override
    public <T> Object getObjectId(final T object)
    {
        if(object == null)
        {
            return null;
        }
        final ObjectFacadeStrategy strategy = strategyRegistry.getStrategy(object);
        if(strategy == null)
        {
            LOG.error(MSG_CANNOT_FIND_OBJECT_STRATEGY_FOR, object);
            return ObjectFacade.super.getObjectId(object);
        }
        return strategy.getObjectId(object);
    }


    @Required
    public void setStrategyRegistry(final AbstractStrategyRegistry<ObjectFacadeStrategy, Object> strategyRegistry)
    {
        this.strategyRegistry = strategyRegistry;
    }


    /**
     * @deprecated since 6.5
     * @see #eventPublisher
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void setEventQueue(final CockpitEventQueue eventQueue)
    {
        this.eventQueue = eventQueue;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    protected CockpitGlobalEventPublisher getEventPublisher()
    {
        return eventPublisher;
    }


    @Required
    public void setEventPublisher(final CockpitGlobalEventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected void publishEvent(final String eventName, final Object object, final Context ctx)
    {
        if(isCockpitEventNotificationEnabled() && !isCockpitEventNotificationDisabledInCtx(ctx))
        {
            getEventPublisher().publish(eventName, object, ctx);
        }
    }


    /**
     * @deprecated since 6.5
     * @see com.hybris.cockpitng.dataaccess.util.impl.DefaultCockpitGlobalEventPublisher
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void populateEventContext(final Context source, final DefaultCockpitEvent destination)
    {
        if(source != null)
        {
            source.getAttributeNames().forEach(a -> destination.getContext().put(a, source.getAttribute(a)));
        }
    }


    protected boolean isCockpitEventNotificationEnabled()
    {
        return BooleanUtils
                        .isNotFalse(BooleanUtils.toBooleanObject(cockpitProperties.getProperty(COCKPITNG_CRUD_COCKPIT_EVENT_NOTIFICATION)));
    }


    protected boolean isCockpitEventNotificationDisabledInCtx(final Context ctx)
    {
        return ctx != null && BooleanUtils.isTrue((Boolean)ctx.getAttribute(ObjectFacade.CTX_PARAM_SUPPRESS_EVENT));
    }
}
