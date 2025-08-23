package de.hybris.platform.ruleengine.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RuleType implements HybrisEnumValue
{
    public static final String _TYPECODE = "RuleType";
    public static final String SIMPLE_CLASSNAME = "RuleType";
    private static final ConcurrentMap<String, RuleType> cache = new ConcurrentHashMap<>();
    public static final RuleType DEFAULT = valueOf("DEFAULT");
    public static final RuleType PROMOTION = valueOf("PROMOTION");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private RuleType(String code)
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
        return "RuleType";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static RuleType valueOf(String code)
    {
        String key = code.toLowerCase();
        RuleType result = cache.get(key);
        if(result == null)
        {
            RuleType newValue = new RuleType(code);
            RuleType previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
