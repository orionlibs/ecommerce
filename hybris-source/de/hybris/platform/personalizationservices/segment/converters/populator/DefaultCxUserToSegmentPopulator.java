package de.hybris.platform.personalizationservices.segment.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import org.springframework.util.Assert;

public class DefaultCxUserToSegmentPopulator implements Populator<CxUserToSegmentModel, UserToSegmentData>
{
    public void populate(CxUserToSegmentModel source, UserToSegmentData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(source.getSegment().getCode());
        target.setAffinity(source.getAffinity());
        if(source.getSegment() != null)
        {
            target.setDescription(source.getSegment().getDescription());
        }
        target.setProvider(source.getProvider());
    }
}
