/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.logic.impl;

import com.hybris.cockpitng.common.logic.AssetsCalculator;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer.CountingRenderer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationException;

public class ObjectAssetsCalculator implements AssetsCalculator
{
    private static final Logger LOG = LoggerFactory.getLogger(CountingRenderer.class);
    private ObjectValueService objectValueService;


    @Override
    public int calculateAssets(final List<String> assetsGroups, final Object object)
    {
        Objects.requireNonNull(assetsGroups, "assetsGroups == null");
        Objects.requireNonNull(object, "object == null");
        final ToIntFunction<String> assetsAmountMapper = group -> calculateAssetsInGroup(group, object);
        return assetsGroups.stream().map(String::trim).mapToInt(assetsAmountMapper).sum();
    }


    protected int calculateAssetsInGroup(final String assetsGroup, final Object object)
    {
        final Optional<Object> value = tryToGetObjectValue(assetsGroup, object);
        final Function<Object, Integer> sizeMapper = o -> Integer.valueOf((o instanceof Collection) ? ((Collection)o).size() : 1);
        return value.map(sizeMapper).orElse(Integer.valueOf(0)).intValue();
    }


    protected Optional<Object> tryToGetObjectValue(final String assetsGroup, final Object object)
    {
        try
        {
            return Optional.ofNullable(getObjectValueService().getValue(assetsGroup, object));
        }
        catch(final EvaluationException e)
        {
            LOG.error("Could not evaluate SpEL expression while getting value from provided object.", e);
            return Optional.empty();
        }
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }
}
