package de.hybris.platform.personalizationfacades.segment.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.personalizationfacades.enums.TriggerConversionOptions;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel;
import java.util.List;
import org.springframework.util.Assert;

public class SegmentWithTriggerPopulator implements Populator<CxSegmentModel, SegmentData>
{
    private ConfigurableConverter<CxSegmentTriggerModel, SegmentTriggerData, TriggerConversionOptions> segmentTriggerConverter;


    public void populate(CxSegmentModel source, SegmentData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        List<SegmentTriggerData> triggers = this.segmentTriggerConverter.convertAll(source.getTriggers(), (Object[])new TriggerConversionOptions[] {TriggerConversionOptions.FOR_SEGMENT});
        target.setSegmentTriggers(triggers);
    }


    public void setSegmentTriggerConverter(ConfigurableConverter<CxSegmentTriggerModel, SegmentTriggerData, TriggerConversionOptions> segmentTriggerConverter)
    {
        this.segmentTriggerConverter = segmentTriggerConverter;
    }


    protected ConfigurableConverter<CxSegmentTriggerModel, SegmentTriggerData, TriggerConversionOptions> getSegmentTriggerConverter()
    {
        return this.segmentTriggerConverter;
    }
}
