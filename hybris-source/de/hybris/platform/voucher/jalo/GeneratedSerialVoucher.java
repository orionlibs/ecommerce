package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSerialVoucher extends Voucher
{
    public static final String CODES = "codes";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Voucher.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("codes", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Media> getCodes(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "codes");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getCodes()
    {
        return getCodes(getSession().getSessionContext());
    }


    public void setCodes(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "codes", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setCodes(Collection<Media> value)
    {
        setCodes(getSession().getSessionContext(), value);
    }
}
