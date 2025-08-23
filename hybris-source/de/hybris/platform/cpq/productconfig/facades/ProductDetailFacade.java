/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades;

import de.hybris.platform.cpq.productconfig.facades.data.ProductDetailData;
import de.hybris.platform.cpq.productconfig.facades.data.ProductThumbnailData;
import java.util.Set;

/**
 * Accessing product details for display in interactive CPQ configuration
 *
 * @deprecated Since 2108.
 */
@Deprecated(since = "2108", forRemoval = true)
public interface ProductDetailFacade
{
    /**
     * Gets CPQ relevant product details per product code
     *
     * @param productCode
     *           Commerce product code
     * @return Product details
     */
    ProductDetailData getProductDetail(String productCode);


    /**
     * Gets product thumbnail images for a set of product codes
     *
     * @param productSet
     *           Set of products we want to get the thumbnail images for
     * @return Set of thumbnail image data, containing code and thumbnail image URL
     */
    Set<ProductThumbnailData> getProductThumbnailImages(Set<String> productSet);
}
