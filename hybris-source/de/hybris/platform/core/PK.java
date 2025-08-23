package de.hybris.platform.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public final class PK implements Serializable, Comparable<PK>
{
    private static final long serialVersionUID = -4406083748222129006L;
    private static long maxTimeOffset = 0L;
    private static long currentTimeOffset = 0L;
    public static final String COUNTER_PK_GENERATOR_FACTORY = "counter.pk.generator.class";
    public static final PK NULL_PK = createFixedUUIDPK(0, 0L);
    public static final PK BIG_PK = createFixedUUIDPK(0, 1072224000000L);
    private static final long DATE_01_01_1995 = 788914800000L;
    private static final long DATE_01_01_2008 = 1199142000000L;
    private static final long DATA_01_01_2009 = 1230764400000L;
    private static final long DATE_OFFSET = 788914800000L;
    private static byte clusterID = -1;
    private static final int MAX_TYPECODE = 32767;
    private static byte next_millicnt = 0;
    private static long last_creationtime = System.currentTimeMillis();
    private final long longValue;


    private static PKCounterGenerator bootstrapFromSystemProperty(String prop)
    {
        PKCounterGenerator tmp = null;
        String className = System.getProperty(prop);
        ClassLoader ctxLoader = null;
        try
        {
            ctxLoader = Thread.currentThread().getContextClassLoader();
            tmp = (PKCounterGenerator)((ctxLoader == null) ? Class.forName(className) : Class.forName(className, true, ctxLoader)).newInstance();
        }
        catch(ClassNotFoundException e)
        {
            if(ctxLoader != null)
            {
                try
                {
                    tmp = (PKCounterGenerator)Class.forName(className).newInstance();
                }
                catch(Exception ex)
                {
                    throw new IllegalStateException("the counter pk generator factory '" + className + "' is illegal", e);
                }
            }
        }
        catch(Exception e)
        {
            throw new IllegalStateException("the counter pk generator factory '" + className + "' is illegal", e);
        }
        return tmp;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static PK createFixedPK(int typecode, long fixNr)
    {
        return createFixedUUIDPK(typecode, fixNr);
    }


    public static PK createFixedUUIDPK(int typecode, long fixNr)
    {
        return new PK(calcLongValue_UUID(typecode, (byte)0, (byte)0, 788914800000L + fixNr));
    }


    public static PK createFixedCounterPK(int typecode, long fixNr)
    {
        return createPK_Counter(typecode, fixNr);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static PK createPK(int typecode)
    {
        return createUUIDPK(typecode);
    }


    public static PK createUUIDPK(int typecode)
    {
        return createPK_UUID(typecode, System.currentTimeMillis());
    }


    public static PK createCounterPK(int typecode)
    {
        return createPK_Counter(typecode, CounterPKGeneratorFactoryHolder.factory.fetchNextCounter(typecode));
    }


    public static PK fromLong(long longValue)
    {
        if(longValue == 0L)
        {
            return null;
        }
        return new PK(longValue);
    }


    public static long getCurrentTimeOffsetInMillis()
    {
        return currentTimeOffset;
    }


    public static long getMaxTimeOffsetInMillis()
    {
        return maxTimeOffset;
    }


    private PK(long longValue)
    {
        if(longValue < 0L)
        {
            throw new PKException("Negative PK values are not allowed (got " + longValue + ")");
        }
        this.longValue = longValue;
    }


    public boolean isCounterBased()
    {
        return isCounterBased(is31LegacyDetectionEnabled());
    }


    public boolean isCounterBased(boolean legacyDetectionEnabled)
    {
        if((this.longValue & 0x80000000000L) == 0L)
        {
            return false;
        }
        return (!legacyDetectionEnabled || isNot31LegacyPK());
    }


    private static long LEGACY_31_TC_BIT = 281474976710656L;
    public static final String LEGACY_PK_31_DETECTION = "legacy.pk31.detection";
    private static final int TC_BITS_COUNT = 15;
    private static final long TC_MASK = 32767L;
    private static final long COUNTER_UPPER_MASK = -17592186044416L;
    private static final long COUNTER_LOWER_MASK = 8796093022207L;
    private static final long MARKER_BIT_AT_43 = 8796093022208L;


    private boolean isNot31LegacyPK()
    {
        return (this.longValue < LEGACY_31_TC_BIT);
    }


    private boolean is31LegacyDetectionEnabled()
    {
        return CounterPKGeneratorFactoryHolder.factory.check31LegacyDetection();
    }


    public int getTypeCode()
    {
        if(isCounterBased())
        {
            return (int)(this.longValue & 0x7FFFL);
        }
        return (int)((this.longValue & 0xFFFF000000000000L) >> 48L);
    }


    public long getCreationTime()
    {
        if(isCounterBased())
        {
            return getCounterInternal();
        }
        return ((this.longValue & 0xFFFFFFFFFF0L) >> 4L) + 788914800000L;
    }


    public long getCounter()
    {
        if(isCounterBased())
        {
            return getCounterInternal();
        }
        throw new PKException("this is a UUID based PK, cannot call getCounter()");
    }


    private long getCounterInternal()
    {
        long lower = this.longValue & 0x7FFFFFFFFFFL;
        long upper = this.longValue & 0xFFFFF00000000000L;
        return (upper >> 1L | lower) >> 15L;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public byte getClusterID()
    {
        if(isCounterBased())
        {
            return 0;
        }
        return (byte)(int)((this.longValue & 0xF00000000000L) >> 44L | (this.longValue & 0xCL) << 2L);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public byte getMilliCnt()
    {
        if(isCounterBased())
        {
            return 0;
        }
        return (byte)(int)(this.longValue & 0x3L);
    }


    public Long getLong()
    {
        return Long.valueOf(this.longValue);
    }


    public long getLongValue()
    {
        return this.longValue;
    }


    public String getTypeCodeAsString()
    {
        return CounterPKGeneratorFactoryHolder.factory.getTypeCodeAsString(getTypeCode());
    }


    public String getLongValueAsString()
    {
        return Long.toString(this.longValue);
    }


    public int hashCode()
    {
        return hashCode(this.longValue);
    }


    public static int hashCode(long longValue)
    {
        long key = longValue;
        key = (key ^ 0xFFFFFFFFFFFFFFFFL) + (key << 18L);
        key ^= key >>> 31L;
        key *= 21L;
        key ^= key >>> 11L;
        key += key << 6L;
        key ^= key >>> 22L;
        return (int)key;
    }


    public boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object instanceof PK)
        {
            return (((PK)object).longValue == this.longValue);
        }
        return false;
    }


    public String toString()
    {
        return getLongValueAsString();
    }


    public String getHex()
    {
        return Long.toHexString(this.longValue);
    }


    public static PK parseHex(String hexPkString)
    {
        return new PK(Long.decode("0x" + hexPkString).longValue());
    }


    public int compareTo(PK pk)
    {
        long thisVal = this.longValue;
        long otherVal = pk.longValue;
        return (thisVal < otherVal) ? -1 : ((thisVal == otherVal) ? 0 : 1);
    }


    public static PK parse(String pkString)
    {
        if(pkString == null)
        {
            return null;
        }
        try
        {
            return fromLong(Long.parseLong(pkString));
        }
        catch(NumberFormatException e)
        {
            throw new PKException("pk has wrong format: '" + pkString + "':" + e.getMessage());
        }
    }


    public static Collection<PK> parse(Collection<String> pkStrings)
    {
        if(pkStrings == null)
        {
            return null;
        }
        if(pkStrings.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<PK> ret = new ArrayList<>(pkStrings.size());
        for(Iterator<String> it = pkStrings.iterator(); it.hasNext(); )
        {
            ret.add(parse(it.next()));
        }
        return ret;
    }


    private static long calcLongValue_UUID(int typecode, byte clusterid, byte millicnt, long creationtime)
    {
        if(typecode < 0 || typecode > 32767)
        {
            throw new IllegalArgumentException("illegal typecode : " + typecode + ", allowed range: 0-32767");
        }
        long longValue = typecode << 48L & 0xFFFF000000000000L;
        longValue += (clusterid & 0xFL) << 44L & 0xF00000000000L;
        longValue += creationtime - 788914800000L << 4L & 0xFFFFFFFFFF0L;
        longValue += (clusterid >> 2) & 0xCL;
        longValue += millicnt & 0x3L;
        longValue &= 0xFFFFF7FFFFFFFFFFL;
        return longValue;
    }


    private static synchronized PK createPK_UUID(int typecode, long creationtime)
    {
        if(clusterID == -1)
        {
            clusterID = (byte)CounterPKGeneratorFactoryHolder.factory.getClusterID();
        }
        if(last_creationtime >= creationtime)
        {
            creationtime = last_creationtime;
        }
        else
        {
            next_millicnt = 0;
            last_creationtime = creationtime;
        }
        if(next_millicnt % 4 == 3)
        {
            creationtime++;
            next_millicnt = 0;
            last_creationtime = creationtime;
        }
        next_millicnt = (byte)(next_millicnt + 1);
        long calcLongValue = calcLongValue_UUID(typecode, clusterID, next_millicnt, creationtime);
        updateTimeOffsetStatistics();
        return new PK(calcLongValue);
    }


    private static PK createPK_Counter(int typecode, long counter)
    {
        if(typecode < 0 || typecode > 32767)
        {
            throw new IllegalArgumentException("illegal typecode : " + typecode + ", allowed range: 0-32767");
        }
        long counterAndTypeCode = counter << 15L | typecode;
        long upperWithoutBit43 = (counterAndTypeCode & 0xFFFFF80000000000L) << 1L;
        long lowerWithoutBit43 = counterAndTypeCode & 0x7FFFFFFFFFFL;
        long rawPKBits = upperWithoutBit43 | 0x80000000000L | lowerWithoutBit43;
        if(rawPKBits < 0L)
        {
            throw new PKException("Illegal counter " + counter + " for creating PK from - leads to negative PK value " + rawPKBits);
        }
        return new PK(rawPKBits);
    }


    private static void updateTimeOffsetStatistics()
    {
        long currentLocalOffset = last_creationtime - System.currentTimeMillis();
        if(currentLocalOffset < 0L)
        {
            currentLocalOffset = 0L;
        }
        maxTimeOffset = Math.max(maxTimeOffset, currentLocalOffset);
        currentTimeOffset = currentLocalOffset;
    }
}
