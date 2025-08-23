package de.hybris.platform.persistence.numberseries;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.numberseries.NumberSeries;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultCachingSerialNumberGenerator implements SerialNumberGenerator
{
    private final Tenant t;
    private final SerialNumberDAO dao;
    private final ConcurrentMap<String, Lock> keyLockMap = new ConcurrentHashMap<>(100, 0.75F, 8);
    private final Map<String, CachedNumberRange> numberCacheMap = new ConcurrentHashMap<>();
    private static final long INVALID = -1L;


    public DefaultCachingSerialNumberGenerator(Tenant t, SerialNumberDAO dao)
    {
        this.t = t;
        this.dao = dao;
    }


    private Lock getLock(String key)
    {
        Lock ret = this.keyLockMap.get(key);
        if(ret == null)
        {
            ret = new ReentrantLock();
            Lock prev = this.keyLockMap.putIfAbsent(key, ret);
            if(prev != null)
            {
                ret = prev;
            }
        }
        return ret;
    }


    private <T> T runLocked(String key, Operation<T> operation, String opMessage)
    {
        Lock lock = getLock(key);
        try
        {
            lock.lockInterruptibly();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while " + opMessage, e);
        }
        try
        {
            return (T)operation.run(key);
        }
        finally
        {
            lock.unlock();
        }
    }


    public void createSeries(String key, int type, long startValue, String template)
    {
        Object object = new Object(this, type, startValue, template);
        runLocked(key, (Operation<?>)object, "creating series");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void createSeries(String key, int type, long startValue)
    {
        createSeries(key, type, startValue, null);
    }


    public NumberSeries getUniqueNumber(String key)
    {
        long number = -1L;
        CachedNumberRange range = this.numberCacheMap.get(key);
        if(range != null)
        {
            number = range.getNext();
        }
        if(isInvalid(number))
        {
            Lock lock = getLock(key);
            try
            {
                lock.lockInterruptibly();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while trying to fetch new number range", e);
            }
            try
            {
                range = this.numberCacheMap.get(key);
                if(range != null)
                {
                    number = range.getNext();
                }
                while(isInvalid(number))
                {
                    range = createAndRegisterNewCachedRange(key);
                    number = range.getNext();
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        Preconditions.checkArgument(!isInvalid(number), "number " + number + " is invalid and cannot be handed out");
        Preconditions.checkArgument((range != null), "range cannot be null");
        return new NumberSeries(key, number, range.getType(), range.getTemplate());
    }


    private CachedNumberRange createAndRegisterNewCachedRange(String key)
    {
        int count = getCacheSize(key);
        NumberSeries nr = getDao().fetchUniqueRange(key, count);
        CachedNumberRange range = new CachedNumberRange(nr.getType(), nr.getCurrentNumber(), count, nr.getTemplate());
        this.numberCacheMap.put(key, range);
        return range;
    }


    private boolean isInvalid(long next)
    {
        return (next == -1L);
    }


    public NumberSeries getInfo(String key)
    {
        Object object = new Object(this);
        return runLocked(key, (Operation<NumberSeries>)object, "getting series info");
    }


    public Collection<NumberSeries> getAllInfo()
    {
        return getDao().getAllCurrent();
    }


    public void removeSeries(String key)
    {
        Object object = new Object(this);
        runLocked(key, (Operation<?>)object, "removing series");
    }


    public void resetSeries(String key, int type, long startValue)
    {
        Object object = new Object(this, type, startValue);
        runLocked(key, (Operation<?>)object, "reseting series");
    }


    public void clearAll()
    {
        this.numberCacheMap.clear();
    }


    protected Tenant getTenant()
    {
        return this.t;
    }


    public SerialNumberDAO getDao()
    {
        return this.dao;
    }


    protected int getCacheSize(String key)
    {
        ConfigIntf config = getTenant().getConfig();
        int ret = config.getInt("numberseries.cache.size." + key, -1);
        if(ret == -1)
        {
            ret = config.getInt("numberseries.cache.size", 1000);
        }
        return Math.max(ret, 1);
    }
}
