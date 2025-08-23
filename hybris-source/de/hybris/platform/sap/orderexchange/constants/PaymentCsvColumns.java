/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.constants;

/**
 * Constants for Payment CSV Columns
 */
public class PaymentCsvColumns
{
    private PaymentCsvColumns()
    {
        throw new IllegalStateException("Utility class");
    }


    @SuppressWarnings("javadoc")
    public static final String REQUEST_ID = "requestId";
    @SuppressWarnings("javadoc")
    public static final String PAYMENT_PROVIDER = "paymentProvider";
    @SuppressWarnings("javadoc")
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    @SuppressWarnings("javadoc")
    public static final String VALID_TO_YEAR = "validToYear";
    @SuppressWarnings("javadoc")
    public static final String VALID_TO_MONTH = "validToMonth";
    @SuppressWarnings("javadoc")
    public static final String CC_OWNER = "ccOwner";
}
