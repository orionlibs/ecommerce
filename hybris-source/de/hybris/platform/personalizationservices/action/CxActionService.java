package de.hybris.platform.personalizationservices.action;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CxActionService
{
    Optional<CxAbstractActionModel> getAction(String paramString, CxVariationModel paramCxVariationModel);


    List<CxAbstractActionModel> getActions(CxVariationModel paramCxVariationModel);


    <T extends CxAbstractActionModel> SearchPageData<T> getActions(CxActionType paramCxActionType, CatalogVersionModel paramCatalogVersionModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    List<CxAbstractActionModel> getActionsForVariations(Collection<CxVariationModel> paramCollection);


    CxAbstractActionModel createAction(CxAbstractActionModel paramCxAbstractActionModel, CxVariationModel paramCxVariationModel);


    void deleteAction(CxAbstractActionModel paramCxAbstractActionModel);
}
