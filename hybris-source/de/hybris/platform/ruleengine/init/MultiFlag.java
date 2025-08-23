package de.hybris.platform.ruleengine.init;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiFlag
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiFlag.class);
    private final Map<String, AtomicBoolean> keyToFlagMap;


    public MultiFlag(ConcurrentMapFactory concurrentMapFactory)
    {
        this.keyToFlagMap = concurrentMapFactory.createNew();
    }


    public boolean compareAndSet(String key, boolean expected, boolean update)
    {
        AtomicBoolean flagForKey = this.keyToFlagMap.computeIfAbsent(key, k -> new AtomicBoolean(false));
        boolean result = flagForKey.compareAndSet(expected, update);
        LOGGER.debug("MultiFlag.compareAndSet called with:  module: {}, expected:{}, update:{}, result:{}", new Object[] {key, Boolean.valueOf(expected), Boolean.valueOf(update),
                        Boolean.valueOf(result)});
        return result;
    }
}
