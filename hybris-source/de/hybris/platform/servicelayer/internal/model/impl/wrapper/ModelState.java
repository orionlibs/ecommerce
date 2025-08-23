package de.hybris.platform.servicelayer.internal.model.impl.wrapper;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;

public enum ModelState
{
    NEW(PersistenceOperation.SAVE),
    MODIFIED(PersistenceOperation.SAVE),
    DELETED(PersistenceOperation.DELETE);
    private final PersistenceOperation persistenceOperation;


    ModelState(PersistenceOperation persistenceOperation)
    {
        this.persistenceOperation = persistenceOperation;
    }


    public PersistenceOperation getPersistenceOperation()
    {
        return this.persistenceOperation;
    }
}
