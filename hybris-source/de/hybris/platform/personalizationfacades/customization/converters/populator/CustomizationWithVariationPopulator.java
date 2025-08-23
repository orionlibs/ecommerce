package de.hybris.platform.personalizationfacades.customization.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.VariationConversionOptions;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.List;
import org.springframework.util.Assert;

public class CustomizationWithVariationPopulator implements Populator<CxCustomizationModel, CustomizationData>
{
    private ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConverter;


    public void populate(CxCustomizationModel source, CustomizationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setVariations(convertVariationList(source));
    }


    protected List<VariationData> convertVariationList(CxCustomizationModel source)
    {
        return this.variationConverter.convertAll(source.getVariations(), (Object[])new VariationConversionOptions[] {VariationConversionOptions.BASE});
    }


    public void setVariationConverter(ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConverter)
    {
        this.variationConverter = variationConverter;
    }


    protected ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> getVariationConverter()
    {
        return this.variationConverter;
    }
}
