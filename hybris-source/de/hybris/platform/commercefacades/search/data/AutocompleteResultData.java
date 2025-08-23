package de.hybris.platform.commercefacades.search.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.io.Serializable;
import java.util.List;

public class AutocompleteResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<AutocompleteSuggestionData> suggestions;
    private List<ProductData> products;


    public void setSuggestions(List<AutocompleteSuggestionData> suggestions)
    {
        this.suggestions = suggestions;
    }


    public List<AutocompleteSuggestionData> getSuggestions()
    {
        return this.suggestions;
    }


    public void setProducts(List<ProductData> products)
    {
        this.products = products;
    }


    public List<ProductData> getProducts()
    {
        return this.products;
    }
}
