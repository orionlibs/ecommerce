package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.CollectionType;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

class DbCollectionType implements CollectionType
{
    private final String rowsHash;
    private final long pk;
    private final String internalCodeLowerCase;


    public DbCollectionType(CollectionTypeRow row)
    {
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(row.getPk());
        this.rowsHash = HashGenerationStrategy.getForCollectionType().getHashFor((Row)row);
        this.pk = row.getPk().longValue();
        this.internalCodeLowerCase = row.getInternalcodelowercase();
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
