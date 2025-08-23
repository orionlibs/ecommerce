package de.hybris.platform.util;

public final class MurmurHash
{
    public static int hash32(byte[] data, int length, int seed)
    {
        int m = 1540483477;
        int r = 24;
        int h = seed ^ length;
        int length4 = length / 4;
        for(int i = 0; i < length4; i++)
        {
            int i4 = i * 4;
            int k = (data[i4 + 0] & 0xFF) + ((data[i4 + 1] & 0xFF) << 8) + ((data[i4 + 2] & 0xFF) << 16) + ((data[i4 + 3] & 0xFF) << 24);
            k *= 1540483477;
            k ^= k >>> 24;
            k *= 1540483477;
            h *= 1540483477;
            h ^= k;
        }
        switch(length % 4)
        {
            case 3:
                h ^= (data[(length & 0xFFFFFFFC) + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[(length & 0xFFFFFFFC) + 1] & 0xFF) << 8;
            case 1:
                h ^= data[length & 0xFFFFFFFC] & 0xFF;
                h *= 1540483477;
                break;
        }
        h ^= h >>> 13;
        h *= 1540483477;
        h ^= h >>> 15;
        return h;
    }


    public static int hash32(byte[] data, int length)
    {
        return hash32(data, length, -1756908916);
    }


    public static int hash32(String text)
    {
        byte[] bytes = text.getBytes();
        return hash32(bytes, bytes.length);
    }


    public static int hash32(String text, int from, int length)
    {
        return hash32(text.substring(from, from + length));
    }


    public static long hash64(byte[] data, int length, int seed)
    {
        long m = -4132994306676758123L;
        int r = 47;
        long h = seed & 0xFFFFFFFFL ^ length * -4132994306676758123L;
        int length8 = length / 8;
        for(int i = 0; i < length8; i++)
        {
            int i8 = i * 8;
            long k = (data[i8 + 0] & 0xFFL) + ((data[i8 + 1] & 0xFFL) << 8L) + ((data[i8 + 2] & 0xFFL) << 16L) + ((data[i8 + 3] & 0xFFL) << 24L) + ((data[i8 + 4] & 0xFFL) << 32L) + ((data[i8 + 5] & 0xFFL) << 40L) + ((data[i8 + 6] & 0xFFL) << 48L) + ((data[i8 + 7] & 0xFFL) << 56L);
            k *= -4132994306676758123L;
            k ^= k >>> 47L;
            k *= -4132994306676758123L;
            h ^= k;
            h *= -4132994306676758123L;
        }
        switch(length % 8)
        {
            case 7:
                h ^= (data[(length & 0xFFFFFFF8) + 6] & 0xFF) << 48L;
            case 6:
                h ^= (data[(length & 0xFFFFFFF8) + 5] & 0xFF) << 40L;
            case 5:
                h ^= (data[(length & 0xFFFFFFF8) + 4] & 0xFF) << 32L;
            case 4:
                h ^= (data[(length & 0xFFFFFFF8) + 3] & 0xFF) << 24L;
            case 3:
                h ^= (data[(length & 0xFFFFFFF8) + 2] & 0xFF) << 16L;
            case 2:
                h ^= (data[(length & 0xFFFFFFF8) + 1] & 0xFF) << 8L;
            case 1:
                h ^= (data[length & 0xFFFFFFF8] & 0xFF);
                h *= -4132994306676758123L;
                break;
        }
        h ^= h >>> 47L;
        h *= -4132994306676758123L;
        h ^= h >>> 47L;
        return h;
    }


    public static long hash64(byte[] data, int length)
    {
        return hash64(data, length, -512093083);
    }


    public static long hash64(String text)
    {
        byte[] bytes = text.getBytes();
        return hash64(bytes, bytes.length);
    }


    public static long hash64(String text, int from, int length)
    {
        return hash64(text.substring(from, from + length));
    }
}
