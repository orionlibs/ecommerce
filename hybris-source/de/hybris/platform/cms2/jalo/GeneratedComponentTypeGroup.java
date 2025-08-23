package de.hybris.platform.cms2.jalo;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedComponentTypeGroup extends GenericItem
{
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String CMSCOMPONENTTYPES = "cmsComponentTypes";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_SRC_ORDERED = "relation.ComponentTypeGroups2ComponentType.source.ordered";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_TGT_ORDERED = "relation.ComponentTypeGroups2ComponentType.target.ordered";
    protected static String COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED = "relation.ComponentTypeGroups2ComponentType.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<CMSComponentType> getCmsComponentTypes(SessionContext ctx)
    {
        List<CMSComponentType> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, "CMSComponentType", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CMSComponentType> getCmsComponentTypes()
    {
        return getCmsComponentTypes(getSession().getSessionContext());
    }


    public long getCmsComponentTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, "CMSComponentType", null);
    }


    public long getCmsComponentTypesCount()
    {
        return getCmsComponentTypesCount(getSession().getSessionContext());
    }


    public void setCmsComponentTypes(SessionContext ctx, Set<CMSComponentType> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void setCmsComponentTypes(Set<CMSComponentType> value)
    {
        setCmsComponentTypes(getSession().getSessionContext(), value);
    }


    public void addToCmsComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void addToCmsComponentTypes(CMSComponentType value)
    {
        addToCmsComponentTypes(getSession().getSessionContext(), value);
    }


    public void removeFromCmsComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.COMPONENTTYPEGROUPS2COMPONENTTYPE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED));
    }


    public void removeFromCmsComponentTypes(CMSComponentType value)
    {
        removeFromCmsComponentTypes(getSession().getSessionContext(), value);
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


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedComponentTypeGroup.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedComponentTypeGroup.setDescription requires a session language", 0);
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


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSComponentType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(COMPONENTTYPEGROUPS2COMPONENTTYPE_MARKMODIFIED);
        }
        return true;
    }
}
