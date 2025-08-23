/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.entity;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ENTITY_TYPE_PREFIX;

import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import org.springframework.beans.factory.annotation.Required;

public class LocalizedEntityTypeGenerator extends SingleEntityTypeGenerator
{
    private DescriptorFactory descriptorFactory;


    @Override
    protected boolean isApplicable(final IntegrationObjectItemModel item)
    {
        return containsLocalizedAttributes(item);
    }


    private boolean containsLocalizedAttributes(final IntegrationObjectItemModel item)
    {
        final TypeDescriptor typeDescriptor = getDescriptorFactory().createItemTypeDescriptor(item);
        return typeDescriptor.getAttributes().stream()
                        .anyMatch(TypeAttributeDescriptor::isLocalized);
    }


    @Override
    protected String generateEntityTypeName(final IntegrationObjectItemModel item)
    {
        return LOCALIZED_ENTITY_TYPE_PREFIX + item.getCode();
    }


    protected DescriptorFactory getDescriptorFactory()
    {
        return descriptorFactory;
    }


    @Required
    public void setDescriptorFactory(final DescriptorFactory descriptorFactory)
    {
        this.descriptorFactory = descriptorFactory;
    }
}
