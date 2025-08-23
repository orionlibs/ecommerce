package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ChangeDetectionJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "ChangeDetectionJob";
    public static final String TYPEPK = "typePK";
    public static final String STREAMID = "streamId";
    public static final String OUTPUT = "output";


    public ChangeDetectionJobModel()
    {
    }


    public ChangeDetectionJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ChangeDetectionJobModel(String _code, String _springId, String _streamId, ComposedTypeModel _typePK)
    {
        setCode(_code);
        setSpringId(_springId);
        setStreamId(_streamId);
        setTypePK(_typePK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ChangeDetectionJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId, String _streamId, ComposedTypeModel _typePK)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
        setStreamId(_streamId);
        setTypePK(_typePK);
    }


    @Accessor(qualifier = "output", type = Accessor.Type.GETTER)
    public MediaModel getOutput()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("output");
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
    public String getStreamId()
    {
        return (String)getPersistenceContext().getPropertyValue("streamId");
    }


    @Accessor(qualifier = "typePK", type = Accessor.Type.GETTER)
    public ComposedTypeModel getTypePK()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("typePK");
    }


    @Accessor(qualifier = "output", type = Accessor.Type.SETTER)
    public void setOutput(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("output", value);
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
    public void setStreamId(String value)
    {
        getPersistenceContext().setPropertyValue("streamId", value);
    }


    @Accessor(qualifier = "typePK", type = Accessor.Type.SETTER)
    public void setTypePK(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("typePK", value);
    }
}
