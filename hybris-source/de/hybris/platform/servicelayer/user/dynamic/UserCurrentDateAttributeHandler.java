package de.hybris.platform.servicelayer.user.dynamic;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

public class UserCurrentDateAttributeHandler implements DynamicAttributeHandler<Date, UserModel>
{
    private TimeService timeService;


    public Date get(UserModel model)
    {
        return this.timeService.getCurrentDateWithTimeNormalized();
    }


    public void set(UserModel model, Date value)
    {
        throw new UnsupportedOperationException();
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
