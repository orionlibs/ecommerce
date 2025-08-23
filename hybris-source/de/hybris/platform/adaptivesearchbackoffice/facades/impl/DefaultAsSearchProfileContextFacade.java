package de.hybris.platform.adaptivesearchbackoffice.facades.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.impl.DefaultAsSearchProfileContext;
import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import de.hybris.platform.adaptivesearchbackoffice.data.CategoryData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchProfileContextFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileContextFacade implements AsSearchProfileContextFacade
{
    private CatalogVersionService catalogVersionService;
    private CategoryService categoryService;
    private CommonI18NService commonI18NService;


    public AsSearchProfileContext createSearchProfileContext(NavigationContextData navigationContext)
    {
        String indexConfiguration = navigationContext.getIndexConfiguration();
        String indexType = navigationContext.getIndexType();
        List<CatalogVersionModel> catalogVersions = resolveCatalogVersions(navigationContext);
        List<CategoryModel> categoryPath = resolveCategoryPath(navigationContext, catalogVersions);
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withCategoryPath(categoryPath).build();
    }


    public AsSearchProfileContext createSearchProfileContext(NavigationContextData navigationContext, SearchContextData searchContext)
    {
        String indexConfiguration = navigationContext.getIndexConfiguration();
        String indexType = navigationContext.getIndexType();
        List<CatalogVersionModel> catalogVersions = resolveCatalogVersions(navigationContext);
        List<CategoryModel> categoryPath = resolveCategoryPath(navigationContext, catalogVersions);
        LanguageModel language = resolveLanguage(searchContext);
        CurrencyModel currency = resolveCurrency(searchContext);
        return (AsSearchProfileContext)DefaultAsSearchProfileContext.builder().withIndexConfiguration(indexConfiguration).withIndexType(indexType)
                        .withCatalogVersions(catalogVersions).withCategoryPath(categoryPath).withLanguage(language).withCurrency(currency)
                        .build();
    }


    protected List<CatalogVersionModel> resolveCatalogVersions(NavigationContextData navigationContext)
    {
        CatalogVersionData catalogVersion = navigationContext.getCatalogVersion();
        if(catalogVersion == null)
        {
            return Collections.emptyList();
        }
        CatalogVersionModel catalogVersionModel = this.catalogVersionService.getCatalogVersion(catalogVersion.getCatalogId(), catalogVersion
                        .getVersion());
        return Collections.singletonList(catalogVersionModel);
    }


    protected List<CategoryModel> resolveCategoryPath(NavigationContextData navigationContext, List<CatalogVersionModel> catalogVersions)
    {
        CategoryData category = navigationContext.getCategory();
        if(category == null || CollectionUtils.isEmpty(category.getPath()) || CollectionUtils.isEmpty(catalogVersions))
        {
            return Collections.emptyList();
        }
        CatalogVersionModel catalogVersion = catalogVersions.get(0);
        return (List<CategoryModel>)category.getPath().stream().map(code -> this.categoryService.getCategoryForCode(catalogVersion, code))
                        .collect(Collectors.toList());
    }


    protected LanguageModel resolveLanguage(SearchContextData searchContext)
    {
        if(searchContext == null || searchContext.getLanguage() == null)
        {
            return null;
        }
        return this.commonI18NService.getLanguage(searchContext.getLanguage());
    }


    protected CurrencyModel resolveCurrency(SearchContextData searchContext)
    {
        if(searchContext == null || searchContext.getCurrency() == null)
        {
            return null;
        }
        return this.commonI18NService.getCurrency(searchContext.getCurrency());
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
