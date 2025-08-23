package de.hybris.platform.acceleratorservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SiteMapChangeFrequencyEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "SiteMapChangeFrequencyEnum";
    public static final String SIMPLE_CLASSNAME = "SiteMapChangeFrequencyEnum";
    private static final ConcurrentMap<String, SiteMapChangeFrequencyEnum> cache = new ConcurrentHashMap<>();
    public static final SiteMapChangeFrequencyEnum ALWAYS = valueOf("always");
    public static final SiteMapChangeFrequencyEnum HOURLY = valueOf("hourly");
    public static final SiteMapChangeFrequencyEnum DAILY = valueOf("daily");
    public static final SiteMapChangeFrequencyEnum WEEKLY = valueOf("weekly");
    public static final SiteMapChangeFrequencyEnum MONTHLY = valueOf("monthly");
    public static final SiteMapChangeFrequencyEnum YEARLY = valueOf("yearly");
    public static final SiteMapChangeFrequencyEnum NEVER = valueOf("never");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private SiteMapChangeFrequencyEnum(String code)
    {
        this.code = code.intern();
        this.codeLowerCase = this.code.toLowerCase().intern();
    }


    public boolean equals(Object obj)
    {
        try
        {
            HybrisEnumValue enum2 = (HybrisEnumValue)obj;
            return (this == enum2 || (enum2 != null &&
                            !getClass().isEnum() && !enum2.getClass().isEnum() &&
                            getType().equalsIgnoreCase(enum2.getType()) && getCode().equalsIgnoreCase(enum2.getCode())));
        }
        catch(ClassCastException e)
        {
            return false;
        }
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SiteMapChangeFrequencyEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static SiteMapChangeFrequencyEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        SiteMapChangeFrequencyEnum result = cache.get(key);
        if(result == null)
        {
            SiteMapChangeFrequencyEnum newValue = new SiteMapChangeFrequencyEnum(code);
            SiteMapChangeFrequencyEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
