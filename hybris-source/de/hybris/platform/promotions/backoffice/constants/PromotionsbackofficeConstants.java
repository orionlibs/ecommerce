/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.promotions.backoffice.constants;

/**
 * Global class for all Ybackoffice constants. You can add global constants for your extension into this class.
 */
public final class PromotionsbackofficeConstants extends GeneratedPromotionsbackofficeConstants
{
    public static final String EXTENSIONNAME = "promotionsbackoffice";


    private PromotionsbackofficeConstants()
    {
        //empty to avoid instantiating this constant class
    }


    // implement here constants used by this extension
    public interface NotificationSource
    {
        String CALCULATION_MESSAGE_SOURCE = GeneratedPromotionsbackofficeConstants.EXTENSIONNAME + "-calculation";
    }
}
