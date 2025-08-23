/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.customerticketingc4cintegration.exception;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class C4CServiceException extends BusinessException
{
    public C4CServiceException(String message)
    {
        super(message);
    }


    public C4CServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
