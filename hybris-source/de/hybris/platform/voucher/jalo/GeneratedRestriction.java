package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRestriction extends GenericItem
{
    public static final String POSITIVE = "positive";
    public static final String DESCRIPTION = "description";
    public static final String VIOLATIONMESSAGE = "violationMessage";
    public static final String RESTRICTIONTYPE = "restrictionType";
    public static final String VOUCHERPOS = "voucherPOS";
    public static final String VOUCHER = "voucher";
    protected static final BidirectionalOneToManyHandler<GeneratedRestriction> VOUCHERHANDLER = new BidirectionalOneToManyHandler(GeneratedVoucherConstants.TC.RESTRICTION, false, "voucher", "voucherPOS", true, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("positive", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("violationMessage", Item.AttributeMode.INITIAL);
        tmp.put("voucherPOS", Item.AttributeMode.INITIAL);
        tmp.put("voucher", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        VOUCHERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRestriction.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedRestriction.setDescription requires a session language", 0);
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


    public Boolean isPositive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "positive");
    }


    public Boolean isPositive()
    {
        return isPositive(getSession().getSessionContext());
    }


    public boolean isPositiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPositive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPositiveAsPrimitive()
    {
        return isPositiveAsPrimitive(getSession().getSessionContext());
    }


    public void setPositive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "positive", value);
    }


    public void setPositive(Boolean value)
    {
        setPositive(getSession().getSessionContext(), value);
    }


    public void setPositive(SessionContext ctx, boolean value)
    {
        setPositive(ctx, Boolean.valueOf(value));
    }


    public void setPositive(boolean value)
    {
        setPositive(getSession().getSessionContext(), value);
    }


    public String getRestrictionType()
    {
        return getRestrictionType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllRestrictionType()
    {
        return getAllRestrictionType(getSession().getSessionContext());
    }


    public String getViolationMessage(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRestriction.getViolationMessage requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "violationMessage");
    }


    public String getViolationMessage()
    {
        return getViolationMessage(getSession().getSessionContext());
    }


    public Map<Language, String> getAllViolationMessage(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "violationMessage", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllViolationMessage()
    {
        return getAllViolationMessage(getSession().getSessionContext());
    }


    public void setViolationMessage(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRestriction.setViolationMessage requires a session language", 0);
        }
        setLocalizedProperty(ctx, "violationMessage", value);
    }


    public void setViolationMessage(String value)
    {
        setViolationMessage(getSession().getSessionContext(), value);
    }


    public void setAllViolationMessage(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "violationMessage", value);
    }


    public void setAllViolationMessage(Map<Language, String> value)
    {
        setAllViolationMessage(getSession().getSessionContext(), value);
    }


    public Voucher getVoucher(SessionContext ctx)
    {
        return (Voucher)getProperty(ctx, "voucher");
    }


    public Voucher getVoucher()
    {
        return getVoucher(getSession().getSessionContext());
    }


    protected void setVoucher(SessionContext ctx, Voucher value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'voucher' is not changeable", 0);
        }
        VOUCHERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setVoucher(Voucher value)
    {
        setVoucher(getSession().getSessionContext(), value);
    }


    Integer getVoucherPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "voucherPOS");
    }


    Integer getVoucherPOS()
    {
        return getVoucherPOS(getSession().getSessionContext());
    }


    int getVoucherPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getVoucherPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getVoucherPOSAsPrimitive()
    {
        return getVoucherPOSAsPrimitive(getSession().getSessionContext());
    }


    void setVoucherPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "voucherPOS", value);
    }


    void setVoucherPOS(Integer value)
    {
        setVoucherPOS(getSession().getSessionContext(), value);
    }


    void setVoucherPOS(SessionContext ctx, int value)
    {
        setVoucherPOS(ctx, Integer.valueOf(value));
    }


    void setVoucherPOS(int value)
    {
        setVoucherPOS(getSession().getSessionContext(), value);
    }


    public abstract String getRestrictionType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllRestrictionType(SessionContext paramSessionContext);
}
