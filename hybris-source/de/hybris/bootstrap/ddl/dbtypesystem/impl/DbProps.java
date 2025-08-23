package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystemItem;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;

class DbProps implements DbTypeSystemItem
{
    private final String rowsHash;
    private final String name;
    private final Long itemPk;
    private final Long langPk;


    public DbProps(PropsRow row)
    {
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(row.getItempk());
        Preconditions.checkNotNull(row.getLangpk());
        this.rowsHash = HashGenerationStrategy.getForProps().getHashFor((Row)row);
        this.name = row.getName();
        this.itemPk = row.getItempk();
        this.langPk = row.getLangpk();
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.combineFrom(new String[] {this.itemPk.toString(), this.langPk.toString(), this.name});
    }


    public String getHash()
    {
        return this.rowsHash;
    }
}
