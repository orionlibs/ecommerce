package de.hybris.platform.cms2.jalo.navigation;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCMSNavigationNode extends CMSItem
{
    public static final String TITLE = "title";
    public static final String VISIBLE = "visible";
    public static final String PARENTPOS = "parentPOS";
    public static final String PARENT = "parent";
    public static final String CHILDREN = "children";
    public static final String LINKS = "links";
    protected static String CMSLINKSFORNAVNODES_SRC_ORDERED = "relation.CMSLinksForNavNodes.source.ordered";
    protected static String CMSLINKSFORNAVNODES_TGT_ORDERED = "relation.CMSLinksForNavNodes.target.ordered";
    protected static String CMSLINKSFORNAVNODES_MARKMODIFIED = "relation.CMSLinksForNavNodes.markmodified";
    public static final String PAGES = "pages";
    protected static String CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED = "relation.CMSContentPagesForNavNodes.source.ordered";
    protected static String CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED = "relation.CMSContentPagesForNavNodes.target.ordered";
    protected static String CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED = "relation.CMSContentPagesForNavNodes.markmodified";
    public static final String ENTRIES = "entries";
    protected static final BidirectionalOneToManyHandler<GeneratedCMSNavigationNode> PARENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSNAVIGATIONNODE, false, "parent", "parentPOS", true, true, 2);
    protected static final OneToManyHandler<CMSNavigationNode> CHILDRENHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSNAVIGATIONNODE, true, "parent", "parentPOS", true, true, 2);
    protected static final OneToManyHandler<CMSNavigationEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSNAVIGATIONENTRY, false, "navigationNode", "navigationNodePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("visible", Item.AttributeMode.INITIAL);
        tmp.put("parentPOS", Item.AttributeMode.INITIAL);
        tmp.put("parent", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CMSNavigationNode> getChildren(SessionContext ctx)
    {
        return (List<CMSNavigationNode>)CHILDRENHANDLER.getValues(ctx, (Item)this);
    }


    public List<CMSNavigationNode> getChildren()
    {
        return getChildren(getSession().getSessionContext());
    }


    public void setChildren(SessionContext ctx, List<CMSNavigationNode> value)
    {
        CHILDRENHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setChildren(List<CMSNavigationNode> value)
    {
        setChildren(getSession().getSessionContext(), value);
    }


    public void addToChildren(SessionContext ctx, CMSNavigationNode value)
    {
        CHILDRENHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToChildren(CMSNavigationNode value)
    {
        addToChildren(getSession().getSessionContext(), value);
    }


    public void removeFromChildren(SessionContext ctx, CMSNavigationNode value)
    {
        CHILDRENHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromChildren(CMSNavigationNode value)
    {
        removeFromChildren(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PARENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<CMSNavigationEntry> getEntries(SessionContext ctx)
    {
        return (List<CMSNavigationEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<CMSNavigationEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<CMSNavigationEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<CMSNavigationEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, CMSNavigationEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(CMSNavigationEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, CMSNavigationEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(CMSNavigationEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSLinkComponent");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("ContentPage");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED);
        }
        return true;
    }


    public List<CMSLinkComponent> getLinks(SessionContext ctx)
    {
        List<CMSLinkComponent> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, "CMSLinkComponent", null,
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true));
        return items;
    }


    public List<CMSLinkComponent> getLinks()
    {
        return getLinks(getSession().getSessionContext());
    }


    public long getLinksCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, "CMSLinkComponent", null);
    }


    public long getLinksCount()
    {
        return getLinksCount(getSession().getSessionContext());
    }


    public void setLinks(SessionContext ctx, List<CMSLinkComponent> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null, value,
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void setLinks(List<CMSLinkComponent> value)
    {
        setLinks(getSession().getSessionContext(), value);
    }


    public void addToLinks(SessionContext ctx, CMSLinkComponent value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void addToLinks(CMSLinkComponent value)
    {
        addToLinks(getSession().getSessionContext(), value);
    }


    public void removeFromLinks(SessionContext ctx, CMSLinkComponent value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void removeFromLinks(CMSLinkComponent value)
    {
        removeFromLinks(getSession().getSessionContext(), value);
    }


    public List<ContentPage> getPages(SessionContext ctx)
    {
        List<ContentPage> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, "ContentPage", null,
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true));
        return items;
    }


    public List<ContentPage> getPages()
    {
        return getPages(getSession().getSessionContext());
    }


    public long getPagesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, "ContentPage", null);
    }


    public long getPagesCount()
    {
        return getPagesCount(getSession().getSessionContext());
    }


    public void setPages(SessionContext ctx, List<ContentPage> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null, value,
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void setPages(List<ContentPage> value)
    {
        setPages(getSession().getSessionContext(), value);
    }


    public void addToPages(SessionContext ctx, ContentPage value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void addToPages(ContentPage value)
    {
        addToPages(getSession().getSessionContext(), value);
    }


    public void removeFromPages(SessionContext ctx, ContentPage value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void removeFromPages(ContentPage value)
    {
        removeFromPages(getSession().getSessionContext(), value);
    }


    public CMSNavigationNode getParent(SessionContext ctx)
    {
        return (CMSNavigationNode)getProperty(ctx, "parent");
    }


    public CMSNavigationNode getParent()
    {
        return getParent(getSession().getSessionContext());
    }


    public void setParent(SessionContext ctx, CMSNavigationNode value)
    {
        PARENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setParent(CMSNavigationNode value)
    {
        setParent(getSession().getSessionContext(), value);
    }


    Integer getParentPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "parentPOS");
    }


    Integer getParentPOS()
    {
        return getParentPOS(getSession().getSessionContext());
    }


    int getParentPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getParentPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getParentPOSAsPrimitive()
    {
        return getParentPOSAsPrimitive(getSession().getSessionContext());
    }


    void setParentPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "parentPOS", value);
    }


    void setParentPOS(Integer value)
    {
        setParentPOS(getSession().getSessionContext(), value);
    }


    void setParentPOS(SessionContext ctx, int value)
    {
        setParentPOS(ctx, Integer.valueOf(value));
    }


    void setParentPOS(int value)
    {
        setParentPOS(getSession().getSessionContext(), value);
    }


    public String getTitle(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSNavigationNode.getTitle requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTitle(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "title", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllTitle()
    {
        return getAllTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSNavigationNode.setTitle requires a session language", 0);
        }
        setLocalizedProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public void setAllTitle(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "title", value);
    }


    public void setAllTitle(Map<Language, String> value)
    {
        setAllTitle(getSession().getSessionContext(), value);
    }


    public Boolean isVisible(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "visible");
    }


    public Boolean isVisible()
    {
        return isVisible(getSession().getSessionContext());
    }


    public boolean isVisibleAsPrimitive(SessionContext ctx)
    {
        Boolean value = isVisible(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isVisibleAsPrimitive()
    {
        return isVisibleAsPrimitive(getSession().getSessionContext());
    }


    public void setVisible(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "visible", value);
    }


    public void setVisible(Boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }


    public void setVisible(SessionContext ctx, boolean value)
    {
        setVisible(ctx, Boolean.valueOf(value));
    }


    public void setVisible(boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }
}
