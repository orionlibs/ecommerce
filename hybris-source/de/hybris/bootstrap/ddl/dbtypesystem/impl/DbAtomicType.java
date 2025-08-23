package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.AtomicType;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

class DbAtomicType implements AtomicType
{
    private final String rowsHash;
    private final long pk;
    private final String internalCodeLowerCase;


    public DbAtomicType(AtomicTypeRow row)
    {
        this.pk = row.getPk().longValue();
        this.internalCodeLowerCase = row.getInternalcodelowercase();
        this.rowsHash = HashGenerationStrategy.getForAtomicType().getHashFor((Row)row);
    }


    public long getPk()
    {
        return this.pk;
    }


    public String getInternalCodeLowerCase()
    {
        return this.internalCodeLowerCase;
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.createFrom(this.pk);
    }


    public String getHash()
    {
        return this.rowsHash;
    }
}
