package de.hybris.platform.stock.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class StockLevelNotFoundException extends SystemException
{
    public StockLevelNotFoundException(String message)
    {
        super(message);
    }
}
