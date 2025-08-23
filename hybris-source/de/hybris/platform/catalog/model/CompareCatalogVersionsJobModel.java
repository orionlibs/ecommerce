package de.hybris.platform.catalog.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CompareCatalogVersionsJobModel extends JobModel
{
    public static final String _TYPECODE = "CompareCatalogVersionsJob";


    public CompareCatalogVersionsJobModel()
    {
    }


    public CompareCatalogVersionsJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompareCatalogVersionsJobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompareCatalogVersionsJobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }
}
