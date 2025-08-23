package de.hybris.platform.directpersistence.record.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.EntityRecord;

public abstract class AbstractEntityRecord implements EntityRecord
{
    private final PK pk;
    private final String type;
    private final long version;
    protected static final long INITIAL_VERSION = 0L;


    public AbstractEntityRecord(PK pk, String type, long version)
    {
        Preconditions.checkArgument((pk != null), "pk is required");
        Preconditions.checkArgument((type != null), "type is required");
        this.pk = pk;
        this.type = type;
        this.version = version;
    }


    public PK getPK()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public long getVersion()
    {
        return this.version;
    }
}
