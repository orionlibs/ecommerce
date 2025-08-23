package de.hybris.platform.commerceservices.backoffice.exception;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;

public class RemoveAdminException extends ObjectSavingException
{
    public RemoveAdminException(String objectId, String message, Throwable cause)
    {
        super(objectId, message, cause);
    }
}
