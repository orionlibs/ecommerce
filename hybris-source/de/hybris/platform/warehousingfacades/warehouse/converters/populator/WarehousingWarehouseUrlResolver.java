/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.warehousingfacades.warehouse.converters.populator;

import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import org.springframework.beans.factory.annotation.Required;

/**
 * Resolves the URI for the given {@link WarehouseModel}, using the {@link WarehouseModel#CODE}
 */
public class WarehousingWarehouseUrlResolver extends AbstractUrlResolver<WarehouseModel>
{
    private final String CACHE_KEY = WarehousingWarehouseUrlResolver.class.getName();
    private String pattern;


    @Override
    protected String getKey(final WarehouseModel source)
    {
        return CACHE_KEY + "." + source.getPk().toString();
    }


    @Override
    protected String resolveInternal(final WarehouseModel source)
    {
        String url = getPattern();
        if(url.contains("{code}"))
        {
            url = url.replace("{code}", urlEncode(source.getCode()).replace("+", "%20"));
        }
        return url;
    }


    protected String getPattern()
    {
        return pattern;
    }


    @Required
    public void setPattern(final String pattern)
    {
        this.pattern = pattern;
    }
}
