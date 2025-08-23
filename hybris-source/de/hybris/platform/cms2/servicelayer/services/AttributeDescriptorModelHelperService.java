package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public interface AttributeDescriptorModelHelperService
{
    Class<?> getAttributeClass(AttributeDescriptorModel paramAttributeDescriptorModel);


    Class<?> getDeclaringEnclosingTypeClass(AttributeDescriptorModel paramAttributeDescriptorModel);
}
