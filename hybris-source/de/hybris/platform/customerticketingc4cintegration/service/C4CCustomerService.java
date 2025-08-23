/*
 * [y] hybris Platform
 *
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.customerticketingc4cintegration.service;

import de.hybris.platform.customerticketingc4cintegration.data.Contact;
import de.hybris.platform.customerticketingc4cintegration.data.IndividualCustomer;
import de.hybris.platform.customerticketingc4cintegration.exception.C4CServiceException;

/**
 * Ticket service to interact with C4C
 */
public interface C4CCustomerService
{
    /**
     * Get Individual Customer by external ID
     * @param customerId commerce customer id
     * @return Individual Customer by External ID
     * @throws C4CServiceException when error occurs due to invalid data
     */
    IndividualCustomer getIndividualCustomerByExternalId(String customerId) throws C4CServiceException;


    /**
     * Get Contact by external ID
     * @param customerId commerce c4c customerId
     * @return Contact by External ID
     * @throws C4CServiceException when error occurs due to invalid data
     */
    Contact getContactByExternalId(String customerId) throws C4CServiceException;
}
