/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.cpq.productconfig.services.ConfigurationServiceLayerHelper;
import de.hybris.platform.cpq.productconfig.services.model.CloudCPQOrderEntryProductInfoModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.site.BaseSiteService;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.apache.log4j.Logger;

/**
 * default implementation of the {@link ConfigurationServiceLayerHelper}
 */
public class DefaultConfigurationServiceLayerHelper implements ConfigurationServiceLayerHelper
{
    private static final Logger LOG = Logger.getLogger(DefaultConfigurationServiceLayerHelper.class);
    /**
     * max pages to process
     */
    public static final int MAXIMUM_PAGES = 10000;
    /**
     * items per page
     */
    public static final int PAGE_SIZE = 100;
    private final BaseSiteService baseSiteService;


    /**
     * @param baseSiteService
     */
    public DefaultConfigurationServiceLayerHelper(final BaseSiteService baseSiteService)
    {
        super();
        this.baseSiteService = baseSiteService;
    }


    @Override
    public CloudCPQOrderEntryProductInfoModel getCPQInfo(final AbstractOrderEntryModel targetEntry)
    {
        if(null != targetEntry && null != targetEntry.getProductInfos())
        {
            final AbstractOrderEntryProductInfoModel productInfo = targetEntry.getProductInfos().stream()
                            .filter(pInfo -> ConfiguratorType.CLOUDCPQCONFIGURATOR.equals(pInfo.getConfiguratorType())).findFirst()
                            .orElse(null);
            return (CloudCPQOrderEntryProductInfoModel)productInfo;
        }
        return null;
    }


    @Override
    public void ensureBaseSiteSetAndExecuteConfigurationAction(final AbstractOrderModel order,
                    final Consumer<BaseSiteModel> action)
    {
        final BaseSiteModel previousBaseSite = baseSiteService.getCurrentBaseSite();
        boolean baseSiteChanged = false;
        if(order != null)
        {
            final BaseSiteModel baseSiteFromOrder = order.getSite();
            if(baseSiteFromOrder != null)
            {
                baseSiteService.setCurrentBaseSite(baseSiteFromOrder, false);
                baseSiteChanged = true;
            }
        }
        try
        {
            action.accept(baseSiteService.getCurrentBaseSite());
        }
        finally
        {
            if(baseSiteChanged)
            {
                baseSiteService.setCurrentBaseSite(previousBaseSite, false);
            }
        }
    }


    @Override
    public <T> void processPageWise(final IntFunction<SearchPageData<T>> searchFunction,
                    final Consumer<List<T>> searchResultConsumer)
    {
        int currentPage = 0;
        boolean hasTotalResultSizeBeenReached;
        do
        {
            hasTotalResultSizeBeenReached = processPage(searchFunction, currentPage, searchResultConsumer);
            currentPage++;
        }
        while(!hasTotalResultSizeBeenReached && currentPage < MAXIMUM_PAGES);
    }


    protected <T> boolean processPage(final IntFunction<SearchPageData<T>> searchFunction, final int currentPage,
                    final Consumer<List<T>> searchResultConsumer)
    {
        final SearchPageData<T> searchPageData = searchFunction.apply(currentPage);
        LOG.info(String.format("Processing search result page %s of %s", searchPageData.getPagination().getCurrentPage() + 1,
                        searchPageData.getPagination().getNumberOfPages()));
        searchResultConsumer.accept(searchPageData.getResults());
        return searchPageData.getResults().size() < PAGE_SIZE;
    }
}
