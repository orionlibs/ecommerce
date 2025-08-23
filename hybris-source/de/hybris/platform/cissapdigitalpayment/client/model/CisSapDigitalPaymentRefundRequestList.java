/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cissapdigitalpayment.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SAP Digital payment refund request list class
 */
public class CisSapDigitalPaymentRefundRequestList
{
    @JsonProperty("Refunds")
    private List<CisSapDigitalPaymentRefundRequest> cisSapDigitalPaymentRefundRequests;


    /**
     * @return the cisSapDigitalPaymentRefundRequests
     */
    public List<CisSapDigitalPaymentRefundRequest> getCisSapDigitalPaymentRefundRequests()
    {
        return cisSapDigitalPaymentRefundRequests;
    }


    /**
     * @param cisSapDigitalPaymentRefundRequests
     *           the cisSapDigitalPaymentRefundRequests to set
     */
    public void setCisSapDigitalPaymentRefundRequests(
                    final List<CisSapDigitalPaymentRefundRequest> cisSapDigitalPaymentRefundRequests)
    {
        this.cisSapDigitalPaymentRefundRequests = cisSapDigitalPaymentRefundRequests;
    }
}
