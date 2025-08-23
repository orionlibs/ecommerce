package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductSearchPage", description = "Representation of a Product Search Page")
public class ProductSearchPageWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "freeTextSearch", value = "Free text search")
    private String freeTextSearch;
    @ApiModelProperty(name = "categoryCode", value = "Code of category")
    private String categoryCode;
    @ApiModelProperty(name = "keywordRedirectUrl", value = "Redirect url address keyword")
    private String keywordRedirectUrl;
    @ApiModelProperty(name = "spellingSuggestion", value = "Spelling suggestion")
    private SpellingSuggestionWsDTO spellingSuggestion;
    @ApiModelProperty(name = "products", value = "List of products")
    private List<ProductWsDTO> products;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination number")
    private PaginationWsDTO pagination;
    @ApiModelProperty(name = "currentQuery", value = "Current query")
    private SearchStateWsDTO currentQuery;
    @ApiModelProperty(name = "breadcrumbs", value = "List of breadcrumbs info")
    private List<BreadcrumbWsDTO> breadcrumbs;
    @ApiModelProperty(name = "facets", value = "List of facets")
    private List<FacetWsDTO> facets;


    public void setFreeTextSearch(String freeTextSearch)
    {
        this.freeTextSearch = freeTextSearch;
    }


    public String getFreeTextSearch()
    {
        return this.freeTextSearch;
    }


    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode;
    }


    public String getCategoryCode()
    {
        return this.categoryCode;
    }


    public void setKeywordRedirectUrl(String keywordRedirectUrl)
    {
        this.keywordRedirectUrl = keywordRedirectUrl;
    }


    public String getKeywordRedirectUrl()
    {
        return this.keywordRedirectUrl;
    }


    public void setSpellingSuggestion(SpellingSuggestionWsDTO spellingSuggestion)
    {
        this.spellingSuggestion = spellingSuggestion;
    }


    public SpellingSuggestionWsDTO getSpellingSuggestion()
    {
        return this.spellingSuggestion;
    }


    public void setProducts(List<ProductWsDTO> products)
    {
        this.products = products;
    }


    public List<ProductWsDTO> getProducts()
    {
        return this.products;
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


    public void setCurrentQuery(SearchStateWsDTO currentQuery)
    {
        this.currentQuery = currentQuery;
    }


    public SearchStateWsDTO getCurrentQuery()
    {
        return this.currentQuery;
    }


    public void setBreadcrumbs(List<BreadcrumbWsDTO> breadcrumbs)
    {
        this.breadcrumbs = breadcrumbs;
    }


    public List<BreadcrumbWsDTO> getBreadcrumbs()
    {
        return this.breadcrumbs;
    }


    public void setFacets(List<FacetWsDTO> facets)
    {
        this.facets = facets;
    }


    public List<FacetWsDTO> getFacets()
    {
        return this.facets;
    }
}
