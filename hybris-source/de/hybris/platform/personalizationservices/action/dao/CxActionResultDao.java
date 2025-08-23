package de.hybris.platform.personalizationservices.action.dao;

import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;
import java.util.Optional;

public interface CxActionResultDao extends Dao
{
    Optional<CxResultsModel> findResultsByKey(String paramString);


    List<CxResultsModel> findAllResultsByKey(String paramString);


    List<CxResultsModel> findResultsBySessionKey(String paramString);
}
