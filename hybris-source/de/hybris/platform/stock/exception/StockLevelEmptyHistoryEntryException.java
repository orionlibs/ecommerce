package de.hybris.platform.stock.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;

public class StockLevelEmptyHistoryEntryException extends SystemException
{
    public StockLevelEmptyHistoryEntryException(String message)
    {
        super(message);
    }
}
