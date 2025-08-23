package de.hybris.platform.commerceservices.search.facetdata;

public class ProductSearchPageData<STATE, RESULT> extends FacetSearchPageData<STATE, RESULT>
{
    private String freeTextSearch;
    private String categoryCode;
    private String keywordRedirectUrl;
    private SpellingSuggestionData<STATE> spellingSuggestion;


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


    public void setSpellingSuggestion(SpellingSuggestionData<STATE> spellingSuggestion)
    {
        this.spellingSuggestion = spellingSuggestion;
    }


    public SpellingSuggestionData<STATE> getSpellingSuggestion()
    {
        return this.spellingSuggestion;
    }
}
