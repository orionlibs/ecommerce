package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ClassificationSystemVersionModel extends CatalogVersionModel
{
    public static final String _TYPECODE = "ClassificationSystemVersion";
    public static final String _CATALOG2VERSIONSRELATION = "Catalog2VersionsRelation";


    public ClassificationSystemVersionModel()
    {
    }


    public ClassificationSystemVersionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationSystemVersionModel(ClassificationSystemModel _catalog, String _version)
    {
        setCatalog((CatalogModel)_catalog);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationSystemVersionModel(ClassificationSystemModel _catalog, ItemModel _owner, String _version)
    {
        setCatalog((CatalogModel)_catalog);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
    public ClassificationSystemModel getCatalog()
    {
        return (ClassificationSystemModel)super.getCatalog();
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
    public void setCatalog(CatalogModel value)
    {
        if(value == null || value instanceof ClassificationSystemModel)
        {
            super.setCatalog(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemModel");
        }
    }
}
