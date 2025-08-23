package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.jalo.header.HeaderDescriptor;

public interface ImpExHeaderError extends ImpExError
{
    HeaderDescriptor getHeader();


    Exception getException();
}
