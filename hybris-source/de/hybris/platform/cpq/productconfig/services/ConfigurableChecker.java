/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.core.model.product.ProductModel;
import javax.annotation.Nonnull;

/**
 * Checks if a product is configurable in the sense of CPQ
 */
public interface ConfigurableChecker
{
    /**
     * Check if the given product is Cloud CPQ configurable base product and can be configured by the Cloud CPQ
     * configurator.
     *
     * @param product
     *           The product to check
     * @return TRUE if it is a Cloud CPQ configurable base product and can be configured by the Cloud CPQ configurator ,
     *         otherwise FALSE
     */
    public boolean isCloudCPQConfigurableProduct(@Nonnull final ProductModel product);
}
