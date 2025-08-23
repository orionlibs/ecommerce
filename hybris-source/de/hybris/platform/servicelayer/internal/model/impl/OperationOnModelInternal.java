package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;

public enum OperationOnModelInternal
{
    SAVE(PersistenceOperation.SAVE),
    DELETE(PersistenceOperation.DELETE),
    OTHER(null);
    private final PersistenceOperation counterpart;


    OperationOnModelInternal(PersistenceOperation counterpart)
    {
        this.counterpart = counterpart;
    }


    public static OperationOnModelInternal from(PersistenceOperation persistenceOperation)
    {
        for(OperationOnModelInternal value : values())
        {
            if(value.counterpart == persistenceOperation)
            {
                return value;
            }
        }
        throw new IllegalArgumentException("Cannot find internal counterpart for " + persistenceOperation);
    }


    public PersistenceOperation toOperationOnModelExternal()
    {
        return this.counterpart;
    }
}
