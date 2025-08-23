/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.search;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchByTypeAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, String>
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchByTypeAction.class);
    private static final String TYPE_OUTPUT_SOCKET = "type";
    @Resource
    private TypeFacade typeFacade;


    @Override
    public ActionResult<String> perform(final ActionContext<Object> ctx)
    {
        final ActionResult<String> result = new ActionResult<String>(ActionResult.SUCCESS);
        result.setData((String)ctx.getData());
        sendOutput(TYPE_OUTPUT_SOCKET, result.getData());
        return result;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        final Object data = ctx.getData();
        if(data instanceof String)
        {
            try
            {
                final DataType type = typeFacade.load((String)data);
                return type.isSearchable();
            }
            catch(final TypeNotFoundException e)
            {
                LOG.warn("Type not found: " + data);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Type not found: " + data, e);
                }
            }
        }
        return false;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        return null;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
