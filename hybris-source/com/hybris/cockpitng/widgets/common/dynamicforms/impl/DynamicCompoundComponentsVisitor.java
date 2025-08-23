/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compound visitor which gets from DynamicForms configuration dynamic visitors {@link DynamicForms#getVisitor()}, loads
 * them from spring context and uses them as its visitors.
 */
public class DynamicCompoundComponentsVisitor extends CompoundComponentsVisitor
{
    private static final Logger LOG = LoggerFactory.getLogger(DynamicCompoundComponentsVisitor.class);


    @Override
    public void initialize(final String typeCode, final WidgetInstanceManager wim, final DynamicForms dynamicForms)
    {
        setVisitors(getDynamicVisitors(dynamicForms));
        super.initialize(typeCode, wim, dynamicForms);
    }


    public static List<ComponentsVisitor> getDynamicVisitors(final DynamicForms dynamicForms)
    {
        final List<ComponentsVisitor> visitors = Lists.newArrayList();
        final Set<String> beans = dynamicForms.getVisitor().stream().map(dv -> dv.getBeanId()).collect(Collectors.toSet());
        beans.forEach(bean -> {
            final ComponentsVisitor visitor = BackofficeSpringUtil.getBean(bean, ComponentsVisitor.class);
            if(visitor != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Dynamic visitor with beanId=\"%s\" has been loaded", bean));
                }
                visitors.add(visitor);
            }
        });
        return visitors;
    }
}
