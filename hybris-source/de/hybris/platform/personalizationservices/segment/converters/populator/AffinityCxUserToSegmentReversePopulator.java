package de.hybris.platform.personalizationservices.segment.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import org.springframework.util.Assert;

public class AffinityCxUserToSegmentReversePopulator implements Populator<UserToSegmentData, CxUserToSegmentModel>
{
    public void populate(UserToSegmentData source, CxUserToSegmentModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setAffinity(source.getAffinity());
    }
}
