/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades;

/**
 * CPQ facade for integration with quotes
 */
public interface QuoteIntegrationFacade
{
    /**
     * Retrieves configuration id for a quote entry
     *
     * @param quotesCode
     *           quote Code
     * @param entryNumber
     *           entry number
     * @return configuration id
     */
    String getConfigurationId(String quotesCode, int entryNumber);


    /**
     * Retrieves product code for a quote entry
     *
     * @param quotesCode
     *           quote code
     * @param entryNumber
     *           entry number
     * @return product code
     */
    String getProductCode(String quotesCode, int entryNumber);
}
