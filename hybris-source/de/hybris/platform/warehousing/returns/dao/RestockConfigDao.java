package de.hybris.platform.warehousing.returns.dao;

import de.hybris.platform.warehousing.model.RestockConfigModel;
import de.hybris.platform.warehousing.returns.RestockException;

public interface RestockConfigDao
{
    RestockConfigModel getRestockConfig() throws RestockException;
}
