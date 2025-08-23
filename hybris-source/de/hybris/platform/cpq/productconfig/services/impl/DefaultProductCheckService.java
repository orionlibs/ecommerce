/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cpq.productconfig.services.ProductCheckService;
import de.hybris.platform.product.daos.ProductDao;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

/**
 * Default implementation of {@link ProductCheckService}
 *
 * @deprecated Since 2108.
 */
@Deprecated(since = "2108", forRemoval = true)
public class DefaultProductCheckService implements ProductCheckService
{
    private static final Logger LOG = Logger.getLogger(DefaultProductCheckService.class);
    private final ProductDao productDao;
    private final CatalogVersionService catalogVersionService;


    /**
     * Constructor, containing mandatory beans
     *
     * @param productDao
     *           Used for doing product queries
     * @param catalogVersionService
     *           For checking on currently active versions
     */
    public DefaultProductCheckService(final ProductDao productDao, final CatalogVersionService catalogVersionService)
    {
        this.productDao = productDao;
        this.catalogVersionService = catalogVersionService;
    }


    protected ProductDao getProductDao()
    {
        return productDao;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }


    @Override
    public Set<String> checkProductIds(final Set<String> productIds)
    {
        Preconditions.checkArgument(productIds != null, "We expect products to check");
        return productIds.stream().map(productId -> checkProductId(productId)).filter(productId -> productId != null)
                        .collect(Collectors.toSet());
    }


    protected String checkProductId(final String productId)
    {
        final Optional<ProductModel> productFound = getCurrentCatalogVersions().stream()
                        .map(version -> getProductDao().findProductsByCode(version, productId)).flatMap(productList -> productList.stream())
                        .findFirst();
        return (productFound.isPresent()) ? productId : null;
    }


    protected List<CatalogVersionModel> getCurrentCatalogVersions()
    {
        final List<CatalogVersionModel> versionList = getCatalogVersionService().getSessionCatalogVersions().stream()
                        .filter(version -> isProductCatalogVersionActive(version)).collect(Collectors.toList());
        Preconditions.checkState(!versionList.isEmpty(), "We expect at least one active catalog version");
        return versionList;
    }


    protected boolean isProductCatalogVersionActive(final CatalogVersionModel currentCatalogVersion)
    {
        final Boolean active = currentCatalogVersion.getActive();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format(
                            "When searching for active catalog versions, we found version %s for catalog %s in session which is active: %s",
                            currentCatalogVersion.getVersion(), currentCatalogVersion.getCatalog().getId(), active.booleanValue()));
        }
        return (active.booleanValue()) && !(currentCatalogVersion.getCatalog() instanceof ContentCatalogModel)
                        && !(currentCatalogVersion.getCatalog() instanceof ClassificationSystemModel);
    }
}
