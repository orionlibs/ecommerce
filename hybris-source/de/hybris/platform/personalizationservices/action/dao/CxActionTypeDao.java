package de.hybris.platform.personalizationservices.action.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import java.util.Map;

public interface CxActionTypeDao<T extends de.hybris.platform.personalizationservices.model.CxAbstractActionModel>
{
    SearchPageData<T> getActions(CatalogVersionModel paramCatalogVersionModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    CxActionType getSupportedType();
}
