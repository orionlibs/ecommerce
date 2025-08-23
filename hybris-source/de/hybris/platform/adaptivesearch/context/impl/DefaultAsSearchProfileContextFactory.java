package de.hybris.platform.adaptivesearch.context.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContextFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2205", forRemoval = true)
public class DefaultAsSearchProfileContextFactory implements AsSearchProfileContextFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAsSearchProfileContextFactory.class);
    private AsSearchProviderFactory asSearchProviderFactory;


    public AsSearchProfileContext createContext(String indexConfiguration, String indexType, List<CatalogVersionModel> catalogVersions, List<CategoryModel> categoryPath)
    {
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withSessionCatalogVersions(catalogVersions).withCategoryPath(categoryPath)
                        .build();
    }


    public AsSearchProfileContext createContext(String indexConfiguration, String indexType, List<CatalogVersionModel> catalogVersions, List<CategoryModel> categoryPath, LanguageModel language, CurrencyModel currency)
    {
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withSessionCatalogVersions(catalogVersions).withCategoryPath(categoryPath)
                        .withLanguage(language).withCurrency(currency).build();
    }


    public AsSearchProfileContext createContext(String indexConfiguration, String indexType, List<CatalogVersionModel> catalogVersions, List<CategoryModel> categoryPath, LanguageModel language, CurrencyModel currency, Map<String, List<String>> qualifiers)
    {
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withSessionCatalogVersions(catalogVersions).withCategoryPath(categoryPath)
                        .withLanguage(language).withCurrency(currency).withQualifiers(qualifiers).build();
    }


    public AsSearchProfileContext createContext(String indexConfiguration, String indexType, List<CatalogVersionModel> catalogVersions, List<CatalogVersionModel> sessionCatalogVersions, List<CategoryModel> categoryPath, LanguageModel language, CurrencyModel currency)
    {
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withSessionCatalogVersions(sessionCatalogVersions)
                        .withCategoryPath(categoryPath).withLanguage(language).withCurrency(currency).build();
    }


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }
}
