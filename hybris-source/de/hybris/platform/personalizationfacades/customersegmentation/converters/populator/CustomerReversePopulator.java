package de.hybris.platform.personalizationfacades.customersegmentation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import org.springframework.util.Assert;

public class CustomerReversePopulator implements Populator<CustomerData, UserModel>
{
    public void populate(CustomerData source, UserModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setUid(source.getUid());
    }
}
