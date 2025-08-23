/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.gigya.gigyab2bservices.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaAuthRequestData;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaAuthResponseData;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaContextData;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaIdentityData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * Populator to populate 'GigyaAuthRequestData' to fetch authorizations from SAP CDC
 */
public class GigyaAuthRequestPopulator implements Populator<CustomerModel, GigyaAuthRequestData>
{
    private Converter<CustomerModel, GigyaIdentityData> gigyaAuthRequestIdentityConverter;
    private Converter<B2BUnitModel, GigyaContextData> gigyaAuthRequestContextConverter;


    @Override
    public void populate(final CustomerModel customer, final GigyaAuthRequestData gigyaAuthRequest) throws ConversionException
    {
        gigyaAuthRequest
                        .setContext(gigyaAuthRequestContextConverter.convert(((B2BCustomerModel)customer).getDefaultB2BUnit()));
        gigyaAuthRequest.setIdentity(gigyaAuthRequestIdentityConverter.convert(customer));
        gigyaAuthRequest.setResponse(new GigyaAuthResponseData());
    }


    public Converter<CustomerModel, GigyaIdentityData> getGigyaAuthRequestIdentityConverter()
    {
        return gigyaAuthRequestIdentityConverter;
    }


    public void setGigyaAuthRequestIdentityConverter(
                    final Converter<CustomerModel, GigyaIdentityData> gigyaAuthRequestIdentityConverter)
    {
        this.gigyaAuthRequestIdentityConverter = gigyaAuthRequestIdentityConverter;
    }


    public Converter<B2BUnitModel, GigyaContextData> getGigyaAuthRequestContextConverter()
    {
        return gigyaAuthRequestContextConverter;
    }


    public void setGigyaAuthRequestContextConverter(
                    final Converter<B2BUnitModel, GigyaContextData> gigyaAuthRequestContextConverter)
    {
        this.gigyaAuthRequestContextConverter = gigyaAuthRequestContextConverter;
    }
}
