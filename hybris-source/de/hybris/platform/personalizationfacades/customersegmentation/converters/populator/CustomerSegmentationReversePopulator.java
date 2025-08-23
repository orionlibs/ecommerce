package de.hybris.platform.personalizationfacades.customersegmentation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class CustomerSegmentationReversePopulator implements Populator<CustomerSegmentationData, CxUserToSegmentModel>
{
    private Converter<SegmentData, CxSegmentModel> segmentConverter;
    private Converter<CustomerData, UserModel> customerConverter;


    public void populate(CustomerSegmentationData source, CxUserToSegmentModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setAffinity(source.getAffinity());
        target.setProvider(source.getProvider());
        if(this.customerConverter != null)
        {
            target.setUser((UserModel)this.customerConverter.convert(source.getCustomer()));
        }
        if(this.segmentConverter != null)
        {
            target.setSegment((CxSegmentModel)this.segmentConverter.convert(source.getSegment()));
        }
    }


    protected Converter<SegmentData, CxSegmentModel> getSegmentConverter()
    {
        return this.segmentConverter;
    }


    protected Converter<CustomerData, UserModel> getCustomerConverter()
    {
        return this.customerConverter;
    }


    public void setCustomerConverter(Converter<CustomerData, UserModel> customerConverter)
    {
        this.customerConverter = customerConverter;
    }


    public void setSegmentConverter(Converter<SegmentData, CxSegmentModel> segmentConverter)
    {
        this.segmentConverter = segmentConverter;
    }
}
