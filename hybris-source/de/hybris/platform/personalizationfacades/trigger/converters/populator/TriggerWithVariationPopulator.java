package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.VariationConversionOptions;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.Collections;
import org.springframework.util.Assert;

public class TriggerWithVariationPopulator implements Populator<CxAbstractTriggerModel, TriggerData>
{
    private ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConfigurableConverter;


    public void populate(CxAbstractTriggerModel source, TriggerData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setVariation(convertVariation(source));
    }


    protected VariationData convertVariation(CxAbstractTriggerModel source)
    {
        return (VariationData)this.variationConfigurableConverter.convert(source.getVariation(), Collections.singletonList(VariationConversionOptions.FOR_TRIGGER));
    }


    public void setVariationConfigurableConverter(ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> variationConfigurableConverter)
    {
        this.variationConfigurableConverter = variationConfigurableConverter;
    }


    protected ConfigurableConverter<CxVariationModel, VariationData, VariationConversionOptions> getVariationConfigurableConverter()
    {
        return this.variationConfigurableConverter;
    }
}
