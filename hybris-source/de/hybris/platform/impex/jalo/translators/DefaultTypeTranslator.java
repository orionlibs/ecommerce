package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;

public class DefaultTypeTranslator extends AbstractTypeTranslator
{
    public AbstractDescriptor.DescriptorParams translate(AbstractDescriptor decriptor, String expr) throws HeaderValidationException
    {
        return (AbstractDescriptor.DescriptorParams)AbstractDescriptor.parseHeaderDescriptor(expr);
    }


    public boolean isValid(String cell)
    {
        return true;
    }
}
