package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AsSearchProvider
{
    Set<AsFeatureFlag> getSupportedFeatures(String paramString);


    List<String> getSupportedQueryContexts(String paramString);


    List<AsIndexConfigurationData> getIndexConfigurations();


    Optional<AsIndexConfigurationData> getIndexConfigurationForCode(String paramString);


    List<AsIndexTypeData> getIndexTypes();


    List<AsIndexTypeData> getIndexTypes(String paramString);


    Optional<AsIndexTypeData> getIndexTypeForCode(String paramString);


    List<AsIndexPropertyData> getIndexProperties(String paramString);


    Optional<AsIndexPropertyData> getIndexPropertyForCode(String paramString1, String paramString2);


    List<CatalogVersionModel> getSupportedCatalogVersions(String paramString1, String paramString2);


    List<LanguageModel> getSupportedLanguages(String paramString1, String paramString2);


    List<CurrencyModel> getSupportedCurrencies(String paramString1, String paramString2);


    List<AsExpressionData> getSupportedFacetExpressions(String paramString);


    boolean isValidFacetExpression(String paramString1, String paramString2);


    List<AsFacetType> getSupportedFacetTypes(String paramString);


    boolean isValidFacetType(String paramString, AsFacetType paramAsFacetType);


    List<AsFacetSortData> getSupportedFacetSorts(String paramString);


    boolean isValidFacetSort(String paramString1, String paramString2);


    List<AsBoostType> getSupportedBoostTypes(String paramString);


    boolean isValidBoostType(String paramString, AsBoostType paramAsBoostType);


    List<AsExpressionData> getSupportedSortExpressions(String paramString);


    boolean isValidSortExpression(String paramString1, String paramString2);


    List<AsExpressionData> getSupportedGroupExpressions(String paramString);


    boolean isValidGroupExpression(String paramString1, String paramString2);


    List<String> getAvailableQualifiers(String paramString1, String paramString2) throws AsException;


    AsSearchResultData search(AsSearchProfileContext paramAsSearchProfileContext, AsSearchQueryData paramAsSearchQueryData) throws AsException;
}
