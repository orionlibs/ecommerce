package de.hybris.platform.impex.jalo;

public interface ErrorHandler
{
    RESULT handleError(ImpExException paramImpExException, ImpExReader paramImpExReader);
}
