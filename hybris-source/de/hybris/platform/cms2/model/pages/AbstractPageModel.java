package de.hybris.platform.cms2.model.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsItemDisplayStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.enums.CmsRobotTag;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AbstractPageModel extends CMSItemModel
{
    public static final String _TYPECODE = "AbstractPage";
    public static final String _ABSTRACTPAGE2USERRELATION = "AbstractPage2UserRelation";
    public static final String _LOCALIZEDPAGERELATION = "LocalizedPageRelation";
    public static final String APPROVALSTATUS = "approvalStatus";
    public static final String DISPLAYSTATUS = "displayStatus";
    public static final String PAGESTATUS = "pageStatus";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String MASTERTEMPLATE = "masterTemplate";
    public static final String DEFAULTPAGE = "defaultPage";
    public static final String ONLYONERESTRICTIONMUSTAPPLY = "onlyOneRestrictionMustApply";
    public static final String PREVIEWIMAGE = "previewImage";
    public static final String CONTENTSLOTS = "contentSlots";
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String MISSINGCONTENTSLOTS = "missingContentSlots";
    public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
    public static final String VIEW = "view";
    public static final String NAVIGATIONNODELIST = "navigationNodeList";
    public static final String COPYTOCATALOGSDISABLED = "copyToCatalogsDisabled";
    public static final String ROBOTTAG = "robotTag";
    public static final String RESTRICTIONS = "restrictions";
    public static final String LOCKEDBY = "lockedBy";
    public static final String ORIGINALPAGE = "originalPage";
    public static final String LOCALIZEDPAGES = "localizedPages";


    public AbstractPageModel()
    {
    }


    public AbstractPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
    public CmsApprovalStatus getApprovalStatus()
    {
        return (CmsApprovalStatus)getPersistenceContext().getPropertyValue("approvalStatus");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "availableContentSlots", type = Accessor.Type.GETTER)
    public String getAvailableContentSlots()
    {
        return (String)getPersistenceContext().getPropertyValue("availableContentSlots");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "contentSlots", type = Accessor.Type.GETTER)
    public List<ContentSlotForPageModel> getContentSlots()
    {
        return (List<ContentSlotForPageModel>)getPersistenceContext().getPropertyValue("contentSlots");
    }


    @Accessor(qualifier = "defaultPage", type = Accessor.Type.GETTER)
    public Boolean getDefaultPage()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("defaultPage");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "displayStatus", type = Accessor.Type.GETTER)
    public CmsItemDisplayStatus getDisplayStatus()
    {
        return (CmsItemDisplayStatus)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "displayStatus");
    }


    @Accessor(qualifier = "localizedPages", type = Accessor.Type.GETTER)
    public Collection<AbstractPageModel> getLocalizedPages()
    {
        return (Collection<AbstractPageModel>)getPersistenceContext().getPropertyValue("localizedPages");
    }


    @Accessor(qualifier = "lockedBy", type = Accessor.Type.GETTER)
    public UserModel getLockedBy()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("lockedBy");
    }


    @Accessor(qualifier = "masterTemplate", type = Accessor.Type.GETTER)
    public PageTemplateModel getMasterTemplate()
    {
        return (PageTemplateModel)getPersistenceContext().getPropertyValue("masterTemplate");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "missingContentSlots", type = Accessor.Type.GETTER)
    public String getMissingContentSlots()
    {
        return (String)getPersistenceContext().getPropertyValue("missingContentSlots");
    }


    @Accessor(qualifier = "navigationNodeList", type = Accessor.Type.GETTER)
    public List<CMSNavigationNodeModel> getNavigationNodeList()
    {
        return (List<CMSNavigationNodeModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "navigationNodeList");
    }


    @Accessor(qualifier = "originalPage", type = Accessor.Type.GETTER)
    public AbstractPageModel getOriginalPage()
    {
        return (AbstractPageModel)getPersistenceContext().getPropertyValue("originalPage");
    }


    @Accessor(qualifier = "pageStatus", type = Accessor.Type.GETTER)
    public CmsPageStatus getPageStatus()
    {
        return (CmsPageStatus)getPersistenceContext().getPropertyValue("pageStatus");
    }


    @Accessor(qualifier = "previewImage", type = Accessor.Type.GETTER)
    public MediaModel getPreviewImage()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("previewImage");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public List<AbstractRestrictionModel> getRestrictions()
    {
        return (List<AbstractRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "robotTag", type = Accessor.Type.GETTER)
    public CmsRobotTag getRobotTag()
    {
        return (CmsRobotTag)getPersistenceContext().getPropertyValue("robotTag");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return getTitle(null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("title", loc);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType()
    {
        return getType(null);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public String getType(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("type", loc);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public String getTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("typeCode");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "view", type = Accessor.Type.GETTER)
    public String getView()
    {
        return (String)getPersistenceContext().getPropertyValue("view");
    }


    @Accessor(qualifier = "copyToCatalogsDisabled", type = Accessor.Type.GETTER)
    public boolean isCopyToCatalogsDisabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("copyToCatalogsDisabled"));
    }


    @Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.GETTER)
    public boolean isOnlyOneRestrictionMustApply()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("onlyOneRestrictionMustApply"));
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
    public void setApprovalStatus(CmsApprovalStatus value)
    {
        getPersistenceContext().setPropertyValue("approvalStatus", value);
    }


    @Accessor(qualifier = "copyToCatalogsDisabled", type = Accessor.Type.SETTER)
    public void setCopyToCatalogsDisabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("copyToCatalogsDisabled", toObject(value));
    }


    @Accessor(qualifier = "defaultPage", type = Accessor.Type.SETTER)
    public void setDefaultPage(Boolean value)
    {
        getPersistenceContext().setPropertyValue("defaultPage", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "localizedPages", type = Accessor.Type.SETTER)
    public void setLocalizedPages(Collection<AbstractPageModel> value)
    {
        getPersistenceContext().setPropertyValue("localizedPages", value);
    }


    @Accessor(qualifier = "lockedBy", type = Accessor.Type.SETTER)
    public void setLockedBy(UserModel value)
    {
        getPersistenceContext().setPropertyValue("lockedBy", value);
    }


    @Accessor(qualifier = "masterTemplate", type = Accessor.Type.SETTER)
    public void setMasterTemplate(PageTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("masterTemplate", value);
    }


    @Accessor(qualifier = "navigationNodeList", type = Accessor.Type.SETTER)
    public void setNavigationNodeList(List<CMSNavigationNodeModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "navigationNodeList", value);
    }


    @Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.SETTER)
    public void setOnlyOneRestrictionMustApply(boolean value)
    {
        getPersistenceContext().setPropertyValue("onlyOneRestrictionMustApply", toObject(value));
    }


    @Accessor(qualifier = "originalPage", type = Accessor.Type.SETTER)
    public void setOriginalPage(AbstractPageModel value)
    {
        getPersistenceContext().setPropertyValue("originalPage", value);
    }


    @Accessor(qualifier = "pageStatus", type = Accessor.Type.SETTER)
    public void setPageStatus(CmsPageStatus value)
    {
        getPersistenceContext().setPropertyValue("pageStatus", value);
    }


    @Accessor(qualifier = "previewImage", type = Accessor.Type.SETTER)
    public void setPreviewImage(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("previewImage", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(List<AbstractRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "robotTag", type = Accessor.Type.SETTER)
    public void setRobotTag(CmsRobotTag value)
    {
        getPersistenceContext().setPropertyValue("robotTag", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        setTitle(value, null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("title", loc, value);
    }
}
