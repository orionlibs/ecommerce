package de.hybris.platform.cms2.version.converter.impl;

import de.hybris.platform.cms2.cmsitems.converter.AttributeStrategyConverterProvider;
import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cms2.common.functions.impl.Functions;
import de.hybris.platform.cms2.common.service.CollectionHelper;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionAttributeDescriptor;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeContentConverter;
import de.hybris.platform.cms2.version.converter.customattribute.CustomAttributeStrategyConverterProvider;
import de.hybris.platform.cms2.version.service.CMSVersionHelper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.persistence.audit.internal.LocalizedAttributesList;
import de.hybris.platform.persistence.audit.payload.PayloadSerializer;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionToDataConverter implements Converter<ItemModel, String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCMSVersionToDataConverter.class);
    private PayloadSerializer payloadSerializer;
    private CommonI18NService commonI18NService;
    private ModelService modelService;
    private AttributeStrategyConverterProvider<VersionAttributeDescriptor> converterProvider;
    private CustomAttributeStrategyConverterProvider customConverterProvider;
    private CollectionHelper collectionHelper;
    private CMSVersionHelper cmsVersionHelper;
    private Predicate<AttributeDescriptorModel> isCollectionPredicate;
    private final String RETURN_TYPE_PROPERTY_NAME = "returntype";
    private final String ELEMENT_TYPE_PROPERTY_NAME = "elementtype";


    public String convert(ItemModel itemModel)
    {
        Map<String, Object> convertedAttributeValues = getConvertedAttributeValues(itemModel);
        Map<String, Object> customConvertedAttributeValues = getCustomConvertedAttributeValues(itemModel);
        Map<String, Object> convertedValues = getCollectionHelper().mergeMaps(convertedAttributeValues, customConvertedAttributeValues, (v1, v2) -> v2);
        return getPayloadSerializer().serialize(convertedValues);
    }


    protected Map<String, Object> getConvertedAttributeValues(ItemModel itemModel)
    {
        List<AttributeDescriptorModel> attributes = getCmsVersionHelper().getSerializableAttributes(itemModel);
        return (Map<String, Object>)attributes.stream()
                        .collect(HashMap::new, (map, attr) -> ((Optional)getAttributeValueMapperForItem(itemModel).apply(attr)).ifPresent(()), HashMap::putAll);
    }


    protected Map<String, Object> getCustomConvertedAttributeValues(ItemModel itemModel)
    {
        return (Map<String, Object>)getCustomConverterProvider()
                        .getConverters(itemModel)
                        .stream()
                        .filter(converter -> converter.getConstrainedBy().test(itemModel))
                        .collect(
                                        Collectors.toMap(CustomAttributeContentConverter::getQualifier, e -> new SLDDataContainer.AttributeValue(e.getQualifier(), e.convertModelToData(itemModel))));
    }


    protected Function<AttributeDescriptorModel, Optional<Object>> getAttributeRelatedData(Supplier<Object> localizedCollectionGetter, Supplier<Object> localizedGetter, Supplier<Object> collectionGetter, Supplier<Object> simpleGetter)
    {
        Predicate<AttributeDescriptorModel> isLocalized = AttributeDescriptorModel::getLocalized;
        Predicate<AttributeDescriptorModel> isCollection = getIsCollectionPredicate();
        return (Function<AttributeDescriptorModel, Optional<Object>>)Functions.ofSupplierConstrainedBy(localizedCollectionGetter, isLocalized.and(isCollection))
                        .orElse(Functions.ofSupplierConstrainedBy(localizedGetter, isLocalized.and(isCollection.negate())))
                        .orElse(Functions.ofSupplierConstrainedBy(collectionGetter, isLocalized.negate().and(isCollection)))
                        .orElse(Functions.ofSupplierConstrainedBy(simpleGetter, isLocalized.negate().and(isCollection.negate())));
    }


    protected Function<AttributeDescriptorModel, Optional<Object>> getAttributeValueMapperForItem(ItemModel itemModel)
    {
        return attribute -> {
            VersionAttributeDescriptor versionAttributeDescriptor = getVersionAttribute(attribute);
            Supplier<Object> localizedCollectionGetter = ();
            Supplier<Object> localizedGetter = ();
            Supplier<Object> collectionGetter = ();
            Supplier<Object> simpleGetter = ();
            return (versionAttributeDescriptor == null) ? Optional.empty() : getAttributeRelatedData(localizedCollectionGetter, localizedGetter, collectionGetter, simpleGetter).apply(attribute);
        };
    }


    protected LocalizedAttributesList prepareLocalizedValueForSerialization(AttributeDescriptorModel attribute, Map<LanguageModel, Object> localizedValues)
    {
        return (LocalizedAttributesList)localizedValues.entrySet().stream()
                        .map(entry -> new SLDDataContainer.AttributeValue(attribute.getQualifier(), entry.getValue(), ((LanguageModel)entry.getKey()).getPk()))
                        .collect(Collectors.toCollection(LocalizedAttributesList::new));
    }


    protected SLDDataContainer.AttributeValue prepareSimpleValueForSerialization(AttributeDescriptorModel attribute, Object convertedValue)
    {
        return new SLDDataContainer.AttributeValue(attribute.getQualifier(), convertedValue);
    }


    protected VersionAttributeDescriptor getVersionAttribute(AttributeDescriptorModel attribute)
    {
        Supplier<Object> localizedCollectionGetter = () -> {
            TypeModel mapTypeModel = (TypeModel)attribute.getAttributeType().getProperty("returntype");
            TypeModel collectionTypeModel = (TypeModel)mapTypeModel.getProperty("elementtype");
            return buildVersionAttributeDescriptor(collectionTypeModel, attribute);
        };
        Supplier<Object> localizedGetter = () -> {
            TypeModel mapTypeModel = (TypeModel)attribute.getAttributeType().getProperty("returntype");
            return buildVersionAttributeDescriptor(mapTypeModel, attribute);
        };
        Supplier<Object> collectionGetter = () -> {
            TypeModel collectionTypeModel = (TypeModel)attribute.getAttributeType().getProperty("elementtype");
            return buildVersionAttributeDescriptor(collectionTypeModel, attribute);
        };
        Supplier<Object> simpleGetter = () -> {
            TypeModel mapTypeModel = attribute.getAttributeType();
            return buildVersionAttributeDescriptor(mapTypeModel, attribute);
        };
        return ((Optional<VersionAttributeDescriptor>)getAttributeRelatedData(localizedCollectionGetter, localizedGetter, collectionGetter, simpleGetter)
                        .apply(attribute)).orElse(null);
    }


    protected VersionAttributeDescriptor buildVersionAttributeDescriptor(TypeModel typeModel, AttributeDescriptorModel attribute)
    {
        return new VersionAttributeDescriptor(typeModel, attribute);
    }


    protected Map<LanguageModel, Object> getMapOfLanguages(ItemModel itemModel, AttributeDescriptorModel attribute)
    {
        return (Map<LanguageModel, Object>)getCommonI18NService().getAllLanguages().stream()
                        .collect(HashMap::new, (map, lang) -> getAttributeValueFromItem(itemModel, attribute, getCommonI18NService().getLocaleForLanguage(lang)).ifPresent(()), HashMap::putAll);
    }


    protected Optional<Object> getAttributeValueFromItem(ItemModel itemModel, AttributeDescriptorModel attribute, Locale locale)
    {
        try
        {
            if(locale == null)
            {
                return Optional.ofNullable(getModelService().getAttributeValue(itemModel, attribute.getQualifier()));
            }
            return Optional.ofNullable(getModelService().getAttributeValue(itemModel, attribute.getQualifier(), locale));
        }
        catch(AttributeNotSupportedException e)
        {
            LOGGER.debug("Attribute [{}] is not supported.", attribute.getQualifier());
            return Optional.empty();
        }
    }


    protected Optional<Object> getAttributeValueFromItem(ItemModel itemModel, AttributeDescriptorModel attribute)
    {
        return getAttributeValueFromItem(itemModel, attribute, null);
    }


    protected Map<LanguageModel, Object> convertLocalizedValue(ItemModel itemModel, AttributeDescriptorModel attribute, Function<Object, Object> converter)
    {
        Map<LanguageModel, Object> localizedValues = getMapOfLanguages(itemModel, attribute);
        return (Map<LanguageModel, Object>)localizedValues.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> converter.apply(e.getValue())));
    }


    protected Collection<Object> convertCollection(VersionAttributeDescriptor versionAttributeDescriptor, Collection<Object> collection)
    {
        return (Collection<Object>)collection.stream().map(element -> convertAttributeValue(versionAttributeDescriptor, element)).collect(Collectors.toList());
    }


    protected Object convertAttributeValue(VersionAttributeDescriptor attribute, Object attributeValue)
    {
        return getConverterProvider().getContentConverter(attribute).convertModelToData(attribute, attributeValue);
    }


    protected PayloadSerializer getPayloadSerializer()
    {
        return this.payloadSerializer;
    }


    @Required
    public void setPayloadSerializer(PayloadSerializer payloadSerializer)
    {
        this.payloadSerializer = payloadSerializer;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected AttributeStrategyConverterProvider<VersionAttributeDescriptor> getConverterProvider()
    {
        return this.converterProvider;
    }


    @Required
    public void setConverterProvider(AttributeStrategyConverterProvider<VersionAttributeDescriptor> converterProvider)
    {
        this.converterProvider = converterProvider;
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


    protected Predicate<AttributeDescriptorModel> getIsCollectionPredicate()
    {
        return this.isCollectionPredicate;
    }


    @Required
    public void setIsCollectionPredicate(Predicate<AttributeDescriptorModel> isCollectionPredicate)
    {
        this.isCollectionPredicate = isCollectionPredicate;
    }
}
