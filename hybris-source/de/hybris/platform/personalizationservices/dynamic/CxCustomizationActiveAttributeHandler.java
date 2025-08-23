package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Date;

public class CxCustomizationActiveAttributeHandler implements DynamicAttributeHandler<Boolean, CxCustomizationModel>
{
    private TimeService timeService;


    public Boolean get(CxCustomizationModel model)
    {
        boolean active = (model.getStatus() == CxItemStatus.ENABLED && isActive(model.getEnabledStartDate(), model.getEnabledEndDate(), this.timeService.getCurrentTime()));
        return Boolean.valueOf(active);
    }


    protected boolean isActive(Date startDate, Date endDate, Date current)
    {
        boolean before = (startDate == null || startDate.before(current));
        boolean after = (endDate == null || endDate.after(current));
        return (before && after);
    }


    public void set(CxCustomizationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("This is read only attribute");
    }


    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }
}
