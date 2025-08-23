package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.daos.AsCategoryDao;
import de.hybris.platform.adaptivesearch.services.AsCategoryService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsCategoryService implements AsCategoryService
{
    protected static final String CURRENT_CATEGORY_PATH = "asCategoryPath";
    private ModelService modelService;
    private SessionService sessionService;
    private CategoryService categoryService;
    private AsCategoryDao asCategoryDao;


    public void setCurrentCategoryPath(List<CategoryModel> categoryPath)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("categoryPath", categoryPath);
        List<PK> pks = (List<PK>)categoryPath.stream().map(AbstractItemModel::getPk).collect(Collectors.toList());
        this.sessionService.setAttribute("asCategoryPath", pks);
    }


    public Optional<List<CategoryModel>> getCurrentCategoryPath()
    {
        List<PK> pks = (List<PK>)this.sessionService.getAttribute("asCategoryPath");
        if(pks == null)
        {
            return Optional.empty();
        }
        Objects.requireNonNull(this.modelService);
        List<CategoryModel> categoryPath = (List<CategoryModel>)pks.stream().map(this.modelService::get).collect(Collectors.toList());
        return Optional.of(categoryPath);
    }


    public void clearCurrentCategoryPath()
    {
        this.sessionService.removeAttribute("asCategoryPath");
    }


    public List<CategoryModel> buildCategoryPath(List<String> categoryCodes, List<CatalogVersionModel> catalogVersions, boolean recursive)
    {
        Set<PK> visitedCategories = new HashSet<>();
        List<CategoryModel> categoryPath = new ArrayList<>();
        for(String categoryCode : categoryCodes)
        {
            CategoryModel category = resolveCategory(categoryCode, catalogVersions);
            if(category != null)
            {
                if(recursive)
                {
                    List<CategoryModel> categoryPathBranch = new ArrayList<>();
                    buildCategoryPathHelper(visitedCategories, categoryPathBranch, category);
                    categoryPath.addAll(categoryPathBranch);
                    continue;
                }
                categoryPath.add(category);
            }
        }
        return categoryPath;
    }


    protected void buildCategoryPathHelper(Set<PK> visitedCategories, List<CategoryModel> categoryPath, CategoryModel category)
    {
        if(visitedCategories.contains(category.getPk()))
        {
            return;
        }
        visitedCategories.add(category.getPk());
        List<CategoryModel> supercategories = category.getSupercategories();
        if(!CollectionUtils.isEmpty(supercategories))
        {
            for(CategoryModel supercategory : supercategories)
            {
                if(isSupportedCategory(supercategory))
                {
                    buildCategoryPathHelper(visitedCategories, categoryPath, supercategory);
                }
            }
        }
        categoryPath.add(category);
    }


    protected CategoryModel resolveCategory(String categoryCode, List<CatalogVersionModel> catalogVersions)
    {
        Collection<CategoryModel> categories = this.categoryService.getCategoriesForCode(categoryCode);
        if(CollectionUtils.isEmpty(categories))
        {
            return null;
        }
        Collection<CategoryModel> matchingCategories = (Collection<CategoryModel>)categories.stream().filter(category -> isMatchingCategory(category, catalogVersions)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(matchingCategories))
        {
            return null;
        }
        Optional<CategoryModel> activeCategory = matchingCategories.stream().filter(this::isActiveCategory).findFirst();
        if(activeCategory.isPresent())
        {
            return activeCategory.get();
        }
        Optional<CategoryModel> nonActiveCategory = matchingCategories.stream().filter(this::isNonActiveCategory).findFirst();
        if(nonActiveCategory.isPresent())
        {
            return nonActiveCategory.get();
        }
        return null;
    }


    protected boolean isMatchingCategory(CategoryModel category, List<CatalogVersionModel> catalogVersions)
    {
        return catalogVersions.contains(category.getCatalogVersion());
    }


    protected boolean isActiveCategory(CategoryModel category)
    {
        return BooleanUtils.isTrue(category.getCatalogVersion().getActive());
    }


    protected boolean isNonActiveCategory(CategoryModel category)
    {
        return !isActiveCategory(category);
    }


    protected boolean isSupportedCategory(CategoryModel categoryModel)
    {
        return !(categoryModel instanceof de.hybris.platform.catalog.model.classification.ClassificationClassModel);
    }


    public List<CategoryModel> getAllCategoriesForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return this.asCategoryDao.findCategoriesByCatalogVersion(catalogVersion);
    }


    public List<List<PK>> getAllCategoryRelationsForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return this.asCategoryDao.findCategoryRelationsByCatalogVersion(catalogVersion);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
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


    public AsCategoryDao getAsCategoryDao()
    {
        return this.asCategoryDao;
    }


    @Required
    public void setAsCategoryDao(AsCategoryDao asCategoryDao)
    {
        this.asCategoryDao = asCategoryDao;
    }
}
