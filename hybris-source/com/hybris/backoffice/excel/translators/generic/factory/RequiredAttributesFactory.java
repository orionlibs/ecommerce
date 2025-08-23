package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

public interface RequiredAttributesFactory
{
    RequiredAttribute create(AttributeDescriptorModel paramAttributeDescriptorModel);


    RequiredAttribute create(ComposedTypeModel paramComposedTypeModel);
}
