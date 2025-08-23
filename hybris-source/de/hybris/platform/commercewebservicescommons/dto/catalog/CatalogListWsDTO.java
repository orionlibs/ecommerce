package de.hybris.platform.commercewebservicescommons.dto.catalog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CatalogList", description = "Representation of a Catalog List")
public class CatalogListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "catalogs", value = "List of catalog items")
    private List<CatalogWsDTO> catalogs;


    public void setCatalogs(List<CatalogWsDTO> catalogs)
    {
        this.catalogs = catalogs;
    }


    public List<CatalogWsDTO> getCatalogs()
    {
        return this.catalogs;
    }
}
