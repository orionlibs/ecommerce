/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.businessobject.interf;

/**
 * Represents the ShipTo object
 *
 */
public interface ShipTo extends PartnerBase
{
    /**
     * @see PartnerBase#clone
     */
    @Override
    @SuppressWarnings("squid:S2975")
    ShipTo clone();
}