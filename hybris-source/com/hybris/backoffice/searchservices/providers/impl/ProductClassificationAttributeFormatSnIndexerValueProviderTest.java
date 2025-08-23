package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.searchservices.daos.SnClassificationAttributeAssignmentModelDao;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductClassificationAttributeFormatSnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ItemModel source;
    @InjectMocks
    private ProductClassificationAttributeFormatSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private ItemModel item;
    @Mock
    private ClassificationSystemService classificationSystemService;
    @Mock
    private ClassificationService classificationService;
    @Mock
    private SnSessionService snSessionService;
    @Mock
    private SnClassificationAttributeAssignmentModelDao snClassificationAttributeAssignmentModelDao;
    static final String FIELD_1_ID = "field1";
    static final PK PRODUCT_PK = PK.fromLong(1L);
    static final String LOWER_CASE_FORMAT = "lowerCaseFormat";
    static final String EXPRESSION = "expression";
    static final String CLASSIFICATON_SYTEM_ID = "ElectronicsClassification";
    static final String CLASSIFICATON_SYTEM_VERSION = "ElectronicsClassification";
    static final String CLASSIFICATON_CLASS_1_CODE = "622";
    static final String CLASSIFICATON_ATTRIBUTE_1_CODE = "Size, 1147";
    static final String CLASSIFICATON_ATTRIBUTE_1 = "ElectronicsClassification/ElectronicsClassification/622.Size, 1147";
    static final String CLASSIFICATON_CLASS_2_CODE = "631";
    static final String CLASSIFICATON_ATTRIBUTE_2_CODE = "Resolution, 80";
    static final String CLASSIFICATON_ATTRIBUTE_2 = "ElectronicsClassification/ElectronicsClassification/631.Resolution, 80";


    @Test
    public void shouldGetSupportedQualifierClasses() throws SnIndexerException
    {
        Assertions.assertThat(this.provider.getSupportedQualifierClasses()).containsExactly((Object[])new Class[] {Locale.class});
    }


    @Test
    public void shouldGetNullFieldValue() throws SnIndexerException
    {
        ProductModel productSource = (ProductModel)Mockito.mock(ProductModel.class);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData data = (ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData)Mockito.mock(ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData.class);
        Set<ProductModel> products = new HashSet<>();
        Map<String, Set<ProductModel>> productsMap = new HashMap<>();
        productsMap.put("CURRENT", products);
        Mockito.when(data.getProducts()).thenReturn(productsMap);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        valueProviderParameters.put("classificationAttribute", "expression");
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, productSource, data)).isEqualTo(null);
    }


    @Test
    public void shouldGetFieldValue() throws SnIndexerException
    {
        ProductModel productSource = (ProductModel)Mockito.mock(ProductModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData data = (ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData)Mockito.mock(ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData.class);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(false));
        products.add(product);
        Map<String, Set<ProductModel>> productsMap = new HashMap<>();
        productsMap.put("CURRENT", products);
        Mockito.when(data.getProducts()).thenReturn(productsMap);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignmentModelMap = new HashMap<>();
        classAttributeAssignmentModelMap.put("expression", classAttributeAssignment);
        Mockito.when(data.getClassAttributeAssignments()).thenReturn(classAttributeAssignmentModelMap);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        Map<PK, FeatureList> featureListMap = new HashMap<>();
        Mockito.when(data.getFeatures()).thenReturn(featureListMap);
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        featureListMap.put(PRODUCT_PK, featureList);
        Feature feature = (Feature)Mockito.mock(Feature.class);
        Mockito.when(featureList.getFeatureByAssignment(classAttributeAssignment)).thenReturn(feature);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues()).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        valueProviderParameters.put("classificationAttribute", "expression");
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, productSource, data)).isEqualTo(stringValue);
    }


    @Test
    public void shouldGetLocalizedFieldValue() throws SnIndexerException
    {
        ProductModel productSource = (ProductModel)Mockito.mock(ProductModel.class);
        Locale locale = Locale.forLanguageTag("en");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData data = (ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData)Mockito.mock(ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData.class);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Map<String, Set<ProductModel>> productsMap = new HashMap<>();
        productsMap.put("CURRENT", products);
        Mockito.when(data.getProducts()).thenReturn(productsMap);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignmentModelMap = new HashMap<>();
        classAttributeAssignmentModelMap.put("expression", classAttributeAssignment);
        Mockito.when(data.getClassAttributeAssignments()).thenReturn(classAttributeAssignmentModelMap);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        Map<PK, FeatureList> featureListMap = new HashMap<>();
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        featureListMap.put(PRODUCT_PK, featureList);
        Mockito.when(data.getFeatures()).thenReturn(featureListMap);
        Feature feature = (Feature)Mockito.mock(Feature.class);
        Mockito.when(featureList.getFeatureByAssignment(classAttributeAssignment)).thenReturn(feature);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues()).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        List<SnQualifier> qualifiers = new ArrayList<>();
        SnQualifier qualifier = (SnQualifier)Mockito.mock(SnQualifier.class);
        Mockito.when(qualifier.getAs((Class)Matchers.any())).thenReturn(locale);
        qualifiers.add(qualifier);
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        valueProviderParameters.put("classificationAttribute", "expression");
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, productSource, data) instanceof Map).isEqualTo(true);
        Map fieldValueMap = (Map)this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, productSource, data);
        Assertions.assertThat(fieldValueMap.containsKey(locale)).isEqualTo(true);
        Assertions.assertThat(fieldValueMap.get(locale)).isEqualTo(stringValue);
    }


    @Test
    public void shouldCollectLocalizedValues() throws SnIndexerException
    {
        List<Locale> locales = new ArrayList<>();
        Locale locale = Locale.forLanguageTag("en");
        locales.add(locale);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData data = (ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData)Mockito.mock(ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData.class);
        List<ProductModel> products = new ArrayList<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        Map<PK, FeatureList> featureListMap = new HashMap<>();
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        featureListMap.put(PRODUCT_PK, featureList);
        Mockito.when(data.getFeatures()).thenReturn(featureListMap);
        Feature feature = (Feature)Mockito.mock(Feature.class);
        Mockito.when(featureList.getFeatureByAssignment(classAttributeAssignment)).thenReturn(feature);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues()).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.collectLocalizedValues(this.fieldWrapper, products, classAttributeAssignment, data, locales) instanceof Map)
                        .isEqualTo(true);
        Map fieldValueMap = (Map)this.provider.collectLocalizedValues(this.fieldWrapper, products, classAttributeAssignment, data, locales);
        Assertions.assertThat(fieldValueMap.containsKey(locale)).isEqualTo(true);
        Assertions.assertThat(fieldValueMap.get(locale)).isEqualTo(stringValue);
    }


    @Test
    public void shouldAddFeatureValuesWithLocalizedFeature()
    {
        LocalizedFeature feature = (LocalizedFeature)Mockito.mock(LocalizedFeature.class);
        List<Locale> locales = new ArrayList<>();
        Locale locale = Locale.forLanguageTag("en");
        locales.add(locale);
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        List<Object> values = new ArrayList();
        localizedValues.put(locale, values);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues(locale)).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        this.provider.addLocalizedFeatureValues(localizedValues, (Feature)feature, locales);
        Assertions.assertThat(((List)localizedValues.get(locale)).size()).isEqualTo(1);
        Assertions.assertThat(((List)localizedValues.get(locale)).get(0)).isEqualTo(stringValue);
    }


    @Test
    public void shouldAddFeatureValuesWithoutLocalizedFeature()
    {
        Feature feature = (Feature)Mockito.mock(Feature.class);
        List<Locale> locales = new ArrayList<>();
        Locale locale = Locale.forLanguageTag("en");
        locales.add(locale);
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        List<Object> values = new ArrayList();
        localizedValues.put(locale, values);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues()).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        this.provider.addLocalizedFeatureValues(localizedValues, feature, locales);
        Assertions.assertThat(((List)localizedValues.get(locale)).size()).isEqualTo(1);
        Assertions.assertThat(((List)localizedValues.get(locale)).get(0)).isEqualTo(stringValue);
    }


    @Test
    public void shouldNotCleanLocalizedValues()
    {
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.lenient().when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.cleanLocalizedValues(this.fieldWrapper, localizedValues)).isEqualTo(null);
    }


    @Test
    public void shouldCleanMultiLocalizedValues()
    {
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        List<Object> values = new ArrayList();
        Locale locale = Locale.forLanguageTag("en");
        localizedValues.put(locale, values);
        String stringValue = "ABC";
        values.add(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.provider.cleanLocalizedValues(this.fieldWrapper, localizedValues) instanceof Map).isEqualTo(true);
        Map fieldValueMap = (Map)this.provider.cleanLocalizedValues(this.fieldWrapper, localizedValues);
        Assertions.assertThat(fieldValueMap.containsKey(locale)).isEqualTo(true);
        Assertions.assertThat(fieldValueMap.get(locale)).isEqualTo(values);
    }


    @Test
    public void shouldCleanLocalizedValues()
    {
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        List<Object> values = new ArrayList();
        Locale locale = Locale.forLanguageTag("en");
        localizedValues.put(locale, values);
        String stringValue = "ABC";
        values.add(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.cleanLocalizedValues(this.fieldWrapper, localizedValues) instanceof Map).isEqualTo(true);
        Map fieldValueMap = (Map)this.provider.cleanLocalizedValues(this.fieldWrapper, localizedValues);
        Assertions.assertThat(fieldValueMap.containsKey(locale)).isEqualTo(true);
        Assertions.assertThat(fieldValueMap.get(locale)).isEqualTo(stringValue);
    }


    @Test
    public void shouldCollectValues()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData data = (ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData)Mockito.mock(ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData.class);
        List<ProductModel> products = new ArrayList<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        Map<PK, FeatureList> featureListMap = new HashMap<>();
        Mockito.when(data.getFeatures()).thenReturn(featureListMap);
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        featureListMap.put(PRODUCT_PK, featureList);
        Feature feature = (Feature)Mockito.mock(Feature.class);
        Mockito.when(featureList.getFeatureByAssignment(classAttributeAssignment)).thenReturn(feature);
        List<FeatureValue> featureValues = new ArrayList<>();
        Mockito.when(feature.getValues()).thenReturn(featureValues);
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.collectValues(this.fieldWrapper, products, classAttributeAssignment, data)).isEqualTo(stringValue);
    }


    @Test
    public void shouldAddFeatureValues()
    {
        List<Object> values = new ArrayList();
        List<FeatureValue> featureValues = new ArrayList<>();
        String stringValue = "ABC";
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        featureValues.add(featureValue);
        Mockito.when(featureValue.getValue()).thenReturn(stringValue);
        this.provider.addFeatureValues(values, featureValues);
        Assertions.assertThat(values.size()).isEqualTo(1);
        Assertions.assertThat(values.get(0)).isEqualTo(stringValue);
    }


    @Test
    public void shouldAddEmptyFeatureValues()
    {
        List<Object> values = new ArrayList();
        List<FeatureValue> featureValues = new ArrayList<>();
        this.provider.addFeatureValues(values, featureValues);
        Assertions.assertThat(values.size()).isEqualTo(0);
    }


    @Test
    public void shouldNotCleanValues()
    {
        List<Object> values = new ArrayList();
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.cleanValues(this.fieldWrapper, values)).isEqualTo(null);
    }


    @Test
    public void shouldCleanMultiValues()
    {
        List<Object> values = new ArrayList();
        String stringValue = "ABC";
        values.add(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.provider.cleanValues(this.fieldWrapper, values)).isEqualTo(values);
    }


    @Test
    public void shouldCleanValues()
    {
        List<Object> values = new ArrayList();
        String stringValue = "ABC";
        values.add(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isMultiValued())).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.provider.cleanValues(this.fieldWrapper, values)).isEqualTo(stringValue);
    }


    @Test
    public void shouldNotFormatValues()
    {
        String stringValue = "ABC";
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.formatValues(this.fieldWrapper, stringValue)).isEqualTo(stringValue);
    }


    @Test
    public void shouldFormatValues()
    {
        String stringValue = "ABC";
        String formatStringValue = "abc";
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", "lowerCaseFormat");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.formatValues(this.fieldWrapper, stringValue)).isEqualTo(formatStringValue);
    }


    @Test
    public void shouldFormatToLowerCaseWithCollection()
    {
        String stringValue = "ABC";
        String formatStringValue = "abc";
        List<Object> stringList = new ArrayList();
        stringList.add(stringValue);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", "lowerCaseFormat");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.formatValues(this.fieldWrapper, stringList) instanceof List).isEqualTo(true);
        List foramtValuesList = (List)this.provider.formatValues(this.fieldWrapper, stringList);
        Assertions.assertThat(foramtValuesList.size()).isEqualTo(1);
        Assertions.assertThat(foramtValuesList.get(0)).isEqualTo(formatStringValue);
    }


    @Test
    public void shouldFormatToLowerCaseWithOtherType()
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", "lowerCaseFormat");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.formatValues(this.fieldWrapper, product)).isEqualTo(product);
    }


    @Test
    public void shouldLoadData() throws SnIndexerException
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Map<String, Object> attributes = new HashMap<>();
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = new HashMap<>();
        attributes.put(ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY, classAttributeAssignments);
        Mockito.when(this.indexerContext.getAttributes()).thenReturn(attributes);
        this.provider.setClassificationService(this.classificationService);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        classAttributeAssignments.put("test", classAttributeAssignment);
        Mockito.when(this.classificationService.getFeatures((ProductModel)Matchers.eq(product), (List)Matchers.any())).thenReturn(featureList);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData productClassificationData = this.provider.loadData(this.indexerContext, fieldWrappers, product);
        Map<String, Set<ProductModel>> productsValues = productClassificationData.getProducts();
        Assertions.assertThat(((Set)productsValues.get("CURRENT")).size()).isEqualTo(1);
        Assertions.assertThat(((Set)productsValues.get("CURRENT")).contains(product))
                        .isEqualTo(true);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignmentsValuesMap = productClassificationData.getClassAttributeAssignments();
        Assertions.assertThat(classAttributeAssignmentsValuesMap).isEqualTo(classAttributeAssignments);
        Map<PK, FeatureList> featuresValuesMap = productClassificationData.getFeatures();
        Assertions.assertThat(featuresValuesMap.containsKey(PRODUCT_PK)).isEqualTo(true);
        Assertions.assertThat((Iterable)featuresValuesMap.get(PRODUCT_PK)).isEqualTo(featureList);
    }


    @Test
    public void shouldCollectClassAttributeAssignments() throws SnIndexerException
    {
        Map<String, Object> attributes = new HashMap<>();
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = new HashMap<>();
        attributes.put(ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY, classAttributeAssignments);
        Mockito.when(this.indexerContext.getAttributes()).thenReturn(attributes);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignmentsValue = this.provider.collectClassAttributeAssignments(this.indexerContext, fieldWrappers);
        Assertions.assertThat(classAttributeAssignmentsValue).isEqualTo(classAttributeAssignments);
    }


    @Test
    public void shouldCollectClassAttributeAssignmentsWithNull() throws SnIndexerException
    {
        Map<String, Object> attributes = new HashMap<>();
        Mockito.when(this.indexerContext.getAttributes()).thenReturn(attributes);
        this.provider.setSnSessionService(this.snSessionService);
        this.provider.setClassificationSystemService(this.classificationSystemService);
        this.provider.setSnClassificationAttributeAssignmentModelDao(this.snClassificationAttributeAssignmentModelDao);
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).initializeSession();
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).disableSearchRestrictions();
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("classificationAttribute", "ElectronicsClassification/ElectronicsClassification/622.Size, 1147");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ClassificationSystemVersionModel classSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationClassModel classClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationAttributeModel classAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.when(this.classificationSystemService.getSystemVersion("ElectronicsClassification", "ElectronicsClassification"))
                        .thenReturn(classSystemVersion);
        Mockito.when(this.classificationSystemService.getClassForCode(classSystemVersion, "622")).thenReturn(classClass);
        Mockito.when(this.classificationSystemService.getAttributeForCode(classSystemVersion, "Size, 1147"))
                        .thenReturn(classAttribute);
        Mockito.when(this.snClassificationAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute))
                        .thenReturn(Optional.of(classAttributeAssignment));
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = this.provider.collectClassAttributeAssignments(this.indexerContext, fieldWrappers);
        Assertions.assertThat(classAttributeAssignments.containsKey("ElectronicsClassification/ElectronicsClassification/622.Size, 1147")).isEqualTo(true);
        Assertions.assertThat(classAttributeAssignments.get("ElectronicsClassification/ElectronicsClassification/622.Size, 1147")).isEqualTo(classAttributeAssignment);
        Assertions.assertThat(attributes
                                        .containsKey(ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY))
                        .isEqualTo(true);
        Assertions.assertThat(attributes
                                        .get(ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY))
                        .isEqualTo(classAttributeAssignments);
    }


    @Test
    public void shouldDoCollectClassAttributeAssignments() throws SnIndexerException
    {
        this.provider.setSnSessionService(this.snSessionService);
        this.provider.setClassificationSystemService(this.classificationSystemService);
        this.provider.setSnClassificationAttributeAssignmentModelDao(this.snClassificationAttributeAssignmentModelDao);
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).initializeSession();
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).disableSearchRestrictions();
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("classificationAttribute", "ElectronicsClassification/ElectronicsClassification/622.Size, 1147");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ClassificationSystemVersionModel classSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationClassModel classClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationAttributeModel classAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.when(this.classificationSystemService.getSystemVersion("ElectronicsClassification", "ElectronicsClassification"))
                        .thenReturn(classSystemVersion);
        Mockito.when(this.classificationSystemService.getClassForCode(classSystemVersion, "622")).thenReturn(classClass);
        Mockito.when(this.classificationSystemService.getAttributeForCode(classSystemVersion, "Size, 1147"))
                        .thenReturn(classAttribute);
        Mockito.when(this.snClassificationAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute))
                        .thenReturn(Optional.of(classAttributeAssignment));
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = this.provider.doCollectClassAttributeAssignments(fieldWrappers);
        Assertions.assertThat(classAttributeAssignments.containsKey("ElectronicsClassification/ElectronicsClassification/622.Size, 1147")).isEqualTo(true);
        Assertions.assertThat(classAttributeAssignments.get("ElectronicsClassification/ElectronicsClassification/622.Size, 1147")).isEqualTo(classAttributeAssignment);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenDoCollectClassAttributeAssignments() throws SnIndexerException
    {
        this.provider.setSnSessionService(this.snSessionService);
        this.provider.setClassificationSystemService(this.classificationSystemService);
        this.provider.setSnClassificationAttributeAssignmentModelDao(this.snClassificationAttributeAssignmentModelDao);
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).initializeSession();
        ((SnSessionService)Mockito.doNothing().when(this.snSessionService)).disableSearchRestrictions();
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("classificationAttribute", "exceptionParameters");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ClassificationSystemVersionModel classSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationClassModel classClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationAttributeModel classAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        Mockito.lenient().when(this.classificationSystemService.getSystemVersion("ElectronicsClassification", "ElectronicsClassification"))
                        .thenReturn(classSystemVersion);
        Mockito.lenient().when(this.classificationSystemService.getClassForCode(classSystemVersion, "622")).thenReturn(classClass);
        Mockito.lenient().when(this.classificationSystemService.getAttributeForCode(classSystemVersion, "Size, 1147"))
                        .thenReturn(classAttribute);
        Mockito.lenient().when(this.snClassificationAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute))
                        .thenReturn(Optional.of(classAttributeAssignment));
        this.provider.doCollectClassAttributeAssignments(fieldWrappers);
    }


    @Test
    public void shouldCollectFeatures()
    {
        this.provider.setClassificationService(this.classificationService);
        List<ProductModel> products = new ArrayList<>();
        List<ClassAttributeAssignmentModel> classAttributeAssignments = new ArrayList<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        FeatureList featureList = (FeatureList)Mockito.mock(FeatureList.class);
        products.add(product);
        classAttributeAssignments.add(classAttributeAssignment);
        Mockito.when(this.classificationService.getFeatures((ProductModel)Matchers.eq(product), (List)Matchers.any())).thenReturn(featureList);
        Mockito.when(product.getPk()).thenReturn(PRODUCT_PK);
        Map<PK, FeatureList> features = this.provider.collectFeatures(products, classAttributeAssignments);
        Assertions.assertThat(features.containsKey(PRODUCT_PK)).isEqualTo(true);
        Assertions.assertThat((Iterable)features.get(PRODUCT_PK)).isEqualTo(featureList);
    }


    @Test
    public void shouldResolveClassificationAttribute()
    {
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("classificationAttribute", ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.resolveClassificationAttribute(this.fieldWrapper))
                        .isEqualTo(ProductClassificationAttributeFormatSnIndexerValueProvider.CLASSIFICATION_ATTRIBUTE_PARAM_DEFAULT_VALUE);
    }


    @Test
    public void shouldResolveFormat()
    {
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("format", ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.resolveFormat(this.fieldWrapper))
                        .isEqualTo(ProductClassificationAttributeFormatSnIndexerValueProvider.FORMAT_PARAM_DEFAULT_VALUE);
    }
}
