package de.hybris.platform.directpersistence.read;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Objects;

public class RelationTypeNotSupportedException extends SystemException
{
    public RelationTypeNotSupportedException(String message)
    {
        super(Objects.<String>requireNonNull(message, "message musn't be null"));
    }
}
