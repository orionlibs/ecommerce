package de.hybris.platform.europe1.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserTaxGroup implements HybrisEnumValue
{
    public static final String _TYPECODE = "UserTaxGroup";
    public static final String SIMPLE_CLASSNAME = "UserTaxGroup";
    private static final ConcurrentMap<String, UserTaxGroup> cache = new ConcurrentHashMap<>();
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private UserTaxGroup(String code)
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
        return "UserTaxGroup";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static UserTaxGroup valueOf(String code)
    {
        String key = code.toLowerCase();
        UserTaxGroup result = cache.get(key);
        if(result == null)
        {
            UserTaxGroup newValue = new UserTaxGroup(code);
            UserTaxGroup previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
