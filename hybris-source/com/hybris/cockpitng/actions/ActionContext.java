/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import java.util.Map;

/**
 * A context object given to a cockpit action to perform.
 *
 * @param <I>
 *           input type of the action
 */
public class ActionContext<I> extends DefaultCockpitContext
{
    public static final String VIEW_MODE_PARAM = "viewMode";
    public static final String VIEWMODE_ICONONLY = "iconOnly";
    public static final String VIEWMODE_ICONANDTEXT = "iconAndText";
    public static final String VIEWMODE_TEXTONLY = "textOnly";
    public static final String VIEWMODE_HIDDEN = "hidden";
    public static final String PARENT_WIDGET_MODEL = "parentWidgetModel";
    public static final String ACTION_UID = "actionUID";
    private final transient Map<String, Object> labels;
    private final transient ActionDefinition definition;
    private String triggerOnKeys;
    private transient I data;


    public ActionContext(final I data, final ActionDefinition definition, final Map<String, Object> parameters,
                    final Map<String, Object> labels)
    {
        super(parameters);
        this.data = data;
        this.definition = definition;
        this.labels = labels;
    }


    public ActionContext(final ActionContext<I> context)
    {
        super(context.getParameters());
        this.data = context.getData();
        this.definition = context.definition;
        this.labels = context.labels;
    }


    public I getData()
    {
        return data;
    }


    public void setData(final I data)
    {
        this.data = data;
    }


    public boolean isHidden()
    {
        return VIEWMODE_HIDDEN.equalsIgnoreCase((String)getParameter(VIEW_MODE_PARAM));
    }


    public boolean isShowIcon()
    {
        return !isHidden() && (isShowIconAndText() || (VIEWMODE_ICONONLY.equalsIgnoreCase((String)getParameter(VIEW_MODE_PARAM))));
    }


    public boolean isShowText()
    {
        return !isHidden() && (isShowIconAndText() || (VIEWMODE_TEXTONLY.equalsIgnoreCase((String)getParameter(VIEW_MODE_PARAM))));
    }


    public boolean isShowIconAndText()
    {
        return !isHidden() && VIEWMODE_ICONANDTEXT.equalsIgnoreCase((String)getParameter(VIEW_MODE_PARAM));
    }


    public String getLabel(final String key)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key);
    }


    public String getLabel(final String key, final Object[] args)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key, args);
    }


    public String getCode()
    {
        return this.definition.getCode();
    }


    public String getName()
    {
        return this.definition.getName();
    }


    public String getIconUri()
    {
        return this.definition.getIconUri();
    }


    public String getIconDisabledUri()
    {
        return this.definition.getIconDisabledUri();
    }


    public String getIconHoverUri()
    {
        return this.definition.getIconHoverUri();
    }


    public String getTriggerOnKeys()
    {
        return triggerOnKeys;
    }


    public void setTriggerOnKeys(final String triggerOnKeys)
    {
        this.triggerOnKeys = triggerOnKeys;
    }


    public ActionDefinition getDefinition()
    {
        return definition;
    }
}
