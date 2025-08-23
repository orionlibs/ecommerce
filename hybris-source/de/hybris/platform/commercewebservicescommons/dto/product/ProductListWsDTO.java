package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductList", description = "Representation of a Product List")
public class ProductListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "products", value = "List of products")
    private List<ProductWsDTO> products;
    @ApiModelProperty(name = "catalog", value = "Catalog of product list")
    private String catalog;
    @ApiModelProperty(name = "version", value = "Version of product list")
    private String version;
    @ApiModelProperty(name = "totalProductCount", value = "Total product count")
    private Integer totalProductCount;
    @ApiModelProperty(name = "totalPageCount", value = "Total page count")
    private Integer totalPageCount;
    @ApiModelProperty(name = "currentPage", value = "Number of current page")
    private Integer currentPage;


    public void setProducts(List<ProductWsDTO> products)
    {
        this.products = products;
    }


    public List<ProductWsDTO> getProducts()
    {
        return this.products;
    }


    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }


    public String getCatalog()
    {
        return this.catalog;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setTotalProductCount(Integer totalProductCount)
    {
        this.totalProductCount = totalProductCount;
    }


    public Integer getTotalProductCount()
    {
        return this.totalProductCount;
    }


    public void setTotalPageCount(Integer totalPageCount)
    {
        this.totalPageCount = totalPageCount;
    }


    public Integer getTotalPageCount()
    {
        return this.totalPageCount;
    }


    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }


    public Integer getCurrentPage()
    {
        return this.currentPage;
    }
}
