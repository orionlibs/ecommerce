/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.orderexchange.constants;

/**
 *
 * Partner roles for sales order processing as defined in SAP ERP. Corresponds to SAP defined entries of table TPAR
 *
 */
public enum PartnerRoles
{
    @SuppressWarnings("javadoc")
    SOLD_TO("AG"), @SuppressWarnings("javadoc")
CONTACT("AP"), @SuppressWarnings("javadoc")
SHIP_TO("WE"), @SuppressWarnings("javadoc")
BILL_TO("RE"), @SuppressWarnings("javadoc")
PLACED_BY("VE");
    private String code;


    private PartnerRoles(final String code)
    {
        this.code = code;
    }


    @SuppressWarnings("javadoc")
    public String getCode()
    {
        return code;
    }
}
