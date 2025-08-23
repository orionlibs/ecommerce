package de.hybris.platform.cms2.version.populator;

import de.hybris.platform.cms2.cmsitems.converter.AttributeContentConverter;
import de.hybris.platform.cms2.cmsitems.converter.AttributeStrategyConverterProvider;
import de.hybris.platform.cms2.common.functions.impl.Functions;
import de.hybris.platform.cms2.common.service.CollectionHelper;
import de.hybris.platform.cms2.version.converter.customattribute.CMSVersionCustomAttribute;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeContentConverter;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeStrategyConverterProvider;
import de.hybris.platform.cms2.version.service.CMSVersionApplyAttributeValuesToModel;
import de.hybris.platform.cms2.version.service.CMSVersionHelper;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.LocalizedTypedValue;
import de.hybris.platform.persistence.audit.payload.json.LocalizedValue;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.persistence.audit.payload.json.ValueType;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CMSVersionToItemModelPopulator implements Populator<AuditPayload, ItemModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CMSVersionToItemModelPopulator.class);
    private CollectionHelper collectionHelper;
    private CMSVersionHelper cmsVersionHelper;
    private CommonI18NService commonI18NService;
    private CustomAttributeStrategyConverterProvider customConverterProvider;
    private AttributeStrategyConverterProvider<String> converterProvider;
    private Predicate<AttributeDescriptorModel> isCollectionPredicate;
    private List<CMSVersionApplyAttributeValuesToModel> cmsVersionApplyAttributesValuesToModelServiceList;


    public void populate(AuditPayload auditPayload, ItemModel itemModel)
    {
        Map<String, Object> convertedSimpleAttributeValues = convertSimpleAttributes(auditPayload.getAttributes());
        Map<String, Object> convertedLocalizedAttributeValues = convertLocalizedAttributes(auditPayload.getLocAttributes());
        Map<String, Object> convertedAttributeValues = getCollectionHelper().mergeMaps(convertedSimpleAttributeValues, convertedLocalizedAttributeValues, (v1, v2) -> v2);
        applyAttributeValuesToItemModel(itemModel, convertedAttributeValues);
        Map<String, Object> emptyCollectionValues = getEmptyCollectionValuesPerQualifier(itemModel, auditPayload);
        applyAttributeValuesToItemModel(itemModel, emptyCollectionValues);
        convertCustomAttributesValues(itemModel, auditPayload.getAttributes());
    }


    protected Map<String, Object> convertSimpleAttributes(Map<String, TypedValue> values)
    {
        return (Map<String, Object>)values.entrySet()
                        .stream()
                        .filter(entry -> !isCustomAttributeDataType(((TypedValue)entry.getValue()).getType().getType()))
                        .collect(HashMap::new, (map, entry) -> ((Optional)getAttributeValueMapperForTypedValue().apply((TypedValue)entry.getValue())).ifPresent(()), HashMap::putAll);
    }


    protected Map<String, Object> convertLocalizedAttributes(Map<String, LocalizedTypedValue> values)
    {
        return (Map<String, Object>)values.entrySet().stream()
                        .filter(entry -> !isCustomAttributeDataType(((LocalizedTypedValue)entry.getValue()).getType().getType()))
                        .collect(HashMap::new, (map, entry) -> ((Optional)getAttributeValueMapperForLocalizedTypedValue().apply((LocalizedTypedValue)entry.getValue())).ifPresent(()), HashMap::putAll);
    }


    protected Function<TypedValue, Optional<Object>> getAttributeValueMapperForTypedValue()
    {
        return typedValue -> {
            Supplier<Object> collectionGetter = ();
            Supplier<Object> simpleGetter = ();
            return getAttributeRelatedData(collectionGetter, simpleGetter).apply(typedValue.getType());
        };
    }


    protected Function<LocalizedTypedValue, Optional<Object>> getAttributeValueMapperForLocalizedTypedValue()
    {
        return localizedTypedValue -> {
            Supplier<Object> localizedCollectionGetter = ();
            Supplier<Object> localizedGetter = ();
            return getAttributeRelatedData(localizedCollectionGetter, localizedGetter).apply(localizedTypedValue.getType());
        };
    }


    protected Object convertLocalizedValue(LocalizedTypedValue localizedTypedValue, Function<List<String>, Object> localizedValueConverter)
    {
        return localizedTypedValue.getValues().stream()
                        .collect(Collectors.toMap(this::getLocale, localizedValue -> localizedValueConverter.apply(localizedValue.getValue())));
    }


    protected Function<List<String>, Object> convertCollectionValue(ValueType valueType)
    {
        String type = valueType.getType();
        AttributeContentConverter<String> converter = getConverterProvider().getContentConverter(type);
        return values -> {
            Stream<Object> streamValues = values.stream().map(()).map(());
            return collectStreamResult(valueType, streamValues);
        };
    }


    protected Function<List<String>, Object> convertSimpleValue(ValueType valueType)
    {
        String type = valueType.getType();
        AttributeContentConverter<String> converter = getConverterProvider().getContentConverter(type);
        return values -> values.stream().findFirst().map(()).map(()).orElse(null);
    }


    protected void applyAttributeValuesToItemModel(ItemModel itemModel, Map<String, Object> values)
    {
        Optional<CMSVersionApplyAttributeValuesToModel> applyService = getCmsVersionApplyAttributesValuesToModelServiceList().stream().filter(service -> service.getConstrainedBy().test(itemModel)).findFirst();
        if(applyService.isPresent())
        {
            ((CMSVersionApplyAttributeValuesToModel)applyService.get()).apply(itemModel, values);
        }
    }


    protected Predicate<AttributeDescriptorModel> isInAuditPayload(AuditPayload payload)
    {
        return attribute -> {
            Set<String> qualifiers = attribute.getLocalized().booleanValue() ? payload.getLocAttributes().keySet() : payload.getAttributes().keySet();
            return qualifiers.stream().anyMatch(());
        };
    }


    protected Map<String, Object> getEmptyCollectionValuesPerQualifier(ItemModel itemModel, AuditPayload auditPayload)
    {
        List<AttributeDescriptorModel> attributes = getCmsVersionHelper().getSerializableAttributes(itemModel);
        return (Map<String, Object>)attributes.stream()
                        .filter(getIsCollectionPredicate())
                        .filter(isInAuditPayload(auditPayload).negate())
                        .collect(Collectors.toMap(DescriptorModel::getQualifier, this::getEmptyCollectionValue));
    }


    protected Object getEmptyCollectionValue(AttributeDescriptorModel attribute)
    {
        if(getIsCollectionPredicate().negate().test(attribute))
        {
            throw new IllegalArgumentException("The attribute should represent a collection.");
        }
        if(attribute.getLocalized().booleanValue())
        {
            return new HashMap<>();
        }
        TypeOfCollectionEnum typeOfCollectionEnum = (TypeOfCollectionEnum)attribute.getAttributeType().getProperty("typeOfCollection");
        return typeOfCollectionEnum.equals(TypeOfCollectionEnum.SET) ? new HashSet() : new ArrayList();
    }


    protected Locale getLocale(LocalizedValue localizedValue)
    {
        String language = localizedValue.getLanguage();
        LanguageModel languageModel = getCommonI18NService().getLanguage(language);
        return getCommonI18NService().getLocaleForLanguage(languageModel);
    }


    protected Collection<Object> collectStreamResult(ValueType valueType, Stream<Object> stream)
    {
        if(valueType.getCollection().equals("2"))
        {
            return stream.collect((Collector)Collectors.toSet());
        }
        if(valueType.getCollection().equals("3"))
        {
            return stream.collect(Collectors.toCollection(java.util.TreeSet::new));
        }
        return stream.collect((Collector)Collectors.toList());
    }


    protected Function<ValueType, Optional<Object>> getAttributeRelatedData(Supplier<Object> collectionGetter, Supplier<Object> simpleGetter)
    {
        Predicate<ValueType> isCollection = this::isCollectionPayloadType;
        return (Function<ValueType, Optional<Object>>)Functions.ofSupplierConstrainedBy(collectionGetter, isCollection)
                        .orElse(Functions.ofSupplierConstrainedBy(simpleGetter, isCollection.negate()));
    }


    protected void convertCustomAttributesValues(ItemModel itemModel, Map<String, TypedValue> values)
    {
        List<CustomAttributeContentConverter> converters = getCustomConverterProvider().getConverters(itemModel);
        values.entrySet().stream()
                        .filter(entry -> isCustomAttributeDataType(((TypedValue)entry.getValue()).getType().getType()))
                        .forEach(entry -> getCustomAttributeConverterByQualifier(converters, (String)entry.getKey()).ifPresent(()));
    }


    protected Optional<CustomAttributeContentConverter> getCustomAttributeConverterByQualifier(List<CustomAttributeContentConverter> converters, String qualifier)
    {
        return converters.stream().filter(converter -> converter.getQualifier().equals(qualifier)).findFirst();
    }


    protected void applyCustomAttributeValue(ItemModel itemModel, CustomAttributeContentConverter converter, TypedValue typedValue)
    {
        if(isCollectionPayloadType(typedValue.getType()))
        {
            typedValue.getValue().forEach(value -> converter.populateItemModel(itemModel, value));
        }
        else
        {
            typedValue.getValue().stream().findFirst().ifPresent(value -> converter.populateItemModel(itemModel, value));
        }
    }


    protected boolean isCustomAttributeDataType(String payloadType)
    {
        Class<?> typeClass = null;
        try
        {
            typeClass = Class.forName(payloadType);
        }
        catch(ClassNotFoundException classNotFoundException)
        {
        }
        return (typeClass != null && CMSVersionCustomAttribute.class.isAssignableFrom(typeClass));
    }


    protected boolean isCollectionPayloadType(ValueType valueType)
    {
        return !valueType.getCollection().isEmpty();
    }


    protected CollectionHelper getCollectionHelper()
    {
        return this.collectionHelper;
    }


    @Required
    public void setCollectionHelper(CollectionHelper collectionHelper)
    {
        this.collectionHelper = collectionHelper;
    }


    protected CMSVersionHelper getCmsVersionHelper()
    {
        return this.cmsVersionHelper;
    }


    @Required
    public void setCmsVersionHelper(CMSVersionHelper cmsVersionHelper)
    {
        this.cmsVersionHelper = cmsVersionHelper;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected CustomAttributeStrategyConverterProvider getCustomConverterProvider()
    {
        return this.customConverterProvider;
    }


    @Required
    public void setCustomConverterProvider(CustomAttributeStrategyConverterProvider customConverterProvider)
    {
        this.customConverterProvider = customConverterProvider;
    }


    protected AttributeStrategyConverterProvider<String> getConverterProvider()
    {
        return this.converterProvider;
    }


    @Required
    public void setConverterProvider(AttributeStrategyConverterProvider<String> converterProvider)
    {
        this.converterProvider = converterProvider;
    }


    protected Predicate<AttributeDescriptorModel> getIsCollectionPredicate()
    {
        return this.isCollectionPredicate;
    }


    @Required
    public void setIsCollectionPredicate(Predicate<AttributeDescriptorModel> isCollectionPredicate)
    {
        this.isCollectionPredicate = isCollectionPredicate;
    }


    protected List<CMSVersionApplyAttributeValuesToModel> getCmsVersionApplyAttributesValuesToModelServiceList()
    {
        return this.cmsVersionApplyAttributesValuesToModelServiceList;
    }


    @Required
    public void setCmsVersionApplyAttributesValuesToModelServiceList(List<CMSVersionApplyAttributeValuesToModel> cmsVersionApplyAttributesValuesToModelServiceList)
    {
        this.cmsVersionApplyAttributesValuesToModelServiceList = cmsVersionApplyAttributesValuesToModelServiceList;
    }
}
