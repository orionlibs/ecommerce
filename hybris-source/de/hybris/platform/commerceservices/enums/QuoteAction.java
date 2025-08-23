package de.hybris.platform.commerceservices.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuoteAction implements HybrisEnumValue
{
    public static final String _TYPECODE = "QuoteAction";
    public static final String SIMPLE_CLASSNAME = "QuoteAction";
    private static final ConcurrentMap<String, QuoteAction> cache = new ConcurrentHashMap<>();
    public static final QuoteAction CREATE = valueOf("CREATE");
    public static final QuoteAction VIEW = valueOf("VIEW");
    public static final QuoteAction SUBMIT = valueOf("SUBMIT");
    public static final QuoteAction SAVE = valueOf("SAVE");
    public static final QuoteAction EDIT = valueOf("EDIT");
    public static final QuoteAction DISCOUNT = valueOf("DISCOUNT");
    public static final QuoteAction CANCEL = valueOf("CANCEL");
    public static final QuoteAction CHECKOUT = valueOf("CHECKOUT");
    public static final QuoteAction ORDER = valueOf("ORDER");
    public static final QuoteAction APPROVE = valueOf("APPROVE");
    public static final QuoteAction REJECT = valueOf("REJECT");
    public static final QuoteAction EXPIRED = valueOf("EXPIRED");
    public static final QuoteAction REQUOTE = valueOf("REQUOTE");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private QuoteAction(String code)
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
        return "QuoteAction";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static QuoteAction valueOf(String code)
    {
        String key = code.toLowerCase();
        QuoteAction result = cache.get(key);
        if(result == null)
        {
            QuoteAction newValue = new QuoteAction(code);
            QuoteAction previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
