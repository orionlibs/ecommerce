package de.hybris.platform.solrfacetsearch.suggester;

import java.util.Collection;
import java.util.Map;

public class SolrSuggestion
{
    private final Map<String, Collection<String>> suggestions;
    private final Collection<String> collations;


    public SolrSuggestion(Map<String, Collection<String>> suggestions, Collection<String> collations)
    {
        this.suggestions = suggestions;
        this.collations = collations;
    }


    @Deprecated(since = "2205")
    public Map<String, Collection<String>> getSuggestions()
    {
        return this.suggestions;
    }


    public Collection<String> getCollates()
    {
        return this.collations;
    }
}
