package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Keyword;
import de.hybris.platform.solrfacetsearch.search.KeywordModifier;
import de.hybris.platform.solrfacetsearch.search.SearchQueryKeywordsResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class DefaultSearchQueryKeywordsResolver implements SearchQueryKeywordsResolver
{
    protected static final Pattern SPLIT_REGEX = Pattern.compile("\"([^\"]*)\"|([^\\s]+)");
    protected static final Pattern WS_REGEX = Pattern.compile("\\s+");


    public List<Keyword> resolveKeywords(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String userQuery)
    {
        List<Keyword> keywords = new ArrayList<>();
        if(StringUtils.isNotBlank(userQuery))
        {
            Matcher splitMatcher = SPLIT_REGEX.matcher(userQuery);
            while(splitMatcher.find())
            {
                if(StringUtils.isNotBlank(splitMatcher.group(1)))
                {
                    String value = WS_REGEX.matcher(splitMatcher.group(1)).replaceAll(" ");
                    keywords.add(new Keyword(value, new KeywordModifier[] {KeywordModifier.EXACT_MATCH}));
                    continue;
                }
                if(StringUtils.isNotBlank(splitMatcher.group(2)))
                {
                    String value = splitMatcher.group(2);
                    keywords.add(new Keyword(value, new KeywordModifier[0]));
                }
            }
        }
        return keywords;
    }
}
