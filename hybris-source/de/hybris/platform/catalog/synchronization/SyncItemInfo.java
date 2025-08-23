package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.core.PK;
import java.io.Serializable;

public class SyncItemInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private PK itemPk;
    private PK syncJobPk;
    private SyncItemStatus syncStatus;
    private Boolean fromSource;
    private PK syncTimestampPk;


    public void setItemPk(PK itemPk)
    {
        this.itemPk = itemPk;
    }


    public PK getItemPk()
    {
        return this.itemPk;
    }


    public void setSyncJobPk(PK syncJobPk)
    {
        this.syncJobPk = syncJobPk;
    }


    public PK getSyncJobPk()
    {
        return this.syncJobPk;
    }


    public void setSyncStatus(SyncItemStatus syncStatus)
    {
        this.syncStatus = syncStatus;
    }


    public SyncItemStatus getSyncStatus()
    {
        return this.syncStatus;
    }


    public void setFromSource(Boolean fromSource)
    {
        this.fromSource = fromSource;
    }


    public Boolean getFromSource()
    {
        return this.fromSource;
    }


    public void setSyncTimestampPk(PK syncTimestampPk)
    {
        this.syncTimestampPk = syncTimestampPk;
    }


    public PK getSyncTimestampPk()
    {
        return this.syncTimestampPk;
    }
}
