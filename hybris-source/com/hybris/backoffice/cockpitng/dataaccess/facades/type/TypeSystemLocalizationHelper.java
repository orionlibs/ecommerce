/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;

/**
 * Provides type system localization
 */
public interface TypeSystemLocalizationHelper
{
    /**
     * Provides localization for {@link AttributeDescriptorModel}
     *
     * @param attributeDescriptorModel attribute to be localized
     * @param attrBuilder builder that will be updated with localized labels
     */
    void localizeAttribute(AttributeDescriptorModel attributeDescriptorModel, DataAttribute.Builder attrBuilder);


    /**
     * Provides localization for {@link TypeModel}
     *
     * @param platformType type to be localized
     * @param typeBuilder builder that will be updated with localized labels
     */
    void localizeType(TypeModel platformType, DataType.Builder typeBuilder);
}
