package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;

public class DefaultAttributeTranslator extends AbstractAttributeTranslator
{
    public boolean isValid(String cell)
    {
        return true;
    }


    public AbstractDescriptor.DescriptorParams translate(AbstractDescriptor descr, String expr) throws HeaderValidationException
    {
        return (AbstractDescriptor.DescriptorParams)AbstractDescriptor.parseColumnDescriptor(expr);
    }
}
