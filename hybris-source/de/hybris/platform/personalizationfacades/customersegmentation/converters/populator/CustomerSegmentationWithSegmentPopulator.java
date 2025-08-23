package de.hybris.platform.personalizationfacades.customersegmentation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class CustomerSegmentationWithSegmentPopulator implements Populator<CxUserToSegmentModel, CustomerSegmentationData>
{
    private Converter<CxSegmentModel, SegmentData> segmentConverter;


    public void populate(CxUserToSegmentModel source, CustomerSegmentationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setSegment((SegmentData)this.segmentConverter.convert(source.getSegment()));
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
