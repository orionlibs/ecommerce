package de.hybris.platform.warehousing.returns.service.impl;

import de.hybris.platform.warehousing.model.RestockConfigModel;
import de.hybris.platform.warehousing.returns.RestockException;
import de.hybris.platform.warehousing.returns.dao.RestockConfigDao;
import de.hybris.platform.warehousing.returns.service.RestockConfigService;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRestockConfigService implements RestockConfigService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestockConfigService.class);
    private RestockConfigDao restockConfigDao;


    public RestockConfigModel getRestockConfig() throws RestockException
    {
        return getRestockConfigDao().getRestockConfig();
    }


    public String getReturnedBinCode()
    {
        String returnedBinCode = "";
        try
        {
            RestockConfigModel restockConfigModel = getRestockConfig();
            if(!Objects.isNull(restockConfigModel) && !Objects.isNull(restockConfigModel.getReturnedBinCode()))
            {
                returnedBinCode = getRestockConfig().getReturnedBinCode();
            }
        }
        catch(RestockException e)
        {
            LOGGER.error(e.getMessage(), (Throwable)e);
        }
        return returnedBinCode;
    }


    protected RestockConfigDao getRestockConfigDao()
    {
        return this.restockConfigDao;
    }


    @Required
    public void setRestockConfigDao(RestockConfigDao restockConfigDao)
    {
        this.restockConfigDao = restockConfigDao;
    }
}
