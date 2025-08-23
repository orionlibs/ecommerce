/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.constants;

/**
 * Constants for Order CSV Columns
 */
public class OrderCsvColumns
{
    private OrderCsvColumns()
    {
        throw new IllegalStateException("Utility class");
    }


    //	order
    @SuppressWarnings("javadoc")
    public static final String DATE = "date";
    @SuppressWarnings("javadoc")
    public static final String DELIVERY_MODE = "deliveryMode";
    @SuppressWarnings("javadoc")
    public static final String PAYMENT_MODE = "paymentMode";
    @SuppressWarnings("javadoc")
    public static final String ORDER_CURRENCY_ISO_CODE = "orderCurrencyIsoCode";
    @SuppressWarnings("javadoc")
    public static final String LOGICAL_SYSTEM = "logicalSystem";
    @SuppressWarnings("javadoc")
    public static final String SALES_ORGANIZATION = "salesOrganization";
    @SuppressWarnings("javadoc")
    public static final String DISTRIBUTION_CHANNEL = "distributionChannel";
    @SuppressWarnings("javadoc")
    public static final String DIVISION = "division";
    //	general columns
    @SuppressWarnings("javadoc")
    public static final String ORDER_ID = "orderId";
    @SuppressWarnings("javadoc")
    public static final String PURCHASE_ORDER_NUMBER = "purchaseOrderNumber";
    @SuppressWarnings("javadoc")
    public static final String BASE_STORE = "baseStore";
    @SuppressWarnings("javadoc")
    public static final String CHANNEL = "channel";
}
