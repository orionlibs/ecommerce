package de.hybris.deltadetection.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ItemVersionMarkerModel extends ItemModel
{
    public static final String _TYPECODE = "ItemVersionMarker";
    public static final String ITEMPK = "itemPK";
    public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
    public static final String VERSIONTS = "versionTS";
    public static final String VERSIONVALUE = "versionValue";
    public static final String LASTVERSIONVALUE = "lastVersionValue";
    public static final String INFO = "info";
    public static final String STREAMID = "streamId";
    public static final String STATUS = "status";


    public ItemVersionMarkerModel()
    {
    }


    public ItemVersionMarkerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemVersionMarkerModel(ComposedTypeModel _itemComposedType, Long _itemPK, ItemVersionMarkerStatus _status, String _streamId, Date _versionTS)
    {
        setItemComposedType(_itemComposedType);
        setItemPK(_itemPK);
        setStatus(_status);
        setStreamId(_streamId);
        setVersionTS(_versionTS);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemVersionMarkerModel(ComposedTypeModel _itemComposedType, Long _itemPK, ItemModel _owner, ItemVersionMarkerStatus _status, String _streamId, Date _versionTS)
    {
        setItemComposedType(_itemComposedType);
        setItemPK(_itemPK);
        setOwner(_owner);
        setStatus(_status);
        setStreamId(_streamId);
        setVersionTS(_versionTS);
    }


    @Accessor(qualifier = "info", type = Accessor.Type.GETTER)
    public String getInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("info");
    }


    @Accessor(qualifier = "itemComposedType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getItemComposedType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("itemComposedType");
    }


    @Accessor(qualifier = "itemPK", type = Accessor.Type.GETTER)
    public Long getItemPK()
    {
        return (Long)getPersistenceContext().getPropertyValue("itemPK");
    }


    @Accessor(qualifier = "lastVersionValue", type = Accessor.Type.GETTER)
    public String getLastVersionValue()
    {
        return (String)getPersistenceContext().getPropertyValue("lastVersionValue");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ItemVersionMarkerStatus getStatus()
    {
        return (ItemVersionMarkerStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.GETTER)
    public String getStreamId()
    {
        return (String)getPersistenceContext().getPropertyValue("streamId");
    }


    @Accessor(qualifier = "versionTS", type = Accessor.Type.GETTER)
    public Date getVersionTS()
    {
        return (Date)getPersistenceContext().getPropertyValue("versionTS");
    }


    @Accessor(qualifier = "versionValue", type = Accessor.Type.GETTER)
    public String getVersionValue()
    {
        return (String)getPersistenceContext().getPropertyValue("versionValue");
    }


    @Accessor(qualifier = "info", type = Accessor.Type.SETTER)
    public void setInfo(String value)
    {
        getPersistenceContext().setPropertyValue("info", value);
    }


    @Accessor(qualifier = "itemComposedType", type = Accessor.Type.SETTER)
    public void setItemComposedType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("itemComposedType", value);
    }


    @Accessor(qualifier = "itemPK", type = Accessor.Type.SETTER)
    public void setItemPK(Long value)
    {
        getPersistenceContext().setPropertyValue("itemPK", value);
    }


    @Accessor(qualifier = "lastVersionValue", type = Accessor.Type.SETTER)
    public void setLastVersionValue(String value)
    {
        getPersistenceContext().setPropertyValue("lastVersionValue", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ItemVersionMarkerStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "streamId", type = Accessor.Type.SETTER)
    public void setStreamId(String value)
    {
        getPersistenceContext().setPropertyValue("streamId", value);
    }


    @Accessor(qualifier = "versionTS", type = Accessor.Type.SETTER)
    public void setVersionTS(Date value)
    {
        getPersistenceContext().setPropertyValue("versionTS", value);
    }


    @Accessor(qualifier = "versionValue", type = Accessor.Type.SETTER)
    public void setVersionValue(String value)
    {
        getPersistenceContext().setPropertyValue("versionValue", value);
    }
}
