package de.hybris.platform.personalizationservices.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface ProcessSelectionStrategy
{
    String retrieveProcessDefinitionName(UserModel paramUserModel, Collection<CatalogVersionModel> paramCollection);
}
