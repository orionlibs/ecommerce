package de.hybris.platform.warehousing.sourcing.ban.service.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.model.SourcingBanModel;
import de.hybris.platform.warehousing.sourcing.ban.dao.SourcingBanDao;
import de.hybris.platform.warehousing.sourcing.ban.service.SourcingBanService;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourcingBanService implements SourcingBanService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSourcingBanService.class);
    protected static final String BAN_DAYS = "warehousing.ban.toobusy.days";
    private SourcingBanDao sourcingBanDao;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private TimeService timeService;


    public SourcingBanModel createSourcingBan(WarehouseModel warehouse)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("warehouse", warehouse);
        LOGGER.debug("Creating SourcingBan for Warehouse: {}", warehouse.getCode());
        SourcingBanModel sourcingBan = (SourcingBanModel)getModelService().create(SourcingBanModel.class);
        sourcingBan.setWarehouse(warehouse);
        getModelService().save(sourcingBan);
        return sourcingBan;
    }


    public Collection<SourcingBanModel> getSourcingBan(Collection<WarehouseModel> warehouses)
    {
        LOGGER.debug("Getting SourcingBans for: {} Warehouses", Integer.valueOf(warehouses.size()));
        return warehouses.isEmpty() ?
                        Collections.EMPTY_LIST :
                        getSourcingBanDao().getSourcingBan(warehouses, getCurrentDateMinusBannedDays());
    }


    protected Date getCurrentDateMinusBannedDays()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimeService().getCurrentTime());
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        try
        {
            int banDays = getConfigurationService().getConfiguration().getInt("warehousing.ban.toobusy.days");
            cal.add(5, -banDays);
            return cal.getTime();
        }
        catch(NumberFormatException e)
        {
            LOGGER.warn("Property {} is missing or not an integer. Using 1 day as default", "warehousing.ban.toobusy.days");
            cal.add(5, -1);
            return cal.getTime();
        }
    }


    protected SourcingBanDao getSourcingBanDao()
    {
        return this.sourcingBanDao;
    }


    @Required
    public void setSourcingBanDao(SourcingBanDao sourcingBanDao)
    {
        this.sourcingBanDao = sourcingBanDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
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
