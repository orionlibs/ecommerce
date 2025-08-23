package de.hybris.platform.personalizationfacades.segment.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import org.springframework.util.Assert;

public class SegmentPopulator implements Populator<CxSegmentModel, SegmentData>
{
    public void populate(CxSegmentModel source, SegmentData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(source.getCode());
        target.setDescription(source.getDescription());
    }
}
