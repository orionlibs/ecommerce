package de.hybris.platform.platformbackoffice.classification.util;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.util.Range;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BackofficeClassificationUtils
{
    public static final String CLASSIFICATION_FEATURE_PREFIX = "ClassificationFeatureEncoded_";
    public static final String FEATURE_QUALIFIER_REGEX = String.format("^%s%s", new Object[] {"ClassificationFeatureEncoded_", "(?:[A-Za-z0-9+\\/]{4})*(?:[A-Za-z0-9\\/]{2}==|[A-Za-z0-9+\\/]{3}=)?$"});
    private static final Pattern FEATURE_QUALIFIER_PATTERN = Pattern.compile(FEATURE_QUALIFIER_REGEX);
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeClassificationUtils.class);


    public static ClassificationInfo convertFeatureToClassificationInfo(Feature feature)
    {
        ClassAttributeAssignmentModel classAttributeAssignment = feature.getClassAttributeAssignment();
        if(BooleanUtils.isTrue(classAttributeAssignment.getLocalized()))
        {
            return convertLocalizedFeature((LocalizedFeature)feature);
        }
        return convertFeature(feature);
    }


    private static ClassificationInfo convertLocalizedFeature(LocalizedFeature feature)
    {
        ClassAttributeAssignmentModel assignment = feature.getClassAttributeAssignment();
        Map<Locale, List<FeatureValue>> localizedFeatureValues = feature.getValuesForAllLocales();
        if(BooleanUtils.isTrue(assignment.getRange()) && BooleanUtils.isTrue(assignment.getMultiValued()))
        {
            Map<Locale, List<Range<FeatureValue>>> localizedRangeList = Maps.newHashMap();
            localizedFeatureValues.forEach((locale, featureValues) -> convertToRangeList(featureValues).ifPresent(()));
            return new ClassificationInfo(assignment, localizedRangeList);
        }
        if(BooleanUtils.isTrue(assignment.getRange()))
        {
            Map<Locale, Range<FeatureValue>> localizedRanges = new HashMap<>();
            localizedFeatureValues.forEach((locale, featureValue) -> localizedRanges.put(locale, convertToRange(featureValue)));
            return new ClassificationInfo(assignment, localizedRanges);
        }
        if(BooleanUtils.isTrue(assignment.getMultiValued()))
        {
            Map<Locale, Collection<FeatureValue>> map = Maps.newHashMap();
            localizedFeatureValues.forEach((locale, featureValues) -> {
                if(CollectionUtils.isNotEmpty(featureValues))
                {
                    values.put(locale, featureValues);
                }
            });
            return new ClassificationInfo(assignment, map);
        }
        Map<Locale, FeatureValue> values = new HashMap<>();
        localizedFeatureValues.forEach((locale, featureValues) -> {
            if(CollectionUtils.isNotEmpty(featureValues))
            {
                values.put(locale, featureValues.get(0));
            }
        });
        return new ClassificationInfo(assignment, values);
    }


    private static ClassificationInfo convertFeature(Feature feature)
    {
        ClassAttributeAssignmentModel assignment = feature.getClassAttributeAssignment();
        if(BooleanUtils.isTrue(feature.getClassAttributeAssignment().getMultiValued()))
        {
            return new ClassificationInfo(assignment,
                            BooleanUtils.isTrue(feature.getClassAttributeAssignment().getRange()) ? convertToRangeList(
                                            feature.getValues()).orElse(new ArrayList<>()) : feature.getValues());
        }
        if(BooleanUtils.isTrue(feature.getClassAttributeAssignment().getRange()))
        {
            return new ClassificationInfo(assignment, convertToRange(feature.getValues()));
        }
        return new ClassificationInfo(assignment, feature.getValue());
    }


    private static Range<FeatureValue> convertToRange(List<FeatureValue> featureValues)
    {
        FeatureValue oStart = !featureValues.isEmpty() ? featureValues.get(0) : null;
        FeatureValue oEnd = (featureValues.size() > 1) ? featureValues.get(1) : null;
        return new Range(oStart, oEnd);
    }


    private static Optional<List<Range<FeatureValue>>> convertToRangeList(List<FeatureValue> featureValues)
    {
        List<Range<FeatureValue>> result = new ArrayList<>();
        FeatureValue start = null;
        for(int i = 0; i < featureValues.size(); i++)
        {
            if(i % 2 == 0)
            {
                start = featureValues.get(i);
            }
            else
            {
                FeatureValue end = featureValues.get(i);
                result.add(new Range(start, end));
            }
        }
        if(featureValues.size() % 2 == 1)
        {
            result.add(new Range(start, null));
        }
        return result.isEmpty() ? Optional.<List<Range<FeatureValue>>>empty() : Optional.<List<Range<FeatureValue>>>of(result);
    }


    public static void updateFeatureWithClassificationInfo(Feature feature, ClassificationInfo info)
    {
        if(feature != null && info != null)
        {
            if(info.isLocalized())
            {
                updateLocalizedFeature((LocalizedFeature)feature, info);
            }
            else
            {
                updateFeature(feature, info);
            }
        }
    }


    private static void updateLocalizedFeature(LocalizedFeature feature, ClassificationInfo info)
    {
        if(info.hasRange() && info.isMultiValue())
        {
            Objects.requireNonNull(feature);
            feature.getValuesForAllLocales().keySet().forEach(feature::removeAllValues);
            ((Map)info.getValue()).forEach((locale, ranges) -> {
                if(CollectionUtils.isNotEmpty(ranges))
                {
                    ranges.stream().filter(()).forEach(());
                }
            });
        }
        else if(info.hasRange())
        {
            ((Map)info.getValue()).forEach((locale, range) -> {
                feature.removeAllValues(locale);
                if(range.getStart() != null)
                {
                    feature.addValue((FeatureValue)range.getStart(), locale);
                }
                if(range.getEnd() != null)
                {
                    feature.addValue((FeatureValue)range.getEnd(), locale);
                }
            });
        }
        else if(info.isMultiValue())
        {
            Objects.requireNonNull(feature);
            feature.getValuesForAllLocales().keySet().forEach(feature::removeAllValues);
            ((Map)info.getValue()).forEach((locale, values) -> {
                if(CollectionUtils.isNotEmpty(values))
                {
                    values.forEach(());
                }
            });
        }
        else
        {
            Objects.requireNonNull(feature);
            feature.getValuesForAllLocales().keySet().forEach(feature::removeAllValues);
            ((Map)info.getValue()).forEach((locale, fValue) -> {
                if(fValue != null)
                {
                    feature.addValue(fValue, locale);
                }
            });
        }
    }


    private static void updateFeature(Feature feature, ClassificationInfo info)
    {
        List<FeatureValue> featureValues = new ArrayList<>();
        if(info.getValue() == null)
        {
            feature.setValues(featureValues);
            return;
        }
        if(info.hasRange() && info.isMultiValue())
        {
            List<Range<FeatureValue>> rangeList = (List<Range<FeatureValue>>)info.getValue();
            rangeList.stream().filter(range -> (range.getStart() != null && range.getEnd() != null)).forEach(range -> {
                featureValues.add((FeatureValue)range.getStart());
                featureValues.add((FeatureValue)range.getEnd());
            });
        }
        else if(info.hasRange())
        {
            Range<FeatureValue> value = (Range<FeatureValue>)info.getValue();
            if(value.getStart() != null)
            {
                featureValues.add((FeatureValue)value.getStart());
            }
            if(value.getEnd() != null)
            {
                featureValues.add((FeatureValue)value.getEnd());
            }
        }
        else if(info.isMultiValue())
        {
            featureValues.addAll((List)info.getValue());
        }
        else
        {
            featureValues.add((FeatureValue)info.getValue());
        }
        feature.setValues(featureValues);
    }


    public static boolean isFeatureQualifier(String qualifier)
    {
        return (StringUtils.isNotBlank(qualifier) && FEATURE_QUALIFIER_PATTERN.matcher(unescapeBase64(qualifier)).matches());
    }


    public static String getFeatureQualifierEncoded(ClassAttributeAssignmentModel classificationAttrAssignment)
    {
        return getFeatureQualifierEncoded(getFeatureQualifier(classificationAttrAssignment));
    }


    public static String getFeatureQualifier(ClassAttributeAssignmentModel classificationAttrAssignment)
    {
        String code = classificationAttrAssignment.getClassificationClass().getCode();
        String attribute = classificationAttrAssignment.getClassificationAttribute().getCode();
        String catalogId = null;
        String systemVersion = null;
        if(classificationAttrAssignment.getSystemVersion() != null)
        {
            catalogId = classificationAttrAssignment.getSystemVersion().getCatalog().getId();
            systemVersion = classificationAttrAssignment.getSystemVersion().getVersion();
        }
        return getFeatureQualifier(catalogId, systemVersion, code, attribute);
    }


    public static String getFeatureQualifierEncoded(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        return getFeatureQualifierEncoded(getFeatureQualifier(catalogId, systemVersion, classificationClass, attribute));
    }


    public static String getFeatureQualifier(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        if(catalogId != null && systemVersion != null)
        {
            return String.format("%s/%s/%s.%s", new Object[] {catalogId, systemVersion, classificationClass, attribute});
        }
        return String.format("%s.%s", new Object[] {classificationClass, attribute});
    }


    public static String getFeatureQualifierEncoded(String qualifier)
    {
        if(qualifier != null)
        {
            return "ClassificationFeatureEncoded_".concat(encodeBase64(qualifier));
        }
        return null;
    }


    public static String getType(ClassificationInfo value)
    {
        String type = String.format("FeatureValue(%s)", new Object[] {getType(value, value.getAssignment())});
        if(value.hasRange())
        {
            type = String.format("Range(%s)", new Object[] {type});
        }
        if(value.isMultiValue())
        {
            type = String.format("List(%s)", new Object[] {type});
        }
        if(value.isLocalized())
        {
            type = String.format("Localized(%s)", new Object[] {type});
        }
        return type;
    }


    private static String getType(ClassificationInfo value, ClassAttributeAssignmentModel assignment)
    {
        ClassificationAttributeTypeEnum attributeType = assignment.getAttributeType();
        if(attributeType == null)
        {
            return "Undefined";
        }
        switch(null.$SwitchMap$de$hybris$platform$catalog$enums$ClassificationAttributeTypeEnum[attributeType.ordinal()])
        {
            case 1:
                return String.class.getCanonicalName();
            case 2:
                return Double.class.getCanonicalName();
            case 3:
                return Boolean.class.getCanonicalName();
            case 4:
                return createClassificationEnumEmbeddedTypeString(value);
            case 5:
                return Date.class.getCanonicalName();
            case 6:
                return String.format("Reference(%s)", new Object[] {assignment.getReferenceType().getCode()});
        }
        throw new IllegalStateException(String.format("Attribute type \"%s\" is not supported", new Object[] {attributeType}));
    }


    private static String createClassificationEnumEmbeddedTypeString(ClassificationInfo info)
    {
        ClassAttributeAssignmentModel assignment = info.getAssignment();
        ClassificationSystemVersionModel systemVersion = assignment.getSystemVersion();
        String versionId = systemVersion.getVersion();
        String catalogId = systemVersion.getCatalog().getId();
        ClassificationAttributeModel attribute = assignment.getClassificationAttribute();
        ClassificationClassModel classificationClass = assignment.getClassificationClass();
        return String.format("ClassificationEnum(%s/%s/%s.%s)", new Object[] {catalogId, versionId, classificationClass.getCode(), attribute
                        .getCode()});
    }


    public static Set<ProductModel> extractProducts(Collection<?> objects)
    {
        return (Set<ProductModel>)objects.stream().filter(object -> object instanceof ProductModel).map(object -> (ProductModel)object)
                        .collect(Collectors.toSet());
    }


    protected static String encodeBase64(String value)
    {
        if(value != null)
        {
            try
            {
                return escapeBase64(Base64.getEncoder().encodeToString(value.getBytes("UTF-8")));
            }
            catch(UnsupportedEncodingException e)
            {
                LOG.error("Can not get qualifier UTF-8 encoded".concat(value), e);
                return escapeBase64(Base64.getEncoder().encodeToString(value.getBytes()));
            }
        }
        return null;
    }


    protected static String escapeBase64(String code)
    {
        return code.replaceAll("/", "_\\$").replaceAll("\\+", "_\\$\\$").replaceAll("=", "_\\$\\$\\$");
    }


    protected static String unescapeBase64(String code)
    {
        return code.replaceAll("_\\$\\$\\$", "=").replaceAll("_\\$\\$", "+").replaceAll("_\\$", "/");
    }
}
