package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.web.session.stale.StaleSessionConfig;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CachingConfig implements StaleSessionConfig
{
    private static final int DEFAULT_TTL_SECONDS = 30;
    private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();
    private final StaleSessionConfig targetConfig;
    private final AtomicReference<Instant> clearInstant = new AtomicReference<>(Instant.now());
    private int ttlSeconds = 30;


    public CachingConfig(StaleSessionConfig targetConfig)
    {
        this.targetConfig = Objects.<StaleSessionConfig>requireNonNull(targetConfig, "targetConfig cannot be null.");
    }


    public boolean isStaleSessionCheckingEnabled(String extensionName)
    {
        clearCacheIfNeeded();
        String cacheKey = "key." + extensionName;
        return ((Boolean)this.cache.computeIfAbsent(cacheKey, k -> Boolean.valueOf(this.targetConfig.isStaleSessionCheckingEnabled(extensionName)))).booleanValue();
    }


    public void setTtlSeconds(int ttlSeconds)
    {
        this.ttlSeconds = ttlSeconds;
    }


    private void clearCacheIfNeeded()
    {
        if(this.ttlSeconds <= 0)
        {
            return;
        }
        Instant previous = this.clearInstant.get();
        if(previous.plusSeconds(this.ttlSeconds).isAfter(Instant.now()))
        {
            return;
        }
        if(this.clearInstant.compareAndSet(previous, Instant.now()))
        {
            this.cache.clear();
        }
    }
}
