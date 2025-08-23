package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProcessTaskLogMaintenanceJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "ProcessTaskLogMaintenanceJob";
    public static final String AGE = "age";
    public static final String NUMBEROFLOGS = "numberOfLogs";
    public static final String QUERYCOUNT = "queryCount";


    public ProcessTaskLogMaintenanceJobModel()
    {
    }


    public ProcessTaskLogMaintenanceJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskLogMaintenanceJobModel(int _age, String _code, int _numberOfLogs, String _springId)
    {
        setAge(_age);
        setCode(_code);
        setNumberOfLogs(_numberOfLogs);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProcessTaskLogMaintenanceJobModel(int _age, String _code, Integer _nodeID, int _numberOfLogs, ItemModel _owner, String _springId)
    {
        setAge(_age);
        setCode(_code);
        setNodeID(_nodeID);
        setNumberOfLogs(_numberOfLogs);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "age", type = Accessor.Type.GETTER)
    public int getAge()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("age"));
    }


    @Accessor(qualifier = "numberOfLogs", type = Accessor.Type.GETTER)
    public int getNumberOfLogs()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("numberOfLogs"));
    }


    @Accessor(qualifier = "queryCount", type = Accessor.Type.GETTER)
    public int getQueryCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("queryCount"));
    }


    @Accessor(qualifier = "age", type = Accessor.Type.SETTER)
    public void setAge(int value)
    {
        getPersistenceContext().setPropertyValue("age", toObject(value));
    }


    @Accessor(qualifier = "numberOfLogs", type = Accessor.Type.SETTER)
    public void setNumberOfLogs(int value)
    {
        getPersistenceContext().setPropertyValue("numberOfLogs", toObject(value));
    }


    @Accessor(qualifier = "queryCount", type = Accessor.Type.SETTER)
    public void setQueryCount(int value)
    {
        getPersistenceContext().setPropertyValue("queryCount", toObject(value));
    }
}
