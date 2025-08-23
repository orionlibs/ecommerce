package de.hybris.platform.stock.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class StockLevelUpdateException extends SystemException
{
    public StockLevelUpdateException(String message)
    {
        super(message);
    }
}
