/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.hooks;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cpq.productconfig.services.ConfigurationService;
import de.hybris.platform.cpq.productconfig.services.model.CloudCPQOrderEntryProductInfoModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.order.strategies.calculation.FindPriceHook;
import de.hybris.platform.util.PriceValue;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * Responsible for reading price from configuration summary
 */
public class DefaultConfigurationFindPriceHook implements FindPriceHook
{
    private static final Logger LOG = Logger.getLogger(DefaultConfigurationFindPriceHook.class);
    private final ConfigurationService configurationService;


    /**
     * @param configurationService
     *           Configuration service
     */
    public DefaultConfigurationFindPriceHook(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    @Override
    public PriceValue findCustomBasePrice(final AbstractOrderEntryModel entry, final PriceValue defaultPrice)
    {
        return findBasePriceFromProductInfo(entry);
    }


    protected PriceValue findBasePriceFromProductInfo(final AbstractOrderEntryModel entry)
    {
        final List<CloudCPQOrderEntryProductInfoModel> relevantProductInfos = getRelevantProductInfos(entry.getProductInfos());
        Preconditions.checkState(relevantProductInfos.size() == 1, "At this point we must get one productInfo instance");
        final CloudCPQOrderEntryProductInfoModel cpqProductInfo = relevantProductInfos.get(0);
        final BigDecimal totalPrice = getConfigurationService().getConfigurationSummary(cpqProductInfo.getConfigurationId())
                        .getConfiguration().getTotalPrice();
        final AbstractOrderModel order = entry.getOrder();
        final PriceValue priceValue = new PriceValue(order.getCurrency().getIsocode(), totalPrice.doubleValue(),
                        order.getNet().booleanValue());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Created price Value: " + priceValue + " from " + totalPrice.toPlainString());
        }
        return priceValue;
    }


    @Override
    public boolean isApplicable(final AbstractOrderEntryModel cartEntry)
    {
        return getRelevantProductInfos(cartEntry.getProductInfos()).toArray().length == 1;
    }


    protected List<CloudCPQOrderEntryProductInfoModel> getRelevantProductInfos(
                    final List<AbstractOrderEntryProductInfoModel> productInfos)
    {
        if(CollectionUtils.isEmpty(productInfos))
        {
            return Collections.emptyList();
        }
        return productInfos.stream()
                        .filter(productInfo -> ConfiguratorType.CLOUDCPQCONFIGURATOR.equals(productInfo.getConfiguratorType()))
                        .filter(productInfo -> productInfo instanceof CloudCPQOrderEntryProductInfoModel)
                        .map(productInfo -> (CloudCPQOrderEntryProductInfoModel)productInfo)
                        .filter(productInfo -> productInfo.getConfigurationId() != null).collect(Collectors.toList());
    }
}
