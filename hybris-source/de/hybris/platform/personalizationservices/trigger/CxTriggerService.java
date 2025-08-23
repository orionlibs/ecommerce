package de.hybris.platform.personalizationservices.trigger;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collection;
import java.util.Optional;

public interface CxTriggerService
{
    CxAbstractTriggerModel createTrigger(CxAbstractTriggerModel paramCxAbstractTriggerModel, CxVariationModel paramCxVariationModel);


    Optional<CxAbstractTriggerModel> getTrigger(String paramString, CxVariationModel paramCxVariationModel);


    Collection<CxAbstractTriggerModel> getTriggers(CxVariationModel paramCxVariationModel);


    Collection<CxVariationModel> getVariationsForUser(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);
}
