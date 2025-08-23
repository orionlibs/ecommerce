package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class OpeningDayModel extends ItemModel
{
    public static final String _TYPECODE = "OpeningDay";
    public static final String OPENINGTIME = "openingTime";
    public static final String CLOSINGTIME = "closingTime";
    public static final String OPENINGSCHEDULE = "openingSchedule";


    public OpeningDayModel()
    {
    }


    public OpeningDayModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OpeningDayModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "closingTime", type = Accessor.Type.GETTER)
    public Date getClosingTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("closingTime");
    }


    @Accessor(qualifier = "openingSchedule", type = Accessor.Type.GETTER)
    public OpeningScheduleModel getOpeningSchedule()
    {
        return (OpeningScheduleModel)getPersistenceContext().getPropertyValue("openingSchedule");
    }


    @Accessor(qualifier = "openingTime", type = Accessor.Type.GETTER)
    public Date getOpeningTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("openingTime");
    }


    @Accessor(qualifier = "closingTime", type = Accessor.Type.SETTER)
    public void setClosingTime(Date value)
    {
        getPersistenceContext().setPropertyValue("closingTime", value);
    }


    @Accessor(qualifier = "openingSchedule", type = Accessor.Type.SETTER)
    public void setOpeningSchedule(OpeningScheduleModel value)
    {
        getPersistenceContext().setPropertyValue("openingSchedule", value);
    }


    @Accessor(qualifier = "openingTime", type = Accessor.Type.SETTER)
    public void setOpeningTime(Date value)
    {
        getPersistenceContext().setPropertyValue("openingTime", value);
    }
}
