/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.enumeration;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

/**
 * DefaultEnumerationValidator which allows to check whether the user has sufficient permissions for updating the data.
 * This Validator is used only when {@value EnumerationAction#PARAMETER_VALIDATOR_ID} parameter is not set.
 */
public class DefaultEnumerationValidator implements EnumerationValidator
{
    private PermissionFacade permissionFacade;
    private ObjectFacade objectFacade;


    @Override
    public boolean validate(final ActionContext<Collection<Object>> context)
    {
        final ObjectFacadeOperationResult<Object> reloadResult = getObjectFacade().reload(context.getData());
        return permissionFacade.canChangeInstancesProperty(reloadResult.getSuccessfulObjects(),
                        (String)context.getParameter(EnumerationAction.PARAMETER_QUALIFIER));
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


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
