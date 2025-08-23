package de.hybris.platform.adaptivesearch.context;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

public interface AsSearchProfileContext
{
    List<String> getQueryContexts();


    String getIndexConfiguration();


    String getIndexType();


    List<CatalogVersionModel> getCatalogVersions();


    List<CatalogVersionModel> getSessionCatalogVersions();


    List<CategoryModel> getCategoryPath();


    LanguageModel getLanguage();


    CurrencyModel getCurrency();


    List<AsKeyword> getKeywords();


    void setKeywords(List<AsKeyword> paramList);


    String getQuery();


    void setQuery(String paramString);


    Map<String, List<String>> getQualifiers();


    Map<String, Object> getAttributes();
}
