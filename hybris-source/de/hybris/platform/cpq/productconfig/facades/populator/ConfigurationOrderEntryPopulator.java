/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades.populator;

import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.cpq.productconfig.services.ConfigurationService;
import de.hybris.platform.cpq.productconfig.services.ConfigurationServiceLayerHelper;
import de.hybris.platform.cpq.productconfig.services.model.CloudCPQOrderEntryProductInfoModel;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;

/**
 * Populator to handle CPQ specific aspects of order entries.
 *
 * @param <T>
 */
public class ConfigurationOrderEntryPopulator<T extends AbstractOrderEntryModel> implements Populator<T, OrderEntryData>
{
    private final ConfigurationServiceLayerHelper serviceLayerHelper;
    private final ConfigurationService configService;


    /**
     * @param servieLayerHelper
     * @param configService
     */
    public ConfigurationOrderEntryPopulator(final ConfigurationServiceLayerHelper servieLayerHelper,
                    final ConfigurationService configService)
    {
        super();
        this.serviceLayerHelper = servieLayerHelper;
        this.configService = configService;
    }


    @Override
    public void populate(final T source, final OrderEntryData target)
    {
        final CloudCPQOrderEntryProductInfoModel cpqInfo = serviceLayerHelper.getCPQInfo(source);
        if(null != cpqInfo)
        {
            final String configId = cpqInfo.getConfigurationId();
            if(StringUtils.isNotEmpty(configId))
            {
                final int numberOfIssues = configService.getNumberOfConfigurationIssues(configId);
                final ProductInfoStatus status = numberOfIssues > 0 ? ProductInfoStatus.ERROR : ProductInfoStatus.SUCCESS;
                target.setStatusSummaryMap(Collections.singletonMap(status, numberOfIssues));
            }
            else
            {
                target.setStatusSummaryMap(Collections.singletonMap(ProductInfoStatus.ERROR, 1));
            }
        }
    }
}
