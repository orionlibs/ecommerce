package de.hybris.datasupplier.services;

import de.hybris.datasupplier.exceptions.DSSenderException;

public interface DSSenderService
{
    boolean send(String paramString) throws DSSenderException;
}
