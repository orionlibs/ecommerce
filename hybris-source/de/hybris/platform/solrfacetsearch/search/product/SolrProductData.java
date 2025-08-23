package de.hybris.platform.solrfacetsearch.search.product;

import java.util.Collection;

public class SolrProductData
{
    private String code;
    private String name;
    private String description;
    private String catalogVersion;
    private String catalog;
    private Long pk;
    private Collection<String> categories;
    private Double price;
    private String ean;


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalog()
    {
        return this.catalog;
    }


    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }


    public Long getPk()
    {
        return this.pk;
    }


    public void setPk(Long pk)
    {
        this.pk = pk;
    }


    public Collection<String> getCategories()
    {
        return this.categories;
    }


    public void setCategories(Collection<String> categories)
    {
        this.categories = categories;
    }


    public Double getPrice()
    {
        return this.price;
    }


    public void setPrice(Double price)
    {
        this.price = price;
    }


    public String getEan()
    {
        return this.ean;
    }


    public void setEan(String ean)
    {
        this.ean = ean;
    }
}
