/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.yorderfulfillment.constants;

/**
 * IDs of Actions as defined in order process
 *
 */
public final class ActionIds
{
    private ActionIds()
    {
        throw new IllegalStateException("Utility class");
    }


    @SuppressWarnings("javadoc")
    public static final String WAIT_FOR_GOODS_ISSUE = "waitForGoodsIssue";
    @SuppressWarnings("javadoc")
    public static final String WAIT_FOR_CONSIGNMENT_CREATION = "waitForConsignmentCreation";
    @SuppressWarnings("javadoc")
    public static final String WAIT_FOR_ERP_CONFIRMATION = "waitForERPConfirmation";
    @SuppressWarnings("javadoc")
    public static final String SET_CANCEL_STATUS = "setCancelStatus";
    @SuppressWarnings("javadoc")
    public static final String SEND_ORDER_AS_IDOC = "sendOrderToErp";
}
