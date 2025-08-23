/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcommonbol.businesspartner.businessobject.impl;

import de.hybris.platform.sap.sapcommonbol.businesspartner.businessobject.interf.PartnerFunctionData;

/**
 * Constant for partner function contact.
 *
 */
public class Contact extends PartnerFunctionBase
{
    @Override
    public String getName()
    {
        return PartnerFunctionData.CONTACT;
    }
}