package de.hybris.platform.personalizationservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import java.util.Collection;
import java.util.List;

public interface CxService
{
    List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel paramUserModel);


    List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel paramUserModel, Collection<CatalogVersionModel> paramCollection);


    List<CxPersonalizationProcessModel> startPersonalizationCalculationProcesses(UserModel paramUserModel, CxCalculationContext paramCxCalculationContext);


    void calculateAndStoreDefaultPersonalization(Collection<CatalogVersionModel> paramCollection);


    void calculateAndStorePersonalization(UserModel paramUserModel);


    void calculateAndStorePersonalization(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void clearPersonalizationInSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void loadPersonalizationInSession(UserModel paramUserModel);


    void loadPersonalizationInSession(UserModel paramUserModel, Collection<CatalogVersionModel> paramCollection);


    List<CxAbstractActionResult> getActionResultsFromSession(UserModel paramUserModel);


    List<CxAbstractActionResult> getActionResultsFromSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void calculateAndLoadPersonalizationInSession(UserModel paramUserModel);


    void calculateAndLoadPersonalizationInSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void calculateAndLoadPersonalizationInSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel, Collection<CxVariationModel> paramCollection);
}
