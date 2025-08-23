package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.WeekDay;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class WeekdayOpeningDayModel extends OpeningDayModel
{
    public static final String _TYPECODE = "WeekdayOpeningDay";
    public static final String DAYOFWEEK = "dayOfWeek";


    public WeekdayOpeningDayModel()
    {
    }


    public WeekdayOpeningDayModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WeekdayOpeningDayModel(WeekDay _dayOfWeek)
    {
        setDayOfWeek(_dayOfWeek);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WeekdayOpeningDayModel(WeekDay _dayOfWeek, ItemModel _owner)
    {
        setDayOfWeek(_dayOfWeek);
        setOwner(_owner);
    }


    @Accessor(qualifier = "dayOfWeek", type = Accessor.Type.GETTER)
    public WeekDay getDayOfWeek()
    {
        return (WeekDay)getPersistenceContext().getPropertyValue("dayOfWeek");
    }


    @Accessor(qualifier = "dayOfWeek", type = Accessor.Type.SETTER)
    public void setDayOfWeek(WeekDay value)
    {
        getPersistenceContext().setPropertyValue("dayOfWeek", value);
    }
}
