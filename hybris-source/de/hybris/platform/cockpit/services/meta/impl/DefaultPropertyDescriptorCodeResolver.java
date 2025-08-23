package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.PropertyDescriptorCodeResolver;

public class DefaultPropertyDescriptorCodeResolver implements PropertyDescriptorCodeResolver
{
    public String resolveClassificationPropertyCode(String classificationClassCode, String classificationAttributeCode)
    {
        String classCodeWithEscapedDots = ClassAttributePropertyDescriptor.escapeDots(classificationClassCode);
        String propertyCodeWithEscapedDots = ClassAttributePropertyDescriptor.escapeDots(classificationAttributeCode);
        return classCodeWithEscapedDots + "." + classCodeWithEscapedDots;
    }
}
