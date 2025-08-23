package de.hybris.platform.commercewebservicescommons.dto.catalog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;

@ApiModel(value = "CatalogVersion", description = "Representation of a Catalog Version")
public class CatalogVersionWsDTO extends AbstractCatalogItemWsDTO
{
    @ApiModelProperty(name = "categories", value = "List of category hierarchies")
    private Collection<CategoryHierarchyWsDTO> categories;


    public void setCategories(Collection<CategoryHierarchyWsDTO> categories)
    {
        this.categories = categories;
    }


    public Collection<CategoryHierarchyWsDTO> getCategories()
    {
        return this.categories;
    }
}
