package de.hybris.platform.jalo.media;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDerivedMedia extends AbstractMedia
{
    public static final String VERSION = "version";
    public static final String MEDIA = "media";
    protected static final BidirectionalOneToManyHandler<GeneratedDerivedMedia> MEDIAHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.DERIVEDMEDIA, false, "media", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("media", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MEDIAHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Media getMedia(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "media");
    }


    public Media getMedia()
    {
        return getMedia(getSession().getSessionContext());
    }


    public void setMedia(SessionContext ctx, Media value)
    {
        MEDIAHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setMedia(Media value)
    {
        setMedia(getSession().getSessionContext(), value);
    }


    public String getVersion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "version");
    }


    public String getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public void setVersion(SessionContext ctx, String value)
    {
        setProperty(ctx, "version", value);
    }


    public void setVersion(String value)
    {
        setVersion(getSession().getSessionContext(), value);
    }
}
