package de.hybris.platform.adaptivesearchbackoffice.facades.impl;

import de.hybris.platform.adaptivesearch.services.AsCategoryService;
import de.hybris.platform.adaptivesearchbackoffice.data.AsCategoryData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsCategoryFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsCategoryFacade implements AsCategoryFacade
{
    protected static final int MAX_CHILDREN_SIZE = 100;
    private L10NService l10nService;
    private CatalogVersionService catalogVersionService;
    private CategoryService categoryService;
    private AsCategoryService asCategoryService;


    public AsCategoryData getCategoryHierarchy()
    {
        return getCategoryHierarchy(null, null);
    }


    public AsCategoryData getCategoryHierarchy(String catalogId, String catalogVersionName)
    {
        AsCategoryData globalCategoryData = createGlobalCategory();
        if(StringUtils.isNotBlank(catalogId) && StringUtils.isNotBlank(catalogVersionName))
        {
            CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
            Map<PK, List<CategoryModel>> categoryRelations = buildCategoryRelations(catalogVersion);
            List<CategoryModel> rootCategories = catalogVersion.getRootCategories();
            if(CollectionUtils.isNotEmpty(rootCategories))
            {
                for(CategoryModel rootCategory : rootCategories)
                {
                    populateCategoryHierarchy(categoryRelations, globalCategoryData, rootCategory);
                }
            }
        }
        return globalCategoryData;
    }


    protected Map<PK, List<CategoryModel>> buildCategoryRelations(CatalogVersionModel catalogVersion)
    {
        List<CategoryModel> categories = this.asCategoryService.getAllCategoriesForCatalogVersion(catalogVersion);
        Map<PK, CategoryModel> categoriesMapping = (Map<PK, CategoryModel>)categories.stream().collect(Collectors.toMap(AbstractItemModel::getPk, category -> category));
        List<List<PK>> categoryRelations = this.asCategoryService.getAllCategoryRelationsForCatalogVersion(catalogVersion);
        Map<PK, List<CategoryModel>> categoryRelationsMapping = new HashMap<>();
        for(List<PK> categoryRelation : categoryRelations)
        {
            PK source = categoryRelation.get(0);
            PK target = categoryRelation.get(1);
            List<CategoryModel> children = categoryRelationsMapping.get(source);
            if(children == null)
            {
                children = new ArrayList<>();
                categoryRelationsMapping.put(source, children);
            }
            CategoryModel child = categoriesMapping.get(target);
            if(child != null)
            {
                children.add(child);
            }
        }
        return categoryRelationsMapping;
    }


    protected void populateCategoryHierarchy(Map<PK, List<CategoryModel>> categoryRelations, AsCategoryData parentCategoryData, CategoryModel category)
    {
        AsCategoryData categoryData = createCategory(category);
        parentCategoryData.getChildren().add(categoryData);
        List<CategoryModel> children = categoryRelations.get(category.getPk());
        if(CollectionUtils.isEmpty(children))
        {
            return;
        }
        int size = children.size();
        if(size > 100)
        {
            int rangeIndex = 0;
            int rangeStart = 1;
            int rangeEnd = Math.min(size, 100);
            AsCategoryData rangeCategoryData = createRangeCategory(rangeStart, rangeEnd);
            categoryData.getChildren().add(rangeCategoryData);
            for(CategoryModel child : children)
            {
                if(rangeIndex >= 100)
                {
                    rangeIndex = 0;
                    rangeStart += 100;
                    rangeEnd = Math.min(size, rangeEnd + 100);
                    rangeCategoryData = createRangeCategory(rangeStart, rangeEnd);
                    categoryData.getChildren().add(rangeCategoryData);
                }
                populateCategoryHierarchy(categoryRelations, rangeCategoryData, child);
                rangeIndex++;
            }
        }
        else
        {
            for(CategoryModel child : children)
            {
                populateCategoryHierarchy(categoryRelations, categoryData, child);
            }
        }
    }


    public List<AsCategoryData> buildCategoryBreadcrumbs(List<String> categoryPath)
    {
        return buildCategoryBreadcrumbs(null, null, categoryPath);
    }


    public List<AsCategoryData> buildCategoryBreadcrumbs(String catalogId, String catalogVersionName, List<String> categoryPath)
    {
        List<AsCategoryData> categoryBreadcrumbs = new ArrayList<>();
        categoryBreadcrumbs.add(createGlobalCategory());
        if(catalogId != null && catalogVersionName != null)
        {
            CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
            for(String category : categoryPath)
            {
                CategoryModel categoryModel = this.categoryService.getCategoryForCode(catalogVersion, category);
                categoryBreadcrumbs.add(createCategory(categoryModel));
            }
        }
        return categoryBreadcrumbs;
    }


    protected AsCategoryData createGlobalCategory()
    {
        AsCategoryData category = new AsCategoryData();
        category.setCode(null);
        category.setName(this.l10nService.getLocalizedString("adaptivesearch.globalcategory"));
        category.setVirtual(false);
        category.setChildren(new ArrayList());
        return category;
    }


    protected AsCategoryData createCategory(CategoryModel categoryModel)
    {
        AsCategoryData category = new AsCategoryData();
        category.setCode(categoryModel.getCode());
        if(StringUtils.isNotBlank(categoryModel.getName()))
        {
            category.setName(categoryModel.getName());
        }
        else
        {
            category.setName("[" + categoryModel.getCode() + "]");
        }
        category.setVirtual(false);
        category.setChildren(new ArrayList());
        return category;
    }


    protected AsCategoryData createRangeCategory(int rangeStart, int rangeEnd)
    {
        AsCategoryData category = new AsCategoryData();
        category.setCode(rangeStart + "_" + rangeEnd);
        category.setName(rangeStart + " ... " + rangeEnd);
        category.setVirtual(true);
        category.setChildren(new ArrayList());
        return category;
    }


    public L10NService getL10nService()
    {
        return this.l10nService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
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


    public AsCategoryService getAsCategoryService()
    {
        return this.asCategoryService;
    }


    @Required
    public void setAsCategoryService(AsCategoryService asCategoryService)
    {
        this.asCategoryService = asCategoryService;
    }
}
