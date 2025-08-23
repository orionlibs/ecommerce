package de.hybris.platform.core.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PhoneContactInfoType implements HybrisEnumValue
{
    public static final String _TYPECODE = "PhoneContactInfoType";
    public static final String SIMPLE_CLASSNAME = "PhoneContactInfoType";
    private static final ConcurrentMap<String, PhoneContactInfoType> cache = new ConcurrentHashMap<>();
    public static final PhoneContactInfoType MOBILE = valueOf("MOBILE");
    public static final PhoneContactInfoType WORK = valueOf("WORK");
    public static final PhoneContactInfoType HOME = valueOf("HOME");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private PhoneContactInfoType(String code)
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
        return "PhoneContactInfoType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static PhoneContactInfoType valueOf(String code)
    {
        String key = code.toLowerCase();
        PhoneContactInfoType result = cache.get(key);
        if(result == null)
        {
            PhoneContactInfoType newValue = new PhoneContactInfoType(code);
            PhoneContactInfoType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
