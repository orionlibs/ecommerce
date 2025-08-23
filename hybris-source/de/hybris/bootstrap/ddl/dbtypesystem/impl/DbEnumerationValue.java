package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.EnumerationValue;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

class DbEnumerationValue implements EnumerationValue
{
    private final String rowsHash;
    private final long pk;
    private final String codeLowerCase;
    private final long typePk;


    public DbEnumerationValue(EnumerationValueRow row)
    {
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(row.getPk());
        this.rowsHash = HashGenerationStrategy.getForEnumerationValue().getHashFor((Row)row);
        this.pk = row.getPk().longValue();
        this.codeLowerCase = row.getCodelowercase();
        this.typePk = row.getTypepkstring().longValue();
    }


    public long getPk()
    {
        return this.pk;
    }


    public String getCodeLowerCase()
    {
        return this.codeLowerCase;
    }


    public long getTypePk()
    {
        return this.typePk;
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
