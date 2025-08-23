package de.hybris.platform.cmsfacades.data;

import de.hybris.platform.cmsfacades.enums.CMSPageOperation;
import java.io.Serializable;

public class CMSPageOperationsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CMSPageOperation operation;
    private String catalogId;
    private String sourceCatalogVersion;
    private String targetCatalogVersion;


    public void setOperation(CMSPageOperation operation)
    {
        this.operation = operation;
    }


    public CMSPageOperation getOperation()
    {
        return this.operation;
    }


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
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
}
