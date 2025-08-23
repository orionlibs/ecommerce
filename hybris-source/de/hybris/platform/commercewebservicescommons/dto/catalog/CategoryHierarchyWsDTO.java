package de.hybris.platform.commercewebservicescommons.dto.catalog;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(value = "CategoryHierarchy", description = "Representation of a Category Hierarchy")
public class CategoryHierarchyWsDTO extends AbstractCatalogItemWsDTO
{
    @ApiModelProperty(name = "subcategories", value = "List of subcategory hierarchies")
    private List<CategoryHierarchyWsDTO> subcategories;
    @ApiModelProperty(name = "products")
    private List<ProductWsDTO> products;
    @ApiModelProperty(name = "level")
    private Integer level;


    public void setSubcategories(List<CategoryHierarchyWsDTO> subcategories)
    {
        this.subcategories = subcategories;
    }


    public List<CategoryHierarchyWsDTO> getSubcategories()
    {
        return this.subcategories;
    }


    public void setProducts(List<ProductWsDTO> products)
    {
        this.products = products;
    }


    public List<ProductWsDTO> getProducts()
    {
        return this.products;
    }


    public void setLevel(Integer level)
    {
        this.level = level;
    }


    public Integer getLevel()
    {
        return this.level;
    }
}
