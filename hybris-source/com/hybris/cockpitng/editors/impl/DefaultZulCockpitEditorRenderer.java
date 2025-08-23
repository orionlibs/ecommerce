/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultZulCockpitEditorRenderer<T> extends AbstractCockpitEditorRenderer<T>
{
    @Override
    public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
    {
        final Map<Object, Object> parameters = Maps.newHashMap();
        fillParametersMap(context, parameters);
        final String editorZul = context.getParameter("componentResourcePath") + "/" + context.getViewSrc();
        final Component components = Executions.createComponents(editorZul, parent, parameters);
        components.addEventListener("onEditorValueChanged", new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                listener.onValueChanged((T)event.getData());
            }
        });
        components.addEventListener("onEditorEvent", new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                final Object data = event.getData();
                if(data instanceof String)
                {
                    listener.onEditorEvent((String)data);
                }
            }
        });
    }


    protected void fillParametersMap(final EditorContext<T> context, final Map<Object, Object> parameters)
    {
        parameters.put("initialValue", context.getInitialValue());
    }
}
