package de.hybris.platform.persistence.hjmp;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

public class HybrisOptimisticLockingFailureException extends ObjectOptimisticLockingFailureException
{
    public HybrisOptimisticLockingFailureException(String msg, Throwable cause)
    {
        super(msg, cause);
    }


    public HybrisOptimisticLockingFailureException(Class persistentClass, Object identifier)
    {
        super(persistentClass, identifier);
    }
}
