package de.hybris.platform.tx;

import com.google.common.base.Suppliers;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class AfterSaveEventChangesCollector
{
    private final Map<PK, Integer> afterSaveEvents;
    protected final int CREATE_DELETE_MASK = 6;
    private final Supplier<Set<String>> ignoredTypes;


    public AfterSaveEventChangesCollector()
    {
        this.afterSaveEvents = new HashMap<>();
        this.CREATE_DELETE_MASK = 6;
        this.ignoredTypes = (Supplier<Set<String>>)Suppliers.memoize(() -> {
            String string = Config.getString("core.aftersave.ignoredtypes", "");
            return (Set)Arrays.<String>stream(StringUtils.split(string, ',')).map(StringUtils::trimToEmpty).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        });
    }


    public void collect(PK pk, int type)
    {
        if(getIgnoredTypes().contains(pk.getTypeCodeAsString()))
        {
            return;
        }
        Integer existedType = this.afterSaveEvents.get(pk);
        if(existedType == null)
        {
            this.afterSaveEvents.put(pk, Integer.valueOf(type));
        }
        else
        {
            int currentType = type | existedType.intValue();
            this.afterSaveEvents.put(pk, Integer.valueOf(currentType));
        }
    }


    private Set<String> getIgnoredTypes()
    {
        return this.ignoredTypes.get();
    }


    public byte[][] getEncodedChanges()
    {
        byte[][] ret = null;
        if(MapUtils.isNotEmpty(this.afterSaveEvents))
        {
            int counter = 0;
            ret = new byte[this.afterSaveEvents.size()][9];
            for(Map.Entry<PK, Integer> _entry : this.afterSaveEvents.entrySet())
            {
                int changesMask = ((Integer)_entry.getValue()).intValue();
                if(!skipChanges(changesMask))
                {
                    ret[counter++] = AfterSaveEventUtils.encodeChanges(_entry.getKey(), mergeChanges(changesMask));
                }
            }
            if(counter == 0)
            {
                ret = null;
            }
            else if(counter < ret.length)
            {
                byte[][] finalRet = new byte[counter][9];
                System.arraycopy(ret, 0, finalRet, 0, counter);
                ret = finalRet;
            }
        }
        return ret;
    }


    protected int mergeChanges(int changesMask)
    {
        if((changesMask & 0x2) == 2)
        {
            return 2;
        }
        if((changesMask & 0x4) == 4)
        {
            return 4;
        }
        return 1;
    }


    protected boolean skipChanges(int changesMask)
    {
        return ((changesMask & 0x6) == 6);
    }


    public void clear()
    {
        this.afterSaveEvents.clear();
    }
}
