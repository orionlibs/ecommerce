package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ScriptingJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "ScriptingJob";
    public static final String SCRIPTURI = "scriptURI";


    public ScriptingJobModel()
    {
    }


    public ScriptingJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptingJobModel(String _code, String _scriptURI, String _springId)
    {
        setCode(_code);
        setScriptURI(_scriptURI);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ScriptingJobModel(String _code, Integer _nodeID, ItemModel _owner, String _scriptURI, String _springId)
    {
        setCode(_code);
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
