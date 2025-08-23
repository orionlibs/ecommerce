/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;

/**
 * Interface for integration with commerce saved carts
 */
public interface SavedCartIntegrationFacade
{
    /**
     * Retrieves configuration id for a cart entry
     *
     * @param cartCode
     *           cart code
     * @param entryNumber
     *           entry number
     * @return configuration id
     * @throws CommerceSaveCartException
     *            in case cart or entry is not found
     */
    String getConfigurationId(String cartCode, int entryNumber) throws CommerceSaveCartException;


    /**
     * Retrieves product code for a cart entry
     *
     * @param cartCode
     *           cart code
     * @param entryNumber
     *           entry number
     * @return product code
     * @throws CommerceSaveCartException
     *            in case cart or entry is not found
     */
    String getProductCode(String cartCode, int entryNumber) throws CommerceSaveCartException;
}
