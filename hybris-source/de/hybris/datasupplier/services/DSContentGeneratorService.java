package de.hybris.datasupplier.services;

import de.hybris.datasupplier.exceptions.DSContentGenerationException;

public interface DSContentGeneratorService
{
    String generateContent() throws DSContentGenerationException;
}
