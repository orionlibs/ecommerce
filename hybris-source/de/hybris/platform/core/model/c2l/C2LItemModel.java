package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class C2LItemModel extends ItemModel
{
    public static final String _TYPECODE = "C2LItem";
    public static final String ACTIVE = "active";
    public static final String ISOCODE = "isocode";
    public static final String NAME = "name";


    public C2LItemModel()
    {
    }


    public C2LItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public C2LItemModel(String _isocode)
    {
        setIsocode(_isocode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public C2LItemModel(String _isocode, ItemModel _owner)
    {
        setIsocode(_isocode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("active");
        return (value != null) ? value : Boolean.valueOf(false);
    }


    @Accessor(qualifier = "isocode", type = Accessor.Type.GETTER)
    public String getIsocode()
    {
        return (String)getPersistenceContext().getPropertyValue("isocode");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "isocode", type = Accessor.Type.SETTER)
    public void setIsocode(String value)
    {
        getPersistenceContext().setPropertyValue("isocode", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }
}
