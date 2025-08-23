package de.hybris.platform.commercewebservicescommons.dto.catalog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "AbstractCatalogItem", description = "Representation of an Abstract Catalog Item")
public class AbstractCatalogItemWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Identifier of abstract catalog item")
    private String id;
    @ApiModelProperty(name = "lastModified", value = "Date of last modification")
    private Date lastModified;
    @ApiModelProperty(name = "name", value = "Name of abstract catalog item")
    private String name;
    @ApiModelProperty(name = "url", value = "Url address of abstract catalog item")
    private String url;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }


    public Date getLastModified()
    {
        return this.lastModified;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }
}
