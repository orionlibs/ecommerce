package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImpExImportJobModel extends JobModel
{
    public static final String _TYPECODE = "ImpExImportJob";
    public static final String MAXTHREADS = "maxThreads";


    public ImpExImportJobModel()
    {
    }


    public ImpExImportJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExImportJobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExImportJobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
    public Integer getMaxThreads()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxThreads");
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
    public void setMaxThreads(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxThreads", value);
    }
}
