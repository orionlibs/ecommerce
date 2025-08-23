package de.hybris.platform.integrationservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ItemTypeMatchEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "ItemTypeMatchEnum";
    public static final String SIMPLE_CLASSNAME = "ItemTypeMatchEnum";
    private static final ConcurrentMap<String, ItemTypeMatchEnum> cache = new ConcurrentHashMap<>();
    public static final ItemTypeMatchEnum ALL_SUB_AND_SUPER_TYPES = valueOf("ALL_SUB_AND_SUPER_TYPES");
    public static final ItemTypeMatchEnum RESTRICT_TO_ITEM_TYPE = valueOf("RESTRICT_TO_ITEM_TYPE");
    public static final ItemTypeMatchEnum ALL_SUBTYPES = valueOf("ALL_SUBTYPES");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ItemTypeMatchEnum(String code)
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
        return "ItemTypeMatchEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ItemTypeMatchEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        ItemTypeMatchEnum result = cache.get(key);
        if(result == null)
        {
            ItemTypeMatchEnum newValue = new ItemTypeMatchEnum(code);
            ItemTypeMatchEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
