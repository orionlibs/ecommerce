package de.hybris.platform.personalizationservices.trigger.dao;

import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.Optional;

public interface CxTriggerDao extends Dao
{
    Collection<CxAbstractTriggerModel> findTriggers(CxVariationModel paramCxVariationModel);


    Optional<CxAbstractTriggerModel> findTriggerByCode(String paramString, CxVariationModel paramCxVariationModel);
}
