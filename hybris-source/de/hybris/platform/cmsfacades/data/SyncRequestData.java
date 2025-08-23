package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class SyncRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String catalogId;
    private String sourceVersionId;
    private String targetVersionId;


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setSourceVersionId(String sourceVersionId)
    {
        this.sourceVersionId = sourceVersionId;
    }


    public String getSourceVersionId()
    {
        return this.sourceVersionId;
    }


    public void setTargetVersionId(String targetVersionId)
    {
        this.targetVersionId = targetVersionId;
    }


    public String getTargetVersionId()
    {
        return this.targetVersionId;
    }
}
