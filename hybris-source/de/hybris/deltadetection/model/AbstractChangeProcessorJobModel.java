package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractChangeProcessorJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "AbstractChangeProcessorJob";
    public static final String INPUT = "input";


    public AbstractChangeProcessorJobModel()
    {
    }


    public AbstractChangeProcessorJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractChangeProcessorJobModel(String _code, MediaModel _input, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractChangeProcessorJobModel(String _code, MediaModel _input, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "input", type = Accessor.Type.GETTER)
    public MediaModel getInput()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("input");
    }


    @Accessor(qualifier = "input", type = Accessor.Type.SETTER)
    public void setInput(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("input", value);
    }
}
