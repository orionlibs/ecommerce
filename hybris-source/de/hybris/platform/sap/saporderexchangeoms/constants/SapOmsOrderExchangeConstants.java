/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saporderexchangeoms.constants;

/**
 * Global class for all SAP OMS data hub inbound constants
 */
public class SapOmsOrderExchangeConstants
{
    private SapOmsOrderExchangeConstants()
    {
        throw new IllegalStateException("Utility class");
    }


    @SuppressWarnings("javadoc")
    public static final String CONSIGNMENT_ACTION_EVENT_NAME = "SapConsignmentActionEvent";
    @SuppressWarnings("javadoc")
    public static final String CONSIGNMENT_PROCESS_CANCELLED = "consignmentProcessCancelled";
    @SuppressWarnings("javadoc")
    public static final String CONSIGNMENT_DELIVERY_CONFIRMED = "consignmentDeliveryConfirmed";
    @SuppressWarnings("javadoc")
    public static final String INTERNAl_VENDOR = "ERP";
    @SuppressWarnings("javadoc")
    public static final String VENDOR_ITEM_CATEGORY = "TAB";
    @SuppressWarnings("javadoc")
    public static final String ERROR_END_MESSAGE = "Sending to ERP went wrong.";
    @SuppressWarnings("javadoc")
    public static final String SAP_CONS = "_sap_cons_";
    @SuppressWarnings("javadoc")
    public static final String CONSIGNMENT_SUBPROCESS_NAME = "sap-oms-consignment-process";
    @SuppressWarnings("javadoc")
    public static final String MISSING_LOG_SYS = "MISSING_LOGICAL_SYSTEM";
    @SuppressWarnings("javadoc")
    public static final String MISSING_SALES_ORG = "MISSING_SALES_ORG";
    @SuppressWarnings("javadoc")
    public static final int DEFAULT_MAX_RETRIES = 10;
    @SuppressWarnings("javadoc")
    public static final int DEFAULT_RETRY_DELAY = 60 * 1000; // 60,000 milliseconds = 1 minute
}
