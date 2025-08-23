/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.saprevenuecloudproduct.inbound;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import java.util.Optional;

/**
 * Translates the combination of {@code ProductModel}' code and marketId to the {@code] ProductModel}
 *
 * @deprecated This class is deprecated since 1811. This is achieved in SAP Cloud Platform Integration by maintaining a
 * separate Value Mapping
 *
 */
@Deprecated(since = "1811", forRemoval = true)
public class SapRevenueCloudProductCodeTranslator extends AbstractValueTranslator
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
    public String exportValue(final Object arg0)
    {
        throw new UnsupportedOperationException();
    }


    /**
     * Imports the product code and market ID combined with ':' and converts it to product
     *
     * @param prodCodeMarkrtId
     *           - product code and market ID combined with ':'
     *
     * @param importItem
     *           - current import item
     *
     * @return {@link Object}
     *
     */
    @Override
    public Object importValue(final String prodCodeMarkrtId, final Item importItem)
    {
        setSapRevenueCloudProductInboudHelper(sapRevenueCloudProductInboudHelper);
        return getSapRevenueCloudProductInboudHelper().processProductForCodeAndMarketId(prodCodeMarkrtId);
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
