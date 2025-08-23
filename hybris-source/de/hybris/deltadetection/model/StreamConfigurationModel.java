package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class StreamConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "StreamConfiguration";
    public static final String _STREAMCONFIGURATIONCONTAINER2STREAMCONFIGURATION = "StreamConfigurationContainer2StreamConfiguration";
    public static final String STREAMID = "streamId";
    public static final String ITEMTYPEFORSTREAM = "itemTypeForStream";
    public static final String WHERECLAUSE = "whereClause";
    public static final String VERSIONSELECTCLAUSE = "versionSelectClause";
    public static final String ACTIVE = "active";
    public static final String INFOEXPRESSION = "infoExpression";
    public static final String CONTAINER = "container";
    public static final String EXCLUDEDTYPES = "excludedTypes";


    public StreamConfigurationModel()
    {
    }


    public StreamConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StreamConfigurationModel(StreamConfigurationContainerModel _container, ComposedTypeModel _itemTypeForStream, String _streamId)
    {
        setContainer(_container);
        setItemTypeForStream(_itemTypeForStream);
        setStreamId(_streamId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StreamConfigurationModel(StreamConfigurationContainerModel _container, ComposedTypeModel _itemTypeForStream, ItemModel _owner, String _streamId)
    {
        setContainer(_container);
        setItemTypeForStream(_itemTypeForStream);
        setOwner(_owner);
        setStreamId(_streamId);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "container", type = Accessor.Type.GETTER)
    public StreamConfigurationContainerModel getContainer()
    {
        return (StreamConfigurationContainerModel)getPersistenceContext().getPropertyValue("container");
    }


    @Accessor(qualifier = "excludedTypes", type = Accessor.Type.GETTER)
    public Set<ComposedTypeModel> getExcludedTypes()
    {
        return (Set<ComposedTypeModel>)getPersistenceContext().getPropertyValue("excludedTypes");
    }


    @Accessor(qualifier = "infoExpression", type = Accessor.Type.GETTER)
    public String getInfoExpression()
    {
        return (String)getPersistenceContext().getPropertyValue("infoExpression");
    }


    @Accessor(qualifier = "itemTypeForStream", type = Accessor.Type.GETTER)
    public ComposedTypeModel getItemTypeForStream()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("itemTypeForStream");
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
    public String getStreamId()
    {
        return (String)getPersistenceContext().getPropertyValue("streamId");
    }


    @Accessor(qualifier = "versionSelectClause", type = Accessor.Type.GETTER)
    public String getVersionSelectClause()
    {
        return (String)getPersistenceContext().getPropertyValue("versionSelectClause");
    }


    @Accessor(qualifier = "whereClause", type = Accessor.Type.GETTER)
    public String getWhereClause()
    {
        return (String)getPersistenceContext().getPropertyValue("whereClause");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "container", type = Accessor.Type.SETTER)
    public void setContainer(StreamConfigurationContainerModel value)
    {
        getPersistenceContext().setPropertyValue("container", value);
    }


    @Accessor(qualifier = "excludedTypes", type = Accessor.Type.SETTER)
    public void setExcludedTypes(Set<ComposedTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("excludedTypes", value);
    }


    @Accessor(qualifier = "infoExpression", type = Accessor.Type.SETTER)
    public void setInfoExpression(String value)
    {
        getPersistenceContext().setPropertyValue("infoExpression", value);
    }


    @Accessor(qualifier = "itemTypeForStream", type = Accessor.Type.SETTER)
    public void setItemTypeForStream(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("itemTypeForStream", value);
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
    public void setStreamId(String value)
    {
        getPersistenceContext().setPropertyValue("streamId", value);
    }


    @Accessor(qualifier = "versionSelectClause", type = Accessor.Type.SETTER)
    public void setVersionSelectClause(String value)
    {
        getPersistenceContext().setPropertyValue("versionSelectClause", value);
    }


    @Accessor(qualifier = "whereClause", type = Accessor.Type.SETTER)
    public void setWhereClause(String value)
    {
        getPersistenceContext().setPropertyValue("whereClause", value);
    }
}
