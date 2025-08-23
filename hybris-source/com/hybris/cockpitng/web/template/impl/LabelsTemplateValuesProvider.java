/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

/**
 * Provides all localized labels under {@link #VALUE_NAME_LABELS}:
 * <ul>
 * <li>{@link #VALUE_NAME_LOCAL}: local widgets labels</li>
 * <li>{@link #VALUE_NAME_GLOBAL}: global labels</li>
 * </ul>
 * <p>
 * Labels are available as nested map elements (i.e. <code>$labels.local.some.label.key</code>
 */
public class LabelsTemplateValuesProvider implements TemplateValuesProvider
{
    private static final String VALUE_NAME_LABELS = "labels";
    private static final String VALUE_NAME_LOCAL = "local";
    private static final String VALUE_NAME_GLOBAL = "global";
    private WidgetLibUtils widgetLibUtils;
    private CockpitComponentDefinitionService componentDefinitionService;


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        final Map<String, Map<String, Object>> labels = new HashMap<>();
        labels.put(VALUE_NAME_GLOBAL, Labels.getSegmentedLabels());
        if(context instanceof Component)
        {
            Component cmp = (Component)context;
            while(!(cmp instanceof Widgetslot) && cmp != null)
            {
                cmp = cmp.getParent();
            }
            if(cmp != null)
            {
                final Map<String, Object> local = (Map<String, Object>)cmp.getAttribute(CockpitWidgetEngine.LABELS_PARAM);
                labels.put(VALUE_NAME_LOCAL, local);
            }
        }
        return Collections.singletonMap(VALUE_NAME_LABELS, labels);
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    protected CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return componentDefinitionService;
    }


    @Required
    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }
}
