package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.strategies.AsFeatureFlag;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

public class MockAsSearchProvider implements AsSearchProvider, InitializingBean
{
    protected static final String QUERY_CONTEXT_DEFAULT = "DEFAULT";
    protected static final String INDEX_CONFIGURATION = "testConfiguration";
    protected static final String INDEX_TYPE = "testIndex";
    protected static final String INDEX_TYPE1 = "testIndex1";
    protected static final String INDEX_TYPE2 = "testIndex2";
    protected static final String INDEX_TYPE_NO_CATALOG_VERSION = "indexNoCatalogVersion";
    protected static final String SCORE_EXPRESSION = "score";
    protected static final String INDEX_PROPERTY_CODE1 = "property1";
    protected static final String INDEX_PROPERTY_CODE2 = "property2";
    protected static final String INDEX_PROPERTY_CODE3 = "property3";
    protected static final String INDEX_PROPERTY_CODE4 = "property4";
    private final Map<String, AsIndexConfigurationData> indexConfigurations = new HashMap<>();
    private final Map<String, AsIndexTypeData> indexTypes = new HashMap<>();
    private final Map<String, AsIndexPropertyData> indexProperties = new HashMap<>();


    public void afterPropertiesSet()
    {
        AsIndexConfigurationData indexConfiguration = new AsIndexConfigurationData();
        indexConfiguration.setCode("testConfiguration");
        indexConfiguration.setName("testConfiguration");
        this.indexConfigurations.put(indexConfiguration.getCode(), indexConfiguration);
        AsIndexTypeData indexType = new AsIndexTypeData();
        indexType.setCode("testIndex");
        indexType.setName("testIndex");
        AsIndexTypeData indexType1 = new AsIndexTypeData();
        indexType1.setCode("testIndex1");
        indexType1.setName("testIndex1");
        AsIndexTypeData indexType2 = new AsIndexTypeData();
        indexType2.setCode("testIndex2");
        indexType2.setName("testIndex2");
        AsIndexTypeData indexTypeNoCatalogVersion = new AsIndexTypeData();
        indexTypeNoCatalogVersion.setCode("indexNoCatalogVersion");
        indexTypeNoCatalogVersion.setName("indexNoCatalogVersion");
        this.indexTypes.put(indexType.getCode(), indexType);
        this.indexTypes.put(indexType1.getCode(), indexType1);
        this.indexTypes.put(indexType2.getCode(), indexType2);
        this.indexTypes.put(indexTypeNoCatalogVersion.getCode(), indexTypeNoCatalogVersion);
        AsIndexPropertyData indexProperty1 = new AsIndexPropertyData();
        indexProperty1.setCode("property1");
        indexProperty1.setName("property1");
        indexProperty1.setType(String.class);
        indexProperty1.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));
        AsIndexPropertyData indexProperty2 = new AsIndexPropertyData();
        indexProperty2.setCode("property2");
        indexProperty2.setName("property2");
        indexProperty2.setType(String.class);
        indexProperty2.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));
        AsIndexPropertyData indexProperty3 = new AsIndexPropertyData();
        indexProperty3.setCode("property3");
        indexProperty3.setName("property3");
        indexProperty3.setType(Float.class);
        indexProperty3.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.GREATER_THAN));
        AsIndexPropertyData indexProperty4 = new AsIndexPropertyData();
        indexProperty4.setCode("property4");
        indexProperty4.setName("property4");
        indexProperty4.setType(String.class);
        indexProperty4.setSupportedBoostOperators(Collections.singleton(AsBoostOperator.EQUAL));
        this.indexProperties.put(indexProperty1.getCode(), indexProperty1);
        this.indexProperties.put(indexProperty2.getCode(), indexProperty2);
        this.indexProperties.put(indexProperty3.getCode(), indexProperty3);
        this.indexProperties.put(indexProperty4.getCode(), indexProperty4);
    }


    public Set<AsFeatureFlag> getSupportedFeatures(String indexType)
    {
        return EnumSet.allOf(AsFeatureFlag.class);
    }


    public List<String> getSupportedQueryContexts(String indexType)
    {
        return List.of("DEFAULT");
    }


    public List<AsIndexConfigurationData> getIndexConfigurations()
    {
        return new ArrayList<>(this.indexConfigurations.values());
    }


    public Optional<AsIndexConfigurationData> getIndexConfigurationForCode(String code)
    {
        return Optional.ofNullable(this.indexConfigurations.get(code));
    }


    public List<AsIndexTypeData> getIndexTypes()
    {
        return new ArrayList<>(this.indexTypes.values());
    }


    public List<AsIndexTypeData> getIndexTypes(String indexConfiguration)
    {
        return new ArrayList<>(this.indexTypes.values());
    }


    public Optional<AsIndexTypeData> getIndexTypeForCode(String code)
    {
        return Optional.ofNullable(this.indexTypes.get(code));
    }


    public List<AsIndexPropertyData> getIndexProperties(String indexType)
    {
        return new ArrayList<>(this.indexProperties.values());
    }


    public Optional<AsIndexPropertyData> getIndexPropertyForCode(String indexType, String code)
    {
        return Optional.ofNullable(this.indexProperties.get(code));
    }


    public List<CatalogVersionModel> getSupportedCatalogVersions(String indexConfiguration, String indexType)
    {
        return Collections.emptyList();
    }


    public List<LanguageModel> getSupportedLanguages(String indexConfiguration, String indexType)
    {
        return Collections.emptyList();
    }


    public List<CurrencyModel> getSupportedCurrencies(String indexConfiguration, String indexType)
    {
        return Collections.emptyList();
    }


    public List<AsExpressionData> getSupportedFacetExpressions(String indexType)
    {
        return (List<AsExpressionData>)this.indexProperties.values().stream().filter(indexProperty -> isValidFacetExpression(indexType, indexProperty.getCode()))
                        .map(this::createExpression).collect(Collectors.toList());
    }


    public boolean isValidFacetExpression(String indexType, String expression)
    {
        return (StringUtils.equals(expression, "property1") || StringUtils.equals(expression, "property2") ||
                        StringUtils.equals(expression, "property3"));
    }


    public List<AsFacetType> getSupportedFacetTypes(String indexType)
    {
        return Arrays.asList(AsFacetType.values());
    }


    public boolean isValidFacetType(String indexType, AsFacetType facetType)
    {
        return true;
    }


    public List<AsFacetSortData> getSupportedFacetSorts(String indexType)
    {
        return Collections.emptyList();
    }


    public boolean isValidFacetSort(String indexType, String sort)
    {
        return false;
    }


    public List<AsBoostType> getSupportedBoostTypes(String indexType)
    {
        return Arrays.asList(AsBoostType.values());
    }


    public boolean isValidBoostType(String indexType, AsBoostType boostType)
    {
        return true;
    }


    public List<AsExpressionData> getSupportedSortExpressions(String indexType)
    {
        List<AsExpressionData> expressions = (List<AsExpressionData>)this.indexProperties.values().stream().filter(indexProperty -> isValidSortExpression(indexType, indexProperty.getCode())).map(this::createExpression).collect(Collectors.toList());
        AsExpressionData scoreExpression = new AsExpressionData();
        scoreExpression.setExpression("score");
        expressions.add(0, scoreExpression);
        return expressions;
    }


    public boolean isValidSortExpression(String indexType, String expression)
    {
        return (StringUtils.equals(expression, "score") || StringUtils.equals(expression, "property1") ||
                        StringUtils.equals(expression, "property2") || StringUtils.equals(expression, "property3"));
    }


    public List<AsExpressionData> getSupportedGroupExpressions(String indexType)
    {
        return (List<AsExpressionData>)this.indexProperties.values().stream().filter(indexProperty -> isValidGroupExpression(indexType, indexProperty.getCode()))
                        .map(indexProperty -> {
                            AsExpressionData expression = new AsExpressionData();
                            expression.setExpression(indexProperty.getCode());
                            expression.setName(indexProperty.getName());
                            return expression;
                        }).collect(Collectors.toList());
    }


    public boolean isValidGroupExpression(String indexType, String expression)
    {
        return (StringUtils.equals(expression, "property1") || StringUtils.equals(expression, "property2") ||
                        StringUtils.equals(expression, "property3"));
    }


    public AsSearchResultData search(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws AsException
    {
        throw new UnsupportedOperationException();
    }


    public List<String> getAvailableQualifiers(String indexType, String indexPropertyCode) throws AsException
    {
        return Collections.emptyList();
    }


    protected AsExpressionData createExpression(AsIndexPropertyData indexProperty)
    {
        AsExpressionData expression = new AsExpressionData();
        expression.setExpression(indexProperty.getCode());
        expression.setName(indexProperty.getName());
        return expression;
    }
}
