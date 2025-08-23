/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.cpq.productconfig.services.model.CloudCPQOrderEntryProductInfoModel;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * helper class providing common operations for the servie layer of sap.hybris.cpq module
 */
public interface ConfigurationServiceLayerHelper
{
    /**
     * extracts the CPQ related product infos from the given entry
     *
     * @param entry
     *           entry
     * @return CPQ related product infos, or null if not existing
     */
    CloudCPQOrderEntryProductInfoModel getCPQInfo(AbstractOrderEntryModel entry);


    /**
     * Executes an action ensuring that that base site of the provided document is set as current base site.<br>
     * After action execution, the previous current base site is restored.
     *
     * @param order
     *           document providing the base site
     * @param action
     *           action to execute
     */
    public void ensureBaseSiteSetAndExecuteConfigurationAction(final AbstractOrderModel order,
                    final Consumer<BaseSiteModel> action);


    /**
     * @param searchFunction
     *           function to deliver pageable results
     * @param searchResultConsumer
     *           consumer for processing the results page wise
     */
    <T> void processPageWise(final IntFunction<SearchPageData<T>> searchFunction, final Consumer<List<T>> searchResultConsumer);
}
