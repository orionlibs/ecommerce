package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.PersistResult;

public class DefaultPersistResult implements PersistResult
{
    private final CrudEnum operation;
    private final PK pk;
    private final Long persistenceVersion;
    private final String typeCode;


    public DefaultPersistResult(CrudEnum operation, PK pk, Long persistenceVersion, String typeCode)
    {
        this.operation = operation;
        this.pk = pk;
        this.persistenceVersion = persistenceVersion;
        this.typeCode = typeCode;
    }


    public DefaultPersistResult(CrudEnum operation, PK pk, Long persistenceVersion)
    {
        this(operation, pk, persistenceVersion, null);
    }


    public DefaultPersistResult(CrudEnum operation, PK pk)
    {
        this(operation, pk, null);
    }


    public CrudEnum getOperation()
    {
        return this.operation;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public Long getPersistenceVersion()
    {
        return this.persistenceVersion;
    }


    public String toString()
    {
        return "Operation: " + this.operation + " [PK:" + this.pk + ", OLock: " + this.persistenceVersion + "]";
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }
}
