/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

/**
 * Compound visitor which uses other visitors to visit different types of UI elements.
 */
public class CompoundComponentsVisitor implements ComponentsVisitor
{
    private static final Logger LOG = LoggerFactory.getLogger(CompoundComponentsVisitor.class);
    private List<ComponentsVisitor> visitors;


    @Override
    public void register(final Component component)
    {
        getVisitors().forEach(v -> v.register(component));
    }


    @Override
    public void unRegister(final Component component)
    {
        getVisitors().forEach(v -> v.unRegister(component));
    }


    @Override
    public void initialize(final String typeCode, final WidgetInstanceManager wim, final DynamicForms dynamicForms)
    {
        getVisitors().forEach(v -> v.initialize(typeCode, wim, dynamicForms));
        if(LOG.isDebugEnabled())
        {
            LOG.debug(this.toString().concat(" initialized"));
        }
    }


    public List<ComponentsVisitor> getVisitors()
    {
        if(visitors == null)
        {
            LOG.warn("Compound visitor doesn't have any visitors");
            return Lists.newArrayList();
        }
        return visitors;
    }


    public void setVisitors(final List<ComponentsVisitor> visitors)
    {
        this.visitors = visitors;
    }
}
