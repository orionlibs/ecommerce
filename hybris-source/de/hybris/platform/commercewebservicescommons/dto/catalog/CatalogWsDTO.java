package de.hybris.platform.commercewebservicescommons.dto.catalog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;

@ApiModel(value = "Catalog", description = "Representation of an Catalog")
public class CatalogWsDTO extends AbstractCatalogItemWsDTO
{
    @ApiModelProperty(name = "catalogVersions", value = "List of versions of catalog")
    private Collection<CatalogVersionWsDTO> catalogVersions;


    public void setCatalogVersions(Collection<CatalogVersionWsDTO> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public Collection<CatalogVersionWsDTO> getCatalogVersions()
    {
        return this.catalogVersions;
    }
}
