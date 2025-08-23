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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.util.ParameterUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ProductClassificationAttributeFormatSnIndexerValueProvider extends AbstractProductSnIndexerValueProvider<ProductModel, ProductClassificationAttributeFormatSnIndexerValueProvider.ProductClassificationData>
{
    public static final String ID = "ProductClassificationAttributeFormatSnIndexerValueProvider";
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    public static final String CLASSIFICATION_ATTRIBUTE_PARAM = "classificationAttribute";
    public static final String CLASSIFICATION_ATTRIBUTE_PARAM_DEFAULT_VALUE = null;
    public static final String FORMAT_PARAM = "format";
    public static final String FORMAT_PARAM_DEFAULT_VALUE = null;
    public static final String FORMAT_PARAM_VALUE_LOWERCASEFORMAT = "lowerCaseFormat";
    protected static final Pattern PATTERN = Pattern.compile("(?<classificationSystemId>[^/]+)/(?<classificationSystemVersion>[^/]+)/(?<classificationClassCode>[^.]+)\\.(?<classificationAttributeCode>.+)");
    protected static final String CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY = ProductClassificationAttributeFormatSnIndexerValueProvider.class
                    .getName() + ".classAttributeAssignments";
    private ClassificationSystemService classificationSystemService;
    private ClassificationService classificationService;
    private SnSessionService snSessionService;
    private SnClassificationAttributeAssignmentModelDao snClassificationAttributeAssignmentModelDao;


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ProductModel source, ProductClassificationData data) throws SnIndexerException
    {
        String classificationAttribute = resolveClassificationAttribute(fieldWrapper);
        String productSelector = resolveProductSelector(fieldWrapper);
        Set<ProductModel> products = (Set<ProductModel>)data.getProducts().get(productSelector);
        if(CollectionUtils.isEmpty(products))
        {
            return null;
        }
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)data.getClassAttributeAssignments().get(classificationAttribute);
        if(fieldWrapper.isLocalized())
        {
            List<Locale> locales = (List<Locale>)fieldWrapper.getQualifiers().stream().map(qualifier -> (Locale)qualifier.getAs(Locale.class)).collect(Collectors.toList());
            return collectLocalizedValues(fieldWrapper, products, classAttributeAssignment, data, locales);
        }
        return collectValues(fieldWrapper, products, classAttributeAssignment, data);
    }


    protected Object collectLocalizedValues(SnIndexerFieldWrapper fieldWrapper, Collection<ProductModel> products, ClassAttributeAssignmentModel classAttributeAssignment, ProductClassificationData data, List<Locale> locales)
    {
        Map<Locale, List<Object>> localizedValues = new HashMap<>();
        for(Locale locale : locales)
        {
            localizedValues.put(locale, new ArrayList());
        }
        for(ProductModel product : products)
        {
            FeatureList featureList = (FeatureList)data.getFeatures().get(product.getPk());
            if(featureList != null)
            {
                Feature feature = featureList.getFeatureByAssignment(classAttributeAssignment);
                if(feature != null)
                {
                    addLocalizedFeatureValues(localizedValues, feature, locales);
                }
            }
        }
        return cleanLocalizedValues(fieldWrapper, localizedValues);
    }


    protected void addLocalizedFeatureValues(Map<Locale, List<Object>> localizedValues, Feature feature, List<Locale> locales)
    {
        if(feature instanceof LocalizedFeature)
        {
            LocalizedFeature localizedFeature = (LocalizedFeature)feature;
            for(Locale locale : locales)
            {
                List<Object> values = localizedValues.get(locale);
                addFeatureValues(values, localizedFeature.getValues(locale));
            }
        }
        else
        {
            for(Locale locale : locales)
            {
                List<Object> values = localizedValues.get(locale);
                addFeatureValues(values, feature.getValues());
            }
        }
    }


    protected Object cleanLocalizedValues(SnIndexerFieldWrapper fieldWrapper, Map<Locale, List<Object>> localizedValues)
    {
        Map<Locale, Object> target = (Map<Locale, Object>)localizedValues.entrySet().stream().filter(entry -> CollectionUtils.isNotEmpty((Collection)entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> fieldWrapper.isMultiValued() ? entry.getValue() : ((List)entry.getValue()).get(0)));
        if(MapUtils.isEmpty(target))
        {
            return null;
        }
        return formatValues(fieldWrapper, target);
    }


    protected Object collectValues(SnIndexerFieldWrapper fieldWrapper, Collection<ProductModel> products, ClassAttributeAssignmentModel classAttributeAssignment, ProductClassificationData data)
    {
        List<Object> values = new ArrayList();
        for(ProductModel product : products)
        {
            FeatureList featureList = (FeatureList)data.getFeatures().get(product.getPk());
            if(featureList != null)
            {
                Feature feature = featureList.getFeatureByAssignment(classAttributeAssignment);
                if(feature != null)
                {
                    addFeatureValues(values, feature.getValues());
                }
            }
        }
        return cleanValues(fieldWrapper, values);
    }


    protected void addFeatureValues(List<Object> values, List<FeatureValue> featureValues)
    {
        if(CollectionUtils.isNotEmpty(featureValues))
        {
            for(FeatureValue featureValue : featureValues)
            {
                Object value = featureValue.getValue();
                if(value != null)
                {
                    values.add(value);
                }
            }
        }
    }


    protected Object cleanValues(SnIndexerFieldWrapper fieldWrapper, List<Object> values)
    {
        Object target;
        if(CollectionUtils.isEmpty(values))
        {
            target = null;
        }
        else if(fieldWrapper.isMultiValued())
        {
            target = values;
        }
        else
        {
            target = values.get(0);
        }
        return formatValues(fieldWrapper, target);
    }


    protected Object formatValues(SnIndexerFieldWrapper fieldWrapper, Object values)
    {
        String productFormat = resolveFormat(fieldWrapper);
        if(StringUtils.equals(productFormat, "lowerCaseFormat"))
        {
            return formatToLowerCase(values);
        }
        return values;
    }


    private Object formatToLowerCase(Object values)
    {
        if(values instanceof String)
        {
            String stringValue = (String)values;
            return StringUtils.lowerCase(stringValue);
        }
        if(values instanceof Collection)
        {
            Collection<Object> collectionValue = (Collection<Object>)values;
            return collectionValue.stream().map(this::formatToLowerCase).collect(Collectors.toList());
        }
        return values;
    }


    protected ProductClassificationData loadData(SnIndexerContext indexerContext, Collection<SnIndexerFieldWrapper> fieldWrappers, ProductModel source) throws SnIndexerException
    {
        Map<String, Set<ProductModel>> products = collectProducts(fieldWrappers, source);
        Set<ProductModel> mergedProducts = mergeProducts(products);
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = collectClassAttributeAssignments(indexerContext, fieldWrappers);
        Map<PK, FeatureList> features = collectFeatures(mergedProducts, classAttributeAssignments.values());
        ProductClassificationData data = new ProductClassificationData();
        data.setProducts(products);
        data.setClassAttributeAssignments(classAttributeAssignments);
        data.setFeatures(features);
        return data;
    }


    protected Map<String, ClassAttributeAssignmentModel> collectClassAttributeAssignments(SnIndexerContext indexerContext, Collection<SnIndexerFieldWrapper> fieldWrappers) throws SnIndexerException
    {
        Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = (Map<String, ClassAttributeAssignmentModel>)indexerContext.getAttributes().get(CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY);
        if(classAttributeAssignments == null)
        {
            classAttributeAssignments = doCollectClassAttributeAssignments(fieldWrappers);
            indexerContext.getAttributes().put(CLASSIFICATION_ATTRIBUTE_ASSIGNMENTS_KEY, classAttributeAssignments);
        }
        return classAttributeAssignments;
    }


    protected Map<String, ClassAttributeAssignmentModel> doCollectClassAttributeAssignments(Collection<SnIndexerFieldWrapper> fieldWrappers) throws SnIndexerException
    {
        try
        {
            this.snSessionService.initializeSession();
            this.snSessionService.disableSearchRestrictions();
            Map<String, ClassAttributeAssignmentModel> classAttributeAssignments = new HashMap<>();
            for(SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
            {
                String classificationAttribute = resolveClassificationAttribute(fieldWrapper);
                if(StringUtils.isBlank(classificationAttribute))
                {
                    throw new SnIndexerException("Required 'classificationAttribute' parameter missing");
                }
                Matcher matcher = PATTERN.matcher(classificationAttribute);
                if(!matcher.find())
                {
                    throw new SnIndexerException("Invalid 'classificationAttribute' parameter value, expected pattern : {classificationSystemId}/{classificationSystemVersion}/{classificationClassCode}.{classificationAttributeCode}");
                }
                String classificationSystemId = matcher.group("classificationSystemId");
                String classificationSystemVersion = matcher.group("classificationSystemVersion");
                String classificationClassCode = matcher.group("classificationClassCode");
                String classificationAttributeCode = matcher.group("classificationAttributeCode");
                if(StringUtils.isBlank(classificationSystemId) || StringUtils.isBlank(classificationSystemVersion) ||
                                StringUtils.isBlank(classificationClassCode) || StringUtils.isBlank(classificationAttributeCode))
                {
                    throw new SnIndexerException("Invalid 'classificationAttribute' parameter value, expected pattern : {classificationSystemId}/{classificationSystemVersion}/{classificationClassCode}.{classificationAttributeCode}");
                }
                ClassificationSystemVersionModel classSystemVersion = this.classificationSystemService.getSystemVersion(classificationSystemId, classificationSystemVersion);
                ClassificationClassModel classClass = this.classificationSystemService.getClassForCode(classSystemVersion, classificationClassCode);
                ClassificationAttributeModel classAttribute = this.classificationSystemService.getAttributeForCode(classSystemVersion, classificationAttributeCode);
                ClassAttributeAssignmentModel classAttributeAssignment = this.snClassificationAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classClass, classAttribute).orElseThrow();
                classAttributeAssignments.put(classificationAttribute, classAttributeAssignment);
            }
            return classAttributeAssignments;
        }
        finally
        {
            this.snSessionService.destroySession();
        }
    }


    protected Map<PK, FeatureList> collectFeatures(Collection<ProductModel> products, Collection<ClassAttributeAssignmentModel> classAttributeAssignments)
    {
        Map<PK, FeatureList> features = new HashMap<>();
        for(ProductModel product : products)
        {
            FeatureList featureList = this.classificationService.getFeatures(product, List.copyOf(classAttributeAssignments));
            features.put(product.getPk(), featureList);
        }
        return features;
    }


    protected String resolveClassificationAttribute(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "classificationAttribute", CLASSIFICATION_ATTRIBUTE_PARAM_DEFAULT_VALUE);
    }


    protected String resolveFormat(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "format", FORMAT_PARAM_DEFAULT_VALUE);
    }


    public ClassificationSystemService getClassificationSystemService()
    {
        return this.classificationSystemService;
    }


    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    public ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    public SnSessionService getSnSessionService()
    {
        return this.snSessionService;
    }


    public void setSnSessionService(SnSessionService snSessionService)
    {
        this.snSessionService = snSessionService;
    }


    public SnClassificationAttributeAssignmentModelDao getSnClassificationAttributeAssignmentModelDao()
    {
        return this.snClassificationAttributeAssignmentModelDao;
    }


    public void setSnClassificationAttributeAssignmentModelDao(SnClassificationAttributeAssignmentModelDao snClassificationAttributeAssignmentModelDao)
    {
        this.snClassificationAttributeAssignmentModelDao = snClassificationAttributeAssignmentModelDao;
    }
}
