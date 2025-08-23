package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.jalo.imp.ValueLine;

public interface ImpExValueLineError extends ImpExError
{
    ValueLine getLine();


    ImpExLineErrorType getErrorType();
}
