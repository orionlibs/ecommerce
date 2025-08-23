package de.hybris.platform.tx;

import com.google.common.primitives.Longs;
import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfterSaveEventUtils
{
    public static byte[] encodeChanges(PK pk, int types)
    {
        byte[] tmp = new byte[9];
        System.arraycopy(Longs.toByteArray(pk.getLongValue()), 0, tmp, 0, 8);
        tmp[8] = (byte)(0x7 & types);
        return tmp;
    }


    public static PK decodePK(byte[] encodedChanges)
    {
        return PK.fromLong(Longs.fromByteArray(Arrays.copyOf(encodedChanges, 8)));
    }


    public static int decodeChangeTypes(byte[] encodedChanges)
    {
        return encodedChanges[8];
    }


    public static Collection<AfterSaveEvent> createEventsFromChanges(byte[][] changes)
    {
        return createEventsFromChanges(changes, true);
    }


    public static Collection<AfterSaveEvent> createEventsFromChanges(byte[][] changes, boolean removeUnnecessaryTypes)
    {
        Map<PK, Integer> pkMap = new HashMap<>();
        for(byte[] change : changes)
        {
            PK pk = decodePK(change);
            int types = decodeChangeTypes(change);
            Integer mappedTypes = pkMap.get(pk);
            if(mappedTypes == null)
            {
                mappedTypes = Integer.valueOf(types);
            }
            else
            {
                mappedTypes = Integer.valueOf(mappedTypes.intValue() | types);
            }
            pkMap.put(pk, mappedTypes);
        }
        List<AfterSaveEvent> events = new ArrayList<>(pkMap.size());
        for(Map.Entry<PK, Integer> e : pkMap.entrySet())
        {
            int allTypes = ((Integer)e.getValue()).intValue();
            int types = removeUnnecessaryTypes ? removeUnnecessaryTypes(allTypes) : allTypes;
            createAndAddEvent(e.getKey(), types, events);
        }
        return events;
    }


    private static int removeUnnecessaryTypes(int alltypes)
    {
        int ret = alltypes;
        if((alltypes & 0x2) != 0)
        {
            ret &= 0xFFFFFFFE;
        }
        else if((alltypes & 0x4) != 0)
        {
            ret &= 0xFFFFFFFE;
        }
        return ret;
    }


    private static void createAndAddEvent(PK pk, int types, List<AfterSaveEvent> toAddTo)
    {
        if((types & 0x4) != 0)
        {
            toAddTo.add(new AfterSaveEvent(pk, 4));
        }
        if((types & 0x1) != 0)
        {
            toAddTo.add(new AfterSaveEvent(pk, 1));
        }
        if((types & 0x2) != 0)
        {
            toAddTo.add(new AfterSaveEvent(pk, 2));
        }
    }
}
