/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeCollectionNodes;
import com.hybris.cockpitng.core.config.impl.jaxb.treecollection.TreeNode;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTreeCollectionConfigFallbackStrategy extends
                AbstractCockpitConfigurationFallbackStrategy<TreeCollectionNodes>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFlowConfigFallbackStrategy.class);


    @Override
    public TreeCollectionNodes loadFallbackConfiguration(final ConfigContext context,
                    final Class<TreeCollectionNodes> configurationType)
    {
        final String attributeType = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        try
        {
            final DataType dataType = loadType(attributeType);
            final TreeCollectionNodes fallbackConfig = new TreeCollectionNodes();
            for(final DataAttribute dataAttribute : dataType.getAttributes())
            {
                final TreeNode node = new TreeNode();
                node.setAttribute(dataAttribute.getQualifier());
                fallbackConfig.getNode().add(node);
            }
            return fallbackConfig;
        }
        catch(final TypeNotFoundException ex)
        {
            LOG.error("Cannot create fallback configuration for TreeCollectionNodes", ex);
            return null;
        }
    }
}
