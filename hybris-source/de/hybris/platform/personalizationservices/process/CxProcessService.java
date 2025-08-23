package de.hybris.platform.personalizationservices.process;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterType;
import java.util.Map;

public interface CxProcessService
{
    CxPersonalizationProcessModel startPersonalizationCalculationProcess(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    CxPersonalizationProcessModel startPersonalizationCalculationProcess(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel, Map<String, Object> paramMap);


    void loadAllParametersFromProcess(CxPersonalizationProcessModel paramCxPersonalizationProcessModel);


    void storeAllParametersForProcess(CxPersonalizationProcessModel paramCxPersonalizationProcessModel);


    void storeParametersForProcess(CxPersonalizationProcessModel paramCxPersonalizationProcessModel, CxProcessParameterType... paramVarArgs);
}
