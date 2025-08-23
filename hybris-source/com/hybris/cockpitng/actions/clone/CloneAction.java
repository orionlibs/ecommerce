/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.clone;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCloningException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import java.util.Collection;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CloneAction supports cloning incoming object.
 */
public class CloneAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    public static final String SOCKET_OUTPUT_CLONED_OBJECT = "clonedObject";
    private static final Logger LOG = LoggerFactory.getLogger(CloneAction.class);
    @Resource
    private TypeFacade typeFacade;
    @Resource
    private ObjectFacade objectFacade;
    @Resource
    private PermissionFacade permissionFacade;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final Object objectToClone = resolveObjectToClone(ctx);
        if(objectToClone == null)
        {
            LOG.warn("Object to be cloned is null.");
            return new ActionResult<>(ActionResult.ERROR, null);
        }
        try
        {
            final Object clonedObject = objectFacade.clone(objectToClone);
            sendOutput(SOCKET_OUTPUT_CLONED_OBJECT, clonedObject);
            return new ActionResult<>(ActionResult.SUCCESS, clonedObject);
        }
        catch(final ObjectCloningException objectCloningException)
        {
            LOG.warn(objectCloningException.getMessage(), objectCloningException);
            return new ActionResult<>(ActionResult.ERROR, objectToClone);
        }
    }


    protected Object resolveObjectToClone(final ActionContext<Object> ctx)
    {
        final Object ctxData = ctx.getData();
        if(ctxData instanceof Collection && !((Collection)ctxData).isEmpty())
        {
            return ((Collection)ctxData).iterator().next();
        }
        else
        {
            return ctxData;
        }
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        if(isDifferentThanOneObject(ctx))
        {
            return false;
        }
        final Object objectToClone = resolveObjectToClone(ctx);
        if(objectToClone == null)
        {
            return false;
        }
        final String type = getTypeFacade().getType(objectToClone);
        final boolean typeInstanceCreationAllowed = isTypeInstanceCreationAllowed(type);
        final boolean isNotNew = !getObjectFacade().isNew(objectToClone);
        final boolean isNotModified = !getObjectFacade().isModified(objectToClone);
        final boolean canChangeType = getPermissionFacade().canChangeType(type);
        final boolean canChangeInstance = getPermissionFacade().canChangeInstance(objectToClone);
        return typeInstanceCreationAllowed && isNotNew && isNotModified && canChangeType && canChangeInstance;
    }


    protected boolean isDifferentThanOneObject(final ActionContext<Object> ctx)
    {
        final Object ctxData = ctx.getData();
        return ctxData instanceof Collection && ((Collection)ctxData).size() != 1;
    }


    protected boolean isTypeInstanceCreationAllowed(final String typeCode)
    {
        if(StringUtils.isNotBlank(typeCode))
        {
            try
            {
                final DataType type = getTypeFacade().load(typeCode);
                return canCreateInstance(type);
            }
            catch(final TypeNotFoundException typeNotFoundException)
            {
                LOG.warn("Type not found: ".concat(typeCode), typeNotFoundException);
            }
        }
        return false;
    }


    protected boolean canCreateInstance(final DataType type)
    {
        final boolean instanceCreationAllowed = getPermissionFacade().canCreateTypeInstance(type.getCode());
        return instanceCreationAllowed && !type.isSingleton();
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }
}
