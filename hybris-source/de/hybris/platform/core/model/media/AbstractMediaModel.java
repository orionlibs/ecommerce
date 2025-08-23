package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractMediaModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractMedia";
    public static final String MIME = "mime";
    public static final String SIZE = "size";
    public static final String DATAPK = "dataPK";
    public static final String LOCATION = "location";
    public static final String LOCATIONHASH = "locationHash";
    public static final String REALFILENAME = "realFileName";


    public AbstractMediaModel()
    {
    }


    public AbstractMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractMediaModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "dataPK", type = Accessor.Type.GETTER)
    public Long getDataPK()
    {
        return (Long)getPersistenceContext().getPropertyValue("dataPK");
    }


    @Accessor(qualifier = "location", type = Accessor.Type.GETTER)
    public String getLocation()
    {
        return (String)getPersistenceContext().getPropertyValue("location");
    }


    @Accessor(qualifier = "locationHash", type = Accessor.Type.GETTER)
    public String getLocationHash()
    {
        return (String)getPersistenceContext().getPropertyValue("locationHash");
    }


    @Accessor(qualifier = "mime", type = Accessor.Type.GETTER)
    public String getMime()
    {
        return (String)getPersistenceContext().getPropertyValue("mime");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getRealfilename()
    {
        return getRealFileName();
    }


    @Accessor(qualifier = "realFileName", type = Accessor.Type.GETTER)
    public String getRealFileName()
    {
        return (String)getPersistenceContext().getPropertyValue("realFileName");
    }


    @Accessor(qualifier = "size", type = Accessor.Type.GETTER)
    public Long getSize()
    {
        return (Long)getPersistenceContext().getPropertyValue("size");
    }


    @Accessor(qualifier = "dataPK", type = Accessor.Type.SETTER)
    public void setDataPK(Long value)
    {
        getPersistenceContext().setPropertyValue("dataPK", value);
    }


    @Accessor(qualifier = "location", type = Accessor.Type.SETTER)
    public void setLocation(String value)
    {
        getPersistenceContext().setPropertyValue("location", value);
    }


    @Accessor(qualifier = "locationHash", type = Accessor.Type.SETTER)
    public void setLocationHash(String value)
    {
        getPersistenceContext().setPropertyValue("locationHash", value);
    }


    @Accessor(qualifier = "mime", type = Accessor.Type.SETTER)
    public void setMime(String value)
    {
        getPersistenceContext().setPropertyValue("mime", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setRealfilename(String value)
    {
        setRealFileName(value);
    }


    @Accessor(qualifier = "realFileName", type = Accessor.Type.SETTER)
    public void setRealFileName(String value)
    {
        getPersistenceContext().setPropertyValue("realFileName", value);
    }


    @Accessor(qualifier = "size", type = Accessor.Type.SETTER)
    public void setSize(Long value)
    {
        getPersistenceContext().setPropertyValue("size", value);
    }
}
