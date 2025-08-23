package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedVoucher extends Discount
{
    public static final String DESCRIPTION = "description";
    public static final String FREESHIPPING = "freeShipping";
    public static final String VALUESTRING = "valueString";
    public static final String RESTRICTIONS = "restrictions";
    public static final String INVALIDATIONS = "invalidations";
    protected static final OneToManyHandler<Restriction> RESTRICTIONSHANDLER = new OneToManyHandler(GeneratedVoucherConstants.TC.RESTRICTION, true, "voucher", "voucherPOS", true, true, 1);
    protected static final OneToManyHandler<VoucherInvalidation> INVALIDATIONSHANDLER = new OneToManyHandler(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION, true, "voucher", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Discount.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("freeShipping", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVoucher.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVoucher.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Boolean isFreeShipping(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "freeShipping");
    }


    public Boolean isFreeShipping()
    {
        return isFreeShipping(getSession().getSessionContext());
    }


    public boolean isFreeShippingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFreeShipping(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFreeShippingAsPrimitive()
    {
        return isFreeShippingAsPrimitive(getSession().getSessionContext());
    }


    public void setFreeShipping(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "freeShipping", value);
    }


    public void setFreeShipping(Boolean value)
    {
        setFreeShipping(getSession().getSessionContext(), value);
    }


    public void setFreeShipping(SessionContext ctx, boolean value)
    {
        setFreeShipping(ctx, Boolean.valueOf(value));
    }


    public void setFreeShipping(boolean value)
    {
        setFreeShipping(getSession().getSessionContext(), value);
    }


    public Collection<VoucherInvalidation> getInvalidations(SessionContext ctx)
    {
        return INVALIDATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<VoucherInvalidation> getInvalidations()
    {
        return getInvalidations(getSession().getSessionContext());
    }


    public void setInvalidations(SessionContext ctx, Collection<VoucherInvalidation> value)
    {
        INVALIDATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setInvalidations(Collection<VoucherInvalidation> value)
    {
        setInvalidations(getSession().getSessionContext(), value);
    }


    public void addToInvalidations(SessionContext ctx, VoucherInvalidation value)
    {
        INVALIDATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToInvalidations(VoucherInvalidation value)
    {
        addToInvalidations(getSession().getSessionContext(), value);
    }


    public void removeFromInvalidations(SessionContext ctx, VoucherInvalidation value)
    {
        INVALIDATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromInvalidations(VoucherInvalidation value)
    {
        removeFromInvalidations(getSession().getSessionContext(), value);
    }


    public Set<Restriction> getRestrictions(SessionContext ctx)
    {
        return (Set<Restriction>)RESTRICTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<Restriction> getRestrictions()
    {
        return getRestrictions(getSession().getSessionContext());
    }


    public void setRestrictions(SessionContext ctx, Set<Restriction> value)
    {
        RESTRICTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRestrictions(Set<Restriction> value)
    {
        setRestrictions(getSession().getSessionContext(), value);
    }


    public void addToRestrictions(SessionContext ctx, Restriction value)
    {
        RESTRICTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRestrictions(Restriction value)
    {
        addToRestrictions(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictions(SessionContext ctx, Restriction value)
    {
        RESTRICTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRestrictions(Restriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), value);
    }


    public String getValueString()
    {
        return getValueString(getSession().getSessionContext());
    }


    public abstract String getValueString(SessionContext paramSessionContext);
}
