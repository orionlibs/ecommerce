/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.constants;

/**
 * Constants for Order Entry CSV Columns
 */
public class OrderEntryCsvColumns
{
    private OrderEntryCsvColumns()
    {
        throw new IllegalStateException("Utility class");
    }


    //           order entry
    @SuppressWarnings("javadoc")
    public static final String QUANTITY = "quantity";
    @SuppressWarnings("javadoc")
    public static final String NAMED_DELIVERY_DATE = "namedDeliveryDate";
    @SuppressWarnings("javadoc")
    public static final String ENTRY_UNIT_CODE = "entryUnitCode";
    @SuppressWarnings("javadoc")
    public static final String PRODUCT_NAME = "productName";
    @SuppressWarnings("javadoc")
    public static final String EXTERNAL_PRODUCT_CONFIGURATION = "externalConfiguration";
    // general columns
    @SuppressWarnings("javadoc")
    public static final String ENTRY_NUMBER = "entryNumber";
    @SuppressWarnings("javadoc")
    public static final String REJECTION_REASON = "rejectionReason";
    @SuppressWarnings("javadoc")
    public static final String PRODUCT_CODE = "productCode";
    @SuppressWarnings("javadoc")
    public static final String WAREHOUSE = "warehouse";
    @SuppressWarnings("javadoc")
    public static final String EXPECTED_SHIPPING_DATE = "shippingDate";
    @SuppressWarnings("javadoc")
    public static final String ITEM_CATEGORY = "itemCategory";
}
