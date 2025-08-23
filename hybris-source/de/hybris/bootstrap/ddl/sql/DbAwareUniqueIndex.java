package de.hybris.bootstrap.ddl.sql;

import org.apache.ddlutils.model.UniqueIndex;

public class DbAwareUniqueIndex extends UniqueIndex implements DbAwareIndex
{
    private IndexCreationMode creationMode;
    private boolean online;


    public DbAwareUniqueIndex()
    {
    }


    public DbAwareUniqueIndex(IndexCreationMode creationMode)
    {
        this(creationMode, false);
    }


    public DbAwareUniqueIndex(IndexCreationMode creationMode, boolean online)
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
