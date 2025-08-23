/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractStatefulAction<INPUT, OUTPUT> extends AbstractComponentWidgetAdapterAware
                implements CockpitAction<INPUT, OUTPUT>
{
    /**
     * Reads a value from parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param actionContext action context
     * @param key value key
     * @param <V> value type
     * @return value found or <code>null</code>
     */
    protected <V> V getValue(final ActionContext<INPUT> actionContext, final String key)
    {
        return restoreModelValue(actionContext, key);
    }


    /**
     * Puts a value to parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param actionContext action context
     * @param key value key
     * @param value value to be put
     */
    protected void setValue(final ActionContext<INPUT> actionContext, final String key, final Object value)
    {
        storeModelValue(actionContext, key, value);
    }


    /**
     * Reads a value from parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param context action context
     * @param key value key
     * @param <V> value type
     * @return value found or <code>null</code>
     */
    protected static <V> V restoreModelValue(final ActionContext<?> context, final String key)
    {
        final WidgetModel widgetModel = (WidgetModel)context.getParameter(ActionContext.PARENT_WIDGET_MODEL);
        if(widgetModel == null)
        {
            return null;
        }
        final Map<String, Object> actionValues = widgetModel.getValue(key, Map.class);
        if(actionValues == null)
        {
            return null;
        }
        final String uid = getActionUID(context);
        return (V)actionValues.get(uid);
    }


    /**
     * Puts a value to parent's widget model.
     * <P>
     * Method is resistant to situation when many instances of a particular action used in single parent widgets.
     *
     * @param context action context
     * @param key value key
     * @param value value to be put
     */
    protected static void storeModelValue(final ActionContext<?> context, final String key, final Object value)
    {
        final WidgetModel widgetModel = (WidgetModel)context.getParameter(ActionContext.PARENT_WIDGET_MODEL);
        Map<String, Object> actionValues = widgetModel.getValue(key, Map.class);
        if(actionValues == null)
        {
            actionValues = new HashMap();
            widgetModel.setValue(key, actionValues);
        }
        final String uid = getActionUID(context);
        actionValues.put(uid, value);
    }


    /**
     * Reads unique identity of action.
     *
     * @param context action context
     * @return unique identity of action
     */
    protected static String getActionUID(final ActionContext<?> context)
    {
        return ObjectUtils.toString(context.getParameter(ActionContext.ACTION_UID));
    }
}
