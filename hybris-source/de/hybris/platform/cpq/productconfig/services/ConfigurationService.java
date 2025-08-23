/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.cpq.productconfig.services.data.ConfigurationSummaryData;
import de.hybris.platform.cpq.productconfig.services.data.ConfigurationSummaryLineItemData;
import java.util.List;

/**
 * Configuration service.
 */
public interface ConfigurationService
{
    /**
     * Creates a new configuration, based on a root product code
     *
     * @param productCode
     *           Root product code
     * @return Configuration Identifier
     */
    String createConfiguration(String productCode);


    /**
     * Retrieves the CPQ configuration summary
     *
     * @param configId
     *           CPQ configuration Id
     *
     * @return CPQ configuration summary
     */
    ConfigurationSummaryData getConfigurationSummary(String configId);


    /**
     * Delete the configuration for given configId
     *
     * @param configId
     *           CPQ configuration Id
     * @return true if deletion was successful
     */
    boolean deleteConfiguration(String configId);


    /**
     * Clones the configuration for given configId
     *
     * @param configId
     *           CPQ configuration Id
     * @param permanent
     *           indicates whether clone should be permanent
     * @return CPQ configuration Id of cloned configuration
     */
    String cloneConfiguration(String configId, boolean permanent);


    /**
     * Removes a cached configuration summary if present. Tolerates summary not being present
     *
     * @param configId
     *           Configuration identifier
     */
    void removeCachedConfigurationSummary(String configId);


    /**
     * Checks if a configuration has issues
     *
     * @param configId
     *           Configuration ID
     * @return Issues?
     */
    boolean hasConfigurationIssues(String configId);


    /**
     * get's the total number of configuration issues
     *
     * @param configId
     *           Identifies the CPQ configuration
     * @return number of configuration issues
     */
    int getNumberOfConfigurationIssues(String configId);


    /**
     * Marks a persisted configuration as permanent.<br>
     * Should be called when the UI is 'done' configuring. Afterwards admin/server is required to do any changes.
     * UI/Client scope is not sufficient anymore to do changes.
     *
     * @param configId
     *           config id
     */
    void makeConfigurationPermanent(String configId);


    /**
     * get's the list of line items
     *
     * @param configId
     *           Identifies the CPQ configuration
     * @return line items
     */
    List<ConfigurationSummaryLineItemData> getLineItems(final String configId);
}
