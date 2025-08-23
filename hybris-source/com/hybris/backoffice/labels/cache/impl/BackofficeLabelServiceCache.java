/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.cache.impl;

import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;
import com.hybris.cockpitng.labels.impl.LabelServiceCache;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeLabelServiceCache implements LabelServiceCache
{
    protected static final String CACHED_TYPE_LABEL = "__BACKOFFICE_OBJECT_LABEL__";
    protected static final String CACHED_TYPE_SHORT_LABEL = "__BACKOFFICE_OBJECT_SHORT_LABEL__";
    protected static final String CACHED_TYPE_DESCRIPTION = "__BACKOFFICE_OBJECT_DESCRIPTION__";
    protected static final String CACHED_TYPE_ICON_PATH = "__BACKOFFICE_OBJECT_ICON_PATH__";
    private CacheController cacheController;
    private ObjectCacheKeyGenerator objectCacheKeyGenerator;


    @Override
    public String getObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_LABEL, getObjectCacheKeyGenerator().computeKey(object)
                        ),
                        new SupplierCacheValueLoaderImpl<>(defaultValue));
    }


    @Override
    public String getShortObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_SHORT_LABEL, getObjectCacheKeyGenerator().computeKey(object)
                        ), new SupplierCacheValueLoaderImpl<>(defaultValue));
    }


    @Override
    public String getObjectDescription(final Object object, final Supplier<String> defaultValue)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_DESCRIPTION, getObjectCacheKeyGenerator().computeKey(object)
                        ), new SupplierCacheValueLoaderImpl<>(defaultValue));
    }


    @Override
    public String getObjectIconPath(final Object object, final Supplier<String> defaultValue)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_ICON_PATH, getObjectCacheKeyGenerator().computeKey(object)
                        ), new SupplierCacheValueLoaderImpl<>(defaultValue));
    }


    protected CacheController getCacheController()
    {
        return cacheController;
    }


    @Required
    public void setCacheController(final CacheController cacheController)
    {
        this.cacheController = cacheController;
    }


    protected ObjectCacheKeyGenerator getObjectCacheKeyGenerator()
    {
        return objectCacheKeyGenerator;
    }


    @Required
    public void setObjectCacheKeyGenerator(final ObjectCacheKeyGenerator objectCacheKeyGenerator)
    {
        this.objectCacheKeyGenerator = objectCacheKeyGenerator;
    }


    private static class SupplierCacheValueLoaderImpl<V> implements CacheValueLoader<V>
    {
        private final Supplier<V> supplier;


        public SupplierCacheValueLoaderImpl(final Supplier<V> supplier)
        {
            this.supplier = supplier;
        }


        @Override
        public V load(final CacheKey key)
        {
            final V value = supplier.get();
            return value == null ? (V)"" : value;
        }
    }
}
