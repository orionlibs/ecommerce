package de.hybris.platform.impex.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ScriptModifierEnum implements HybrisEnumValue
{
    public static final String _TYPECODE = "ScriptModifierEnum";
    public static final String SIMPLE_CLASSNAME = "ScriptModifierEnum";
    private static final ConcurrentMap<String, ScriptModifierEnum> cache = new ConcurrentHashMap<>();
    public static final ScriptModifierEnum DE_HYBRIS_PLATFORM_IMPEX_JALO_EXP_GENERATOR_MIGRATIONSCRIPTMODIFIER = valueOf("de_hybris_platform_impex_jalo_exp_generator_MigrationScriptModifier");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private ScriptModifierEnum(String code)
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
        return "ScriptModifierEnum";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static ScriptModifierEnum valueOf(String code)
    {
        String key = code.toLowerCase();
        ScriptModifierEnum result = cache.get(key);
        if(result == null)
        {
            ScriptModifierEnum newValue = new ScriptModifierEnum(code);
            ScriptModifierEnum previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
