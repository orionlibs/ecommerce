/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.fulltextsearch;

import com.hybris.cockpitng.config.fulltextsearch.jaxb.FieldListType;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FieldType;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFullTextSearchConfigurationFallbackStrategy
                extends AbstractCockpitConfigurationFallbackStrategy<FulltextSearch>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFullTextSearchConfigurationFallbackStrategy.class);


    @Override
    public FulltextSearch loadFallbackConfiguration(final ConfigContext context, final Class<FulltextSearch> configurationType)
    {
        final FulltextSearch result = new FulltextSearch();
        result.setFieldList(new FieldListType());
        try
        {
            final Set<String> attributes = getMandatoryAttributes(context);
            if(CollectionUtils.isNotEmpty(attributes))
            {
                attributes.stream().map((attr) -> {
                    final FieldType fieldType = new FieldType();
                    fieldType.setName(attr);
                    return fieldType;
                }).forEach(result.getFieldList().getField()::add);
            }
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", FulltextSearch.class);
        }
        return result;
    }


    protected Set<String> getMandatoryAttributes(final ConfigContext context) throws TypeNotFoundException
    {
        return getMandatoryAttributes(getTypeFromContext(context));
    }


    protected String getTypeFromContext(final ConfigContext context)
    {
        final String type = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        if(StringUtils.isBlank(type))
        {
            throw new IllegalArgumentException("Configuration context does not contain 'type' attribute");
        }
        return type;
    }
}
