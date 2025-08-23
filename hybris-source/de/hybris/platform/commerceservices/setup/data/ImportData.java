package de.hybris.platform.commerceservices.setup.data;

import java.io.Serializable;
import java.util.List;

public class ImportData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String productCatalogName;
    private List<String> contentCatalogNames;
    private List<String> storeNames;


    public void setProductCatalogName(String productCatalogName)
    {
        this.productCatalogName = productCatalogName;
    }


    public String getProductCatalogName()
    {
        return this.productCatalogName;
    }


    public void setContentCatalogNames(List<String> contentCatalogNames)
    {
        this.contentCatalogNames = contentCatalogNames;
    }


    public List<String> getContentCatalogNames()
    {
        return this.contentCatalogNames;
    }


    public void setStoreNames(List<String> storeNames)
    {
        this.storeNames = storeNames;
    }


    public List<String> getStoreNames()
    {
        return this.storeNames;
    }
}
