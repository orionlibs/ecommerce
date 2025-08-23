/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.delete;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class DeletePermissionAction implements CockpitAction<Object, Object>
{
    private static final String NEEDS_CONFIRMATION = "needsConfirmation";
    private static final String DELETE_CONFIRM_LABEL_KEY = "delete.confirm";


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        return ctx.getLabel(DELETE_CONFIRM_LABEL_KEY);
    }


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final List<Object> permissionsToRemove = new ArrayList<Object>();
        if(ctx.getData() instanceof Collection)
        {
            permissionsToRemove.addAll((Collection<Object>)ctx.getData());
        }
        else
        {
            permissionsToRemove.add(ctx.getData());
        }
        return new ActionResult<Object>(ActionResult.SUCCESS, permissionsToRemove);
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        final Object actionData = ctx.getData();
        if(actionData instanceof Collection && (CollectionUtils.isNotEmpty((Collection)ctx.getData())))
        {
            for(final Object object : (Collection)ctx.getData())
            {
                if(object instanceof PermissionInfo && isNotInherited((PermissionInfo)object))
                {
                    return true;
                }
            }
        }
        else if(actionData instanceof PermissionInfo)
        {
            return isNotInherited((PermissionInfo)actionData);
        }
        return false;
    }


    private boolean isNotInherited(final PermissionInfo permissionInfo)
    {
        for(final Permission permission : permissionInfo.getPermissions())
        {
            if(!permission.isInherited())
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return Boolean.valueOf((String)ctx.getParameter(NEEDS_CONFIRMATION));
    }
}
