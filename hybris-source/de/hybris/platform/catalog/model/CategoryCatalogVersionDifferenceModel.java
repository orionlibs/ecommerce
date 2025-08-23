package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.CategoryDifferenceMode;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CategoryCatalogVersionDifferenceModel extends CatalogVersionDifferenceModel
{
    public static final String _TYPECODE = "CategoryCatalogVersionDifference";
    public static final String SOURCECATEGORY = "sourceCategory";
    public static final String TARGETCATEGORY = "targetCategory";
    public static final String MODE = "mode";


    public CategoryCatalogVersionDifferenceModel()
    {
    }


    public CategoryCatalogVersionDifferenceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CategoryCatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, CategoryDifferenceMode _mode, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setMode(_mode);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CategoryCatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, CategoryDifferenceMode _mode, ItemModel _owner, CategoryModel _sourceCategory, CatalogVersionModel _sourceVersion, CategoryModel _targetCategory, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setMode(_mode);
        setOwner(_owner);
        setSourceCategory(_sourceCategory);
        setSourceVersion(_sourceVersion);
        setTargetCategory(_targetCategory);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public CategoryDifferenceMode getMode()
    {
        return (CategoryDifferenceMode)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "sourceCategory", type = Accessor.Type.GETTER)
    public CategoryModel getSourceCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("sourceCategory");
    }


    @Accessor(qualifier = "targetCategory", type = Accessor.Type.GETTER)
    public CategoryModel getTargetCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("targetCategory");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(CategoryDifferenceMode value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "sourceCategory", type = Accessor.Type.SETTER)
    public void setSourceCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("sourceCategory", value);
    }


    @Accessor(qualifier = "targetCategory", type = Accessor.Type.SETTER)
    public void setTargetCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("targetCategory", value);
    }
}
