package de.hybris.deltadetection.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsumeAllChangesJobModel extends AbstractChangeProcessorJobModel
{
    public static final String _TYPECODE = "ConsumeAllChangesJob";


    public ConsumeAllChangesJobModel()
    {
    }


    public ConsumeAllChangesJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumeAllChangesJobModel(String _code, MediaModel _input, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumeAllChangesJobModel(String _code, MediaModel _input, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }
}
