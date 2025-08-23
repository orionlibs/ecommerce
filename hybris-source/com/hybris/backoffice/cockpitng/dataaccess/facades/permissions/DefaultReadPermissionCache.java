/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions;

import com.google.common.collect.Lists;
import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReadPermissionCache implements ReadPermissionCache
{
    protected static final String CACHED_TYPE_TYPE_READ_PERMISSION = "__BACKOFFICE_TYPE_READ_PERMISSION__";
    protected static final String CACHED_TYPE_TYPE_ATTRIBUTES_READ_PERMISSION = "__BACKOFFICE_TYPE_ATTRIBUTES_READ_PERMISSION__";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReadPermissionCache.class);
    private static final String COULD_NOT_FIND_ATTRIBUTE_EXCEPTION_MESSAGE = "Could not find attribute descriptor for given: %s.%s";
    private final PermissionCRUDService permissionCRUDService;
    private CacheController cacheController;
    private ObjectCacheKeyGenerator objectCacheKeyGenerator;


    public DefaultReadPermissionCache(final PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    @Override
    public boolean canReadType(final String typeCode)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_TYPE_READ_PERMISSION,
                                        getObjectCacheKeyGenerator().computeKey(typeCode)
                        ), (CacheKey key) -> {
                            try
                            {
                                return getPermissionCRUDService().canReadType(typeCode);
                            }
                            catch(final UnknownIdentifierException uie)
                            {
                                LOG.warn(uie.getMessage(), uie);
                            }
                            return false;
                        });
    }


    @Override
    public boolean canReadAttribute(final String typeCode, final String attribute)
    {
        return getCacheController().getWithLoader(
                        getObjectCacheKeyGenerator().createCacheKey(
                                        CACHED_TYPE_TYPE_ATTRIBUTES_READ_PERMISSION,
                                        getObjectCacheKeyGenerator().computeKey(Lists.newArrayList(typeCode, attribute))
                        ), (CacheKey key) -> {
                            try
                            {
                                return getPermissionCRUDService().canReadAttribute(typeCode, attribute);
                            }
                            catch(final UnknownIdentifierException uie)
                            {
                                LOG.debug(String.format(COULD_NOT_FIND_ATTRIBUTE_EXCEPTION_MESSAGE, typeCode, attribute));
                                return true;
                            }
                        });
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return permissionCRUDService;
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
}
