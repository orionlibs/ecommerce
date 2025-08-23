package de.hybris.platform.b2b.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class B2BGroupEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "B2BGroupEnum";
    public static final String SIMPLE_CLASSNAME = "B2BGroupEnum";
    private static final ConcurrentMap<String, B2BGroupEnum> cache = new ConcurrentHashMap<>();
    public static final B2BGroupEnum B2BADMINGROUP = valueOf("b2badmingroup");
    public static final B2BGroupEnum B2BCUSTOMERGROUP = valueOf("b2bcustomergroup");
    public static final B2BGroupEnum B2BAPPROVERGROUP = valueOf("b2bapprovergroup");
    public static final B2BGroupEnum B2BMANAGERGROUP = valueOf("b2bmanagergroup");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private B2BGroupEnum(String code)
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
        return "B2BGroupEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static B2BGroupEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        B2BGroupEnum result = cache.get(key);
        if(result == null)
        {
            B2BGroupEnum newValue = new B2BGroupEnum(code);
            B2BGroupEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
