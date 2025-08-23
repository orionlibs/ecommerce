package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class AddressValidator implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof AddressModel)
        {
            AddressModel address = (AddressModel)model;
            CountryModel country = address.getCountry();
            RegionModel region = address.getRegion();
            if(country != null && region != null)
            {
                if(region.getCountry() == null || !region.getCountry().equals(country))
                {
                    throw new InterceptorException("Region '" + region.getIsocode() + "' doesn't belong to Country '" + country
                                    .getIsocode() + "'.");
                }
            }
        }
    }
}
