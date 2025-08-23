package de.hybris.platform.servicelayer.stats;

import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.log4j.Logger;

public class DefaultStatisticsData implements StatisticsData
{
    private static final Logger LOG = Logger.getLogger(StatisticsData.class);
    private final Map<String, NavigableMap<Long, Float>> levelOneData = new ConcurrentHashMap<>();
    private final Map<String, NavigableMap<Long, Float>> levelTwoData = new ConcurrentHashMap<>();
    private final Map<String, NavigableMap<Long, Float>> levelThreeData = new ConcurrentHashMap<>();
    private long sizeLvl1Ms;
    private long sizeLvl2Ms;
    private long sizeLvl3Ms;


    public synchronized boolean addDataCollector(String name)
    {
        if(this.levelOneData.containsKey(name))
        {
            return false;
        }
        try
        {
            this.levelOneData.put(name, new ConcurrentSkipListMap<>());
            this.levelTwoData.put(name, new ConcurrentSkipListMap<>());
            this.levelThreeData.put(name, new ConcurrentSkipListMap<>());
            return true;
        }
        catch(Exception e)
        {
            LOG.error("DataCollector adding failed. " + e.getMessage());
            return false;
        }
    }


    public synchronized boolean removeDataCollector(String name)
    {
        if(!this.levelOneData.containsKey(name) || !this.levelTwoData.containsKey(name) || !this.levelThreeData.containsKey(name))
        {
            return false;
        }
        try
        {
            this.levelOneData.remove(name);
            this.levelTwoData.remove(name);
            this.levelThreeData.remove(name);
            return true;
        }
        catch(Exception e)
        {
            LOG.error("DataCollector removing failed. " + e.getMessage());
            return false;
        }
    }


    public synchronized boolean containsDataCollector(String name)
    {
        boolean contains = (this.levelOneData.containsKey(name) && this.levelTwoData.containsKey(name) && this.levelThreeData.containsKey(name));
        return contains;
    }


    public synchronized boolean putData(String name, long timestamp, float value)
    {
        try
        {
            if(!containsDataCollector(name))
            {
                LOG.info("Putting data on DataCollector [" + name + "] failed. No such DataCollector available.");
                return false;
            }
            ((NavigableMap<Long, Float>)this.levelOneData.get(name)).put(Long.valueOf(timestamp), Float.valueOf(value));
            balanceDataLevels(name);
            return true;
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn("Putting data to DataCollector [" + name + "] failed. " + e.getMessage(), e);
            }
            else
            {
                LOG.warn("Putting data to DataCollector [" + name + "] failed. " + e.getMessage());
            }
            return false;
        }
    }


    protected void printStats(String name)
    {
        long minL1Ts = Long.MAX_VALUE;
        long maxL1Ts = Long.MIN_VALUE;
        float minL1 = Float.MAX_VALUE;
        float maxL1 = Float.MIN_VALUE;
        NavigableMap<Long, Float> l1map = this.levelOneData.get(name);
        for(Map.Entry<Long, Float> e : l1map.entrySet())
        {
            minL1Ts = Math.min(((Long)e.getKey()).longValue(), minL1Ts);
            maxL1Ts = Math.max(((Long)e.getKey()).longValue(), maxL1Ts);
            minL1 = Math.min(((Float)e.getValue()).floatValue(), minL1);
            maxL1 = Math.max(((Float)e.getValue()).floatValue(), maxL1);
        }
        System.out.println("stats: name=" + name + ",l1TsMin=" + minL1Ts + ",l1TsMax=" + maxL1Ts + ",l1Min=" + minL1 + ",l1Max=" + maxL1 + ",ticks=" + l1map
                        .size());
    }


    public synchronized void balanceDataLevels(String name)
    {
        long now = System.currentTimeMillis();
        NavigableMap<Long, Float> l1 = this.levelOneData.get(name);
        NavigableMap<Long, Float> l2 = this.levelTwoData.get(name);
        NavigableMap<Long, Float> l3 = this.levelThreeData.get(name);
        trimAndAggregate(l1, l2, now, this.sizeLvl1Ms, 10);
        trimAndAggregate(l2, l3, now, this.sizeLvl2Ms, 10);
        trimAndAggregate(l3, null, now, this.sizeLvl3Ms, 10);
    }


    protected void trimAndAggregate(NavigableMap<Long, Float> fromMap, NavigableMap<Long, Float> toMap, long now, long millisToKeep, int ticksToRemoveAtOnce)
    {
        long timeValidFrom = now - millisToKeep;
        boolean compressAndMove = (toMap != null);
        while(!fromMap.isEmpty() && ((Long)fromMap.firstKey()).longValue() < timeValidFrom)
        {
            LongSummaryStatistics timeStats = compressAndMove ? new LongSummaryStatistics() : null;
            DoubleSummaryStatistics valueStats = compressAndMove ? new DoubleSummaryStatistics() : null;
            int count = 0;
            Map.Entry<Long, Float> entry = fromMap.pollFirstEntry();
            for(; entry != null && count < ticksToRemoveAtOnce && (compressAndMove || ((Long)entry.getKey()).longValue() < timeValidFrom);
                            entry = fromMap.pollFirstEntry(), count++)
            {
                if(compressAndMove)
                {
                    timeStats.accept(((Long)entry.getKey()).longValue());
                    valueStats.accept(((Float)entry.getValue()).doubleValue());
                }
            }
            if(compressAndMove)
            {
                long timestampAvg = timeStats.getMax();
                float valueAvg = (float)valueStats.getMax();
                toMap.put(Long.valueOf(timestampAvg), Float.valueOf(valueAvg));
            }
        }
    }


    public synchronized Object[][] getTimePeriodData(String name, long start, long end, long utcOffset)
    {
        if(!containsDataCollector(name))
        {
            LOG.error("Getting data from collector [" + name + "] failed.");
            return null;
        }
        try
        {
            NavigableMap<Long, Float> combined;
            Long startKey = Long.valueOf(start);
            Long endKey = Long.valueOf(end);
            SortedMap<Long, Float> subLvl1 = ((NavigableMap<Long, Float>)this.levelOneData.get(name)).subMap(startKey, true, endKey, true);
            SortedMap<Long, Float> subLvl2 = ((NavigableMap<Long, Float>)this.levelTwoData.get(name)).subMap(startKey, true, endKey, true);
            SortedMap<Long, Float> subLvl3 = ((NavigableMap<Long, Float>)this.levelThreeData.get(name)).subMap(startKey, true, endKey, true);
            if(subLvl1.size() + subLvl2.size() + subLvl3.size() > 15000)
            {
                combined = sliceToMaxLength(subLvl1, subLvl2, subLvl3, 15000);
            }
            else
            {
                combined = new TreeMap<>();
                combined.putAll(subLvl1);
                combined.putAll(subLvl2);
                combined.putAll(subLvl3);
            }
            Object[][] result = convertMapToArray(combined);
            convertToLocalTime(utcOffset, result);
            return result;
        }
        catch(Exception e)
        {
            LOG.error("Getting Time period data failed. " + e.getMessage());
            return null;
        }
    }


    public synchronized Object[][] getAllData(String name, int maxTicks, long utcOffset)
    {
        if(!containsDataCollector(name))
        {
            return null;
        }
        try
        {
            if(getCurrentSize(name) > maxTicks)
            {
                NavigableMap<Long, Float> map = sliceToMaxLength(this.levelOneData.get(name), this.levelTwoData.get(name), this.levelThreeData
                                .get(name), maxTicks);
                map.pollLastEntry();
                map.put((Long)((NavigableMap)this.levelOneData.get(name)).lastKey(), (Float)((NavigableMap)this.levelOneData.get(name)).get(((NavigableMap)this.levelOneData.get(name)).lastKey()));
                Object[][] arrayOfObject = convertMapToArray(map);
                convertToLocalTime(utcOffset, arrayOfObject);
                return arrayOfObject;
            }
            TreeMap<Long, Float> combined = new TreeMap<>();
            combined.putAll(this.levelOneData.get(name));
            combined.putAll(this.levelTwoData.get(name));
            combined.putAll(this.levelThreeData.get(name));
            Object[][] result = convertMapToArray(combined);
            convertToLocalTime(utcOffset, result);
            return result;
        }
        catch(Exception e)
        {
            LOG.error("Getting all Data failed. " + e.getMessage());
            return null;
        }
    }


    public synchronized Object[][] getTickAsArray(String name, long period, int maxTicks, long utcOffset)
    {
        if(!containsDataCollector(name) || maxTicks <= 0 || ((NavigableMap)this.levelOneData.get(name)).size() == 0)
        {
            return null;
        }
        try
        {
            Long toKey = Long.valueOf(System.currentTimeMillis());
            Long fromKey = Long.valueOf(toKey.longValue() - period);
            SortedMap<Long, Float> subLvl1 = ((NavigableMap<Long, Float>)this.levelOneData.get(name)).subMap(fromKey, true, toKey, true);
            SortedMap<Long, Float> subLvl2 = ((NavigableMap<Long, Float>)this.levelTwoData.get(name)).subMap(fromKey, true, toKey, true);
            SortedMap<Long, Float> subLvl3 = ((NavigableMap<Long, Float>)this.levelThreeData.get(name)).subMap(fromKey, true, toKey, true);
            NavigableMap<Long, Float> sliced = sliceToMaxLength(subLvl1, subLvl2, subLvl3, maxTicks - 2);
            sliced.pollLastEntry();
            Map.Entry<Long, Float> entry = ((NavigableMap<Long, Float>)this.levelOneData.get(name)).lastEntry();
            sliced.put(entry.getKey(), entry.getValue());
            Object[][] result = convertMapToArray(sliced);
            convertToLocalTime(utcOffset, result);
            return result;
        }
        catch(Exception e)
        {
            LOG.error("Getting Ticks failed. " + e.getMessage());
            return null;
        }
    }


    private Object[][] convertMapToArray(NavigableMap<Long, Float> map)
    {
        Object[][] result = new Object[map.size()][2];
        int i = 0;
        for(Map.Entry<Long, Float> e : map.entrySet())
        {
            result[i][0] = e.getKey();
            result[i][1] = e.getValue();
            i++;
        }
        return result;
    }


    private NavigableMap<Long, Float> sliceToMaxLength(SortedMap<Long, Float> mapLvl1, SortedMap<Long, Float> mapLvl2, SortedMap<Long, Float> mapLvl3, int maxTicks)
    {
        long rangeLvl1 = (mapLvl1.size() > 1) ? (((Long)mapLvl1.lastKey()).longValue() - ((Long)mapLvl1.firstKey()).longValue()) : 0L;
        long rangeLvl2 = (mapLvl2.size() > 1) ? (((Long)mapLvl2.lastKey()).longValue() - ((Long)mapLvl2.firstKey()).longValue()) : 0L;
        long rangeLvl3 = (mapLvl3.size() > 1) ? (((Long)mapLvl3.lastKey()).longValue() - ((Long)mapLvl3.firstKey()).longValue()) : 0L;
        long avgPeriod = rangeLvl1 + rangeLvl2 + rangeLvl3;
        double rateLvl1 = rangeLvl1 / avgPeriod;
        double rateLvl2 = rangeLvl2 / avgPeriod;
        double rateLvl3 = rangeLvl3 / avgPeriod;
        int nrTicksLvl1 = (int)Math.round(maxTicks * rateLvl1);
        int nrTicksLvl2 = (int)Math.round(maxTicks * rateLvl2);
        int nrTicksLvl3 = (int)Math.round(maxTicks * rateLvl3);
        NavigableMap<Long, Float> result = new TreeMap<>();
        result.putAll(getPartOfMap(mapLvl1, nrTicksLvl1));
        result.putAll(getPartOfMap(mapLvl2, nrTicksLvl2));
        result.putAll(getPartOfMap(mapLvl3, nrTicksLvl3));
        return result;
    }


    private SortedMap<Long, Float> getPartOfMap(SortedMap<Long, Float> map, int maxTicks)
    {
        if(maxTicks < 1)
        {
            return Collections.emptySortedMap();
        }
        if(map.size() < maxTicks)
        {
            return map;
        }
        SortedMap<Long, Float> result = new TreeMap<>();
        Object[] keys = map.keySet().toArray();
        int factor = (int)Math.ceil((map.size() / maxTicks));
        int i;
        for(i = 0; i < map.size() - factor; i += factor)
        {
            long key = 0L;
            float value = 0.0F;
            for(int x = 0; x < factor; x++)
            {
                key += ((Long)keys[i + x]).longValue();
                value += ((Float)map.get(keys[i + x])).floatValue();
            }
            result.put(Long.valueOf(key / factor), Float.valueOf(value / factor));
        }
        return result;
    }


    private void convertToLocalTime(long utcOffset, Object[][] convert)
    {
        for(int i = 0; i < convert.length; i++)
        {
            convert[i][0] = Long.valueOf(((Long)convert[i][0]).longValue() - utcOffset);
        }
    }


    public synchronized int getCurrentSize(String name)
    {
        if(containsDataCollector(name))
        {
            return ((NavigableMap)this.levelOneData.get(name)).size() + ((NavigableMap)this.levelTwoData.get(name)).size() + ((NavigableMap)this.levelThreeData.get(name)).size();
        }
        return -1;
    }


    public int[] getLevelSizes(String name)
    {
        return new int[] {((NavigableMap)this.levelOneData
                        .get(name)).size(), ((NavigableMap)this.levelTwoData.get(name)).size(), ((NavigableMap)this.levelThreeData.get(name)).size()};
    }


    public void setSizeLvl1Ms(long ms)
    {
        this.sizeLvl1Ms = ms;
    }


    public void setSizeLvl2Ms(long ms)
    {
        this.sizeLvl2Ms = ms;
    }


    public void setSizeLvl3Ms(long ms)
    {
        this.sizeLvl3Ms = ms;
    }


    public synchronized float getCollectorValue(String name, long timestamp)
    {
        Long key = Long.valueOf(timestamp);
        if(((NavigableMap)this.levelOneData.get(name)).containsKey(key))
        {
            return ((Float)((NavigableMap)this.levelOneData.get(name)).get(key)).floatValue();
        }
        if(((NavigableMap)this.levelTwoData.get(name)).containsKey(key))
        {
            return ((Float)((NavigableMap)this.levelTwoData.get(name)).get(key)).floatValue();
        }
        if(((NavigableMap)this.levelThreeData.get(name)).containsKey(key))
        {
            return ((Float)((NavigableMap)this.levelThreeData.get(name)).get(key)).floatValue();
        }
        return -1.0F;
    }
}
