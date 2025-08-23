/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zul.Html;

/**
 * Example widget demonstrating WYSIWYG editor.
 */
public class ExampleWysiwygWidgetController extends DefaultWidgetController
{
    protected static final String WYSIWYG_EDITOR = "wysiwygEditor";
    protected static final String READ_MODEL = "readModel";
    private static final String INITIAL_VALUE = "Hello World!";
    private static final String MODEL_PROPERTY_KEY = "editorContent";
    private static final long serialVersionUID = 5071614506531034399L;
    protected transient Html container = null;


    @Override
    public ComponentInfo doBeforeCompose(final Page _page, final Component parent, final ComponentInfo compInfo)
    {
        final Map<Locale, Object> initialValue = new HashMap<>();
        initialValue.put(Locale.ENGLISH, INITIAL_VALUE);
        getModel().setValue(MODEL_PROPERTY_KEY, initialValue);
        return super.doBeforeCompose(_page, parent, compInfo);
    }


    @ViewEvent(componentID = WYSIWYG_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void editorValueChange(final Event event)
    {
        final String eventData = (String)event.getData();
        if(container != null)
        {
            container.setContent(eventData);
        }
    }


    @ViewEvent(componentID = READ_MODEL, eventName = Events.ON_CLICK)
    public void readValueFromModel()
    {
        if(container != null)
        {
            container.setContent(ObjectUtils.toString(getModel().getValue(MODEL_PROPERTY_KEY, Map.class).get(Locale.ENGLISH)));
        }
    }
}
