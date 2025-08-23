package de.hybris.platform.personalizationservices.action.dao;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CxActionDao extends Dao
{
    Optional<CxAbstractActionModel> findActionByCode(String paramString, CxVariationModel paramCxVariationModel);


    List<CxAbstractActionModel> findActions(CxVariationModel paramCxVariationModel);


    List<CxAbstractActionModel> findActionsForVariations(Collection<CxVariationModel> paramCollection);
}
