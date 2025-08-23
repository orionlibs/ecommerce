package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class BatchJobModel extends JobModel
{
    public static final String _TYPECODE = "BatchJob";
    public static final String STEPS = "steps";


    public BatchJobModel()
    {
    }


    public BatchJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BatchJobModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BatchJobModel(String _code, Integer _nodeID, ItemModel _owner)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
    }


    @Accessor(qualifier = "steps", type = Accessor.Type.GETTER)
    public List<StepModel> getSteps()
    {
        return (List<StepModel>)getPersistenceContext().getPropertyValue("steps");
    }


    @Accessor(qualifier = "steps", type = Accessor.Type.SETTER)
    public void setSteps(List<StepModel> value)
    {
        getPersistenceContext().setPropertyValue("steps", value);
    }
}
