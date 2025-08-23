package de.hybris.platform.commercewebservicescommons.dto.queues;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ProductExpressUpdateElement", description = "Representation of a Product Express Update Element")
public class ProductExpressUpdateElementWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of product express update element")
    private String code;
    @ApiModelProperty(name = "catalogId", value = "Catalog identifier")
    private String catalogId;
    @ApiModelProperty(name = "catalogVersion", value = "Catalog version")
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
