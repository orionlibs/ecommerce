package de.hybris.platform.commercewebservicescommons.dto.store;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "StoreFinderStockSearchPage", description = "Representation of a Store Finder Stock Search Page")
public class StoreFinderStockSearchPageWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "stores", value = "List of stores")
    private List<PointOfServiceStockWsDTO> stores;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination")
    private PaginationWsDTO pagination;
    @ApiModelProperty(name = "locationText", value = "Location text")
    private String locationText;
    @ApiModelProperty(name = "sourceLatitude", value = "Source latitude")
    private Double sourceLatitude;
    @ApiModelProperty(name = "sourceLongitude", value = "Source longitude")
    private Double sourceLongitude;
    @ApiModelProperty(name = "boundNorthLatitude", value = "Bound to north latitude")
    private Double boundNorthLatitude;
    @ApiModelProperty(name = "boundEastLongitude", value = "Bound to east longitude")
    private Double boundEastLongitude;
    @ApiModelProperty(name = "boundSouthLatitude", value = "Bound to south latitude")
    private Double boundSouthLatitude;
    @ApiModelProperty(name = "boundWestLongitude", value = "Bound to west longitude")
    private Double boundWestLongitude;
    @ApiModelProperty(name = "product", value = "Product")
    private ProductWsDTO product;


    public void setStores(List<PointOfServiceStockWsDTO> stores)
    {
        this.stores = stores;
    }


    public List<PointOfServiceStockWsDTO> getStores()
    {
        return this.stores;
    }


    public void setSorts(List<SortWsDTO> sorts)
    {
        this.sorts = sorts;
    }


    public List<SortWsDTO> getSorts()
    {
        return this.sorts;
    }


    public void setPagination(PaginationWsDTO pagination)
    {
        this.pagination = pagination;
    }


    public PaginationWsDTO getPagination()
    {
        return this.pagination;
    }


    public void setLocationText(String locationText)
    {
        this.locationText = locationText;
    }


    public String getLocationText()
    {
        return this.locationText;
    }


    public void setSourceLatitude(Double sourceLatitude)
    {
        this.sourceLatitude = sourceLatitude;
    }


    public Double getSourceLatitude()
    {
        return this.sourceLatitude;
    }


    public void setSourceLongitude(Double sourceLongitude)
    {
        this.sourceLongitude = sourceLongitude;
    }


    public Double getSourceLongitude()
    {
        return this.sourceLongitude;
    }


    public void setBoundNorthLatitude(Double boundNorthLatitude)
    {
        this.boundNorthLatitude = boundNorthLatitude;
    }


    public Double getBoundNorthLatitude()
    {
        return this.boundNorthLatitude;
    }


    public void setBoundEastLongitude(Double boundEastLongitude)
    {
        this.boundEastLongitude = boundEastLongitude;
    }


    public Double getBoundEastLongitude()
    {
        return this.boundEastLongitude;
    }


    public void setBoundSouthLatitude(Double boundSouthLatitude)
    {
        this.boundSouthLatitude = boundSouthLatitude;
    }


    public Double getBoundSouthLatitude()
    {
        return this.boundSouthLatitude;
    }


    public void setBoundWestLongitude(Double boundWestLongitude)
    {
        this.boundWestLongitude = boundWestLongitude;
    }


    public Double getBoundWestLongitude()
    {
        return this.boundWestLongitude;
    }


    public void setProduct(ProductWsDTO product)
    {
        this.product = product;
    }


    public ProductWsDTO getProduct()
    {
        return this.product;
    }
}
