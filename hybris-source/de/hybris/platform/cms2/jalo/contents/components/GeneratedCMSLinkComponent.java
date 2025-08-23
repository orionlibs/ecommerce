package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.navigation.CMSNavigationNode;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCMSLinkComponent extends SimpleCMSComponent
{
    public static final String LINKNAME = "linkName";
    public static final String EXTERNAL = "external";
    public static final String URL = "url";
    public static final String CONTENTPAGELABELORID = "contentPageLabelOrId";
    public static final String PRODUCTCODE = "productCode";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String TARGET = "target";
    public static final String NAVIGATIONNODES = "navigationNodes";
    protected static String CMSLINKSFORNAVNODES_SRC_ORDERED = "relation.CMSLinksForNavNodes.source.ordered";
    protected static String CMSLINKSFORNAVNODES_TGT_ORDERED = "relation.CMSLinksForNavNodes.target.ordered";
    protected static String CMSLINKSFORNAVNODES_MARKMODIFIED = "relation.CMSLinksForNavNodes.markmodified";
    public static final String CONTENTPAGEPOS = "contentPagePOS";
    public static final String CONTENTPAGE = "contentPage";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    public static final String CATEGORYPOS = "categoryPOS";
    public static final String CATEGORY = "category";
    protected static final BidirectionalOneToManyHandler<GeneratedCMSLinkComponent> CONTENTPAGEHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "contentPage", "contentPagePOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedCMSLinkComponent> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "product", "productPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedCMSLinkComponent> CATEGORYHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "category", "categoryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("linkName", Item.AttributeMode.INITIAL);
        tmp.put("external", Item.AttributeMode.INITIAL);
        tmp.put("url", Item.AttributeMode.INITIAL);
        tmp.put("target", Item.AttributeMode.INITIAL);
        tmp.put("contentPagePOS", Item.AttributeMode.INITIAL);
        tmp.put("contentPage", Item.AttributeMode.INITIAL);
        tmp.put("productPOS", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("categoryPOS", Item.AttributeMode.INITIAL);
        tmp.put("category", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Category getCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "category");
    }


    public Category getCategory()
    {
        return getCategory(getSession().getSessionContext());
    }


    public void setCategory(SessionContext ctx, Category value)
    {
        CATEGORYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCategory(Category value)
    {
        setCategory(getSession().getSessionContext(), value);
    }


    public String getCategoryCode()
    {
        return getCategoryCode(getSession().getSessionContext());
    }


    Integer getCategoryPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "categoryPOS");
    }


    Integer getCategoryPOS()
    {
        return getCategoryPOS(getSession().getSessionContext());
    }


    int getCategoryPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCategoryPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCategoryPOSAsPrimitive()
    {
        return getCategoryPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCategoryPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "categoryPOS", value);
    }


    void setCategoryPOS(Integer value)
    {
        setCategoryPOS(getSession().getSessionContext(), value);
    }


    void setCategoryPOS(SessionContext ctx, int value)
    {
        setCategoryPOS(ctx, Integer.valueOf(value));
    }


    void setCategoryPOS(int value)
    {
        setCategoryPOS(getSession().getSessionContext(), value);
    }


    public ContentPage getContentPage(SessionContext ctx)
    {
        return (ContentPage)getProperty(ctx, "contentPage");
    }


    public ContentPage getContentPage()
    {
        return getContentPage(getSession().getSessionContext());
    }


    public void setContentPage(SessionContext ctx, ContentPage value)
    {
        CONTENTPAGEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setContentPage(ContentPage value)
    {
        setContentPage(getSession().getSessionContext(), value);
    }


    public String getContentPageLabelOrId()
    {
        return getContentPageLabelOrId(getSession().getSessionContext());
    }


    Integer getContentPagePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "contentPagePOS");
    }


    Integer getContentPagePOS()
    {
        return getContentPagePOS(getSession().getSessionContext());
    }


    int getContentPagePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getContentPagePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getContentPagePOSAsPrimitive()
    {
        return getContentPagePOSAsPrimitive(getSession().getSessionContext());
    }


    void setContentPagePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "contentPagePOS", value);
    }


    void setContentPagePOS(Integer value)
    {
        setContentPagePOS(getSession().getSessionContext(), value);
    }


    void setContentPagePOS(SessionContext ctx, int value)
    {
        setContentPagePOS(ctx, Integer.valueOf(value));
    }


    void setContentPagePOS(int value)
    {
        setContentPagePOS(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONTENTPAGEHANDLER.newInstance(ctx, allAttributes);
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        CATEGORYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isExternal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "external");
    }


    public Boolean isExternal()
    {
        return isExternal(getSession().getSessionContext());
    }


    public boolean isExternalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExternal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalAsPrimitive()
    {
        return isExternalAsPrimitive(getSession().getSessionContext());
    }


    public void setExternal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "external", value);
    }


    public void setExternal(Boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public void setExternal(SessionContext ctx, boolean value)
    {
        setExternal(ctx, Boolean.valueOf(value));
    }


    public void setExternal(boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSNavigationNode");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED);
        }
        return true;
    }


    public String getLinkName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSLinkComponent.getLinkName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "linkName");
    }


    public String getLinkName()
    {
        return getLinkName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllLinkName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "linkName", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllLinkName()
    {
        return getAllLinkName(getSession().getSessionContext());
    }


    public void setLinkName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSLinkComponent.setLinkName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "linkName", value);
    }


    public void setLinkName(String value)
    {
        setLinkName(getSession().getSessionContext(), value);
    }


    public void setAllLinkName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "linkName", value);
    }


    public void setAllLinkName(Map<Language, String> value)
    {
        setAllLinkName(getSession().getSessionContext(), value);
    }


    public List<CMSNavigationNode> getNavigationNodes(SessionContext ctx)
    {
        List<CMSNavigationNode> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, "CMSNavigationNode", null,
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true));
        return items;
    }


    public List<CMSNavigationNode> getNavigationNodes()
    {
        return getNavigationNodes(getSession().getSessionContext());
    }


    public long getNavigationNodesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, "CMSNavigationNode", null);
    }


    public long getNavigationNodesCount()
    {
        return getNavigationNodesCount(getSession().getSessionContext());
    }


    public void setNavigationNodes(SessionContext ctx, List<CMSNavigationNode> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null, value,
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void setNavigationNodes(List<CMSNavigationNode> value)
    {
        setNavigationNodes(getSession().getSessionContext(), value);
    }


    public void addToNavigationNodes(SessionContext ctx, CMSNavigationNode value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void addToNavigationNodes(CMSNavigationNode value)
    {
        addToNavigationNodes(getSession().getSessionContext(), value);
    }


    public void removeFromNavigationNodes(SessionContext ctx, CMSNavigationNode value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSLINKSFORNAVNODES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CMSLINKSFORNAVNODES_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CMSLINKSFORNAVNODES_MARKMODIFIED));
    }


    public void removeFromNavigationNodes(CMSNavigationNode value)
    {
        removeFromNavigationNodes(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(SessionContext ctx, Product value)
    {
        PRODUCTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public String getProductCode()
    {
        return getProductCode(getSession().getSessionContext());
    }


    Integer getProductPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "productPOS");
    }


    Integer getProductPOS()
    {
        return getProductPOS(getSession().getSessionContext());
    }


    int getProductPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getProductPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getProductPOSAsPrimitive()
    {
        return getProductPOSAsPrimitive(getSession().getSessionContext());
    }


    void setProductPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "productPOS", value);
    }


    void setProductPOS(Integer value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    void setProductPOS(SessionContext ctx, int value)
    {
        setProductPOS(ctx, Integer.valueOf(value));
    }


    void setProductPOS(int value)
    {
        setProductPOS(getSession().getSessionContext(), value);
    }


    public EnumerationValue getTarget(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "target");
    }


    public EnumerationValue getTarget()
    {
        return getTarget(getSession().getSessionContext());
    }


    public void setTarget(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "target", value);
    }


    public void setTarget(EnumerationValue value)
    {
        setTarget(getSession().getSessionContext(), value);
    }


    public String getUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "url");
    }


    public String getUrl()
    {
        return getUrl(getSession().getSessionContext());
    }


    public void setUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "url", value);
    }


    public void setUrl(String value)
    {
        setUrl(getSession().getSessionContext(), value);
    }


    public abstract String getCategoryCode(SessionContext paramSessionContext);


    public abstract String getContentPageLabelOrId(SessionContext paramSessionContext);


    public abstract String getProductCode(SessionContext paramSessionContext);
}
