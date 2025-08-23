/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.create;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Popup;

public class CreatePermissionActionRenderer extends DefaultActionRenderer<Object, Object>
{
    private static final String EDITOR_WRAPPER_SCLASS = "yw-custompopup-referenceeditor";
    private static final String REFERENCE_CHOOSER_POPUP_POSITION = "after_start";
    private static final String WIDGET_INSTANCE_MANAGER_ATTR = "widgetInstanceManager";
    private static final String POPUP_CTX_PARAM = "popup_component";


    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        super.render(parent, action, context, updateMode, listener);
        final Popup referenceChooserPopup = renderReferenceChooserPopup(parent, action, context, listener);
        context.setParameter(POPUP_CTX_PARAM, referenceChooserPopup);
    }


    @Override
    protected EventListener<? extends Event> createEventListener(final CockpitAction<Object, Object> action,
                    final ActionContext<Object> context, final ActionListener<Object> listener)
    {
        return new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                final Object popup = context.getParameter(POPUP_CTX_PARAM);
                if(popup instanceof Popup)
                {
                    ((Popup)popup).open(event.getTarget(), REFERENCE_CHOOSER_POPUP_POSITION);
                    Events.sendEvent("onFocusEditor", (Popup)popup, null);
                }
            }
        };
    }


    private Popup renderReferenceChooserPopup(final Component parent, final CockpitAction<Object, Object> action,
                    final ActionContext<Object> context, final ActionListener<Object> listener)
    {
        final Popup referenceChooserPopup = new Popup();
        final Div wrapperDiv = new Div();
        wrapperDiv.setSclass(EDITOR_WRAPPER_SCLASS);
        final Editor referenceEditor = new Editor();
        final Widgetslot parentWidgetslot = WidgetTreeUIUtils.getParentWidgetslot(parent);
        final WidgetInstanceManager instanceManager = (WidgetInstanceManager)parentWidgetslot
                        .getAttribute(WIDGET_INSTANCE_MANAGER_ATTR);
        referenceEditor.setWidgetInstanceManager(instanceManager);
        referenceEditor.setNestedObjectCreationDisabled(true);
        if(context.getData() instanceof String)
        {
            referenceEditor.setType(String.format("Reference(%s)", (String)context.getData()));
            referenceEditor.setParent(wrapperDiv);
            referenceEditor.addEventListener(Editor.ON_VALUE_CHANGED, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event)
                {
                    if(event.getData() != null)
                    {
                        context.setParameter(CreatePermissionAction.SELECTED_REFERENCE, event.getData());
                        perform(action, context, listener);
                        referenceChooserPopup.close();
                        referenceEditor.setValue(null);
                    }
                }
            });
            referenceEditor.afterCompose();
            referenceChooserPopup.appendChild(wrapperDiv);
            referenceChooserPopup.setParent(parent);
        }
        referenceChooserPopup.addEventListener("onFocusEditor", new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                referenceEditor.focus();
            }
        });
        return referenceChooserPopup;
    }
}
