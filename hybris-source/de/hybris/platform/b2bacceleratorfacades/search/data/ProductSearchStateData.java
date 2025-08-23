package de.hybris.platform.b2bacceleratorfacades.search.data;

import de.hybris.platform.commercefacades.search.data.SearchStateData;

public class ProductSearchStateData extends SearchStateData
{
    private Boolean populateVariants;


    public void setPopulateVariants(Boolean populateVariants)
    {
        this.populateVariants = populateVariants;
    }


    public Boolean getPopulateVariants()
    {
        return this.populateVariants;
    }
}
