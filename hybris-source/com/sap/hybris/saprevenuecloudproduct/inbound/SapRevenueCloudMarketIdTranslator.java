/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.saprevenuecloudproduct.inbound;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import java.util.Optional;

/**
 * Sap Revenue Cloud market ID translator. Responsible for translating the market ID to the catalog version in the impex
 *
 * @deprecated This class is deprecated since 1811. Market to catalog mapping is now achieved in SAP Cloud Platform Integration 
 * by maintaining a separate Value Mapping
 */
@Deprecated(since = "1811", forRemoval = true)
public class SapRevenueCloudMarketIdTranslator extends AbstractValueTranslator
{
    private SapRevenueCloudProductInboudHelper sapRevenueCloudProductInboudHelper;
    private static final String SAP_REVENUE_CLOUD_PRODUCT_INBOUND_HELPER = "defaultSapRevenueCloudProductInboudHelper";


    /**
     * No export supported. Throws {@link UnsupportedOperationException}
     *
     * @param obj
     *           - imput object
     *
     * @return {@link String}
     *
     */
    @Override
    public String exportValue(final Object obj)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * Imports the market ID and converts it to catalog version
     *
     * @param marketId
     *           - market ID
     *
     * @param importItem
     *           - current import item
     *
     * @return {@link Object}
     *
     */
    @Override
    public Object importValue(final String marketId, final Item importItem)
    {
        setSapRevenueCloudProductInboudHelper(sapRevenueCloudProductInboudHelper);
        return getSapRevenueCloudProductInboudHelper().processCatalogVersionForProduct(marketId);
    }


    /**
     * @return the sapRevenueCloudProductInboudHelper
     */
    public SapRevenueCloudProductInboudHelper getSapRevenueCloudProductInboudHelper()
    {
        return sapRevenueCloudProductInboudHelper;
    }


    /**
     * @param sapRevenueCloudProductInboudHelper
     *           the sapRevenueCloudProductInboudHelper to set
     */
    public void setSapRevenueCloudProductInboudHelper(final SapRevenueCloudProductInboudHelper sapRevenueCloudProductInboudHelper)
    {
        this.sapRevenueCloudProductInboudHelper = Optional.ofNullable(sapRevenueCloudProductInboudHelper)
                        .orElseGet(() -> (SapRevenueCloudProductInboudHelper)Registry.getApplicationContext()
                                        .getBean(SAP_REVENUE_CLOUD_PRODUCT_INBOUND_HELPER));
    }
}
