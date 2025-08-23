/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node.factory.impl;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.config.explorertree.jaxb.Parameter;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import com.hybris.cockpitng.tree.node.factory.NodeFactory;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicNodeFactory implements NodeFactory<com.hybris.cockpitng.config.explorertree.jaxb.DynamicNode>
{
    public static final Logger LOG = LoggerFactory.getLogger(DynamicNodeFactory.class);


    @Override
    public NavigationNode createNavigationNode(final com.hybris.cockpitng.config.explorertree.jaxb.DynamicNode nodeConfig)
    {
        return createNavigationNode(nodeConfig, new DefaultCockpitContext());
    }


    @Override
    public NavigationNode createNavigationNode(final com.hybris.cockpitng.config.explorertree.jaxb.DynamicNode nodeConfig,
                    final CockpitContext context)
    {
        Validate.notNull(nodeConfig, "nodeConfig");
        final DynamicNode node = nodeConfig.getIndexingDepth() != null
                        ? new DynamicNode(nodeConfig.getId(), true, resolvePopulator(nodeConfig), nodeConfig.getIndexingDepth().intValue())
                        : new DynamicNode(nodeConfig.getId(), true, resolvePopulator(nodeConfig));
        node.setName(nodeConfig.getId());
        node.setNameLocKey(nodeConfig.getId());
        node.setExpandedByDefault(BooleanUtils.isTrue(nodeConfig.getExpandedByDefault()));
        final CockpitContext ctx = new DefaultCockpitContext();
        for(final Parameter parameter : nodeConfig.getDynamicNodeParameter())
        {
            ctx.setParameter(parameter.getName(), parameter.getValue());
        }
        ctx.addAllParameters(context);
        node.setContext(ctx);
        return node;
    }


    protected DynamicNodePopulator resolvePopulator(final com.hybris.cockpitng.config.explorertree.jaxb.DynamicNode nodeConfig)
    {
        final String beanId = nodeConfig.getPopulatorBeanId();
        final String className = nodeConfig.getPopulatorClassName();
        if(StringUtils.isNotBlank(className))
        {
            if(StringUtils.isNotBlank(beanId))
            {
                LOG.warn("Ambiguous populator definition for dynamic node {}, using class-name {}", nodeConfig.getId(), className);
            }
            return BackofficeSpringUtil.createClassInstance(className, DynamicNodePopulator.class);
        }
        else if(StringUtils.isNotBlank(beanId))
        {
            return BackofficeSpringUtil.getBean(beanId);
        }
        else
        {
            throw new IllegalStateException("Not defined populator for dynamic node ".concat(nodeConfig.getId()));
        }
    }
}
