package de.hybris.platform.personalizationfacades.variation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.CustomizationConversionOptions;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collections;
import org.springframework.util.Assert;

public class VariationWithCustomizationPopulator implements Populator<CxVariationModel, VariationData>
{
    private ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> customizationConverter;


    public void populate(CxVariationModel source, VariationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCustomization(convertCustomization(source));
    }


    protected CustomizationData convertCustomization(CxVariationModel source)
    {
        return (CustomizationData)this.customizationConverter.convert(source.getCustomization(),
                        Collections.singletonList(CustomizationConversionOptions.FOR_VARIATION));
    }


    public void setCustomizationConverter(ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> customizationConverter)
    {
        this.customizationConverter = customizationConverter;
    }


    protected ConfigurableConverter<CxCustomizationModel, CustomizationData, CustomizationConversionOptions> getCustomizationConverter()
    {
        return this.customizationConverter;
    }
}
