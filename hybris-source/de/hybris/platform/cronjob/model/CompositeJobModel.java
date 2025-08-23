package de.hybris.platform.cronjob.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CompositeJobModel extends JobModel
{
    public static final String _TYPECODE = "CompositeJob";


    public CompositeJobModel()
    {
    }


    public CompositeJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeJobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeJobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }
}
