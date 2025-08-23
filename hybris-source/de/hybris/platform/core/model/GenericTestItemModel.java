package de.hybris.platform.core.model;

import de.hybris.platform.servicelayer.model.ItemModelContext;

public class GenericTestItemModel extends ItemModel
{
    public static final String _TYPECODE = "GenericTestItem";


    public GenericTestItemModel()
    {
    }


    public GenericTestItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GenericTestItemModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
