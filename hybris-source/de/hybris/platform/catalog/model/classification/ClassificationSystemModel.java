package de.hybris.platform.catalog.model.classification;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ClassificationSystemModel extends CatalogModel
{
    public static final String _TYPECODE = "ClassificationSystem";


    public ClassificationSystemModel()
    {
    }


    public ClassificationSystemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationSystemModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationSystemModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }
}
