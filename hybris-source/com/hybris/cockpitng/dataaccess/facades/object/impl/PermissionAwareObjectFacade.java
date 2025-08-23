/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object.impl;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningPermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreatePermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletePermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectReadPermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavePermissionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extends {@link DefaultObjectFacade} and adds permission checks on each action.
 */
public class PermissionAwareObjectFacade extends DefaultObjectFacade
{
    private PermissionFacade permissionFacade;
    private LabelService labelService;


    @Override
    public <T> T load(final String id, final Context ctx) throws ObjectNotFoundException
    {
        final T object = super.load(id, ctx);
        final String type = getTypeFacade().getType(object);
        if(type != null && !permissionFacade.canReadType(type))
        {
            throw new ObjectReadPermissionException(id);
        }
        return object;
    }


    @Override
    public <T> void delete(final T object, final Context ctx) throws ObjectDeletionException
    {
        if(!permissionFacade.canRemoveInstance(object))
        {
            throw new ObjectDeletePermissionException(String.valueOf(object));
        }
        super.delete(object, ctx);
    }


    @Override
    public <T> ObjectFacadeOperationResult<T> delete(final Collection<T> objects, final Context ctx)
    {
        final boolean canDelete = objects.stream().allMatch(permissionFacade::canRemoveInstance);
        if(canDelete)
        {
            return super.delete(objects, ctx);
        }
        final ObjectFacadeOperationResult<T> result = new ObjectFacadeOperationResult<>();
        objects.stream()
                        .forEach(object -> result.addFailedObject(object, new ObjectDeletePermissionException(String.valueOf(object))));
        return result;
    }


    @Override
    public <T> T create(final String typeId, final Context ctx) throws ObjectCreationException
    {
        if(!permissionFacade.canCreateTypeInstance(typeId))
        {
            throw new ObjectCreatePermissionException(typeId);
        }
        return super.create(typeId, ctx);
    }


    @Override
    public <T> T clone(final T objectToClone, final Context ctx) throws ObjectCloningException
    {
        if(!permissionFacade.canCreateTypeInstance(typeFacade.getType(objectToClone)))
        {
            throw new ObjectCloningPermissionException(labelService.getObjectLabel(objectToClone));
        }
        return super.clone(objectToClone, ctx);
    }


    @Override
    public <T> T save(final T objectToSave, final Context ctx) throws ObjectSavingException
    {
        final boolean saveAllowed;
        if(!isNew(objectToSave))
        {
            saveAllowed = permissionFacade.canChangeInstance(objectToSave);
        }
        else
        {
            final String type = getTypeFacade().getType(objectToSave);
            saveAllowed = permissionFacade.canCreateTypeInstance(type) && permissionFacade.canChangeInstance(objectToSave);
        }
        if(!saveAllowed)
        {
            throw new ObjectSavePermissionException(labelService.getObjectLabel(objectToSave), Collections.<String>emptyList());
        }
        final Set<String> notPermittedProperties = new LinkedHashSet<>();
        for(final String property : getModifiedProperties(objectToSave, ctx))
        {
            if(!permissionFacade.canChangeInstanceProperty(objectToSave, property))
            {
                notPermittedProperties.add(property);
            }
        }
        if(CollectionUtils.isNotEmpty(notPermittedProperties))
        {
            throw new ObjectSavePermissionException(labelService.getObjectLabel(objectToSave), notPermittedProperties);
        }
        return super.save(objectToSave, ctx);
    }


    /**
     * Tries to identify modified properties.
     *
     * @param objectToSave
     *           The object with potentially modified properties.
     * @param ctx
     *           An additional {@link Context}.
     */
    protected <T> Set<String> getModifiedProperties(final T objectToSave, final Context ctx)
    {
        return Collections.<String>emptySet();
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    /**
     * Active type facade.
     *
     * @return currently used type facade.
     */
    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
