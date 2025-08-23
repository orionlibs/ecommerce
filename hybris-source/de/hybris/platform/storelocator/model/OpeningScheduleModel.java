package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class OpeningScheduleModel extends ItemModel
{
    public static final String _TYPECODE = "OpeningSchedule";
    public static final String _OPENINGDAYS2SCHEDULERELATION = "OpeningDays2ScheduleRelation";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String OPENINGDAYS = "openingDays";


    public OpeningScheduleModel()
    {
    }


    public OpeningScheduleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpeningScheduleModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpeningScheduleModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
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


    @Accessor(qualifier = "openingDays", type = Accessor.Type.GETTER)
    public Collection<OpeningDayModel> getOpeningDays()
    {
        return (Collection<OpeningDayModel>)getPersistenceContext().getPropertyValue("openingDays");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
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


    @Accessor(qualifier = "openingDays", type = Accessor.Type.SETTER)
    public void setOpeningDays(Collection<OpeningDayModel> value)
    {
        getPersistenceContext().setPropertyValue("openingDays", value);
    }
}
