package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.CMSPageType;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.ContentSlotName;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedPageTemplate extends CMSItem
{
    public static final String ACTIVE = "active";
    public static final String VELOCITYTEMPLATE = "velocityTemplate";
    public static final String FRONTENDTEMPLATENAME = "frontendTemplateName";
    public static final String CONTENTSLOTS = "contentSlots";
    public static final String PREVIEWICON = "previewIcon";
    public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
    public static final String RESTRICTEDPAGETYPES = "restrictedPageTypes";
    protected static String VALIDPAGETYPESFORTEMPLATES_SRC_ORDERED = "relation.ValidPageTypesForTemplates.source.ordered";
    protected static String VALIDPAGETYPESFORTEMPLATES_TGT_ORDERED = "relation.ValidPageTypesForTemplates.target.ordered";
    protected static String VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED = "relation.ValidPageTypesForTemplates.markmodified";
    protected static final OneToManyHandler<ContentSlotName> AVAILABLECONTENTSLOTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CONTENTSLOTNAME, true, "template", "templatePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("velocityTemplate", Item.AttributeMode.INITIAL);
        tmp.put("frontendTemplateName", Item.AttributeMode.INITIAL);
        tmp.put("previewIcon", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public List<ContentSlotName> getAvailableContentSlots(SessionContext ctx)
    {
        return (List<ContentSlotName>)AVAILABLECONTENTSLOTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<ContentSlotName> getAvailableContentSlots()
    {
        return getAvailableContentSlots(getSession().getSessionContext());
    }


    public void setAvailableContentSlots(SessionContext ctx, List<ContentSlotName> value)
    {
        AVAILABLECONTENTSLOTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAvailableContentSlots(List<ContentSlotName> value)
    {
        setAvailableContentSlots(getSession().getSessionContext(), value);
    }


    public void addToAvailableContentSlots(SessionContext ctx, ContentSlotName value)
    {
        AVAILABLECONTENTSLOTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAvailableContentSlots(ContentSlotName value)
    {
        addToAvailableContentSlots(getSession().getSessionContext(), value);
    }


    public void removeFromAvailableContentSlots(SessionContext ctx, ContentSlotName value)
    {
        AVAILABLECONTENTSLOTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAvailableContentSlots(ContentSlotName value)
    {
        removeFromAvailableContentSlots(getSession().getSessionContext(), value);
    }


    public List<ContentSlotForTemplate> getContentSlots()
    {
        return getContentSlots(getSession().getSessionContext());
    }


    public String getFrontendTemplateName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "frontendTemplateName");
    }


    public String getFrontendTemplateName()
    {
        return getFrontendTemplateName(getSession().getSessionContext());
    }


    public void setFrontendTemplateName(SessionContext ctx, String value)
    {
        setProperty(ctx, "frontendTemplateName", value);
    }


    public void setFrontendTemplateName(String value)
    {
        setFrontendTemplateName(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSPageType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED);
        }
        return true;
    }


    public Media getPreviewIcon(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "previewIcon");
    }


    public Media getPreviewIcon()
    {
        return getPreviewIcon(getSession().getSessionContext());
    }


    public void setPreviewIcon(SessionContext ctx, Media value)
    {
        setProperty(ctx, "previewIcon", value);
    }


    public void setPreviewIcon(Media value)
    {
        setPreviewIcon(getSession().getSessionContext(), value);
    }


    public Set<CMSPageType> getRestrictedPageTypes(SessionContext ctx)
    {
        List<CMSPageType> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, "CMSPageType", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CMSPageType> getRestrictedPageTypes()
    {
        return getRestrictedPageTypes(getSession().getSessionContext());
    }


    public long getRestrictedPageTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, "CMSPageType", null);
    }


    public long getRestrictedPageTypesCount()
    {
        return getRestrictedPageTypesCount(getSession().getSessionContext());
    }


    public void setRestrictedPageTypes(SessionContext ctx, Set<CMSPageType> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void setRestrictedPageTypes(Set<CMSPageType> value)
    {
        setRestrictedPageTypes(getSession().getSessionContext(), value);
    }


    public void addToRestrictedPageTypes(SessionContext ctx, CMSPageType value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void addToRestrictedPageTypes(CMSPageType value)
    {
        addToRestrictedPageTypes(getSession().getSessionContext(), value);
    }


    public void removeFromRestrictedPageTypes(SessionContext ctx, CMSPageType value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDPAGETYPESFORTEMPLATES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDPAGETYPESFORTEMPLATES_MARKMODIFIED));
    }


    public void removeFromRestrictedPageTypes(CMSPageType value)
    {
        removeFromRestrictedPageTypes(getSession().getSessionContext(), value);
    }


    public String getVelocityTemplate(SessionContext ctx)
    {
        return (String)getProperty(ctx, "velocityTemplate");
    }


    public String getVelocityTemplate()
    {
        return getVelocityTemplate(getSession().getSessionContext());
    }


    public void setVelocityTemplate(SessionContext ctx, String value)
    {
        setProperty(ctx, "velocityTemplate", value);
    }


    public void setVelocityTemplate(String value)
    {
        setVelocityTemplate(getSession().getSessionContext(), value);
    }


    public abstract List<ContentSlotForTemplate> getContentSlots(SessionContext paramSessionContext);
}
