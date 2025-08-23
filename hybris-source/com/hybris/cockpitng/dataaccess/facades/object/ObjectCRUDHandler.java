/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import java.util.Collection;
import java.util.Objects;

/**
 * Interface providing methods for basic CRUD operations. Don't implement this interface directly but use one of its
 * sub-interfaces.
 */
public interface ObjectCRUDHandler
{
    String OBJECTS_DELETED_EVENT = "objectsDeleted";
    String OBJECT_CREATED_EVENT = "objectCreated";
    String OBJECTS_UPDATED_EVENT = "objectsUpdated";


    /**
     * Gets an object instance by it's id.
     *
     * @param id
     *           for which the object is needed
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return the object instance for given id loaded with attributes defined in context
     * @throws ObjectNotFoundException
     *            if no object with the given id could be found
     */
    <T> T load(String id, Context ctx) throws ObjectNotFoundException;


    /**
     * Deletes given object from the system.
     *
     * @param object
     *           which should get deleted
     * @param ctx
     *           context specifying application settings
     * @throws ObjectDeletionException
     *            if the given object could not be deleted
     */
    <T> void delete(final T object, final Context ctx) throws ObjectDeletionException;


    /**
     * Deletes many given objects from the system.
     * Default implementation calls {@link #delete(Object, Context)} in a loop and does not publish
     * {@link #OBJECTS_DELETED_EVENT} event.
     *
     * @param objects
     *           which should get deleted
     * @param ctx
     * @return Result of the operation containing both successful and failed objects. Should never be null.
     */
    default <T> ObjectFacadeOperationResult<T> delete(final Collection<T> objects, final Context ctx)
    {
        final ObjectFacadeOperationResult<T> result = new ObjectFacadeOperationResult<>();
        for(final T object : objects)
        {
            try
            {
                delete(object, ctx);
                result.addSuccessfulObject(object);
            }
            catch(final ObjectDeletionException e)
            {
                result.addFailedObject(object, e);
            }
        }
        return result;
    }


    /**
     * Creates a new object in the system using given object data.
     *
     * @param typeId
     *           the data for creation of object in system
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return the created object with attributes loaded as specified in context
     * @throws ObjectCreationException
     *            if the object couldn't be created, like an object with the specified id already exists
     */
    <T> T create(String typeId, Context ctx) throws ObjectCreationException;


    /**
     * Persist an object by given object data.
     *
     * @param object
     *           the data for updating an object in system
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return the updated object
     * @throws ObjectSavingException
     *            if the object couldn't be persisted, like a unique attribute was updated to be non-unique
     */
    <T> T save(T object, Context ctx) throws ObjectSavingException;


    /**
     * Persist objects by given collection data.
     *
     * @param objects
     *           the data for updating objects in system.
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings.
     * @return ObjectFacadeOperationResult object.
     */
    default <T> ObjectFacadeOperationResult<T> save(final Collection<T> objects, final Context ctx)
    {
        final ObjectFacadeOperationResult<T> result = new ObjectFacadeOperationResult<>();
        for(final T object : objects)
        {
            try
            {
                result.addSuccessfulObject(save(object, ctx));
            }
            catch(final ObjectSavingException exception)
            {
                result.addFailedObject(object, exception);
            }
        }
        return result;
    }


    /**
     * Gets a fresh instance of the given object. Usually it re-fetches a domain object from the persistence layer, but
     * could also just return the same instance, if the object is not managed.
     *
     * @param object
     *           The object that should be refreshed.
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return A new instance of the given object, or the object itself, if it's not managed.
     * @throws ObjectNotFoundException
     *            If the current object couldn't be found (could be deleted in the mean time).
     */
    <T> T reload(T object, Context ctx) throws ObjectNotFoundException;


    /**
     * Gets a fresh instance of the given collection of objects. Usually it re-fetches a domain object from the persistence
     * layer, but could also just return the same instance, if the object is not managed. If an item in collection couldn't
     * be found (could be deleted in the mean time) then item won't be added to collection
     *
     * @param objects
     *           The collection of object that should be refreshed.
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings
     * @return Two collections: one with objects that succeeded reloading (new instances of the given objects, or the object
     *         itself, if it's not managed) and second collection with objects that failed to be reloaded.
     */
    default <T> ObjectFacadeOperationResult<T> reload(final Collection<T> objects, final Context ctx)
    {
        final ObjectFacadeOperationResult<T> result = new ObjectFacadeOperationResult<>();
        if(objects == null)
        {
            return result;
        }
        for(final T objectToReload : objects)
        {
            try
            {
                result.addSuccessfulObject(reload(objectToReload, ctx));
            }
            catch(final ObjectNotFoundException e)
            {
                result.addFailedObject(objectToReload, e);
            }
        }
        return result;
    }


    /**
     * Tells if an object has been created and has not been saved yet.
     *
     * @param object
     *           the object to check.
     * @param <T>
     *           object type.
     * @return true if an object has been created and has not been saved yet.
     */
    default <T> boolean isNew(final T object)
    {
        return false;
    }


    /**
     * Tells if an object is not persisted or has unsaved changes.
     *
     * @param object
     *           the object to check.
     * @param <T>
     *           object type.
     * @return true if an object has been modified.
     */
    default <T> boolean isModified(final T object)
    {
        return false;
    }


    /**
     * Tells if an object was once persisted, but now is deleted
     *
     * @param object
     *           the object to check.
     * @param <T>
     *           object type.
     * @return true if an object has been deleted
     */
    default <T> boolean isDeleted(final T object)
    {
        return false;
    }


    /**
     * Clone an object by given object data.
     *
     * @param objectToClone
     *           - source object to be cloned.
     * @param ctx
     *           context specifying the loaded attributes of the returned object as well as application settings.
     * @return ObjectFacadeOperationResult object.
     * @return cloned object.
     * @throws ObjectCloningException
     *            if cannot perform the clone.
     */
    default <T> T clone(final T objectToClone, final Context ctx) throws ObjectCloningException
    {
        return objectToClone;
    }


    /**
     * @param object
     *           The object which identifier should be returned
     * @return object identifier
     */
    default <T> Object getObjectId(final T object)
    {
        return Objects.hashCode(object);
    }
}
