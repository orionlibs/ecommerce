package de.hybris.platform.personalizationfacades.segment.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class SegmentReversePopulator implements Populator<SegmentData, CxSegmentModel>
{
    public void populate(SegmentData source, CxSegmentModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if(StringUtils.isEmpty(target.getCode()))
        {
            target.setCode(source.getCode());
        }
        target.setDescription(source.getDescription());
    }
}
