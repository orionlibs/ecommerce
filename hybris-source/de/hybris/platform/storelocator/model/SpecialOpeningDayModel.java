package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Locale;

public class SpecialOpeningDayModel extends OpeningDayModel
{
    public static final String _TYPECODE = "SpecialOpeningDay";
    public static final String DATE = "date";
    public static final String CLOSED = "closed";
    public static final String NAME = "name";
    public static final String MESSAGE = "message";


    public SpecialOpeningDayModel()
    {
    }


    public SpecialOpeningDayModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SpecialOpeningDayModel(Date _date)
    {
        setDate(_date);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SpecialOpeningDayModel(Date _date, ItemModel _owner)
    {
        setDate(_date);
        setOwner(_owner);
    }


    @Accessor(qualifier = "date", type = Accessor.Type.GETTER)
    public Date getDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("date");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return getMessage(null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("message", loc);
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


    @Accessor(qualifier = "closed", type = Accessor.Type.GETTER)
    public boolean isClosed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("closed"));
    }


    @Accessor(qualifier = "closed", type = Accessor.Type.SETTER)
    public void setClosed(boolean value)
    {
        getPersistenceContext().setPropertyValue("closed", toObject(value));
    }


    @Accessor(qualifier = "date", type = Accessor.Type.SETTER)
    public void setDate(Date value)
    {
        getPersistenceContext().setPropertyValue("date", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        setMessage(value, null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("message", loc, value);
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
