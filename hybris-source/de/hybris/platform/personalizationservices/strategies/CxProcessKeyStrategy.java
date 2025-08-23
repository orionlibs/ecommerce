package de.hybris.platform.personalizationservices.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;

public interface CxProcessKeyStrategy
{
    String getProcessKey(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);
}
