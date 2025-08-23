package de.hybris.platform.cms2.jalo.contents;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.CMSComponentType;
import de.hybris.platform.cms2.jalo.ComponentTypeGroup;
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedContentSlotName extends GenericItem
{
    public static final String NAME = "name";
    public static final String COMPTYPEGROUP = "compTypeGroup";
    public static final String TEMPLATEPOS = "templatePOS";
    public static final String TEMPLATE = "template";
    public static final String VALIDCOMPONENTTYPES = "validComponentTypes";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_SRC_ORDERED = "relation.ValidComponentTypesForContentSlots.source.ordered";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_TGT_ORDERED = "relation.ValidComponentTypesForContentSlots.target.ordered";
    protected static String VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED = "relation.ValidComponentTypesForContentSlots.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedContentSlotName> TEMPLATEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CONTENTSLOTNAME, false, "template", "templatePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("compTypeGroup", Item.AttributeMode.INITIAL);
        tmp.put("templatePOS", Item.AttributeMode.INITIAL);
        tmp.put("template", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ComponentTypeGroup getCompTypeGroup(SessionContext ctx)
    {
        return (ComponentTypeGroup)getProperty(ctx, "compTypeGroup");
    }


    public ComponentTypeGroup getCompTypeGroup()
    {
        return getCompTypeGroup(getSession().getSessionContext());
    }


    public void setCompTypeGroup(SessionContext ctx, ComponentTypeGroup value)
    {
        setProperty(ctx, "compTypeGroup", value);
    }


    public void setCompTypeGroup(ComponentTypeGroup value)
    {
        setCompTypeGroup(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        TEMPLATEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSComponentType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public PageTemplate getTemplate(SessionContext ctx)
    {
        return (PageTemplate)getProperty(ctx, "template");
    }


    public PageTemplate getTemplate()
    {
        return getTemplate(getSession().getSessionContext());
    }


    protected void setTemplate(SessionContext ctx, PageTemplate value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'template' is not changeable", 0);
        }
        TEMPLATEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setTemplate(PageTemplate value)
    {
        setTemplate(getSession().getSessionContext(), value);
    }


    Integer getTemplatePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "templatePOS");
    }


    Integer getTemplatePOS()
    {
        return getTemplatePOS(getSession().getSessionContext());
    }


    int getTemplatePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getTemplatePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getTemplatePOSAsPrimitive()
    {
        return getTemplatePOSAsPrimitive(getSession().getSessionContext());
    }


    void setTemplatePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "templatePOS", value);
    }


    void setTemplatePOS(Integer value)
    {
        setTemplatePOS(getSession().getSessionContext(), value);
    }


    void setTemplatePOS(SessionContext ctx, int value)
    {
        setTemplatePOS(ctx, Integer.valueOf(value));
    }


    void setTemplatePOS(int value)
    {
        setTemplatePOS(getSession().getSessionContext(), value);
    }


    public Set<CMSComponentType> getValidComponentTypes(SessionContext ctx)
    {
        List<CMSComponentType> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, "CMSComponentType", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CMSComponentType> getValidComponentTypes()
    {
        return getValidComponentTypes(getSession().getSessionContext());
    }


    public long getValidComponentTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, "CMSComponentType", null);
    }


    public long getValidComponentTypesCount()
    {
        return getValidComponentTypesCount(getSession().getSessionContext());
    }


    public void setValidComponentTypes(SessionContext ctx, Set<CMSComponentType> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void setValidComponentTypes(Set<CMSComponentType> value)
    {
        setValidComponentTypes(getSession().getSessionContext(), value);
    }


    public void addToValidComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void addToValidComponentTypes(CMSComponentType value)
    {
        addToValidComponentTypes(getSession().getSessionContext(), value);
    }


    public void removeFromValidComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORCONTENTSLOTS, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORCONTENTSLOTS_MARKMODIFIED));
    }


    public void removeFromValidComponentTypes(CMSComponentType value)
    {
        removeFromValidComponentTypes(getSession().getSessionContext(), value);
    }
}
