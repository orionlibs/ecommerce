package de.hybris.platform.commercewebservices.core.queues.data;

import java.io.Serializable;

public class ProductExpressUpdateElementData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String catalogId;
    private String catalogVersion;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }
}
