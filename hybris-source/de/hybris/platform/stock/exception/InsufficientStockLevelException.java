package de.hybris.platform.stock.exception;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class InsufficientStockLevelException extends BusinessException
{
    public InsufficientStockLevelException(String message)
    {
        super(message);
    }
}
