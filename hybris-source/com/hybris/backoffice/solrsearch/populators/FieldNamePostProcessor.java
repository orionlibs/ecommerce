package com.hybris.backoffice.solrsearch.populators;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Locale;

public interface FieldNamePostProcessor
{
    String process(SearchQuery paramSearchQuery, Locale paramLocale, String paramString);
}
