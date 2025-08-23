package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;

public interface HeaderCellTranslator
{
    boolean isValid(String paramString);


    AbstractDescriptor.DescriptorParams translate(AbstractDescriptor paramAbstractDescriptor, String paramString) throws HeaderValidationException;
}
