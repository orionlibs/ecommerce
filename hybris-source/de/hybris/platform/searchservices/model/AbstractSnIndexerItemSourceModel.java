package de.hybris.platform.searchservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractSnIndexerItemSourceModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractSnIndexerItemSource";


    public AbstractSnIndexerItemSourceModel()
    {
    }


    public AbstractSnIndexerItemSourceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractSnIndexerItemSourceModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
