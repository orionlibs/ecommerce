package de.hybris.platform.personalizationfacades.customersegmentation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.segmentation.SegmentationHelper;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class CustomerSegmentationPopulator implements Populator<CxUserToSegmentModel, CustomerSegmentationData>
{
    private SegmentationHelper segmentationHelper;


    public void populate(CxUserToSegmentModel source, CustomerSegmentationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCode(calculateCode(source));
        target.setAffinity(source.getAffinity());
        target.setProvider(source.getProvider());
        if(source.getBaseSite() != null)
        {
            target.setBaseSite(source.getBaseSite().getUid());
        }
    }


    protected String calculateCode(CxUserToSegmentModel source)
    {
        return this.segmentationHelper.getCustomerSegmentationCode(source);
    }


    protected SegmentationHelper getSegmentationHelper()
    {
        return this.segmentationHelper;
    }


    @Required
    public void setSegmentationHelper(SegmentationHelper segmentationHelper)
    {
        this.segmentationHelper = segmentationHelper;
    }
}
