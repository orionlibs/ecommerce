package de.hybris.platform.cmscockpit.jalo;

import de.hybris.platform.cms2.jalo.preview.PreviewData;
import de.hybris.platform.cmscockpit.constants.GeneratedCmscockpitConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCmscockpitManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("liveEditVariant", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.cms2.jalo.preview.PreviewData", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public String getName()
    {
        return "cmscockpit";
    }


    public EnumerationValue getLiveEditVariant(SessionContext ctx, PreviewData item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedCmscockpitConstants.Attributes.PreviewData.LIVEEDITVARIANT);
    }


    public EnumerationValue getLiveEditVariant(PreviewData item)
    {
        return getLiveEditVariant(getSession().getSessionContext(), item);
    }


    public void setLiveEditVariant(SessionContext ctx, PreviewData item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedCmscockpitConstants.Attributes.PreviewData.LIVEEDITVARIANT, value);
    }


    public void setLiveEditVariant(PreviewData item, EnumerationValue value)
    {
        setLiveEditVariant(getSession().getSessionContext(), item, value);
    }
}
