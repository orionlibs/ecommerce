package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.mediaconversion.constants.GeneratedMediaConversionConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedConversionGroup extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String SUPPORTEDFORMATS = "supportedFormats";
    protected static String CONVERSIONGROUPTOFORMATREL_SRC_ORDERED = "relation.ConversionGroupToFormatRel.source.ordered";
    protected static String CONVERSIONGROUPTOFORMATREL_TGT_ORDERED = "relation.ConversionGroupToFormatRel.target.ordered";
    protected static String CONVERSIONGROUPTOFORMATREL_MARKMODIFIED = "relation.ConversionGroupToFormatRel.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ConversionMediaFormat");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CONVERSIONGROUPTOFORMATREL_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedConversionGroup.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedConversionGroup.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Set<ConversionMediaFormat> getSupportedFormats(SessionContext ctx)
    {
        List<ConversionMediaFormat> items = getLinkedItems(ctx, true, GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL, "ConversionMediaFormat", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<ConversionMediaFormat> getSupportedFormats()
    {
        return getSupportedFormats(getSession().getSessionContext());
    }


    public long getSupportedFormatsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL, "ConversionMediaFormat", null);
    }


    public long getSupportedFormatsCount()
    {
        return getSupportedFormatsCount(getSession().getSessionContext());
    }


    public void setSupportedFormats(SessionContext ctx, Set<ConversionMediaFormat> value)
    {
        setLinkedItems(ctx, true, GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CONVERSIONGROUPTOFORMATREL_MARKMODIFIED));
    }


    public void setSupportedFormats(Set<ConversionMediaFormat> value)
    {
        setSupportedFormats(getSession().getSessionContext(), value);
    }


    public void addToSupportedFormats(SessionContext ctx, ConversionMediaFormat value)
    {
        addLinkedItems(ctx, true, GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CONVERSIONGROUPTOFORMATREL_MARKMODIFIED));
    }


    public void addToSupportedFormats(ConversionMediaFormat value)
    {
        addToSupportedFormats(getSession().getSessionContext(), value);
    }


    public void removeFromSupportedFormats(SessionContext ctx, ConversionMediaFormat value)
    {
        removeLinkedItems(ctx, true, GeneratedMediaConversionConstants.Relations.CONVERSIONGROUPTOFORMATREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CONVERSIONGROUPTOFORMATREL_MARKMODIFIED));
    }


    public void removeFromSupportedFormats(ConversionMediaFormat value)
    {
        removeFromSupportedFormats(getSession().getSessionContext(), value);
    }
}
