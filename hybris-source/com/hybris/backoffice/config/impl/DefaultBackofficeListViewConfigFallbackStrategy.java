/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.config.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultListViewConfigFallbackStrategy;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.type.ViewAttributeDescriptorModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Strategy for creating default Backoffice fallback configuration for List View.
 */
public class DefaultBackofficeListViewConfigFallbackStrategy extends DefaultListViewConfigFallbackStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeListViewConfigFallbackStrategy.class);
    private TypeService typeService;


    @Override
    public ListView loadFallbackConfiguration(final ConfigContext context, final Class<ListView> configurationType)
    {
        final ListView configuration = super.loadFallbackConfiguration(context, configurationType);
        final String typeCode = context.getAttribute(DefaultConfigContext.CONTEXT_TYPE);
        if(StringUtils.isNotBlank(typeCode))
        {
            try
            {
                final TypeModel typeForCode = getTypeService().getTypeForCode(typeCode);
                final Set<String> qualifiers = Sets.newTreeSet();
                if(typeForCode instanceof ViewTypeModel)
                {
                    qualifiers.addAll(((ViewTypeModel)typeForCode).getColumns().stream()
                                    .map(ViewAttributeDescriptorModel::getQualifier).collect(Collectors.toList()));
                }
                else if(typeForCode instanceof ComposedTypeModel)
                {
                    final Collection<DataAttribute> attributes = getTypeFacade().load(typeCode).getAttributes();
                    qualifiers
                                    .addAll(attributes.stream().filter(attribute -> !(attribute.getDefinedType() instanceof CollectionDataType))
                                                    .map(DataAttribute::getQualifier).collect(Collectors.toSet()));
                }
                if(!qualifiers.isEmpty())
                {
                    configuration.getColumn().removeIf(next -> qualifiers.contains(next.getQualifier()));
                }
                addColumnForEveryAttributeToConfiguration(qualifiers, configuration);
            }
            catch(final UnknownIdentifierException | TypeNotFoundException uie)
            {
                LOG.debug(uie.getMessage(), uie);
            }
        }
        return configuration;
    }


    public TypeService getTypeService()
    {
        return typeService;
    }


    @Required
    public void setTypeService(final TypeService typeService)
    {
        this.typeService = typeService;
    }
}
