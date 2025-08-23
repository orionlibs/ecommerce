package de.hybris.platform.commons.jalo;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedMediaFormatter extends Formatter
{
    public static final String INPUTMIMETYPE = "inputMimeType";
    public static final String FORMATS = "formats";
    protected static String FORMAT2MEDFORREL_SRC_ORDERED = "relation.Format2MedForRel.source.ordered";
    protected static String FORMAT2MEDFORREL_TGT_ORDERED = "relation.Format2MedForRel.target.ordered";
    protected static String FORMAT2MEDFORREL_MARKMODIFIED = "relation.Format2MedForRel.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Formatter.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("inputMimeType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Format> getFormats(SessionContext ctx)
    {
        List<Format> items = getLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, "Format", null, false, false);
        return items;
    }


    public Collection<Format> getFormats()
    {
        return getFormats(getSession().getSessionContext());
    }


    public long getFormatsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, "Format", null);
    }


    public long getFormatsCount()
    {
        return getFormatsCount(getSession().getSessionContext());
    }


    public void setFormats(SessionContext ctx, Collection<Format> value)
    {
        setLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void setFormats(Collection<Format> value)
    {
        setFormats(getSession().getSessionContext(), value);
    }


    public void addToFormats(SessionContext ctx, Format value)
    {
        addLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void addToFormats(Format value)
    {
        addToFormats(getSession().getSessionContext(), value);
    }


    public void removeFromFormats(SessionContext ctx, Format value)
    {
        removeLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void removeFromFormats(Format value)
    {
        removeFromFormats(getSession().getSessionContext(), value);
    }


    public String getInputMimeType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "inputMimeType");
    }


    public String getInputMimeType()
    {
        return getInputMimeType(getSession().getSessionContext());
    }


    public void setInputMimeType(SessionContext ctx, String value)
    {
        setProperty(ctx, "inputMimeType", value);
    }


    public void setInputMimeType(String value)
    {
        setInputMimeType(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Format");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED);
        }
        return true;
    }
}
