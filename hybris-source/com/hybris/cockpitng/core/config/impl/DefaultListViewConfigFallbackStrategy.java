/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListViewConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<ListView>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListViewConfigFallbackStrategy.class);


    @Override
    public ListView loadFallbackConfiguration(final ConfigContext context, final Class<ListView> configurationType)
    {
        ListView fallbackEditorsConfig = null;
        try
        {
            final String typeCode = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
            fallbackEditorsConfig = new ListView();
            final Set<String> attributes = getAttributes(typeCode, MANDATORY, UNIQUE);
            addColumnForEveryAttributeToConfiguration(attributes, fallbackEditorsConfig);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            LOG.warn(e.getMessage());
        }
        return fallbackEditorsConfig;
    }


    protected void addColumnForEveryAttributeToConfiguration(final Set<String> attributes, final ListView configuration)
    {
        if(CollectionUtils.isNotEmpty(attributes))
        {
            final List<ListColumn> columns = configuration.getColumn();
            for(final String qualifier : attributes)
            {
                final ListColumn column = new ListColumn();
                column.setQualifier(qualifier);
                columns.add(column);
            }
        }
    }
}
