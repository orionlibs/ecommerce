package de.hybris.platform.personalizationfacades.variation.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationfacades.AbstractBaseFacade;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.ItemStatus;
import de.hybris.platform.personalizationfacades.enums.VariationConversionOptions;
import de.hybris.platform.personalizationfacades.variation.VariationFacade;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultVariationFacade extends AbstractBaseFacade implements VariationFacade
{
    private static final String CUSTOMIZATION = "Customization";
    private static final String VARIATION = "Variation";
    private CxVariationService variationService;
    private CxCustomizationService customizationService;
    private ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConverter;
    private Converter<VariationData, CxVariationModel> variationReverseConverter;
    private KeyGenerator variationCodeGenerator;


    public VariationData getVariation(String customizationCode, String variationCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        return getVariation(customizationCode, variationCode, catalogVersion);
    }


    protected VariationData getVariation(String customizationCode, String variationCode, CatalogVersionModel catalogVersion)
    {
        CxCustomizationModel customizationModel = getCustomization(customizationCode, catalogVersion);
        Optional<CxVariationModel> variation = this.variationService.getVariation(variationCode, customizationModel);
        CxVariationModel variationModel = variation.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Variation", variationCode));
        return convert(variationModel, new VariationConversionOptions[] {VariationConversionOptions.FOR_CUSTOMIZATION});
    }


    public List<VariationData> getVariations(String customizationCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxCustomizationModel customizationModel = getCustomization(customizationCode, catalogVersion);
        List<CxVariationModel> variations = filterVariations(customizationModel.getVariations(), catalogVersion);
        return this.variationConverter.convertAll(variations, (Object[])new VariationConversionOptions[] {VariationConversionOptions.FOR_CUSTOMIZATION});
    }


    protected List<CxVariationModel> filterVariations(List<CxVariationModel> variations, CatalogVersionModel catalogVersion)
    {
        if(variations == null || variations.isEmpty())
        {
            return Collections.emptyList();
        }
        return (List<CxVariationModel>)variations.stream()
                        .filter(v -> catalogVersion.equals(v.getCatalogVersion()))
                        .collect(Collectors.toList());
    }


    public VariationData createVariation(String customizationCode, VariationData variation, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        Assert.notNull(variation, "Variation cannot be null");
        validateName("Variation", variation.getName());
        setDefaultPropertiesForData(variation);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxCustomizationModel customizationModel = getCustomization(customizationCode, catalogVersion);
        this.variationService.getVariation(variation.getCode(), customizationModel)
                        .ifPresent(v -> throwAlreadyExists("Variation", variation.getCode()));
        CxVariationModel model = this.variationService.createVariation((CxVariationModel)this.variationReverseConverter.convert(variation), customizationModel, variation
                        .getRank());
        return convert(model, new VariationConversionOptions[] {VariationConversionOptions.BASE});
    }


    protected void setDefaultPropertiesForData(VariationData variation)
    {
        if(StringUtils.isEmpty(variation.getCode()))
        {
            variation.setCode((String)this.variationCodeGenerator.generate());
        }
        if(variation.getStatus() == null)
        {
            variation.setStatus(Boolean.TRUE.equals(variation.getEnabled()) ? ItemStatus.ENABLED : ItemStatus.DISABLED);
        }
    }


    public VariationData updateVariation(String customizationCode, String variationCode, VariationData variation, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        Assert.notNull(variation, "Variation cannot be null");
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        variation.setCode(variationCode);
        CxCustomizationModel customizationModel = getCustomization(customizationCode, catalogVersion);
        CxVariationModel existingVariation = (CxVariationModel)this.variationService.getVariation(variation.getCode(), customizationModel).orElseThrow(() -> createUnknownIdentifierException("Variation", variation.getCode()));
        CxVariationModel model = (CxVariationModel)this.variationReverseConverter.convert(variation, existingVariation);
        getModelService().save(model);
        getModelService().save(model.getCustomization());
        return convert(model, new VariationConversionOptions[] {VariationConversionOptions.BASE});
    }


    public void deleteVariation(String customizationCode, String variationCode, String catalogId, String catalogVersionId)
    {
        validateCode("Customization", customizationCode);
        validateCode("Variation", variationCode);
        CatalogVersionModel catalogVersion = getCatalogVersion(catalogId, catalogVersionId);
        CxCustomizationModel customizationModel = getCustomization(customizationCode, catalogVersion);
        CxVariationModel variation = (CxVariationModel)this.variationService.getVariation(variationCode, customizationModel).orElseThrow(() -> createUnknownIdentifierException("Variation", variationCode));
        getModelService().remove(variation);
        getModelService().refresh(customizationModel);
    }


    protected CxCustomizationModel getCustomization(String customizationCode, CatalogVersionModel catalogVersion)
    {
        Optional<CxCustomizationModel> customization = this.customizationService.getCustomization(customizationCode, catalogVersion);
        return customization.<Throwable>orElseThrow(() -> createUnknownIdentifierException("Customization", customizationCode));
    }


    protected VariationData convert(CxVariationModel variation, VariationConversionOptions... options)
    {
        return (VariationData)this.variationConverter.convert(variation, Arrays.asList(options));
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


    @Required
    public void setVariationConverter(ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConverter)
    {
        this.variationConverter = variationConverter;
    }


    protected ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> getVariationConverter()
    {
        return this.variationConverter;
    }


    @Required
    public void setVariationService(CxVariationService variationService)
    {
        this.variationService = variationService;
    }


    protected CxVariationService getVariationService()
    {
        return this.variationService;
    }


    @Required
    public void setVariationReverseConverter(Converter<VariationData, CxVariationModel> variationReverseConverter)
    {
        this.variationReverseConverter = variationReverseConverter;
    }


    protected Converter<VariationData, CxVariationModel> getVariationReverseConverter()
    {
        return this.variationReverseConverter;
    }


    protected KeyGenerator getVariationCodeGenerator()
    {
        return this.variationCodeGenerator;
    }


    @Required
    public void setVariationCodeGenerator(KeyGenerator variationCodeGenerator)
    {
        this.variationCodeGenerator = variationCodeGenerator;
    }
}
