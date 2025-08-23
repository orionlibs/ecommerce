/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.edit;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    public static final String FORWARD_SELECTION_TO_SOCKET_KEY = "forwardSelectionToSocket";
    public static final String FORWARD_SELECTION_AS_KEY = "forwardSelectionAs";
    public static final String COLLECTION = "collection";
    public static final String LIST = "list";
    public static final String SET = "set";
    private static final Logger LOG = LoggerFactory.getLogger(EditAction.class);


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final Object data = ctx.getData();
        Collection<?> elements;
        if(data instanceof Collection)
        {
            elements = (Collection)data;
        }
        else
        {
            elements = Collections.singleton(data);
        }
        elements = convertToRequestedType(ctx, elements);
        final ActionResult<Object> result = new ActionResult<>(ActionResult.SUCCESS, elements);
        final Object forward = ctx.getParameter(FORWARD_SELECTION_TO_SOCKET_KEY);
        if(forward instanceof String && StringUtils.isNotBlank((CharSequence)forward))
        {
            result.addOutputSocketToSend((String)forward, elements);
        }
        return result;
    }


    protected Collection convertToRequestedType(final ActionContext<Object> ctx, final Collection<?> elements)
    {
        final Object cast = ctx.getParameter(FORWARD_SELECTION_AS_KEY);
        if(cast instanceof String)
        {
            switch(((String)cast).toLowerCase())
            {
                case LIST:
                    return Lists.newArrayList(elements);
                case SET:
                    return Sets.newHashSet(elements);
                case COLLECTION:
                    return elements;
                default:
                    LOG.warn("Unknown type of collection: '{}'. Using the passed value of type: '{}'", cast,
                                    elements.getClass().getCanonicalName());
                    return elements;
            }
        }
        return elements;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        final Object data = ctx.getData();
        if(data instanceof Collection)
        {
            return CollectionUtils.isNotEmpty((Collection)data);
        }
        return data != null;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }
}
