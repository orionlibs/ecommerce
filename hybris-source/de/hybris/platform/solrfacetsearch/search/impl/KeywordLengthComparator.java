package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.Comparator;

public class KeywordLengthComparator implements Comparator<SolrFacetSearchKeywordRedirectModel>
{
    public int compare(SolrFacetSearchKeywordRedirectModel keywordRedirectModel1, SolrFacetSearchKeywordRedirectModel keywordRedirectModel2)
    {
        int length_1 = 0;
        int length_2 = 0;
        if(keywordRedirectModel1.getKeyword() != null)
        {
            length_1 = keywordRedirectModel1.getKeyword().length();
        }
        if(keywordRedirectModel2.getKeyword() != null)
        {
            length_2 = keywordRedirectModel2.getKeyword().length();
        }
        if(length_1 < length_2)
        {
            return 1;
        }
        if(length_1 > length_2)
        {
            return -1;
        }
        return 0;
    }
}
