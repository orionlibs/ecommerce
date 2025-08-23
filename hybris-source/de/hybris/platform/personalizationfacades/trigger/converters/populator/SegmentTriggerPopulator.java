package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class SegmentTriggerPopulator implements Populator<CxSegmentTriggerModel, SegmentTriggerData>
{
    private Converter<CxSegmentModel, SegmentData> segmentConverter;


    public void populate(CxSegmentTriggerModel source, SegmentTriggerData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setGroupBy(source.getGroupBy().getCode());
        target.setSegments(this.segmentConverter.convertAll(source.getSegments()));
    }


    protected Converter<CxSegmentModel, SegmentData> getSegmentConverter()
    {
        return this.segmentConverter;
    }


    public void setSegmentConverter(Converter<CxSegmentModel, SegmentData> segmentConverter)
    {
        this.segmentConverter = segmentConverter;
    }
}
