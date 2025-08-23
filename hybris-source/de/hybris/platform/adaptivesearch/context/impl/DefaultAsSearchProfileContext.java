package de.hybris.platform.adaptivesearch.context.impl;

import de.hybris.platform.adaptivesearch.context.AsKeyword;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAsSearchProfileContext implements AsSearchProfileContext
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAsSearchProfileContext.class);
    private List<String> queryContexts;
    private String indexConfiguration;
    private String indexType;
    private List<CatalogVersionModel> catalogVersions;
    private List<CatalogVersionModel> sessionCatalogVersions;
    private List<CategoryModel> categoryPath;
    private LanguageModel language;
    private CurrencyModel currency;
    private List<AsKeyword> keywords;
    private String query;
    private Map<String, List<String>> qualifiers;
    private final Map<String, Object> attributes = new HashMap<>();


    public List<String> getQueryContexts()
    {
        if(this.queryContexts == null)
        {
            return List.of();
        }
        return this.queryContexts;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setQueryContexts(List<String> queryContexts)
    {
        this.queryContexts = queryContexts;
    }


    public String getIndexConfiguration()
    {
        return this.indexConfiguration;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setIndexConfiguration(String indexConfiguration)
    {
        this.indexConfiguration = indexConfiguration;
    }


    public String getIndexType()
    {
        return this.indexType;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setIndexType(String indexType)
    {
        this.indexType = indexType;
    }


    public List<CatalogVersionModel> getCatalogVersions()
    {
        if(this.catalogVersions == null)
        {
            return List.of();
        }
        return this.catalogVersions;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setCatalogVersions(List<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public List<CatalogVersionModel> getSessionCatalogVersions()
    {
        if(this.sessionCatalogVersions == null)
        {
            return getCatalogVersions();
        }
        return this.sessionCatalogVersions;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setSessionCatalogVersions(List<CatalogVersionModel> sessionCatalogVersions)
    {
        this.sessionCatalogVersions = sessionCatalogVersions;
    }


    public List<CategoryModel> getCategoryPath()
    {
        if(this.categoryPath == null)
        {
            return List.of();
        }
        return this.categoryPath;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setCategoryPath(List<CategoryModel> categoryPath)
    {
        this.categoryPath = categoryPath;
    }


    public LanguageModel getLanguage()
    {
        return this.language;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setLanguage(LanguageModel language)
    {
        this.language = language;
    }


    public CurrencyModel getCurrency()
    {
        return this.currency;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setCurrency(CurrencyModel currency)
    {
        this.currency = currency;
    }


    public List<AsKeyword> getKeywords()
    {
        if(this.keywords == null)
        {
            return List.of();
        }
        return this.keywords;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setKeywords(List<AsKeyword> keywords)
    {
        this.keywords = keywords;
    }


    public String getQuery()
    {
        return this.query;
    }


    @Deprecated(since = "2205", forRemoval = true)
    public void setQuery(String query)
    {
        this.query = query;
    }


    public Map<String, List<String>> getQualifiers()
    {
        if(this.qualifiers == null)
        {
            return Map.of();
        }
        return this.qualifiers;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public static DefaultAsSearchProfileContextBuilder builder()
    {
        return new DefaultAsSearchProfileContextBuilder();
    }
}
