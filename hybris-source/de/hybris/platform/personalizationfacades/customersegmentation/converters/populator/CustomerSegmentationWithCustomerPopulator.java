package de.hybris.platform.personalizationfacades.customersegmentation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class CustomerSegmentationWithCustomerPopulator implements Populator<CxUserToSegmentModel, CustomerSegmentationData>
{
    private Converter<UserModel, CustomerData> customerConverter;


    public void populate(CxUserToSegmentModel source, CustomerSegmentationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCustomer((CustomerData)this.customerConverter.convert(source.getUser()));
    }


    protected Converter<UserModel, CustomerData> getCustomerConverter()
    {
        return this.customerConverter;
    }


    public void setCustomerConverter(Converter<UserModel, CustomerData> customerConverter)
    {
        this.customerConverter = customerConverter;
    }
}
