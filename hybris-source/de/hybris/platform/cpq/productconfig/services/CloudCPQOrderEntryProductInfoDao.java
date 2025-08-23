/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.cpq.productconfig.services.model.CloudCPQOrderEntryProductInfoModel;
import java.util.List;

/**
 * Data Access Object for {@link CloudCPQOrderEntryProductInfoModel}
 */
public interface CloudCPQOrderEntryProductInfoDao
{
    /**
     * @param entry
     *           entry to compare to
     * @param configurationId
     *           cpq configuration id
     * @return <code>true</code>, only if the given configuration id is not related to any other entry than the given
     *         one.
     */
    public boolean isOnlyRelatedToGivenEntry(final AbstractOrderEntryModel entry, String configurationId);


    /**
     * @param configId
     *           cpq configuration id
     * @return all {@link CloudCPQOrderEntryProductInfoModel}s that are linked to the given cpq configuration id
     */
    public List<CloudCPQOrderEntryProductInfoModel> getAllConfigurationProductInfosById(final String configId);


    /**
     * Fetches all {@link CloudCPQOrderEntryProductInfoModel}s that have a non null configuration id.
     *
     * @param pageSize
     *           page size
     * @param currentPage
     *           current page
     * @return pageable result
     */
    public SearchPageData<CloudCPQOrderEntryProductInfoModel> getAllConfigurationProductInfos(final int pageSize,
                    final int currentPage);
}
