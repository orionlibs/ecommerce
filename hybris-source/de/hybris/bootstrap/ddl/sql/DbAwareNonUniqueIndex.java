package de.hybris.bootstrap.ddl.sql;

import org.apache.ddlutils.model.NonUniqueIndex;

public class DbAwareNonUniqueIndex extends NonUniqueIndex implements DbAwareIndex
{
    private IndexCreationMode creationMode;
    private boolean online;


    public DbAwareNonUniqueIndex()
    {
    }


    public DbAwareNonUniqueIndex(IndexCreationMode creationMode)
    {
        this(creationMode, false);
    }


    public DbAwareNonUniqueIndex(IndexCreationMode creationMode, boolean online)
    {
        this.creationMode = creationMode;
        this.online = online;
    }


    public IndexCreationMode getCreationMode()
    {
        return this.creationMode;
    }


    public boolean isOnline()
    {
        return this.online;
    }
}
