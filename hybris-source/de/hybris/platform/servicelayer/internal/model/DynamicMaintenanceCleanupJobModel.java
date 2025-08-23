package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DynamicMaintenanceCleanupJobModel extends MaintenanceCleanupJobModel
{
    public static final String _TYPECODE = "DynamicMaintenanceCleanupJob";
    public static final String SEARCHSCRIPT = "searchScript";
    public static final String PROCESSSCRIPT = "processScript";


    public DynamicMaintenanceCleanupJobModel()
    {
    }


    public DynamicMaintenanceCleanupJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicMaintenanceCleanupJobModel(String _code, String _springId)
    {
        setCode(_code);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicMaintenanceCleanupJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "processScript", type = Accessor.Type.GETTER)
    public String getProcessScript()
    {
        return (String)getPersistenceContext().getPropertyValue("processScript");
    }


    @Accessor(qualifier = "searchScript", type = Accessor.Type.GETTER)
    public String getSearchScript()
    {
        return (String)getPersistenceContext().getPropertyValue("searchScript");
    }


    @Accessor(qualifier = "processScript", type = Accessor.Type.SETTER)
    public void setProcessScript(String value)
    {
        getPersistenceContext().setPropertyValue("processScript", value);
    }


    @Accessor(qualifier = "searchScript", type = Accessor.Type.SETTER)
    public void setSearchScript(String value)
    {
        getPersistenceContext().setPropertyValue("searchScript", value);
    }
}
