package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.JaloSystemException;

public class PromotionException extends JaloSystemException
{
    public PromotionException(String message)
    {
        super(message);
    }


    public PromotionException(String message, int errorCode)
    {
        super(message, errorCode);
    }


    public PromotionException(Throwable nested)
    {
        super(nested);
    }


    public PromotionException(Throwable nested, int errorCode)
    {
        super(nested, errorCode);
    }


    public PromotionException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }
}
