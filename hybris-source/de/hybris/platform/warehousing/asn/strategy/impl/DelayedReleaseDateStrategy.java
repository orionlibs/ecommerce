package de.hybris.platform.warehousing.asn.strategy.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.asn.strategy.AsnReleaseDateStrategy;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DelayedReleaseDateStrategy implements AsnReleaseDateStrategy
{
    public static final String DELAY_DAYS = "warehousing.asn.delay.days";
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedReleaseDateStrategy.class);
    private ConfigurationService configurationService;


    public Date getReleaseDateForStockLevel(AdvancedShippingNoticeEntryModel asnEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("asnEntry", asnEntry);
        Calendar cal = Calendar.getInstance();
        cal.setTime(asnEntry.getAsn().getReleaseDate());
        try
        {
            int delayDays = getConfigurationService().getConfiguration().getInt("warehousing.asn.delay.days");
            cal.add(5, delayDays);
            return cal.getTime();
        }
        catch(NoSuchElementException | NumberFormatException e)
        {
            LOGGER.warn("Property {} is missing or not an integer. Using 0 day as default", "warehousing.asn.delay.days");
            cal.add(5, 0);
            return cal.getTime();
        }
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
