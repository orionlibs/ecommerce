package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ScriptChangeConsumptionJobModel extends AbstractChangeProcessorJobModel
{
    public static final String _TYPECODE = "ScriptChangeConsumptionJob";
    public static final String SCRIPTURI = "scriptURI";


    public ScriptChangeConsumptionJobModel()
    {
    }


    public ScriptChangeConsumptionJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptChangeConsumptionJobModel(String _code, MediaModel _input, String _scriptURI, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setScriptURI(_scriptURI);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptChangeConsumptionJobModel(String _code, MediaModel _input, Integer _nodeID, ItemModel _owner, String _scriptURI, String _springId)
    {
        setCode(_code);
        setInput(_input);
        setNodeID(_nodeID);
        setOwner(_owner);
        setScriptURI(_scriptURI);
        setSpringId(_springId);
    }


    @Accessor(qualifier = "scriptURI", type = Accessor.Type.GETTER)
    public String getScriptURI()
    {
        return (String)getPersistenceContext().getPropertyValue("scriptURI");
    }


    @Accessor(qualifier = "scriptURI", type = Accessor.Type.SETTER)
    public void setScriptURI(String value)
    {
        getPersistenceContext().setPropertyValue("scriptURI", value);
    }
}
