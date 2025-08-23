package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.cms2.jalo.contents.components.VideoComponent;
import de.hybris.platform.cms2.jalo.navigation.CMSNavigationNode;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedContentPage extends AbstractPage
{
    public static final String LABEL = "label";
    public static final String HOMEPAGE = "homepage";
    public static final String LABELORID = "labelOrId";
    public static final String NAVIGATIONNODES = "navigationNodes";
    protected static String CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED = "relation.CMSContentPagesForNavNodes.source.ordered";
    protected static String CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED = "relation.CMSContentPagesForNavNodes.target.ordered";
    protected static String CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED = "relation.CMSContentPagesForNavNodes.markmodified";
    public static final String LINKCOMPONENTS = "linkComponents";
    public static final String VIDEOCOMPONENTS = "videoComponents";
    protected static final OneToManyHandler<CMSLinkComponent> LINKCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "contentPage", "contentPagePOS", true, true, 2);
    protected static final OneToManyHandler<VideoComponent> VIDEOCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "contentPage", "contentPagePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPage.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("label", Item.AttributeMode.INITIAL);
        tmp.put("homepage", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isHomepage(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "homepage");
    }


    public Boolean isHomepage()
    {
        return isHomepage(getSession().getSessionContext());
    }


    public boolean isHomepageAsPrimitive(SessionContext ctx)
    {
        Boolean value = isHomepage(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isHomepageAsPrimitive()
    {
        return isHomepageAsPrimitive(getSession().getSessionContext());
    }


    public void setHomepage(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "homepage", value);
    }


    public void setHomepage(Boolean value)
    {
        setHomepage(getSession().getSessionContext(), value);
    }


    public void setHomepage(SessionContext ctx, boolean value)
    {
        setHomepage(ctx, Boolean.valueOf(value));
    }


    public void setHomepage(boolean value)
    {
        setHomepage(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSNavigationNode");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED);
        }
        return true;
    }


    public String getLabel(SessionContext ctx)
    {
        return (String)getProperty(ctx, "label");
    }


    public String getLabel()
    {
        return getLabel(getSession().getSessionContext());
    }


    public void setLabel(SessionContext ctx, String value)
    {
        setProperty(ctx, "label", value);
    }


    public void setLabel(String value)
    {
        setLabel(getSession().getSessionContext(), value);
    }


    public String getLabelOrId()
    {
        return getLabelOrId(getSession().getSessionContext());
    }


    public List<CMSLinkComponent> getLinkComponents(SessionContext ctx)
    {
        return (List<CMSLinkComponent>)LINKCOMPONENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CMSLinkComponent> getLinkComponents()
    {
        return getLinkComponents(getSession().getSessionContext());
    }


    public void setLinkComponents(SessionContext ctx, List<CMSLinkComponent> value)
    {
        LINKCOMPONENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setLinkComponents(List<CMSLinkComponent> value)
    {
        setLinkComponents(getSession().getSessionContext(), value);
    }


    public void addToLinkComponents(SessionContext ctx, CMSLinkComponent value)
    {
        LINKCOMPONENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToLinkComponents(CMSLinkComponent value)
    {
        addToLinkComponents(getSession().getSessionContext(), value);
    }


    public void removeFromLinkComponents(SessionContext ctx, CMSLinkComponent value)
    {
        LINKCOMPONENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromLinkComponents(CMSLinkComponent value)
    {
        removeFromLinkComponents(getSession().getSessionContext(), value);
    }


    public List<CMSNavigationNode> getNavigationNodes(SessionContext ctx)
    {
        List<CMSNavigationNode> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, "CMSNavigationNode", null,
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true));
        return items;
    }


    public List<CMSNavigationNode> getNavigationNodes()
    {
        return getNavigationNodes(getSession().getSessionContext());
    }


    public long getNavigationNodesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, "CMSNavigationNode", null);
    }


    public long getNavigationNodesCount()
    {
        return getNavigationNodesCount(getSession().getSessionContext());
    }


    public void setNavigationNodes(SessionContext ctx, List<CMSNavigationNode> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null, value,
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void setNavigationNodes(List<CMSNavigationNode> value)
    {
        setNavigationNodes(getSession().getSessionContext(), value);
    }


    public void addToNavigationNodes(SessionContext ctx, CMSNavigationNode value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void addToNavigationNodes(CMSNavigationNode value)
    {
        addToNavigationNodes(getSession().getSessionContext(), value);
    }


    public void removeFromNavigationNodes(SessionContext ctx, CMSNavigationNode value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSCONTENTPAGESFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSCONTENTPAGESFORNAVNODES_MARKMODIFIED));
    }


    public void removeFromNavigationNodes(CMSNavigationNode value)
    {
        removeFromNavigationNodes(getSession().getSessionContext(), value);
    }


    public List<VideoComponent> getVideoComponents(SessionContext ctx)
    {
        return (List<VideoComponent>)VIDEOCOMPONENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<VideoComponent> getVideoComponents()
    {
        return getVideoComponents(getSession().getSessionContext());
    }


    public void setVideoComponents(SessionContext ctx, List<VideoComponent> value)
    {
        VIDEOCOMPONENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setVideoComponents(List<VideoComponent> value)
    {
        setVideoComponents(getSession().getSessionContext(), value);
    }


    public void addToVideoComponents(SessionContext ctx, VideoComponent value)
    {
        VIDEOCOMPONENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToVideoComponents(VideoComponent value)
    {
        addToVideoComponents(getSession().getSessionContext(), value);
    }


    public void removeFromVideoComponents(SessionContext ctx, VideoComponent value)
    {
        VIDEOCOMPONENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromVideoComponents(VideoComponent value)
    {
        removeFromVideoComponents(getSession().getSessionContext(), value);
    }


    public abstract String getLabelOrId(SessionContext paramSessionContext);
}
