package de.hybris.platform.cms2.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.GenericItem;
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

public abstract class GeneratedCMSVersion extends GenericItem
{
    public static final String UID = "uid";
    public static final String TRANSACTIONID = "transactionId";
    public static final String ITEMUID = "itemUid";
    public static final String ITEMTYPECODE = "itemTypeCode";
    public static final String ITEMCATALOGVERSION = "itemCatalogVersion";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String RETAIN = "retain";
    public static final String PAYLOAD = "payload";
    public static final String RELATEDPARENTS = "relatedParents";
    protected static String CMSVERSION2CMSVERSION_SRC_ORDERED = "relation.CMSVersion2CMSVersion.source.ordered";
    protected static String CMSVERSION2CMSVERSION_TGT_ORDERED = "relation.CMSVersion2CMSVersion.target.ordered";
    protected static String CMSVERSION2CMSVERSION_MARKMODIFIED = "relation.CMSVersion2CMSVersion.markmodified";
    public static final String RELATEDCHILDREN = "relatedChildren";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("transactionId", Item.AttributeMode.INITIAL);
        tmp.put("itemUid", Item.AttributeMode.INITIAL);
        tmp.put("itemTypeCode", Item.AttributeMode.INITIAL);
        tmp.put("itemCatalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("label", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("retain", Item.AttributeMode.INITIAL);
        tmp.put("payload", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSVersion");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED);
        }
        return true;
    }


    public CatalogVersion getItemCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "itemCatalogVersion");
    }


    public CatalogVersion getItemCatalogVersion()
    {
        return getItemCatalogVersion(getSession().getSessionContext());
    }


    public void setItemCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "itemCatalogVersion", value);
    }


    public void setItemCatalogVersion(CatalogVersion value)
    {
        setItemCatalogVersion(getSession().getSessionContext(), value);
    }


    public String getItemTypeCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "itemTypeCode");
    }


    public String getItemTypeCode()
    {
        return getItemTypeCode(getSession().getSessionContext());
    }


    public void setItemTypeCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "itemTypeCode", value);
    }


    public void setItemTypeCode(String value)
    {
        setItemTypeCode(getSession().getSessionContext(), value);
    }


    public String getItemUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "itemUid");
    }


    public String getItemUid()
    {
        return getItemUid(getSession().getSessionContext());
    }


    public void setItemUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "itemUid", value);
    }


    public void setItemUid(String value)
    {
        setItemUid(getSession().getSessionContext(), value);
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


    public String getPayload(SessionContext ctx)
    {
        return (String)getProperty(ctx, "payload");
    }


    public String getPayload()
    {
        return getPayload(getSession().getSessionContext());
    }


    public void setPayload(SessionContext ctx, String value)
    {
        setProperty(ctx, "payload", value);
    }


    public void setPayload(String value)
    {
        setPayload(getSession().getSessionContext(), value);
    }


    public List<CMSVersion> getRelatedChildren(SessionContext ctx)
    {
        List<CMSVersion> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, "CMSVersion", null,
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false);
        return items;
    }


    public List<CMSVersion> getRelatedChildren()
    {
        return getRelatedChildren(getSession().getSessionContext());
    }


    public long getRelatedChildrenCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, "CMSVersion", null);
    }


    public long getRelatedChildrenCount()
    {
        return getRelatedChildrenCount(getSession().getSessionContext());
    }


    public void setRelatedChildren(SessionContext ctx, List<CMSVersion> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null, value,
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void setRelatedChildren(List<CMSVersion> value)
    {
        setRelatedChildren(getSession().getSessionContext(), value);
    }


    public void addToRelatedChildren(SessionContext ctx, CMSVersion value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void addToRelatedChildren(CMSVersion value)
    {
        addToRelatedChildren(getSession().getSessionContext(), value);
    }


    public void removeFromRelatedChildren(SessionContext ctx, CMSVersion value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void removeFromRelatedChildren(CMSVersion value)
    {
        removeFromRelatedChildren(getSession().getSessionContext(), value);
    }


    public Collection<CMSVersion> getRelatedParents(SessionContext ctx)
    {
        List<CMSVersion> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, "CMSVersion", null,
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<CMSVersion> getRelatedParents()
    {
        return getRelatedParents(getSession().getSessionContext());
    }


    public long getRelatedParentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, "CMSVersion", null);
    }


    public long getRelatedParentsCount()
    {
        return getRelatedParentsCount(getSession().getSessionContext());
    }


    public void setRelatedParents(SessionContext ctx, Collection<CMSVersion> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null, value,
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void setRelatedParents(Collection<CMSVersion> value)
    {
        setRelatedParents(getSession().getSessionContext(), value);
    }


    public void addToRelatedParents(SessionContext ctx, CMSVersion value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void addToRelatedParents(CMSVersion value)
    {
        addToRelatedParents(getSession().getSessionContext(), value);
    }


    public void removeFromRelatedParents(SessionContext ctx, CMSVersion value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CMSVERSION2CMSVERSION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CMSVERSION2CMSVERSION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CMSVERSION2CMSVERSION_MARKMODIFIED));
    }


    public void removeFromRelatedParents(CMSVersion value)
    {
        removeFromRelatedParents(getSession().getSessionContext(), value);
    }


    public Boolean isRetain(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "retain");
    }


    public Boolean isRetain()
    {
        return isRetain(getSession().getSessionContext());
    }


    public boolean isRetainAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRetain(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRetainAsPrimitive()
    {
        return isRetainAsPrimitive(getSession().getSessionContext());
    }


    public void setRetain(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "retain", value);
    }


    public void setRetain(Boolean value)
    {
        setRetain(getSession().getSessionContext(), value);
    }


    public void setRetain(SessionContext ctx, boolean value)
    {
        setRetain(ctx, Boolean.valueOf(value));
    }


    public void setRetain(boolean value)
    {
        setRetain(getSession().getSessionContext(), value);
    }


    public String getTransactionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "transactionId");
    }


    public String getTransactionId()
    {
        return getTransactionId(getSession().getSessionContext());
    }


    public void setTransactionId(SessionContext ctx, String value)
    {
        setProperty(ctx, "transactionId", value);
    }


    public void setTransactionId(String value)
    {
        setTransactionId(getSession().getSessionContext(), value);
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    public void setUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uid", value);
    }


    public void setUid(String value)
    {
        setUid(getSession().getSessionContext(), value);
    }
}
