package de.hybris.platform.cms2.systemsetup;

import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSyncSearchRestrictionService;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "cms2")
public class CMS2SystemSetup
{
    private static final Logger LOG = Logger.getLogger(CMS2SystemSetup.class);
    private static final String SELECT_PK_FROM = "SELECT {pk} FROM {";
    private static final String SYNCHRONIZATION_BLOCKED_IS_NULL_OR_FALSE = " ( {item:synchronizationBlocked} IS NULL OR {item:synchronizationBlocked} = 0 )";
    @Resource(name = "processCodeGenerator")
    private PersistentKeyGenerator processCodeGenerator;
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private UserService userService;
    private TypeService typeService;
    private CMSSyncSearchRestrictionService cmsSyncSearchRestrictionService;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createNumberSeriesForTypes()
    {
        createNumberSeries("CMSItem");
        createNumberSeries("AbstractPage");
        createNumberSeries("ContentSlot");
        createNumberSeries("AbstractCMSComponent");
        createNumberSeries("ContentSlotForTemplate");
        createNumberSeries("ContentSlotForPage");
        createNumberSeries("CMSSite");
        createNumberSeries("ContentCatalog");
        createNumberSeries("AbstractRestriction");
        createNumberSeries("CMSNavigationNode");
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void migrateCMSNavigationEntries()
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT {pk} FROM {").append("CMSNavigationEntry").append(" AS ne} WHERE {")
                        .append("uid").append("} IS NULL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
        SearchResult<CMSNavigationEntryModel> result = getFlexibleSearchService().search(query);
        List<CMSNavigationEntryModel> entries = result.getResult();
        createNumberSeries("CMSNavigationEntry");
        for(CMSNavigationEntryModel entry : entries)
        {
            entry.setUid(String.valueOf(this.processCodeGenerator.generate()));
            if(entry.getNavigationNode() != null)
            {
                entry.setCatalogVersion(entry.getNavigationNode().getCatalogVersion());
            }
            getModelService().save(entry);
        }
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void initializeDefaultPageStatus()
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {pk} FROM {").append("AbstractPage").append("} WHERE {")
                        .append("pageStatus").append("} IS NULL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
        SearchResult<AbstractPageModel> result = getFlexibleSearchService().search(query);
        result.getResult().forEach(pageModel -> {
            pageModel.setPageStatus(CmsPageStatus.ACTIVE);
            getModelService().save(pageModel);
        });
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL, patch = true, required = true)
    public void createSyncSearchRestrictions()
    {
        EnumerationValueModel approvedStatus = getTypeService().getEnumerationValue("CmsApprovalStatus", CmsApprovalStatus.APPROVED
                        .getCode());
        getCmsSyncSearchRestrictionService()
                        .createCmsSyncSearchRestriction("Sync_Only_Approved_Pages_Restriction", AbstractPageModel.class, " {item:approvalStatus}=" + approvedStatus
                                        .getPk());
        getCmsSyncSearchRestrictionService()
                        .createCmsSyncSearchRestriction("Sync_Only_Approved_Slots_For_Page_Restriction", ContentSlotForPageModel.class, " EXISTS ({{ SELECT 1 FROM { ContentSlotForPage AS slot4page JOIN AbstractPage AS p ON {p:pk} = {slot4page:page} } WHERE {item:pk} = {slot4page:pk} }})");
        getCmsSyncSearchRestrictionService()
                        .createCmsSyncSearchRestriction("Sync_Only_Approved_Slots_Restriction", ContentSlotModel.class, " ( {item:synchronizationBlocked} IS NULL OR {item:synchronizationBlocked} = 0 )");
        getCmsSyncSearchRestrictionService()
                        .createCmsSyncSearchRestriction("Sync_Only_Approved_Components_Restriction", AbstractCMSComponentModel.class, " ( {item:synchronizationBlocked} IS NULL OR {item:synchronizationBlocked} = 0 )");
        getCmsSyncSearchRestrictionService()
                        .createCmsSyncSearchRestriction("Sync_Only_Approved_Restrictions_Restriction", AbstractRestrictionModel.class, " ( {item:synchronizationBlocked} IS NULL OR {item:synchronizationBlocked} = 0 )");
    }


    protected void createNumberSeries(String key)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating NumberSeries for " + key);
        }
        this.processCodeGenerator.setKey(key);
        this.processCodeGenerator.generate();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected CMSSyncSearchRestrictionService getCmsSyncSearchRestrictionService()
    {
        return this.cmsSyncSearchRestrictionService;
    }


    @Required
    public void setCmsSyncSearchRestrictionService(CMSSyncSearchRestrictionService cmsSyncSearchRestrictionService)
    {
        this.cmsSyncSearchRestrictionService = cmsSyncSearchRestrictionService;
    }
}
