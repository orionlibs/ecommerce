package de.hybris.platform.solrfacetsearch.model.hmc;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ComposedIndexedTypeModel extends ItemModel
{
    public static final String _TYPECODE = "ComposedIndexedType";


    public ComposedIndexedTypeModel()
    {
    }


    public ComposedIndexedTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComposedIndexedTypeModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
