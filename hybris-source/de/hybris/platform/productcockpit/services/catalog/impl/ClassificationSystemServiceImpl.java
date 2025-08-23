package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.productcockpit.services.catalog.ClassificationSystemService;
import java.util.ArrayList;
import java.util.List;

public class ClassificationSystemServiceImpl implements ClassificationSystemService
{
    private CatalogService catalogService = null;


    public CatalogService getCatalogService()
    {
        if(this.catalogService == null)
        {
            this.catalogService = (CatalogService)Registry.getApplicationContext().getBean("defaultCatalogService");
        }
        return this.catalogService;
    }


    public List<ClassificationAttributeModel> getClassificationAttributes(ClassificationClassModel model)
    {
        List<ClassificationAttributeModel> result = new ArrayList<>();
        if(model != null)
        {
            for(ClassificationAttributeModel classificationAttributeModel : model.getDeclaredClassificationAttributes())
            {
                result.add(classificationAttributeModel);
            }
        }
        return result;
    }


    public List<ClassificationClassModel> getClassificationRootCategories(ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        List<ClassificationClassModel> result = new ArrayList<>();
        if(classificationSystemVersionModel != null)
        {
            for(CategoryModel categoryModel : classificationSystemVersionModel.getRootCategories())
            {
                if(categoryModel instanceof ClassificationClassModel)
                {
                    result.add((ClassificationClassModel)categoryModel);
                }
            }
        }
        return result;
    }


    public List<ClassificationClassModel> getClassificationSubcategories(ClassificationClassModel category)
    {
        List<ClassificationClassModel> result = new ArrayList<>();
        if(category != null)
        {
            for(CategoryModel categoryModel : category.getCategories())
            {
                if(categoryModel instanceof ClassificationClassModel)
                {
                    result.add((ClassificationClassModel)categoryModel);
                }
            }
        }
        return result;
    }


    public List<ClassificationSystemModel> getClassificationSystems()
    {
        List<ClassificationSystemModel> result = new ArrayList<>();
        for(CatalogModel catalogModel : getCatalogService().getAllCatalogs())
        {
            if(catalogModel instanceof ClassificationSystemModel)
            {
                result.add((ClassificationSystemModel)catalogModel);
            }
        }
        return result;
    }


    public List<ClassificationSystemVersionModel> getClassificationSystemVersions(ClassificationSystemModel classificationSystemModel)
    {
        List<ClassificationSystemVersionModel> result = new ArrayList<>();
        if(classificationSystemModel != null)
        {
            for(CatalogVersionModel catalogVersionModel : classificationSystemModel.getCatalogVersions())
            {
                if(catalogVersionModel instanceof ClassificationSystemVersionModel)
                {
                    result.add((ClassificationSystemVersionModel)catalogVersionModel);
                }
            }
        }
        return result;
    }


    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }
}
