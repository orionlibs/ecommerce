package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.SuggestionData;
import java.io.Serializable;
import java.util.List;

public class SuggestionDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<SuggestionData> suggestions;


    public void setSuggestions(List<SuggestionData> suggestions)
    {
        this.suggestions = suggestions;
    }


    public List<SuggestionData> getSuggestions()
    {
        return this.suggestions;
    }
}
