package de.hybris.platform.warehousing.returns.service;

import de.hybris.platform.warehousing.model.RestockConfigModel;
import de.hybris.platform.warehousing.returns.RestockException;

public interface RestockConfigService
{
    RestockConfigModel getRestockConfig() throws RestockException;


    String getReturnedBinCode();
}
