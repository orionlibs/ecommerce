package de.hybris.platform.personalizationfacades.variation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.TriggerConversionOptions;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import java.util.List;
import org.springframework.util.Assert;

public class VariationWithTriggerPopulator implements Populator<CxVariationModel, VariationData>
{
    private ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> triggerConverter;


    public void populate(CxVariationModel source, VariationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setTriggers(convertTriggers(source));
    }


    protected List<TriggerData> convertTriggers(CxVariationModel source)
    {
        return this.triggerConverter.convertAll(source.getTriggers(), (Object[])new TriggerConversionOptions[] {TriggerConversionOptions.FOR_VARIATION});
    }


    protected ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> getTriggerConverter()
    {
        return this.triggerConverter;
    }


    public void setTriggerConverter(ConfigurableConverter<CxAbstractTriggerModel, TriggerData, TriggerConversionOptions> triggerConverter)
    {
        this.triggerConverter = triggerConverter;
    }
}
