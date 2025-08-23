/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cissapdigitalpayment.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SAP Digital payment authorization result list
 */
public class CisSapDigitalPaymentAuthorizationResultList
{
    @JsonProperty("Authorizations")
    private List<CisSapDigitalPaymentAuthorizationResult> cisSapDigitalPaymentAuthorizationResults;


    /**
     * @return the cisSapDigitalPaymentAuthorizationResults
     */
    public List<CisSapDigitalPaymentAuthorizationResult> getCisSapDigitalPaymentAuthorizationResults()
    {
        return cisSapDigitalPaymentAuthorizationResults;
    }


    /**
     * @param cisSapDigitalPaymentAuthorizationResults
     *           the cisSapDigitalPaymentAuthorizationResults to set
     */
    public void setCisSapDigitalPaymentAuthorizationResults(
                    final List<CisSapDigitalPaymentAuthorizationResult> cisSapDigitalPaymentAuthorizationResults)
    {
        this.cisSapDigitalPaymentAuthorizationResults = cisSapDigitalPaymentAuthorizationResults;
    }
}
