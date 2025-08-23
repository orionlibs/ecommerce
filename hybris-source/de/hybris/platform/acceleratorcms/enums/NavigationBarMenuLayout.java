package de.hybris.platform.acceleratorcms.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NavigationBarMenuLayout implements HybrisEnumValue
{
    public static final String _TYPECODE = "NavigationBarMenuLayout";
    public static final String SIMPLE_CLASSNAME = "NavigationBarMenuLayout";
    private static final ConcurrentMap<String, NavigationBarMenuLayout> cache = new ConcurrentHashMap<>();
    public static final NavigationBarMenuLayout AUTO = valueOf("AUTO");
    public static final NavigationBarMenuLayout RIGHT_EDGE = valueOf("RIGHT_EDGE");
    public static final NavigationBarMenuLayout LEFT_EDGE = valueOf("LEFT_EDGE");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private NavigationBarMenuLayout(String code)
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
        return "NavigationBarMenuLayout";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static NavigationBarMenuLayout valueOf(String code)
    {
        String key = code.toLowerCase();
        NavigationBarMenuLayout result = cache.get(key);
        if(result == null)
        {
            NavigationBarMenuLayout newValue = new NavigationBarMenuLayout(code);
            NavigationBarMenuLayout previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
