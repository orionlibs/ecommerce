package de.hybris.platform.cronjob.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RemoveItemsJobModel extends JobModel
{
    public static final String _TYPECODE = "RemoveItemsJob";


    public RemoveItemsJobModel()
    {
    }


    public RemoveItemsJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveItemsJobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveItemsJobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }
}
