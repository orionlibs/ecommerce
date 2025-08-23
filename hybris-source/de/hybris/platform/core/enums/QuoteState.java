package de.hybris.platform.core.enums;

import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuoteState implements HybrisEnumValue
{
    public static final String _TYPECODE = "QuoteState";
    public static final String SIMPLE_CLASSNAME = "QuoteState";
    private static final ConcurrentMap<String, QuoteState> cache = new ConcurrentHashMap<>();
    public static final QuoteState BUYER_APPROVED = valueOf("BUYER_APPROVED");
    public static final QuoteState CREATED = valueOf("CREATED");
    public static final QuoteState BUYER_DRAFT = valueOf("BUYER_DRAFT");
    public static final QuoteState DRAFT = valueOf("DRAFT");
    public static final QuoteState BUYER_SUBMITTED = valueOf("BUYER_SUBMITTED");
    public static final QuoteState SUBMITTED = valueOf("SUBMITTED");
    public static final QuoteState BUYER_OFFER = valueOf("BUYER_OFFER");
    public static final QuoteState OFFER = valueOf("OFFER");
    public static final QuoteState BUYER_ACCEPTED = valueOf("BUYER_ACCEPTED");
    public static final QuoteState ORDERED = valueOf("ORDERED");
    public static final QuoteState BUYER_REJECTED = valueOf("BUYER_REJECTED");
    public static final QuoteState CANCELLED = valueOf("CANCELLED");
    public static final QuoteState BUYER_ORDERED = valueOf("BUYER_ORDERED");
    public static final QuoteState EXPIRED = valueOf("EXPIRED");
    public static final QuoteState SELLER_REQUEST = valueOf("SELLER_REQUEST");
    public static final QuoteState SELLER_DRAFT = valueOf("SELLER_DRAFT");
    public static final QuoteState SELLER_SUBMITTED = valueOf("SELLER_SUBMITTED");
    public static final QuoteState SELLERAPPROVER_DRAFT = valueOf("SELLERAPPROVER_DRAFT");
    public static final QuoteState SELLERAPPROVER_PENDING = valueOf("SELLERAPPROVER_PENDING");
    public static final QuoteState SELLERAPPROVER_REJECTED = valueOf("SELLERAPPROVER_REJECTED");
    public static final QuoteState SELLERAPPROVER_APPROVED = valueOf("SELLERAPPROVER_APPROVED");
    private final String code;
    private final String codeLowerCase;
    private static final long serialVersionUID = 0L;


    private QuoteState(String code)
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
        return "QuoteState";
    }


    public int hashCode()
    {
        return this.codeLowerCase.hashCode();
    }


    public String toString()
    {
        return this.code.toString();
    }


    public static QuoteState valueOf(String code)
    {
        String key = code.toLowerCase();
        QuoteState result = cache.get(key);
        if(result == null)
        {
            QuoteState newValue = new QuoteState(code);
            QuoteState previous = cache.putIfAbsent(key, newValue);
            result = (previous != null) ? previous : newValue;
        }
        return result;
    }


    private Object writeReplace()
    {
        return new HybrisDynamicEnumValueSerializedForm(getClass(), getCode());
    }
}
