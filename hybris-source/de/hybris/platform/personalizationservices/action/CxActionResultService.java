package de.hybris.platform.personalizationservices.action;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CxActionResultService
{
    void setActionResultsInSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel, List<CxAbstractActionResult> paramList);


    void clearActionResultsInSession(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    List<CxAbstractActionResult> getActionResults(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void loadActionResultsInSession(UserModel paramUserModel, Collection<CatalogVersionModel> paramCollection);


    void storeActionResults(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel, List<CxAbstractActionResult> paramList);


    Optional<CxResultsModel> getCxResults(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    void storeDefaultActionResults(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel, List<CxAbstractActionResult> paramList);
}
