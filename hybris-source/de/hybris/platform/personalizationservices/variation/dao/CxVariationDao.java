package de.hybris.platform.personalizationservices.variation.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CxVariationDao extends Dao
{
    Optional<CxVariationModel> findVariationByCode(String paramString, CxCustomizationModel paramCxCustomizationModel);


    Collection<CxVariationModel> findVariations(Collection<CxVariationKey> paramCollection, CatalogVersionModel paramCatalogVersionModel);


    SearchPageData<CxVariationModel> findVariations(CxCustomizationModel paramCxCustomizationModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);
}
