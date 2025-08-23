package de.hybris.platform.warehousing.allocation.strategy.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.allocation.strategy.ShippingDateStrategy;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class NextDayShippingDateStrategy implements ShippingDateStrategy
{
    private TimeService timeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NextDayShippingDateStrategy.class);


    public Date getExpectedShippingDate(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNull(consignment, "Consignment cannot be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimeService().getCurrentTime());
        cal.add(5, 1);
        LOGGER.debug("Adding 1 day as default delay in consignment's shipping");
        return cal.getTime();
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
