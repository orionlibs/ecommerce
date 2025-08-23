package de.hybris.platform.regioncache.region;

public interface RegionLifecycleCallback
{
    void onAfterRemove(Object paramObject);


    void onAfterEvict(Object paramObject);
}
