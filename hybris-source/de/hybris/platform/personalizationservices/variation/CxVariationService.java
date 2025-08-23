package de.hybris.platform.personalizationservices.variation;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CxVariationService
{
    CxVariationModel createVariation(CxVariationModel paramCxVariationModel, CxCustomizationModel paramCxCustomizationModel, Integer paramInteger);


    Optional<CxVariationModel> getVariation(String paramString, CxCustomizationModel paramCxCustomizationModel);


    Collection<CxVariationModel> getVariations(Collection<CxVariationKey> paramCollection, CatalogVersionModel paramCatalogVersionModel);


    SearchPageData<CxVariationModel> getVariations(CxCustomizationModel paramCxCustomizationModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    List<CxVariationModel> getActiveVariations(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);
}
