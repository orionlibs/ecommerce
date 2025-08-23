/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object;

import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import java.util.Collection;

/**
 * Facade for retrieval and modifying of objects.
 */
public interface ObjectFacade extends ObjectCRUDHandler
{
    String CTX_PARAM_SUPPRESS_EVENT = "suppress_event";
    /**
     * @deprecated since 6.7 use {@link #CTX_PARAM_SUPPRESS_EVENT}. Since 6.7 it is not used and will be removed.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    String CTX_DISABLE_CRUD_COCKPIT_EVENT_NOTIFICATION = CTX_PARAM_SUPPRESS_EVENT;


    /**
     * Gets an object instance by it's id.
     *
     * @param id
     *           The unique qualifier of the object.
     * @throws ObjectNotFoundException
     *            if no object with the given id could be found.
     * @return loaded object instance for given id
     */
    <T> T load(String id) throws ObjectNotFoundException;


    /**
     * Deletes an object from system.
     *
     * @param object
     *           The object which should be deleted.
     * @throws ObjectDeletionException
     *            if the given object could not be deleted.
     */
    <T> void delete(T object) throws ObjectDeletionException;


    /**
     * Deletes many objects from the system.
     *
     * @param objects
     *           Objects which should be deleted.
     * @return Result of the operation. Should never be null.
     */
    default <T> ObjectFacadeOperationResult<T> delete(final Collection<T> objects)
    {
        return delete(objects, new DefaultContext());
    }


    /**
     * Creates a new object in the system using given object data.
     *
     * @param typeId
     *           The type qualifier specifying the type of which the new object is an instance.
     * @return The newly created object.
     * @throws ObjectCreationException
     *            if the object couldn't be created, like an object with the specified id already exists.
     */
    <T> T create(String typeId) throws ObjectCreationException;


    /**
     * Persist an object by given object data.
     *
     * @param objectToSave
     *           An object, on which the save action should be applied (usually persisting in a DBS)
     * @return The updated object, if saving it was successful.
     * @throws ObjectSavingException
     *            if the object couldn't be persisted, like a unique attribute was updated to be non-unique.
     */
    <T> T save(final T objectToSave) throws ObjectSavingException;


    /**
     * Persist objects by given collection data.
     *
     * @param objects
     *           the data for updating objects in system.
     * @return ObjectFacadeOperationResult object.
     */
    default <T> ObjectFacadeOperationResult<T> save(final Collection<T> objects)
    {
        return save(objects, new DefaultContext());
    }


    /**
     * Gets a fresh instance of the given object. Usually it re-fetches a domain object from the persistence layer, but
     * could also just return the same instance, if the object is not managed.
     *
     * @param object
     *           The object that should be refreshed.
     * @return A new instance of the given object, or the object itself, if it's not managed.
     * @throws ObjectNotFoundException
     *            If the current object couldn't be found (could be deleted in the mean time).
     */
    <T> T reload(T object) throws ObjectNotFoundException;


    /**
     * Gets a fresh instance of the given collection of objects. Usually it re-fetches a domain object from the persistence
     * layer, but could also just return the same instance, if the object is not managed. If an item in collection couldn't
     * be found (could be deleted in the mean time) then item won't be added to collection
     *
     * @param objects
     *           The collection of object that should be refreshed.
     * @return Two collections: one with objects that succeeded reloading (new instances of the given objects, or the object
     *         itself, if it's not managed) and second collection with objects that failed to be reloaded.
     */
    default <T> ObjectFacadeOperationResult<T> reload(final Collection<T> objects)
    {
        return reload(objects, new DefaultContext());
    }


    /**
     * Clone an object by given object data.
     *
     * @param objectToClone
     *           - source object to be cloned.
     * @return cloned object.
     * @throws ObjectCloningException
     *            if cannot perform the clone.
     */
    default <T> T clone(final T objectToClone) throws ObjectCloningException
    {
        return clone(objectToClone, new DefaultContext());
    }


    /**
     * Checks if object is persisted
     * @param object object to check
     * @return true if object was persisted, false if not
     */
    default boolean isPersisted(final Object object)
    {
        return !isNew(object) && !isModified(object);
    }
}
