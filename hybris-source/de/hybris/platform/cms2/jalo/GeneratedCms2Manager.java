package de.hybris.platform.cms2.jalo;

import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.ContentCatalog;
import de.hybris.platform.cms2.jalo.contents.ContentSlotName;
import de.hybris.platform.cms2.jalo.contents.components.CMSFlexComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSImageComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSParagraphComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSSiteContextComponent;
import de.hybris.platform.cms2.jalo.contents.components.PDFDocumentComponent;
import de.hybris.platform.cms2.jalo.contents.components.VideoComponent;
import de.hybris.platform.cms2.jalo.contents.containers.ABTestCMSComponentContainer;
import de.hybris.platform.cms2.jalo.contents.contentslot.ContentSlot;
import de.hybris.platform.cms2.jalo.navigation.CMSNavigationEntry;
import de.hybris.platform.cms2.jalo.navigation.CMSNavigationNode;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.cms2.jalo.pages.CatalogPage;
import de.hybris.platform.cms2.jalo.pages.CategoryPage;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.cms2.jalo.pages.ProductPage;
import de.hybris.platform.cms2.jalo.preview.CMSPreviewTicket;
import de.hybris.platform.cms2.jalo.preview.PreviewData;
import de.hybris.platform.cms2.jalo.relations.CMSRelation;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForPage;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForTemplate;
import de.hybris.platform.cms2.jalo.restrictions.BaseStoreTimeRestriction;
import de.hybris.platform.cms2.jalo.restrictions.CMSInverseRestriction;
import de.hybris.platform.cms2.jalo.restrictions.CampaignRestriction;
import de.hybris.platform.cms2.jalo.restrictions.CatalogRestriction;
import de.hybris.platform.cms2.jalo.restrictions.CategoryRestriction;
import de.hybris.platform.cms2.jalo.restrictions.GroupRestriction;
import de.hybris.platform.cms2.jalo.restrictions.ProductRestriction;
import de.hybris.platform.cms2.jalo.restrictions.TimeRestriction;
import de.hybris.platform.cms2.jalo.restrictions.UserRestriction;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCms2Manager extends Extension
{
    protected static String USERSFORRESTRICTION_SRC_ORDERED = "relation.UsersForRestriction.source.ordered";
    protected static String USERSFORRESTRICTION_TGT_ORDERED = "relation.UsersForRestriction.target.ordered";
    protected static String USERSFORRESTRICTION_MARKMODIFIED = "relation.UsersForRestriction.markmodified";
    protected static final OneToManyHandler<AbstractPage> ABSTRACTPAGE2USERRELATIONLOCKEDPAGESHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.ABSTRACTPAGE, false, "lockedBy", null, false, true, 0);
    protected static String CATALOGSFORRESTRICTION_SRC_ORDERED = "relation.CatalogsForRestriction.source.ordered";
    protected static String CATALOGSFORRESTRICTION_TGT_ORDERED = "relation.CatalogsForRestriction.target.ordered";
    protected static String CATALOGSFORRESTRICTION_MARKMODIFIED = "relation.CatalogsForRestriction.markmodified";
    protected static String CATEGORIESFORRESTRICTION_SRC_ORDERED = "relation.CategoriesForRestriction.source.ordered";
    protected static String CATEGORIESFORRESTRICTION_TGT_ORDERED = "relation.CategoriesForRestriction.target.ordered";
    protected static String CATEGORIESFORRESTRICTION_MARKMODIFIED = "relation.CategoriesForRestriction.markmodified";
    protected static final OneToManyHandler<CMSLinkComponent> CMSLINKCOMPONENTSFORCATEGORYLINKCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "category", "categoryPOS", true, true, 2);
    protected static final OneToManyHandler<VideoComponent> VIDEOCOMPONENTSFORCATEGORYVIDEOCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "category", "categoryPOS", true, true, 2);
    protected static String PRODUCTSFORRESTRICTION_SRC_ORDERED = "relation.ProductsForRestriction.source.ordered";
    protected static String PRODUCTSFORRESTRICTION_TGT_ORDERED = "relation.ProductsForRestriction.target.ordered";
    protected static String PRODUCTSFORRESTRICTION_MARKMODIFIED = "relation.ProductsForRestriction.markmodified";
    protected static final OneToManyHandler<CMSLinkComponent> CMSLINKCOMPONENTSFORPRODUCTLINKCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSLINKCOMPONENT, false, "product", "productPOS", true, true, 2);
    protected static final OneToManyHandler<VideoComponent> VIDEOCOMPONENTSFORPRODUCTVIDEOCOMPONENTSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.VIDEOCOMPONENT, false, "product", "productPOS", true, true, 2);
    protected static String USERGROUPSFORRESTRICTION_SRC_ORDERED = "relation.UserGroupsForRestriction.source.ordered";
    protected static String USERGROUPSFORRESTRICTION_TGT_ORDERED = "relation.UserGroupsForRestriction.target.ordered";
    protected static String USERGROUPSFORRESTRICTION_MARKMODIFIED = "relation.UserGroupsForRestriction.markmodified";
    protected static String CAMPAIGNSFORRESTRICTION_SRC_ORDERED = "relation.CampaignsForRestriction.source.ordered";
    protected static String CAMPAIGNSFORRESTRICTION_TGT_ORDERED = "relation.CampaignsForRestriction.target.ordered";
    protected static String CAMPAIGNSFORRESTRICTION_MARKMODIFIED = "relation.CampaignsForRestriction.markmodified";
    protected static String PREVIEWDATATOCATALOGVERSION_SRC_ORDERED = "relation.PreviewDataToCatalogVersion.source.ordered";
    protected static String PREVIEWDATATOCATALOGVERSION_TGT_ORDERED = "relation.PreviewDataToCatalogVersion.target.ordered";
    protected static String PREVIEWDATATOCATALOGVERSION_MARKMODIFIED = "relation.PreviewDataToCatalogVersion.markmodified";
    protected static String WORKFLOWTEMPLATEFORCATALOGVERSION_SRC_ORDERED = "relation.WorkflowTemplateForCatalogVersion.source.ordered";
    protected static String WORKFLOWTEMPLATEFORCATALOGVERSION_TGT_ORDERED = "relation.WorkflowTemplateForCatalogVersion.target.ordered";
    protected static String WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED = "relation.WorkflowTemplateForCatalogVersion.markmodified";
    protected static String STORETIMERESTRICTION2BASESTORE_SRC_ORDERED = "relation.StoreTimeRestriction2BaseStore.source.ordered";
    protected static String STORETIMERESTRICTION2BASESTORE_TGT_ORDERED = "relation.StoreTimeRestriction2BaseStore.target.ordered";
    protected static String STORETIMERESTRICTION2BASESTORE_MARKMODIFIED = "relation.StoreTimeRestriction2BaseStore.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("authorizedToUnlockPages", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Boolean isAuthorizedToUnlockPages(SessionContext ctx, User item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCms2Constants.Attributes.User.AUTHORIZEDTOUNLOCKPAGES);
    }


    public Boolean isAuthorizedToUnlockPages(User item)
    {
        return isAuthorizedToUnlockPages(getSession().getSessionContext(), item);
    }


    public boolean isAuthorizedToUnlockPagesAsPrimitive(SessionContext ctx, User item)
    {
        Boolean value = isAuthorizedToUnlockPages(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAuthorizedToUnlockPagesAsPrimitive(User item)
    {
        return isAuthorizedToUnlockPagesAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setAuthorizedToUnlockPages(SessionContext ctx, User item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCms2Constants.Attributes.User.AUTHORIZEDTOUNLOCKPAGES, value);
    }


    public void setAuthorizedToUnlockPages(User item, Boolean value)
    {
        setAuthorizedToUnlockPages(getSession().getSessionContext(), item, value);
    }


    public void setAuthorizedToUnlockPages(SessionContext ctx, User item, boolean value)
    {
        setAuthorizedToUnlockPages(ctx, item, Boolean.valueOf(value));
    }


    public void setAuthorizedToUnlockPages(User item, boolean value)
    {
        setAuthorizedToUnlockPages(getSession().getSessionContext(), item, value);
    }


    public Collection<CatalogVersion> getCatalogVersions(SessionContext ctx, WorkflowTemplate item)
    {
        List<CatalogVersion> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, "CatalogVersion", null, false, false);
        return items;
    }


    public Collection<CatalogVersion> getCatalogVersions(WorkflowTemplate item)
    {
        return getCatalogVersions(getSession().getSessionContext(), item);
    }


    public long getCatalogVersionsCount(SessionContext ctx, WorkflowTemplate item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, "CatalogVersion", null);
    }


    public long getCatalogVersionsCount(WorkflowTemplate item)
    {
        return getCatalogVersionsCount(getSession().getSessionContext(), item);
    }


    public void setCatalogVersions(SessionContext ctx, WorkflowTemplate item, Collection<CatalogVersion> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void setCatalogVersions(WorkflowTemplate item, Collection<CatalogVersion> value)
    {
        setCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void addToCatalogVersions(SessionContext ctx, WorkflowTemplate item, CatalogVersion value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void addToCatalogVersions(WorkflowTemplate item, CatalogVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, WorkflowTemplate item, CatalogVersion value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void removeFromCatalogVersions(WorkflowTemplate item, CatalogVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public Collection<BaseStoreTimeRestriction> getCmsTimeRestrictions(SessionContext ctx, BaseStore item)
    {
        List<BaseStoreTimeRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, "CMSBaseStoreTimeRestriction", null, false, false);
        return items;
    }


    public Collection<BaseStoreTimeRestriction> getCmsTimeRestrictions(BaseStore item)
    {
        return getCmsTimeRestrictions(getSession().getSessionContext(), item);
    }


    public long getCmsTimeRestrictionsCount(SessionContext ctx, BaseStore item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, "CMSBaseStoreTimeRestriction", null);
    }


    public long getCmsTimeRestrictionsCount(BaseStore item)
    {
        return getCmsTimeRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setCmsTimeRestrictions(SessionContext ctx, BaseStore item, Collection<BaseStoreTimeRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void setCmsTimeRestrictions(BaseStore item, Collection<BaseStoreTimeRestriction> value)
    {
        setCmsTimeRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToCmsTimeRestrictions(SessionContext ctx, BaseStore item, BaseStoreTimeRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void addToCmsTimeRestrictions(BaseStore item, BaseStoreTimeRestriction value)
    {
        addToCmsTimeRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromCmsTimeRestrictions(SessionContext ctx, BaseStore item, BaseStoreTimeRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void removeFromCmsTimeRestrictions(BaseStore item, BaseStoreTimeRestriction value)
    {
        removeFromCmsTimeRestrictions(getSession().getSessionContext(), item, value);
    }


    public ABTestCMSComponentContainer createABTestCMSComponentContainer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.ABTESTCMSCOMPONENTCONTAINER);
            return (ABTestCMSComponentContainer)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ABTestCMSComponentContainer : " + e.getMessage(), 0);
        }
    }


    public ABTestCMSComponentContainer createABTestCMSComponentContainer(Map attributeValues)
    {
        return createABTestCMSComponentContainer(getSession().getSessionContext(), attributeValues);
    }


    public CatalogPage createCatalogPage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CATALOGPAGE);
            return (CatalogPage)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogPage : " + e.getMessage(), 0);
        }
    }


    public CatalogPage createCatalogPage(Map attributeValues)
    {
        return createCatalogPage(getSession().getSessionContext(), attributeValues);
    }


    public CategoryPage createCategoryPage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CATEGORYPAGE);
            return (CategoryPage)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CategoryPage : " + e.getMessage(), 0);
        }
    }


    public CategoryPage createCategoryPage(Map attributeValues)
    {
        return createCategoryPage(getSession().getSessionContext(), attributeValues);
    }


    public BaseStoreTimeRestriction createCMSBaseStoreTimeRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSBASESTORETIMERESTRICTION);
            return (BaseStoreTimeRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSBaseStoreTimeRestriction : " + e.getMessage(), 0);
        }
    }


    public BaseStoreTimeRestriction createCMSBaseStoreTimeRestriction(Map attributeValues)
    {
        return createCMSBaseStoreTimeRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CampaignRestriction createCMSCampaignRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSCAMPAIGNRESTRICTION);
            return (CampaignRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSCampaignRestriction : " + e.getMessage(), 0);
        }
    }


    public CampaignRestriction createCMSCampaignRestriction(Map attributeValues)
    {
        return createCMSCampaignRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CatalogRestriction createCMSCatalogRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSCATALOGRESTRICTION);
            return (CatalogRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSCatalogRestriction : " + e.getMessage(), 0);
        }
    }


    public CatalogRestriction createCMSCatalogRestriction(Map attributeValues)
    {
        return createCMSCatalogRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CategoryRestriction createCMSCategoryRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSCATEGORYRESTRICTION);
            return (CategoryRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSCategoryRestriction : " + e.getMessage(), 0);
        }
    }


    public CategoryRestriction createCMSCategoryRestriction(Map attributeValues)
    {
        return createCMSCategoryRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CMSComponentType createCMSComponentType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSCOMPONENTTYPE);
            return (CMSComponentType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSComponentType : " + e.getMessage(), 0);
        }
    }


    public CMSComponentType createCMSComponentType(Map attributeValues)
    {
        return createCMSComponentType(getSession().getSessionContext(), attributeValues);
    }


    public CMSFlexComponent createCMSFlexComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSFLEXCOMPONENT);
            return (CMSFlexComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSFlexComponent : " + e.getMessage(), 0);
        }
    }


    public CMSFlexComponent createCMSFlexComponent(Map attributeValues)
    {
        return createCMSFlexComponent(getSession().getSessionContext(), attributeValues);
    }


    public CMSImageComponent createCMSImageComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSIMAGECOMPONENT);
            return (CMSImageComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSImageComponent : " + e.getMessage(), 0);
        }
    }


    public CMSImageComponent createCMSImageComponent(Map attributeValues)
    {
        return createCMSImageComponent(getSession().getSessionContext(), attributeValues);
    }


    public CMSInverseRestriction createCMSInverseRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSINVERSERESTRICTION);
            return (CMSInverseRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSInverseRestriction : " + e.getMessage(), 0);
        }
    }


    public CMSInverseRestriction createCMSInverseRestriction(Map attributeValues)
    {
        return createCMSInverseRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CMSLinkComponent createCMSLinkComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSLINKCOMPONENT);
            return (CMSLinkComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSLinkComponent : " + e.getMessage(), 0);
        }
    }


    public CMSLinkComponent createCMSLinkComponent(Map attributeValues)
    {
        return createCMSLinkComponent(getSession().getSessionContext(), attributeValues);
    }


    public CMSNavigationEntry createCMSNavigationEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSNAVIGATIONENTRY);
            return (CMSNavigationEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSNavigationEntry : " + e.getMessage(), 0);
        }
    }


    public CMSNavigationEntry createCMSNavigationEntry(Map attributeValues)
    {
        return createCMSNavigationEntry(getSession().getSessionContext(), attributeValues);
    }


    public CMSNavigationNode createCMSNavigationNode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSNAVIGATIONNODE);
            return (CMSNavigationNode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSNavigationNode : " + e.getMessage(), 0);
        }
    }


    public CMSNavigationNode createCMSNavigationNode(Map attributeValues)
    {
        return createCMSNavigationNode(getSession().getSessionContext(), attributeValues);
    }


    public CMSPageType createCMSPageType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSPAGETYPE);
            return (CMSPageType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSPageType : " + e.getMessage(), 0);
        }
    }


    public CMSPageType createCMSPageType(Map attributeValues)
    {
        return createCMSPageType(getSession().getSessionContext(), attributeValues);
    }


    public CMSParagraphComponent createCMSParagraphComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSPARAGRAPHCOMPONENT);
            return (CMSParagraphComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSParagraphComponent : " + e.getMessage(), 0);
        }
    }


    public CMSParagraphComponent createCMSParagraphComponent(Map attributeValues)
    {
        return createCMSParagraphComponent(getSession().getSessionContext(), attributeValues);
    }


    public CMSPreviewTicket createCMSPreviewTicket(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSPREVIEWTICKET);
            return (CMSPreviewTicket)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSPreviewTicket : " + e.getMessage(), 0);
        }
    }


    public CMSPreviewTicket createCMSPreviewTicket(Map attributeValues)
    {
        return createCMSPreviewTicket(getSession().getSessionContext(), attributeValues);
    }


    public ProductRestriction createCMSProductRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSPRODUCTRESTRICTION);
            return (ProductRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSProductRestriction : " + e.getMessage(), 0);
        }
    }


    public ProductRestriction createCMSProductRestriction(Map attributeValues)
    {
        return createCMSProductRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CMSRelation createCMSRelation(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSRELATION);
            return (CMSRelation)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSRelation : " + e.getMessage(), 0);
        }
    }


    public CMSRelation createCMSRelation(Map attributeValues)
    {
        return createCMSRelation(getSession().getSessionContext(), attributeValues);
    }


    public CMSSite createCMSSite(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSSITE);
            return (CMSSite)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSSite : " + e.getMessage(), 0);
        }
    }


    public CMSSite createCMSSite(Map attributeValues)
    {
        return createCMSSite(getSession().getSessionContext(), attributeValues);
    }


    public CMSSiteContextComponent createCMSSiteContextComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSSITECONTEXTCOMPONENT);
            return (CMSSiteContextComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSSiteContextComponent : " + e.getMessage(), 0);
        }
    }


    public CMSSiteContextComponent createCMSSiteContextComponent(Map attributeValues)
    {
        return createCMSSiteContextComponent(getSession().getSessionContext(), attributeValues);
    }


    public TimeRestriction createCMSTimeRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSTIMERESTRICTION);
            return (TimeRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSTimeRestriction : " + e.getMessage(), 0);
        }
    }


    public TimeRestriction createCMSTimeRestriction(Map attributeValues)
    {
        return createCMSTimeRestriction(getSession().getSessionContext(), attributeValues);
    }


    public GroupRestriction createCMSUserGroupRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSUSERGROUPRESTRICTION);
            return (GroupRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSUserGroupRestriction : " + e.getMessage(), 0);
        }
    }


    public GroupRestriction createCMSUserGroupRestriction(Map attributeValues)
    {
        return createCMSUserGroupRestriction(getSession().getSessionContext(), attributeValues);
    }


    public UserRestriction createCMSUserRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSUSERRESTRICTION);
            return (UserRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSUserRestriction : " + e.getMessage(), 0);
        }
    }


    public UserRestriction createCMSUserRestriction(Map attributeValues)
    {
        return createCMSUserRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CMSVersion createCMSVersion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSVERSION);
            return (CMSVersion)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSVersion : " + e.getMessage(), 0);
        }
    }


    public CMSVersion createCMSVersion(Map attributeValues)
    {
        return createCMSVersion(getSession().getSessionContext(), attributeValues);
    }


    public CMSWorkflowComment createCMSWorkflowComment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CMSWORKFLOWCOMMENT);
            return (CMSWorkflowComment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CMSWorkflowComment : " + e.getMessage(), 0);
        }
    }


    public CMSWorkflowComment createCMSWorkflowComment(Map attributeValues)
    {
        return createCMSWorkflowComment(getSession().getSessionContext(), attributeValues);
    }


    public ComponentTypeGroup createComponentTypeGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.COMPONENTTYPEGROUP);
            return (ComponentTypeGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ComponentTypeGroup : " + e.getMessage(), 0);
        }
    }


    public ComponentTypeGroup createComponentTypeGroup(Map attributeValues)
    {
        return createComponentTypeGroup(getSession().getSessionContext(), attributeValues);
    }


    public ContentCatalog createContentCatalog(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTCATALOG);
            return (ContentCatalog)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentCatalog : " + e.getMessage(), 0);
        }
    }


    public ContentCatalog createContentCatalog(Map attributeValues)
    {
        return createContentCatalog(getSession().getSessionContext(), attributeValues);
    }


    public ContentPage createContentPage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTPAGE);
            return (ContentPage)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentPage : " + e.getMessage(), 0);
        }
    }


    public ContentPage createContentPage(Map attributeValues)
    {
        return createContentPage(getSession().getSessionContext(), attributeValues);
    }


    public ContentSlot createContentSlot(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTSLOT);
            return (ContentSlot)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentSlot : " + e.getMessage(), 0);
        }
    }


    public ContentSlot createContentSlot(Map attributeValues)
    {
        return createContentSlot(getSession().getSessionContext(), attributeValues);
    }


    public ContentSlotForPage createContentSlotForPage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTSLOTFORPAGE);
            return (ContentSlotForPage)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentSlotForPage : " + e.getMessage(), 0);
        }
    }


    public ContentSlotForPage createContentSlotForPage(Map attributeValues)
    {
        return createContentSlotForPage(getSession().getSessionContext(), attributeValues);
    }


    public ContentSlotForTemplate createContentSlotForTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTSLOTFORTEMPLATE);
            return (ContentSlotForTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentSlotForTemplate : " + e.getMessage(), 0);
        }
    }


    public ContentSlotForTemplate createContentSlotForTemplate(Map attributeValues)
    {
        return createContentSlotForTemplate(getSession().getSessionContext(), attributeValues);
    }


    public ContentSlotName createContentSlotName(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.CONTENTSLOTNAME);
            return (ContentSlotName)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ContentSlotName : " + e.getMessage(), 0);
        }
    }


    public ContentSlotName createContentSlotName(Map attributeValues)
    {
        return createContentSlotName(getSession().getSessionContext(), attributeValues);
    }


    public PageTemplate createPageTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.PAGETEMPLATE);
            return (PageTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PageTemplate : " + e.getMessage(), 0);
        }
    }


    public PageTemplate createPageTemplate(Map attributeValues)
    {
        return createPageTemplate(getSession().getSessionContext(), attributeValues);
    }


    public PDFDocumentComponent createPDFDocumentComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.PDFDOCUMENTCOMPONENT);
            return (PDFDocumentComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PDFDocumentComponent : " + e.getMessage(), 0);
        }
    }


    public PDFDocumentComponent createPDFDocumentComponent(Map attributeValues)
    {
        return createPDFDocumentComponent(getSession().getSessionContext(), attributeValues);
    }


    public PreviewData createPreviewData(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.PREVIEWDATA);
            return (PreviewData)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PreviewData : " + e.getMessage(), 0);
        }
    }


    public PreviewData createPreviewData(Map attributeValues)
    {
        return createPreviewData(getSession().getSessionContext(), attributeValues);
    }


    public ProductPage createProductPage(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.PRODUCTPAGE);
            return (ProductPage)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductPage : " + e.getMessage(), 0);
        }
    }


    public ProductPage createProductPage(Map attributeValues)
    {
        return createProductPage(getSession().getSessionContext(), attributeValues);
    }


    public RestrictionType createRestrictionType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.RESTRICTIONTYPE);
            return (RestrictionType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RestrictionType : " + e.getMessage(), 0);
        }
    }


    public RestrictionType createRestrictionType(Map attributeValues)
    {
        return createRestrictionType(getSession().getSessionContext(), attributeValues);
    }


    public VideoComponent createVideoComponent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCms2Constants.TC.VIDEOCOMPONENT);
            return (VideoComponent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VideoComponent : " + e.getMessage(), 0);
        }
    }


    public VideoComponent createVideoComponent(Map attributeValues)
    {
        return createVideoComponent(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "cms2";
    }


    public List<CMSLinkComponent> getLinkComponents(SessionContext ctx, Category item)
    {
        return (List<CMSLinkComponent>)CMSLINKCOMPONENTSFORCATEGORYLINKCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<CMSLinkComponent> getLinkComponents(Category item)
    {
        return getLinkComponents(getSession().getSessionContext(), item);
    }


    public void setLinkComponents(SessionContext ctx, Category item, List<CMSLinkComponent> value)
    {
        CMSLINKCOMPONENTSFORCATEGORYLINKCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setLinkComponents(Category item, List<CMSLinkComponent> value)
    {
        setLinkComponents(getSession().getSessionContext(), item, value);
    }


    public void addToLinkComponents(SessionContext ctx, Category item, CMSLinkComponent value)
    {
        CMSLINKCOMPONENTSFORCATEGORYLINKCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToLinkComponents(Category item, CMSLinkComponent value)
    {
        addToLinkComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromLinkComponents(SessionContext ctx, Category item, CMSLinkComponent value)
    {
        CMSLINKCOMPONENTSFORCATEGORYLINKCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromLinkComponents(Category item, CMSLinkComponent value)
    {
        removeFromLinkComponents(getSession().getSessionContext(), item, value);
    }


    public List<CMSLinkComponent> getLinkComponents(SessionContext ctx, Product item)
    {
        return (List<CMSLinkComponent>)CMSLINKCOMPONENTSFORPRODUCTLINKCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<CMSLinkComponent> getLinkComponents(Product item)
    {
        return getLinkComponents(getSession().getSessionContext(), item);
    }


    public void setLinkComponents(SessionContext ctx, Product item, List<CMSLinkComponent> value)
    {
        CMSLINKCOMPONENTSFORPRODUCTLINKCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setLinkComponents(Product item, List<CMSLinkComponent> value)
    {
        setLinkComponents(getSession().getSessionContext(), item, value);
    }


    public void addToLinkComponents(SessionContext ctx, Product item, CMSLinkComponent value)
    {
        CMSLINKCOMPONENTSFORPRODUCTLINKCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToLinkComponents(Product item, CMSLinkComponent value)
    {
        addToLinkComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromLinkComponents(SessionContext ctx, Product item, CMSLinkComponent value)
    {
        CMSLINKCOMPONENTSFORPRODUCTLINKCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromLinkComponents(Product item, CMSLinkComponent value)
    {
        removeFromLinkComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<AbstractPage> getLockedPages(SessionContext ctx, User item)
    {
        return ABSTRACTPAGE2USERRELATIONLOCKEDPAGESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<AbstractPage> getLockedPages(User item)
    {
        return getLockedPages(getSession().getSessionContext(), item);
    }


    public void setLockedPages(SessionContext ctx, User item, Collection<AbstractPage> value)
    {
        ABSTRACTPAGE2USERRELATIONLOCKEDPAGESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setLockedPages(User item, Collection<AbstractPage> value)
    {
        setLockedPages(getSession().getSessionContext(), item, value);
    }


    public void addToLockedPages(SessionContext ctx, User item, AbstractPage value)
    {
        ABSTRACTPAGE2USERRELATIONLOCKEDPAGESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToLockedPages(User item, AbstractPage value)
    {
        addToLockedPages(getSession().getSessionContext(), item, value);
    }


    public void removeFromLockedPages(SessionContext ctx, User item, AbstractPage value)
    {
        ABSTRACTPAGE2USERRELATIONLOCKEDPAGESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromLockedPages(User item, AbstractPage value)
    {
        removeFromLockedPages(getSession().getSessionContext(), item, value);
    }


    public Collection<PreviewData> getPreviews(SessionContext ctx, CatalogVersion item)
    {
        List<PreviewData> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, "PreviewData", null, false, false);
        return items;
    }


    public Collection<PreviewData> getPreviews(CatalogVersion item)
    {
        return getPreviews(getSession().getSessionContext(), item);
    }


    public long getPreviewsCount(SessionContext ctx, CatalogVersion item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, "PreviewData", null);
    }


    public long getPreviewsCount(CatalogVersion item)
    {
        return getPreviewsCount(getSession().getSessionContext(), item);
    }


    public void setPreviews(SessionContext ctx, CatalogVersion item, Collection<PreviewData> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void setPreviews(CatalogVersion item, Collection<PreviewData> value)
    {
        setPreviews(getSession().getSessionContext(), item, value);
    }


    public void addToPreviews(SessionContext ctx, CatalogVersion item, PreviewData value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void addToPreviews(CatalogVersion item, PreviewData value)
    {
        addToPreviews(getSession().getSessionContext(), item, value);
    }


    public void removeFromPreviews(SessionContext ctx, CatalogVersion item, PreviewData value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void removeFromPreviews(CatalogVersion item, PreviewData value)
    {
        removeFromPreviews(getSession().getSessionContext(), item, value);
    }


    public Collection<UserRestriction> getRestrictions(SessionContext ctx, User item)
    {
        List<UserRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, "CMSUserRestriction", null, false, false);
        return items;
    }


    public Collection<UserRestriction> getRestrictions(User item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, User item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, "CMSUserRestriction", null);
    }


    public long getRestrictionsCount(User item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, User item, Collection<UserRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(User item, Collection<UserRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, User item, UserRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(User item, UserRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, User item, UserRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(User item, UserRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public Collection<CatalogRestriction> getRestrictions(SessionContext ctx, Catalog item)
    {
        List<CatalogRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, "CMSCatalogRestriction", null, false, false);
        return items;
    }


    public Collection<CatalogRestriction> getRestrictions(Catalog item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, Catalog item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, "CMSCatalogRestriction", null);
    }


    public long getRestrictionsCount(Catalog item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, Catalog item, Collection<CatalogRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(Catalog item, Collection<CatalogRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, Catalog item, CatalogRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(Catalog item, CatalogRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, Catalog item, CatalogRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(Catalog item, CatalogRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public Collection<CategoryRestriction> getRestrictions(SessionContext ctx, Category item)
    {
        List<CategoryRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, "CMSCategoryRestriction", null, false, false);
        return items;
    }


    public Collection<CategoryRestriction> getRestrictions(Category item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, Category item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, "CMSCategoryRestriction", null);
    }


    public long getRestrictionsCount(Category item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, Category item, Collection<CategoryRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(Category item, Collection<CategoryRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, Category item, CategoryRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(Category item, CategoryRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, Category item, CategoryRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(Category item, CategoryRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductRestriction> getRestrictions(SessionContext ctx, Product item)
    {
        List<ProductRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, "CMSProductRestriction", null, false, false);
        return items;
    }


    public Collection<ProductRestriction> getRestrictions(Product item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, "CMSProductRestriction", null);
    }


    public long getRestrictionsCount(Product item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, Product item, Collection<ProductRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(Product item, Collection<ProductRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, Product item, ProductRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(Product item, ProductRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, Product item, ProductRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(Product item, ProductRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public Collection<GroupRestriction> getRestrictions(SessionContext ctx, UserGroup item)
    {
        List<GroupRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, "CMSUserGroupRestriction", null, false, false);
        return items;
    }


    public Collection<GroupRestriction> getRestrictions(UserGroup item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, UserGroup item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, "CMSUserGroupRestriction", null);
    }


    public long getRestrictionsCount(UserGroup item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, UserGroup item, Collection<GroupRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(UserGroup item, Collection<GroupRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, UserGroup item, GroupRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(UserGroup item, GroupRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, UserGroup item, GroupRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(UserGroup item, GroupRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public Collection<CampaignRestriction> getRestrictions(SessionContext ctx, Campaign item)
    {
        List<CampaignRestriction> items = item.getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, "CMSCampaignRestriction", null, false, false);
        return items;
    }


    public Collection<CampaignRestriction> getRestrictions(Campaign item)
    {
        return getRestrictions(getSession().getSessionContext(), item);
    }


    public long getRestrictionsCount(SessionContext ctx, Campaign item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, "CMSCampaignRestriction", null);
    }


    public long getRestrictionsCount(Campaign item)
    {
        return getRestrictionsCount(getSession().getSessionContext(), item);
    }


    public void setRestrictions(SessionContext ctx, Campaign item, Collection<CampaignRestriction> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void setRestrictions(Campaign item, Collection<CampaignRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), item, value);
    }


    public void addToRestrictions(SessionContext ctx, Campaign item, CampaignRestriction value)
    {
        item.addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToRestrictions(Campaign item, CampaignRestriction value)
    {
        addToRestrictions(getSession().getSessionContext(), item, value);
    }


    public void removeFromRestrictions(SessionContext ctx, Campaign item, CampaignRestriction value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CAMPAIGNSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CAMPAIGNSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromRestrictions(Campaign item, CampaignRestriction value)
    {
        removeFromRestrictions(getSession().getSessionContext(), item, value);
    }


    public List<VideoComponent> getVideoComponents(SessionContext ctx, Category item)
    {
        return (List<VideoComponent>)VIDEOCOMPONENTSFORCATEGORYVIDEOCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<VideoComponent> getVideoComponents(Category item)
    {
        return getVideoComponents(getSession().getSessionContext(), item);
    }


    public void setVideoComponents(SessionContext ctx, Category item, List<VideoComponent> value)
    {
        VIDEOCOMPONENTSFORCATEGORYVIDEOCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setVideoComponents(Category item, List<VideoComponent> value)
    {
        setVideoComponents(getSession().getSessionContext(), item, value);
    }


    public void addToVideoComponents(SessionContext ctx, Category item, VideoComponent value)
    {
        VIDEOCOMPONENTSFORCATEGORYVIDEOCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToVideoComponents(Category item, VideoComponent value)
    {
        addToVideoComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromVideoComponents(SessionContext ctx, Category item, VideoComponent value)
    {
        VIDEOCOMPONENTSFORCATEGORYVIDEOCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromVideoComponents(Category item, VideoComponent value)
    {
        removeFromVideoComponents(getSession().getSessionContext(), item, value);
    }


    public List<VideoComponent> getVideoComponents(SessionContext ctx, Product item)
    {
        return (List<VideoComponent>)VIDEOCOMPONENTSFORPRODUCTVIDEOCOMPONENTSHANDLER.getValues(ctx, (Item)item);
    }


    public List<VideoComponent> getVideoComponents(Product item)
    {
        return getVideoComponents(getSession().getSessionContext(), item);
    }


    public void setVideoComponents(SessionContext ctx, Product item, List<VideoComponent> value)
    {
        VIDEOCOMPONENTSFORPRODUCTVIDEOCOMPONENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setVideoComponents(Product item, List<VideoComponent> value)
    {
        setVideoComponents(getSession().getSessionContext(), item, value);
    }


    public void addToVideoComponents(SessionContext ctx, Product item, VideoComponent value)
    {
        VIDEOCOMPONENTSFORPRODUCTVIDEOCOMPONENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToVideoComponents(Product item, VideoComponent value)
    {
        addToVideoComponents(getSession().getSessionContext(), item, value);
    }


    public void removeFromVideoComponents(SessionContext ctx, Product item, VideoComponent value)
    {
        VIDEOCOMPONENTSFORPRODUCTVIDEOCOMPONENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromVideoComponents(Product item, VideoComponent value)
    {
        removeFromVideoComponents(getSession().getSessionContext(), item, value);
    }


    public Collection<WorkflowTemplate> getWorkflowTemplates(SessionContext ctx, CatalogVersion item)
    {
        List<WorkflowTemplate> items = item.getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, "WorkflowTemplate", null, false, false);
        return items;
    }


    public Collection<WorkflowTemplate> getWorkflowTemplates(CatalogVersion item)
    {
        return getWorkflowTemplates(getSession().getSessionContext(), item);
    }


    public long getWorkflowTemplatesCount(SessionContext ctx, CatalogVersion item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, "WorkflowTemplate", null);
    }


    public long getWorkflowTemplatesCount(CatalogVersion item)
    {
        return getWorkflowTemplatesCount(getSession().getSessionContext(), item);
    }


    public void setWorkflowTemplates(SessionContext ctx, CatalogVersion item, Collection<WorkflowTemplate> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void setWorkflowTemplates(CatalogVersion item, Collection<WorkflowTemplate> value)
    {
        setWorkflowTemplates(getSession().getSessionContext(), item, value);
    }


    public void addToWorkflowTemplates(SessionContext ctx, CatalogVersion item, WorkflowTemplate value)
    {
        item.addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void addToWorkflowTemplates(CatalogVersion item, WorkflowTemplate value)
    {
        addToWorkflowTemplates(getSession().getSessionContext(), item, value);
    }


    public void removeFromWorkflowTemplates(SessionContext ctx, CatalogVersion item, WorkflowTemplate value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.WORKFLOWTEMPLATEFORCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATEFORCATALOGVERSION_MARKMODIFIED));
    }


    public void removeFromWorkflowTemplates(CatalogVersion item, WorkflowTemplate value)
    {
        removeFromWorkflowTemplates(getSession().getSessionContext(), item, value);
    }
}
