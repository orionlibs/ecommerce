package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class MaintenanceCleanupJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "MaintenanceCleanupJob";
    public static final String THRESHOLD = "threshold";
    public static final String SEARCHTYPE = "searchType";


    public MaintenanceCleanupJobModel()
    {
    }


    public MaintenanceCleanupJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MaintenanceCleanupJobModel(String _code, String _springId)
    {
        setCode(_code);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MaintenanceCleanupJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "searchType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getSearchType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("searchType");
    }


    @Accessor(qualifier = "threshold", type = Accessor.Type.GETTER)
    public Integer getThreshold()
    {
        return (Integer)getPersistenceContext().getPropertyValue("threshold");
    }


    @Accessor(qualifier = "searchType", type = Accessor.Type.SETTER)
    public void setSearchType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("searchType", value);
    }


    @Accessor(qualifier = "threshold", type = Accessor.Type.SETTER)
    public void setThreshold(Integer value)
    {
        getPersistenceContext().setPropertyValue("threshold", value);
    }
}
