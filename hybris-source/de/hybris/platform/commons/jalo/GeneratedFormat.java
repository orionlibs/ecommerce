package de.hybris.platform.commons.jalo;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedFormat extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String INITIAL = "initial";
    public static final String DOCUMENTTYPE = "documentType";
    public static final String CHAINED = "chained";
    protected static String FORMAT2MEDFORREL_SRC_ORDERED = "relation.Format2MedForRel.source.ordered";
    protected static String FORMAT2MEDFORREL_TGT_ORDERED = "relation.Format2MedForRel.target.ordered";
    protected static String FORMAT2MEDFORREL_MARKMODIFIED = "relation.Format2MedForRel.markmodified";
    public static final String VALIDFOR = "validFor";
    protected static String FORMAT2COMTYPREL_SRC_ORDERED = "relation.Format2ComTypRel.source.ordered";
    protected static String FORMAT2COMTYPREL_TGT_ORDERED = "relation.Format2ComTypRel.target.ordered";
    protected static String FORMAT2COMTYPREL_MARKMODIFIED = "relation.Format2ComTypRel.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("initial", Item.AttributeMode.INITIAL);
        tmp.put("documentType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<MediaFormatter> getChained(SessionContext ctx)
    {
        List<MediaFormatter> items = getLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, "MediaFormatter", null, false, false);
        return items;
    }


    public Collection<MediaFormatter> getChained()
    {
        return getChained(getSession().getSessionContext());
    }


    public long getChainedCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, "MediaFormatter", null);
    }


    public long getChainedCount()
    {
        return getChainedCount(getSession().getSessionContext());
    }


    public void setChained(SessionContext ctx, Collection<MediaFormatter> value)
    {
        setLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void setChained(Collection<MediaFormatter> value)
    {
        setChained(getSession().getSessionContext(), value);
    }


    public void addToChained(SessionContext ctx, MediaFormatter value)
    {
        addLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void addToChained(MediaFormatter value)
    {
        addToChained(getSession().getSessionContext(), value);
    }


    public void removeFromChained(SessionContext ctx, MediaFormatter value)
    {
        removeLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2MEDFORREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED));
    }


    public void removeFromChained(MediaFormatter value)
    {
        removeFromChained(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public EnumerationValue getDocumentType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "documentType");
    }


    public EnumerationValue getDocumentType()
    {
        return getDocumentType(getSession().getSessionContext());
    }


    public void setDocumentType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "documentType", value);
    }


    public void setDocumentType(EnumerationValue value)
    {
        setDocumentType(getSession().getSessionContext(), value);
    }


    public ItemFormatter getInitial(SessionContext ctx)
    {
        return (ItemFormatter)getProperty(ctx, "initial");
    }


    public ItemFormatter getInitial()
    {
        return getInitial(getSession().getSessionContext());
    }


    public void setInitial(SessionContext ctx, ItemFormatter value)
    {
        setProperty(ctx, "initial", value);
    }


    public void setInitial(ItemFormatter value)
    {
        setInitial(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("MediaFormatter");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(FORMAT2MEDFORREL_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("ComposedType");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedFormat.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedFormat.setName requires a session language", 0);
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


    public Collection<ComposedType> getValidFor(SessionContext ctx)
    {
        List<ComposedType> items = getLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, "ComposedType", null, false, false);
        return items;
    }


    public Collection<ComposedType> getValidFor()
    {
        return getValidFor(getSession().getSessionContext());
    }


    public long getValidForCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, "ComposedType", null);
    }


    public long getValidForCount()
    {
        return getValidForCount(getSession().getSessionContext());
    }


    public void setValidFor(SessionContext ctx, Collection<ComposedType> value)
    {
        setLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void setValidFor(Collection<ComposedType> value)
    {
        setValidFor(getSession().getSessionContext(), value);
    }


    public void addToValidFor(SessionContext ctx, ComposedType value)
    {
        addLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void addToValidFor(ComposedType value)
    {
        addToValidFor(getSession().getSessionContext(), value);
    }


    public void removeFromValidFor(SessionContext ctx, ComposedType value)
    {
        removeLinkedItems(ctx, true, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void removeFromValidFor(ComposedType value)
    {
        removeFromValidFor(getSession().getSessionContext(), value);
    }
}
