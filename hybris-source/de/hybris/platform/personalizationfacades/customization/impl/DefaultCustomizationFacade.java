package de.hybris.platform.personalizationfacades.customization.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.customization.CustomizationFacade;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.CustomizationConversionOptions;
import de.hybris.platform.personalizationfacades.enums.ItemStatus;
import de.hybris.platform.personalizationfacades.trigger.TriggerFacade;
import de.hybris.platform.personalizationfacades.variation.VariationFacade;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultCustomizationFacade extends AbstractBaseFacade implements CustomizationFacade
{
    private static final String CUSTOMIZATION = "Customization";
    private static final Logger LOG = Logger.getLogger(DefaultCustomizationFacade.class);
    private CxCustomizationService customizationService;
    private ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> customizationConverter;
    private Converter<CustomizationData, CxCustomizationModel> customizationReverseConverter;
    private VariationFacade variationFacade;
    private TriggerFacade triggerFacade;
    private KeyGenerator customizationCodeGenerator;


    public List<CustomizationData> getCustomizations(String catalogId, String catalogVersionId)
    {
        List<CxCustomizationModel> customizations = this.customizationService.getCustomizations(getCatalogVersion(catalogId, catalogVersionId));
        return this.customizationConverter.convertAll(customizations, (Object[])new CustomizationConversionOptions[] {CustomizationConversionOptions.FULL});
    }


    public SearchPageData<CustomizationData> getCustomizations(String catalogId, String catalogVersionId, Map<String, String> params, SearchPageData<?> pagination)
    {
        SearchPageData<CxCustomizationModel> customizations = this.customizationService.getCustomizations(getCatalogVersion(catalogId, catalogVersionId), params, pagination);
        return convertSearchPage(customizations, c -> this.customizationConverter.convertAll(c, (Object[])new CustomizationConversionOptions[] {CustomizationConversionOptions.FULL}));
    }


    public CustomizationData getCustomization(String customizationCode, String catalogId, String catalogVersionId)
    {
        CxCustomizationModel customization = (CxCustomizationModel)this.customizationService.getCustomization(customizationCode, getCatalogVersion(catalogId, catalogVersionId)).orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
        return createCustomizationData(customization, new CustomizationConversionOptions[] {CustomizationConversionOptions.FULL});
    }


    public CustomizationData createCustomization(CustomizationData customization, String catalogId, String catalogVersionId)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        validateNewCustomizationData(customization, catalogVersion);
        setDefaultPropertiesForData(customization);
        CxCustomizationsGroupModel cxCustomizationsGroupModel = getOrCreateCustomizationGroup(catalogVersion);
        CxCustomizationModel model = this.customizationService.createCustomization((CxCustomizationModel)this.customizationReverseConverter
                        .convert(customization), cxCustomizationsGroupModel, customization.getRank());
        return createCustomizationData(model, new CustomizationConversionOptions[] {CustomizationConversionOptions.BASE});
    }


    public CustomizationData createCustomizationWithRelatedObjects(CustomizationData customization, String catalogId, String catalogVersionId)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        validateNewCustomizationData(customization, catalogVersion);
        setDefaultPropertiesForData(customization);
        CxCustomizationModel model = (CxCustomizationModel)executeInTransaction(() -> {
            CxCustomizationsGroupModel cxCustomizationsGroupModel = getOrCreateCustomizationGroup(catalogVersion);
            CxCustomizationModel newCust = this.customizationService.createCustomization((CxCustomizationModel)this.customizationReverseConverter.convert(customization), cxCustomizationsGroupModel, customization.getRank());
            createVariations(customization.getVariations(), customization.getCode(), catalogId, catalogVersionId);
            return newCust;
        });
        return createCustomizationData(model, new CustomizationConversionOptions[] {CustomizationConversionOptions.BASE});
    }


    protected CxCustomizationsGroupModel getOrCreateCustomizationGroup(CatalogVersionModel catalogVersion)
    {
        if(this.customizationService.isDefaultGroup(catalogVersion))
        {
            return this.customizationService.getDefaultGroup(catalogVersion);
        }
        return createCustomizationGroup(catalogVersion);
    }


    protected CxCustomizationsGroupModel createCustomizationGroup(CatalogVersionModel catalogVersion)
    {
        CxCustomizationsGroupModel cxCustomizationsGroupModel = (CxCustomizationsGroupModel)getModelService().create(CxCustomizationsGroupModel.class);
        cxCustomizationsGroupModel.setCode("default");
        cxCustomizationsGroupModel.setCatalogVersion(catalogVersion);
        getModelService().save(cxCustomizationsGroupModel);
        return cxCustomizationsGroupModel;
    }


    protected void setDefaultPropertiesForData(CustomizationData customization)
    {
        if(StringUtils.isEmpty(customization.getCode()))
        {
            customization.setCode((String)this.customizationCodeGenerator.generate());
        }
        if(customization.getStatus() == null)
        {
            customization.setStatus(ItemStatus.ENABLED);
        }
    }


    protected void validateNewCustomizationData(CustomizationData customization, CatalogVersionModel catalogVersion)
    {
        Assert.notNull(customization, "Parameter [customization] must not be null");
        validateName("Customization", customization.getName());
        if(StringUtils.isNotEmpty(customization.getCode()))
        {
            this.customizationService.getCustomization(customization.getCode(), catalogVersion)
                            .ifPresent(c -> throwAlreadyExists("Customization", customization.getCode()));
        }
    }


    protected void refreshAll(CxCustomizationModel customization)
    {
        if(customization == null)
        {
            return;
        }
        try
        {
            getModelService().refresh(customization);
            customization.getVariations().forEach(v -> {
                getModelService().refresh(v);
                Objects.requireNonNull(getModelService());
                v.getTriggers().forEach(getModelService()::refresh);
            });
        }
        catch(RuntimeException e)
        {
            LOG.warn("Refresh customization failed", e);
        }
    }


    protected void createVariations(List<VariationData> variations, String customizationCode, String catalogId, String catalogVersionId)
    {
        if(CollectionUtils.isEmpty(variations))
        {
            return;
        }
        variations.forEach(variation -> {
            this.variationFacade.createVariation(customizationCode, variation, catalogId, catalogVersionId);
            createTriggers(variation.getTriggers(), customizationCode, variation.getCode(), catalogId, catalogVersionId);
        });
    }


    protected void createTriggers(List<TriggerData> triggers, String customizationCode, String variationCode, String catalogId, String catalogVersionId)
    {
        if(CollectionUtils.isEmpty(triggers))
        {
            return;
        }
        triggers.forEach(trigger -> this.triggerFacade.createTrigger(customizationCode, variationCode, trigger, catalogId, catalogVersionId));
    }


    public CustomizationData updateCustomization(String customizationCode, CustomizationData customization, String catalogId, String catalogVersionId)
    {
        CxCustomizationModel existingCustomization = validateCustomizationDataForUpdate(customizationCode, customization, catalogId, catalogVersionId);
        customization.setCode(customizationCode);
        CxCustomizationModel model = (CxCustomizationModel)this.customizationReverseConverter.convert(customization, existingCustomization);
        getModelService().save(model);
        getModelService().save(model.getGroup());
        return createCustomizationData(model, new CustomizationConversionOptions[] {CustomizationConversionOptions.BASE});
    }


    protected CxCustomizationModel validateCustomizationDataForUpdate(String customizationCode, CustomizationData customizationData, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        Assert.notNull(customizationData, "Parameter [customizationData] must not be null");
        return (CxCustomizationModel)this.customizationService.getCustomization(customizationCode, getCatalogVersion(catalogId, catalogVersionId))
                        .orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
    }


    public CustomizationData updateCustomizationWithRelatedObjects(String customizationCode, CustomizationData customizationData, String catalogId, String catalogVersionId)
    {
        CxCustomizationModel existingCustomization = validateCustomizationDataForUpdate(customizationCode, customizationData, catalogId, catalogVersionId);
        customizationData.setCode(customizationCode);
        CxCustomizationModel model = (CxCustomizationModel)this.customizationReverseConverter.convert(customizationData, existingCustomization);
        executeInTransaction(() -> {
            getModelService().save(model);
            getModelService().save(model.getGroup());
            updateVariations(customizationData.getVariations(), model, catalogId, catalogVersionId);
            return model;
        } () -> refreshAll(existingCustomization));
        return createCustomizationData(model, new CustomizationConversionOptions[] {CustomizationConversionOptions.BASE});
    }


    protected void updateVariations(List<VariationData> variations, CxCustomizationModel customization, String catalogId, String catalogVersionId)
    {
        if(CollectionUtils.isEmpty(variations))
        {
            getModelService().removeAll(customization.getVariations());
            getModelService().refresh(customization);
            return;
        }
        Map<String, CxVariationModel> existingVariations = getCodeToVariationMap(customization.getVariations());
        removeItems(customization, existingVariations, (Set<String>)variations.stream().map(v -> v.getCode()).collect(Collectors.toSet()));
        variations.forEach(variationData -> {
            if(existingVariations.containsKey(variationData.getCode()))
            {
                this.variationFacade.updateVariation(customization.getCode(), variationData.getCode(), variationData, catalogId, catalogVersionId);
                updateTriggers(variationData.getTriggers(), (CxVariationModel)existingVariations.get(variationData.getCode()), catalogId, catalogVersionId);
            }
            else
            {
                this.variationFacade.createVariation(customization.getCode(), variationData, catalogId, catalogVersionId);
                createTriggers(variationData.getTriggers(), customization.getCode(), variationData.getCode(), catalogId, catalogVersionId);
            }
        });
    }


    protected Map<String, CxVariationModel> getCodeToVariationMap(Collection<CxVariationModel> items)
    {
        return (Map<String, CxVariationModel>)items.stream().collect(Collectors.toMap(v -> v.getCode(), v -> v));
    }


    protected <P extends de.hybris.platform.core.model.ItemModel, I extends de.hybris.platform.core.model.ItemModel> void removeItems(P parent, Map<String, I> existingItems, Set<String> items)
    {
        Objects.requireNonNull(existingItems);
        Objects.requireNonNull(getModelService());
        existingItems.keySet().stream().filter(triggerCode -> !items.contains(triggerCode)).map(existingItems::get).forEach(getModelService()::remove);
        getModelService().refresh(parent);
    }


    protected void updateTriggers(List<TriggerData> triggers, CxVariationModel variation, String catalogId, String catalogVersionId)
    {
        if(CollectionUtils.isEmpty(triggers))
        {
            getModelService().removeAll(variation.getTriggers());
            getModelService().refresh(variation);
            return;
        }
        Map<String, CxAbstractTriggerModel> existingTriggers = getCodeToTriggerMap(variation.getTriggers());
        removeItems(variation, existingTriggers, (Set<String>)triggers.stream().map(v -> v.getCode()).collect(Collectors.toSet()));
        String customizationCode = variation.getCustomization().getCode();
        triggers.forEach(trigger -> {
            if(existingTriggers.containsKey(trigger.getCode()))
            {
                this.triggerFacade.updateTrigger(customizationCode, variation.getCode(), trigger.getCode(), trigger, catalogId, catalogVersionId);
            }
            else
            {
                this.triggerFacade.createTrigger(customizationCode, variation.getCode(), trigger, catalogId, catalogVersionId);
            }
        });
        getModelService().refresh(variation);
    }


    protected Map<String, CxAbstractTriggerModel> getCodeToTriggerMap(Collection<CxAbstractTriggerModel> items)
    {
        return (Map<String, CxAbstractTriggerModel>)items.stream().collect(Collectors.toMap(v -> v.getCode(), v -> v));
    }


    public void removeCustomization(String customizationCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        CxCustomizationModel customization = (CxCustomizationModel)this.customizationService.getCustomization(customizationCode, getCatalogVersion(catalogId, catalogVersionId)).orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
        CxCustomizationsGroupModel group = customization.getGroup();
        getModelService().remove(customization);
        getModelService().refresh(group);
    }


    protected CustomizationData createCustomizationData(CxCustomizationModel input, CustomizationConversionOptions... options)
    {
        return (CustomizationData)this.customizationConverter.convert(input, Arrays.asList(options));
    }


    @Required
    public void setCustomizationConverter(ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> customizationConverter)
    {
        this.customizationConverter = customizationConverter;
    }


    protected ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> getCustomizationConverter()
    {
        return this.customizationConverter;
    }


    @Required
    public void setCustomizationReverseConverter(Converter<CustomizationData, CxCustomizationModel> customizationReverseConverter)
    {
        this.customizationReverseConverter = customizationReverseConverter;
    }


    protected Converter<CustomizationData, CxCustomizationModel> getCustomizationReverseConverter()
    {
        return this.customizationReverseConverter;
    }


    @Required
    public void setCustomizationService(CxCustomizationService customizationService)
    {
        this.customizationService = customizationService;
    }


    protected CxCustomizationService getCustomizationService()
    {
        return this.customizationService;
    }


    protected TriggerFacade getTriggerFacade()
    {
        return this.triggerFacade;
    }


    @Required
    public void setTriggerFacade(TriggerFacade triggerFacade)
    {
        this.triggerFacade = triggerFacade;
    }


    protected VariationFacade getVariationFacade()
    {
        return this.variationFacade;
    }


    @Required
    public void setVariationFacade(VariationFacade variationFacade)
    {
        this.variationFacade = variationFacade;
    }


    protected KeyGenerator getCustomizationCodeGenerator()
    {
        return this.customizationCodeGenerator;
    }


    @Required
    public void setCustomizationCodeGenerator(KeyGenerator customizationCodeGenerator)
    {
        this.customizationCodeGenerator = customizationCodeGenerator;
    }
}
