package de.hybris.platform.personalizationfacades.trigger.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationfacades.enums.TriggerConversionOptions;
import de.hybris.platform.personalizationfacades.trigger.TriggerFacade;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.trigger.CxTriggerService;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.List;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

public class DefaultTriggerFacade extends AbstractBaseFacade implements TriggerFacade
{
    private static final String TRIGGER = "Trigger";
    private static final String VARIATION = "Variation";
    private static final String CUSTOMIZATION = "Customization";
    private CxTriggerService cxTriggerService;
    private CxVariationService cxVariationService;
    private CxCustomizationService cxCustomizationService;
    private CxSegmentService cxSegmentService;
    private ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> cxTriggerConfigurableConverter;
    private Converter<TriggerData, CxAbstractTriggerModel> cxTriggerReverseConverter;
    private KeyGenerator triggerCodeGenerator;


    public TriggerData getTrigger(String customizationCode, String variationCode, String triggerCode, String catalogId, String catalogVersionId)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variation = getVariation(customizationCode, variationCode, catalogVersion);
        CxAbstractTriggerModel triggerModel = (CxAbstractTriggerModel)this.cxTriggerService.getTrigger(triggerCode, variation).orElseThrow(() -> createUnknownIdentifierException("Trigger", triggerCode));
        return convertTrigger(triggerModel, new TriggerConversionOptions[] {TriggerConversionOptions.FOR_VARIATION});
    }


    public List<TriggerData> getTriggers(String customizationCode, String variationCode, String catalogId, String catalogVersionId)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variation = getVariation(customizationCode, variationCode, catalogVersion);
        Collection<CxAbstractTriggerModel> triggers = this.cxTriggerService.getTriggers(variation);
        return this.cxTriggerConfigurableConverter.convertAll(triggers, (Object[])new TriggerConversionOptions[] {TriggerConversionOptions.FOR_VARIATION});
    }


    public TriggerData createTrigger(String customizationCode, String variationCode, TriggerData data, String catalogId, String catalogVersionId)
    {
        ServicesUtil.validateParameterNotNull(data, "TriggerData can't be null");
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variation = getVariation(customizationCode, variationCode, catalogVersion);
        setDefaultPropertiesForData(data);
        this.cxTriggerService.getTrigger(data.getCode(), variation).ifPresent(a -> throwAlreadyExists("Trigger", data.getCode()));
        try
        {
            CxAbstractTriggerModel result = this.cxTriggerService.createTrigger((CxAbstractTriggerModel)this.cxTriggerReverseConverter.convert(data), variation);
            return convertTrigger(result, new TriggerConversionOptions[] {TriggerConversionOptions.FOR_VARIATION});
        }
        catch(ModelSavingException e)
        {
            throw new IllegalArgumentException("Invalid data, can't create trigger", e);
        }
    }


    public TriggerData updateTrigger(String customizationCode, String variationCode, String triggerCode, TriggerData data, String catalogId, String catalogVersionId)
    {
        validateCode("Trigger", triggerCode);
        ServicesUtil.validateParameterNotNull(data, "TriggerData can't be null");
        data.setCode(triggerCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variation = getVariation(customizationCode, variationCode, catalogVersion);
        CxAbstractTriggerModel existingTrigger = (CxAbstractTriggerModel)this.cxTriggerService.getTrigger(triggerCode, variation).orElseThrow(() -> createUnknownIdentifierException("Trigger", triggerCode));
        try
        {
            CxAbstractTriggerModel model = (CxAbstractTriggerModel)this.cxTriggerReverseConverter.convert(data, existingTrigger);
            getModelService().save(model);
            return convertTrigger(model, new TriggerConversionOptions[] {TriggerConversionOptions.FOR_VARIATION});
        }
        catch(ModelSavingException e)
        {
            throw new IllegalArgumentException("Invalid data, can't update trigger", e);
        }
    }


    public void deleteTrigger(String customizationCode, String variationCode, String triggerCode, String catalogId, String catalogVersionId)
    {
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxVariationModel variation = getVariation(customizationCode, variationCode, catalogVersion);
        CxAbstractTriggerModel trigger = (CxAbstractTriggerModel)this.cxTriggerService.getTrigger(triggerCode, variation).orElseThrow(() -> createUnknownIdentifierException("Trigger", triggerCode));
        getModelService().remove(trigger);
        getModelService().refresh(variation);
    }


    protected CxVariationModel getVariation(String customizationCode, String variationCode, CatalogVersionModel catalogVersion)
    {
        CxCustomizationModel customization = (CxCustomizationModel)this.cxCustomizationService.getCustomization(customizationCode, catalogVersion).orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
        return (CxVariationModel)this.cxVariationService.getVariation(variationCode, customization)
                        .orElseThrow(() -> createUnknownIdentifierException("Variation", variationCode));
    }


    protected TriggerData convertTrigger(CxAbstractTriggerModel model, TriggerConversionOptions... options)
    {
        return (TriggerData)this.cxTriggerConfigurableConverter.convert(model, Lists.newArrayList((Object[])options));
    }


    protected void setDefaultPropertiesForData(TriggerData data)
    {
        if(StringUtils.isEmpty(data.getCode()))
        {
            data.setCode((String)this.triggerCodeGenerator.generate());
        }
    }


    protected ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> getCxTriggerConfigurableConverter()
    {
        return this.cxTriggerConfigurableConverter;
    }


    public void setCxTriggerConfigurableConverter(ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> cxTriggerConfigurableConverter)
    {
        this.cxTriggerConfigurableConverter = cxTriggerConfigurableConverter;
    }


    protected Converter<TriggerData, CxAbstractTriggerModel> getCxTriggerReverseConverter()
    {
        return this.cxTriggerReverseConverter;
    }


    public void setCxTriggerReverseConverter(Converter<TriggerData, CxAbstractTriggerModel> cxTriggerReverseConverter)
    {
        this.cxTriggerReverseConverter = cxTriggerReverseConverter;
    }


    protected CxTriggerService getCxTriggerService()
    {
        return this.cxTriggerService;
    }


    public void setCxTriggerService(CxTriggerService cxTriggerService)
    {
        this.cxTriggerService = cxTriggerService;
    }


    protected CxVariationService getCxVariationService()
    {
        return this.cxVariationService;
    }


    public void setCxVariationService(CxVariationService cxVariationService)
    {
        this.cxVariationService = cxVariationService;
    }


    protected CxCustomizationService getCxCustomizationService()
    {
        return this.cxCustomizationService;
    }


    public void setCxCustomizationService(CxCustomizationService cxCustomizationService)
    {
        this.cxCustomizationService = cxCustomizationService;
    }


    protected KeyGenerator getTriggerCodeGenerator()
    {
        return this.triggerCodeGenerator;
    }


    public void setTriggerCodeGenerator(KeyGenerator triggerCodeGenerator)
    {
        this.triggerCodeGenerator = triggerCodeGenerator;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }
}
