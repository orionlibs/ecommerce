/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cissapdigitalpayment.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SAP Digital payment charge request list class
 */
public class CisSapDigitalPaymentChargeRequestList
{
    @JsonProperty("Charges")
    private List<CisSapDigitalPaymentChargeRequest> cisSapDigitalPaymentChargeRequests;


    /**
     * @return the cisSapDigitalPaymentChargeRequests
     */
    public List<CisSapDigitalPaymentChargeRequest> getCisSapDigitalPaymentChargeRequests()
    {
        return cisSapDigitalPaymentChargeRequests;
    }


    /**
     * @param cisSapDigitalPaymentChargeRequests
     *           the cisSapDigitalPaymentChargeRequests to set
     */
    public void setCisSapDigitalPaymentChargeRequests(
                    final List<CisSapDigitalPaymentChargeRequest> cisSapDigitalPaymentChargeRequests)
    {
        this.cisSapDigitalPaymentChargeRequests = cisSapDigitalPaymentChargeRequests;
    }
}
