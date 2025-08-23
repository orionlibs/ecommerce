package de.hybris.platform.regioncache.key.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import javax.annotation.Resource;

public class GenerationalCacheDelegate
{
    private GenerationalCounterService<String> counterService;


    @Resource(name = "generationalCounterService")
    public void setCounterService(GenerationalCounterService<String> counterService)
    {
        this.counterService = counterService;
    }


    public CacheKey getGenerationalCacheKey(CacheKey key)
    {
        if(key instanceof RegistrableCacheKey)
        {
            RegistrableCacheKey registrableCacheKey = (RegistrableCacheKey)key;
            Object[] dependentTypeCodes = registrableCacheKey.getDependentTypes();
            Preconditions.checkArgument((dependentTypeCodes != null && dependentTypeCodes.length > 0));
            String[] dependentTypeCodeStrings = new String[dependentTypeCodes.length];
            for(int i = 0; i < dependentTypeCodes.length; i++)
            {
                dependentTypeCodeStrings[i] = dependentTypeCodes[i].toString();
            }
            registrableCacheKey.setDependentTypeGenerations(this.counterService.getGenerations((Object[])dependentTypeCodeStrings, key
                            .getTenantId()));
        }
        return key;
    }


    public void incrementGeneration(CacheKey key)
    {
        String typeCode = getNonRegistrableTypeCode(key);
        if(typeCode != null)
        {
            this.counterService.incrementGeneration(typeCode, key.getTenantId());
        }
    }


    private String getNonRegistrableTypeCode(CacheKey key)
    {
        if(!(key instanceof RegistrableCacheKey))
        {
            return key.getTypeCode().toString();
        }
        return null;
    }
}
