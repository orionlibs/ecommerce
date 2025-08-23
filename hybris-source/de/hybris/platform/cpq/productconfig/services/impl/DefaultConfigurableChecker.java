/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.impl;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cpq.productconfig.services.ConfigurableChecker;
import de.hybris.platform.product.ConfiguratorSettingsService;

/**
 * Support class to check if a specific product is configurable with the Cloud CPQ configurator.
 */
public class DefaultConfigurableChecker implements ConfigurableChecker
{
    private final ConfiguratorSettingsService configuratorSettingsService;


    /**
     * Default constructor accepting dependencies
     *
     * @param configuratorSettingsService
     *           configuratorSettingsService
     */
    public DefaultConfigurableChecker(final ConfiguratorSettingsService configuratorSettingsService)
    {
        this.configuratorSettingsService = configuratorSettingsService;
    }


    @Override
    public boolean isCloudCPQConfigurableProduct(final ProductModel product)
    {
        return getConfiguratorSettingsService().getConfiguratorSettingsForProduct(product).stream()
                        .anyMatch(model -> model.getConfiguratorType() == ConfiguratorType.CLOUDCPQCONFIGURATOR);
    }


    protected ConfiguratorSettingsService getConfiguratorSettingsService()
    {
        return configuratorSettingsService;
    }
}
