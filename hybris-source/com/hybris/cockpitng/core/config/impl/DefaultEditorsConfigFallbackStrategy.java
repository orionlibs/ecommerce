/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.EditorGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.EditorProperty;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Editors;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEditorsConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<Editors>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorsConfigFallbackStrategy.class);
    private static final String EDITOR_GROUP = "common";


    @Override
    public Editors loadFallbackConfiguration(final ConfigContext context, final Class<Editors> configurationType)
    {
        Editors fallbackEditorsConfig = null;
        try
        {
            final String typeCode = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
            final Set<String> attributes = getAttributes(typeCode, MANDATORY, UNIQUE);
            if(CollectionUtils.isNotEmpty(attributes))
            {
                fallbackEditorsConfig = new Editors();
                final List<EditorGroup> group = fallbackEditorsConfig.getGroup();
                final EditorGroup mandatoryGroup = new EditorGroup();
                mandatoryGroup.setQualifier(EDITOR_GROUP);
                final List<EditorProperty> properties = mandatoryGroup.getProperty();
                for(final String qualifier : attributes)
                {
                    final EditorProperty property = new EditorProperty();
                    property.setQualifier(qualifier);
                    property.setLabel(qualifier);
                    properties.add(property);
                }
                group.add(mandatoryGroup);
            }
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
}
