/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cissapdigitalpayment.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SAP Digital payment authorization request list class
 */
public class CisSapDigitalPaymentAuthorizationRequestList
{
    @JsonProperty("Authorizations")
    private List<CisSapDigitalPaymentAuthorizationRequest> cisSapDigitalPaymentAuthorizationRequests;


    /**
     * @return the cisSapDigitalPaymentAuthorizationRequests
     */
    public List<CisSapDigitalPaymentAuthorizationRequest> getCisSapDigitalPaymentAuthorizationRequests()
    {
        return cisSapDigitalPaymentAuthorizationRequests;
    }


    /**
     * @param cisSapDigitalPaymentAuthorizationRequests
     *           the cisSapDigitalPaymentAuthorizationRequests to set
     */
    public void setCisSapDigitalPaymentAuthorizationRequests(
                    final List<CisSapDigitalPaymentAuthorizationRequest> cisSapDigitalPaymentAuthorizationRequests)
    {
        this.cisSapDigitalPaymentAuthorizationRequests = cisSapDigitalPaymentAuthorizationRequests;
    }
}
