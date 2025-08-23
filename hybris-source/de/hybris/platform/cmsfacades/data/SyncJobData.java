package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;

public class SyncJobData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String syncStatus;
    private Date startDate;
    private Date endDate;
    private Date creationDate;
    private Date lastModifiedDate;
    private String syncResult;
    private String sourceCatalogVersion;
    private String targetCatalogVersion;
    private String code;


    public void setSyncStatus(String syncStatus)
    {
        this.syncStatus = syncStatus;
    }


    public String getSyncStatus()
    {
        return this.syncStatus;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    public Date getStartDate()
    {
        return this.startDate;
    }


    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    public Date getEndDate()
    {
        return this.endDate;
    }


    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }


    public Date getCreationDate()
    {
        return this.creationDate;
    }


    public void setLastModifiedDate(Date lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }


    public Date getLastModifiedDate()
    {
        return this.lastModifiedDate;
    }


    public void setSyncResult(String syncResult)
    {
        this.syncResult = syncResult;
    }


    public String getSyncResult()
    {
        return this.syncResult;
    }


    public void setSourceCatalogVersion(String sourceCatalogVersion)
    {
        this.sourceCatalogVersion = sourceCatalogVersion;
    }


    public String getSourceCatalogVersion()
    {
        return this.sourceCatalogVersion;
    }


    public void setTargetCatalogVersion(String targetCatalogVersion)
    {
        this.targetCatalogVersion = targetCatalogVersion;
    }


    public String getTargetCatalogVersion()
    {
        return this.targetCatalogVersion;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }
}
