package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMultiCodeCoupon extends AbstractCoupon
{
    public static final String CODEGENERATIONCONFIGURATION = "codeGenerationConfiguration";
    public static final String GENERATEDCODES = "generatedCodes";
    public static final String ALPHABET = "alphabet";
    public static final String SIGNATURE = "signature";
    public static final String COUPONCODENUMBER = "couponCodeNumber";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCoupon.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("codeGenerationConfiguration", Item.AttributeMode.INITIAL);
        tmp.put("generatedCodes", Item.AttributeMode.INITIAL);
        tmp.put("alphabet", Item.AttributeMode.INITIAL);
        tmp.put("signature", Item.AttributeMode.INITIAL);
        tmp.put("couponCodeNumber", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAlphabet(SessionContext ctx)
    {
        return (String)getProperty(ctx, "alphabet");
    }


    public String getAlphabet()
    {
        return getAlphabet(getSession().getSessionContext());
    }


    protected void setAlphabet(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'alphabet' is not changeable", 0);
        }
        setProperty(ctx, "alphabet", value);
    }


    protected void setAlphabet(String value)
    {
        setAlphabet(getSession().getSessionContext(), value);
    }


    public CodeGenerationConfiguration getCodeGenerationConfiguration(SessionContext ctx)
    {
        return (CodeGenerationConfiguration)getProperty(ctx, "codeGenerationConfiguration");
    }


    public CodeGenerationConfiguration getCodeGenerationConfiguration()
    {
        return getCodeGenerationConfiguration(getSession().getSessionContext());
    }


    public void setCodeGenerationConfiguration(SessionContext ctx, CodeGenerationConfiguration value)
    {
        setProperty(ctx, "codeGenerationConfiguration", value);
    }


    public void setCodeGenerationConfiguration(CodeGenerationConfiguration value)
    {
        setCodeGenerationConfiguration(getSession().getSessionContext(), value);
    }


    public Long getCouponCodeNumber(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "couponCodeNumber");
    }


    public Long getCouponCodeNumber()
    {
        return getCouponCodeNumber(getSession().getSessionContext());
    }


    public long getCouponCodeNumberAsPrimitive(SessionContext ctx)
    {
        Long value = getCouponCodeNumber(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getCouponCodeNumberAsPrimitive()
    {
        return getCouponCodeNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setCouponCodeNumber(SessionContext ctx, Long value)
    {
        setProperty(ctx, "couponCodeNumber", value);
    }


    public void setCouponCodeNumber(Long value)
    {
        setCouponCodeNumber(getSession().getSessionContext(), value);
    }


    public void setCouponCodeNumber(SessionContext ctx, long value)
    {
        setCouponCodeNumber(ctx, Long.valueOf(value));
    }


    public void setCouponCodeNumber(long value)
    {
        setCouponCodeNumber(getSession().getSessionContext(), value);
    }


    public Collection<Media> getGeneratedCodes(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "generatedCodes");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getGeneratedCodes()
    {
        return getGeneratedCodes(getSession().getSessionContext());
    }


    public void setGeneratedCodes(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "generatedCodes", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setGeneratedCodes(Collection<Media> value)
    {
        setGeneratedCodes(getSession().getSessionContext(), value);
    }


    public String getSignature(SessionContext ctx)
    {
        return (String)getProperty(ctx, "signature");
    }


    public String getSignature()
    {
        return getSignature(getSession().getSessionContext());
    }


    protected void setSignature(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'signature' is not changeable", 0);
        }
        setProperty(ctx, "signature", value);
    }


    protected void setSignature(String value)
    {
        setSignature(getSession().getSessionContext(), value);
    }
}
