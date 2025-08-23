package de.hybris.platform.cmscockpit.services.impl;

import bsh.EvalError;
import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.ValueContainerMap;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.ScriptEvaluationService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsCockpitServiceImpl extends AbstractServiceImpl implements CmsCockpitService
{
    public static final Logger LOG = Logger.getLogger(CmsCockpitServiceImpl.class);
    private CMSAdminContentSlotService contentSlotService = null;
    private CMSAdminSiteService adminSiteService = null;
    private GenericRandomNameProducer genericRandomNameProducer = null;
    private LocalizationService localizationService;
    private SystemService systemService;
    private CMSAdminPageService cmsAdminPageService;
    private PermissionCheckingService permissionCheckingService;
    private UserService userService;
    private ScriptEvaluationService scriptEvaluationService;


    public List<String> getAllApprovalStatusCodes()
    {
        List<String> ret = new ArrayList<>();
        List<EnumerationValue> values = EnumerationManager.getInstance().getEnumerationType(GeneratedCms2Constants.TC.CMSAPPROVALSTATUS).getValues();
        for(EnumerationValue enumerationValue : values)
        {
            ret.add(enumerationValue.getCode());
        }
        return ret;
    }


    public String getApprovalStatusName(String code)
    {
        HybrisEnumValue approvalStateFromCode = getApprovalStatusFromCode(code);
        if(approvalStateFromCode != null)
        {
            return TypeTools.getEnumName(approvalStateFromCode);
        }
        return null;
    }


    public HybrisEnumValue getApprovalStatusFromCode(String code)
    {
        PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(GeneratedCms2Constants.TC.ABSTRACTPAGE + ".approvalStatus");
        List<Object> enumerationValues = UISessionUtils.getCurrentSession().getTypeService().getAvailableValues(descriptor);
        for(Object enumerationObject : enumerationValues)
        {
            if(enumerationObject instanceof HybrisEnumValue)
            {
                HybrisEnumValue enumValue = (HybrisEnumValue)enumerationObject;
                if(enumValue.getCode().equals(code))
                {
                    return enumValue;
                }
            }
        }
        return null;
    }


    public void setApprovalStatus(TypedObject typedObject, String code)
    {
        try
        {
            if(typedObject.getObject() instanceof AbstractPageModel)
            {
                AbstractPageModel abstractPage = (AbstractPageModel)typedObject.getObject();
                HybrisEnumValue approvalStatus = getApprovalStatusFromCode(code);
                if(!abstractPage.getApprovalStatus().equals(approvalStatus))
                {
                    ObjectValueContainer itemValueContainer = TypeTools.createValueContainer(typedObject, typedObject
                                                    .getType().getPropertyDescriptors(),
                                    UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), true);
                    ValueContainerMap valueContainerMap = new ValueContainerMap(itemValueContainer, true, null);
                    Map<Object, Object> currentValues = new HashMap<>((Map<?, ?>)valueContainerMap);
                    currentValues.put("approvalStatus", approvalStatus);
                    if(!currentValues.get("approvalStatus").equals(valueContainerMap.get("approvalStatus")))
                    {
                        getScriptEvaluationService().evaluateActivationScripts((ItemModel)abstractPage, currentValues, (Map)valueContainerMap, "save");
                    }
                    getModelService().setAttributeValue(abstractPage, "approvalStatus", approvalStatus);
                }
            }
        }
        catch(EvalError e)
        {
            LOG.error("Could not change approval status of item (Reason: " + e.getMessage() + ").", (Throwable)e);
        }
    }


    public String getApprovalStatusCode(TypedObject item)
    {
        String ret = null;
        if(item.getObject() instanceof AbstractPageModel)
        {
            CmsApprovalStatus approvalStatus = ((AbstractPageModel)item.getObject()).getApprovalStatus();
            if(approvalStatus != null)
            {
                ret = approvalStatus.getCode();
            }
        }
        return ret;
    }


    public boolean isPartOfTemplate(TypedObject cmsComponent)
    {
        return false;
    }


    public boolean isRestricted(TypedObject cmsComponent)
    {
        try
        {
            AbstractCMSComponentModel componentModel = (AbstractCMSComponentModel)cmsComponent.getObject();
            return !componentModel.getRestrictions().isEmpty();
        }
        catch(Exception e)
        {
            LOG.info(e);
            return false;
        }
    }


    public TypedObject clonePageFirstLevel(TypedObject page, String name)
    {
        AbstractPageModel ret = null;
        try
        {
            if(page != null && page.getObject() instanceof AbstractPageModel)
            {
                AbstractPageModel pageModel = (AbstractPageModel)page.getObject();
                ret = (AbstractPageModel)getModelService().create(pageModel.getClass());
                Preconditions.checkArgument((ret.getClass() == pageModel.getClass()));
                ret.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.ABSTRACTPAGE, "page"));
                ret.setDefaultPage(Boolean.FALSE);
                ret.setOnlyOneRestrictionMustApply(pageModel.isOnlyOneRestrictionMustApply());
                ret.setApprovalStatus(CmsApprovalStatus.CHECK);
                ret.setMasterTemplate(pageModel.getMasterTemplate());
                ret.setCatalogVersion(pageModel.getCatalogVersion());
                ret.setRestrictions(pageModel.getRestrictions());
                if(name == null)
                {
                    ret.setName(pageModel.getName());
                }
                else
                {
                    ret.setName(name);
                }
                for(String iso : getSystemService().getAvailableLanguageIsos())
                {
                    Locale loc = getLocalizationService().getLocaleByString(iso);
                    ret.setTitle(pageModel.getTitle(loc), loc);
                }
                cloneContentSlots(pageModel, ret);
                if(ret instanceof ContentPageModel)
                {
                    ((ContentPageModel)ret).setLabel(((ContentPageModel)pageModel).getLabel());
                }
            }
            if(ret != null)
            {
                getModelService().save(ret);
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
        return this.typeService.wrapItem(ret);
    }


    protected void cloneContentSlots(AbstractPageModel source, AbstractPageModel target)
    {
        List<ContentSlotForPageModel> contentSlots = source.getContentSlots();
        for(ContentSlotForPageModel contentSlotForPageModel : contentSlots)
        {
            ContentSlotModel original = contentSlotForPageModel.getContentSlot();
            ContentSlotModel dollySlot = cloneContentSlot(original);
            getModelService().save(dollySlot);
            ContentSlotForPageModel dollyRelation = cloneContentSlotForPageRelation(contentSlotForPageModel);
            dollyRelation.setContentSlot(dollySlot);
            dollyRelation.setPage(target);
            getModelService().save(dollyRelation);
        }
    }


    protected ContentSlotForPageModel cloneContentSlotForPageRelation(ContentSlotForPageModel original)
    {
        ContentSlotForPageModel ret = (ContentSlotForPageModel)getModelService().create(ContentSlotForPageModel.class);
        ret.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOTFORPAGE, "slotRel"));
        ret.setCatalogVersion(original.getCatalogVersion());
        ret.setPosition(original.getPosition());
        return ret;
    }


    protected ContentSlotModel cloneContentSlot(ContentSlotModel original)
    {
        ContentSlotModel csModel = (ContentSlotModel)getModelService().create(ContentSlotModel.class);
        csModel.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT, "slot"));
        csModel.setActive(original.getActive());
        csModel.setActiveFrom(original.getActiveFrom());
        csModel.setActiveUntil(original.getActiveUntil());
        csModel.setCatalogVersion(original.getCatalogVersion());
        csModel.setCmsComponents(new ArrayList(original.getCmsComponents()));
        csModel.setCurrentPosition(original.getCurrentPosition());
        csModel.setName(original.getName());
        return csModel;
    }


    public String createRestrictionTooltip(TypedObject restriction)
    {
        String ret = null;
        try
        {
            AbstractRestrictionModel restrictionModel = (AbstractRestrictionModel)restriction.getObject();
            ret = restrictionModel.getDescription();
        }
        catch(Exception e)
        {
            LOG.info(e);
        }
        return ret;
    }


    public CMSAdminContentSlotService getCMSAdminContentSlotService()
    {
        if(this.contentSlotService == null)
        {
            this.contentSlotService = (CMSAdminContentSlotService)SpringUtil.getBean("cmsAdminContentSlotService");
        }
        return this.contentSlotService;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return this.genericRandomNameProducer;
    }


    public CMSAdminSiteService getAdminSiteService()
    {
        if(this.adminSiteService == null)
        {
            this.adminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.adminSiteService;
    }


    public Collection<CMSSiteModel> getWebsites()
    {
        return getSites();
    }


    public List<AbstractPageModel> getRecentlyEditedPages(int count)
    {
        if(UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn("AbstractPage", "create"))
        {
            try
            {
                String query = "SELECT {pk} FROM {abstractPage} ORDER BY {modifiedTime} DESC";
                FlexibleSearchService flexibleSearchService = (FlexibleSearchService)SpringUtil.getBean("flexibleSearchService");
                FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery("SELECT {pk} FROM {abstractPage} ORDER BY {modifiedTime} DESC");
                flexibleSearchQuery.setStart(0);
                flexibleSearchQuery.setCount(count);
                SearchResult<AbstractPageModel> search = flexibleSearchService.search(flexibleSearchQuery);
                return search.getResult();
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return Collections.EMPTY_LIST;
    }


    public void setGenericRandomNameProducer(GenericRandomNameProducer genericRandomNameProducer)
    {
        this.genericRandomNameProducer = genericRandomNameProducer;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public void setLocalizationService(LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }


    protected LocalizationService getLocalizationService()
    {
        return this.localizationService;
    }


    public List<TypedObject> getPersonalizedPages(TypedObject page)
    {
        return getPersonalizedPages(page, page);
    }


    public List<TypedObject> getPersonalizedPages(TypedObject page, TypedObject excludePage)
    {
        List<TypedObject> ret = new ArrayList<>();
        CatalogVersionModel catVer = null;
        if(page.getObject() instanceof AbstractPageModel)
        {
            catVer = ((AbstractPageModel)page.getObject()).getCatalogVersion();
        }
        else
        {
            LOG.error("Could not get personalized pages since item '" + page + "' is not of type 'AbstractPageModel'");
            return Collections.EMPTY_LIST;
        }
        Collection<AbstractPageModel> allPagesWithSameType = getCmsAdminPageService().getAllPagesByType(page.getType().getCode(), catVer);
        if(excludePage != null)
        {
            allPagesWithSameType.remove(excludePage.getObject());
        }
        if(page.getObject() instanceof ContentPageModel)
        {
            String labelOrId = ((ContentPageModel)page.getObject()).getLabelOrId();
            if(labelOrId != null)
            {
                for(AbstractPageModel abstractPageModel : allPagesWithSameType)
                {
                    if(labelOrId.equals(((ContentPageModel)abstractPageModel).getLabelOrId()))
                    {
                        ret.add(getTypeService().wrapItem(abstractPageModel));
                    }
                }
            }
        }
        else
        {
            ret.addAll(getTypeService().wrapItems(allPagesWithSameType));
        }
        return ret;
    }


    public TypedObject getDefaultPage(TypedObject originalPage)
    {
        for(TypedObject page : getPersonalizedPages(originalPage, null))
        {
            if(page.getObject() instanceof AbstractPageModel)
            {
                if(Boolean.TRUE.equals(((AbstractPageModel)page.getObject()).getDefaultPage()))
                {
                    return page;
                }
                continue;
            }
            LOG.error("" + page + " is not of type 'AbstractPageModel'.");
            return null;
        }
        return null;
    }


    @Required
    public void setCmsAdminPageService(CMSAdminPageService cmsAdminPageService)
    {
        this.cmsAdminPageService = cmsAdminPageService;
    }


    protected CMSAdminPageService getCmsAdminPageService()
    {
        return this.cmsAdminPageService;
    }


    public Collection<CMSSiteModel> getSites()
    {
        if(getPermissionCheckingService()
                        .checkTypePermission("CMSSite", (PrincipalModel)getUserService().getCurrentUser(), "read")
                        .isGranted())
        {
            return getAdminSiteService().getSites();
        }
        return Collections.emptyList();
    }


    public PermissionCheckingService getPermissionCheckingService()
    {
        return this.permissionCheckingService;
    }


    public void setPermissionCheckingService(PermissionCheckingService permissionCheckingService)
    {
        this.permissionCheckingService = permissionCheckingService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setScriptEvaluationService(ScriptEvaluationService scriptEvaluationService)
    {
        this.scriptEvaluationService = scriptEvaluationService;
    }


    protected ScriptEvaluationService getScriptEvaluationService()
    {
        return this.scriptEvaluationService;
    }
}
