/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.ymkt.recommendationwebservices.populators;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

/**
 * Populator for dropdown controls.
 */
public class OfferRecommendationDropdownComponentTypeAttributePopulator
                implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{
    @Override
    public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target)
    {
        target.setUri("/sapymktrecommendationwebservices/v1/data/offer/" + source.getQualifier());
        target.setPaged(false);
    }
}
