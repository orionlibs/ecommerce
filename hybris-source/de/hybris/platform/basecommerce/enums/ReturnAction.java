package de.hybris.platform.basecommerce.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReturnAction implements HybrisEnumValue
{
    public static final String _TYPECODE = "ReturnAction";
    public static final String SIMPLE_CLASSNAME = "ReturnAction";
    private static final ConcurrentMap<String, ReturnAction> cache = new ConcurrentHashMap<>();
    public static final ReturnAction IMMEDIATE = valueOf("IMMEDIATE");
    public static final ReturnAction HOLD = valueOf("HOLD");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ReturnAction(String code)
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
        return "ReturnAction";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ReturnAction valueOf(String code)
    {
        String key = code.toLowerCase();
        ReturnAction result = cache.get(key);
        if(result == null)
        {
            ReturnAction newValue = new ReturnAction(code);
            ReturnAction previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
