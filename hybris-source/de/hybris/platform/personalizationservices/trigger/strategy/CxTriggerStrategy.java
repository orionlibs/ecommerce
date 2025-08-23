package de.hybris.platform.personalizationservices.trigger.strategy;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collection;

public interface CxTriggerStrategy
{
    Collection<CxVariationModel> getVariations(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);
}
