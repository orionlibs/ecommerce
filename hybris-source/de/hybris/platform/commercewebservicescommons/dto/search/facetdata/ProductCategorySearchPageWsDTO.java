package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import de.hybris.platform.commercewebservicescommons.dto.product.CategoryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(value = "ProductCategorySearchPage", description = "Representation od a Product Category Search Page")
public class ProductCategorySearchPageWsDTO extends ProductSearchPageWsDTO
{
    @ApiModelProperty(name = "subCategories", value = "List of subcategories")
    private List<CategoryWsDTO> subCategories;


    public void setSubCategories(List<CategoryWsDTO> subCategories)
    {
        this.subCategories = subCategories;
    }


    public List<CategoryWsDTO> getSubCategories()
    {
        return this.subCategories;
    }
}
