package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPreviewTicketDao;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSPreviewService extends AbstractCMSService implements CMSPreviewService
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSPreviewService.class.getName());
    private CMSPreviewTicketDao cmsPreviewTicketDao;
    private CMSSiteService cmsSiteService;
    private TaskService taskService;
    private ObjectFactory<PagePreviewCriteriaData> cmsPagePreviewCriteriaDataFactory;


    public PreviewDataModel clonePreviewData(PreviewDataModel original)
    {
        if(original == null)
        {
            return null;
        }
        ModelService modelService = getModelService();
        if(!modelService.isNew(original) && !modelService.isRemoved(original))
        {
            modelService.refresh(original);
        }
        return (PreviewDataModel)modelService.clone(original);
    }


    public CMSPreviewTicketModel createPreviewTicket(PreviewDataModel previewData)
    {
        CMSPreviewTicketModel ticket = new CMSPreviewTicketModel();
        ticket.setId(generateTicketId());
        ticket.setPreviewData(previewData);
        getModelService().save(ticket);
        TaskModel task = (TaskModel)getModelService().create(TaskModel.class);
        task.setRunnerBean("previewTicketRemoveTaskRunner");
        task.setContextItem((ItemModel)ticket);
        task.setExecutionDate(new Date(System.currentTimeMillis() + 900000L));
        getTaskService().scheduleTask(task);
        return ticket;
    }


    public Collection<CatalogModel> getEditableCatalogs(CMSSiteModel cmsSiteModel, CatalogVersionModel selectedCatalogVersion)
    {
        Collection<CatalogModel> editableCatalogs = new HashSet<>(getCmsSiteService().getAllCatalogs(cmsSiteModel));
        editableCatalogs.removeAll(cmsSiteModel.getClassificationCatalogs());
        if(selectedCatalogVersion != null)
        {
            CatalogModel currentCatalogModel = selectedCatalogVersion.getCatalog();
            editableCatalogs.remove(currentCatalogModel);
        }
        return editableCatalogs;
    }


    public CMSPreviewTicketModel getPreviewTicket(String ticketId)
    {
        CMSPreviewTicketModel ret = null;
        if(StringUtils.isNotBlank(ticketId))
        {
            List<CMSPreviewTicketModel> previewTickets = getCmsPreviewTicketDao().findPreviewTicketsForId(ticketId);
            if(CollectionUtils.isNotEmpty(previewTickets))
            {
                ret = previewTickets.get(0);
            }
        }
        return ret;
    }


    public String storePreviewTicket(CMSPreviewTicketModel ticket)
    {
        String id = null;
        if(ticket == null || getModelService().isRemoved(ticket))
        {
            LOG.info("Preview ticket could not be stored. Reason: Specified ticket was not valid.");
        }
        else
        {
            getModelService().save(ticket);
            id = ticket.getId();
        }
        return id;
    }


    public PagePreviewCriteriaData getPagePreviewCriteria()
    {
        PagePreviewCriteriaData pagePreviewCriteriaData = (PagePreviewCriteriaData)getCmsPagePreviewCriteriaDataFactory().getObject();
        String previewTicketId = (String)getSessionService().getAttribute("cmsTicketId");
        if(StringUtils.isNotBlank(previewTicketId))
        {
            CMSPreviewTicketModel previewTicket = getPreviewTicket(previewTicketId);
            if(previewTicket != null && previewTicket.getPreviewData() != null && previewTicket
                            .getPreviewData().getVersion() != null)
            {
                pagePreviewCriteriaData.setVersionUid(previewTicket.getPreviewData().getVersion().getUid());
            }
        }
        return pagePreviewCriteriaData;
    }


    public CatalogVersionModel getPreviewContentCatalogVersion()
    {
        String previewTicketId = (String)getSessionService().getAttribute("cmsTicketId");
        if(StringUtils.isNotBlank(previewTicketId))
        {
            CMSPreviewTicketModel previewTicket = getPreviewTicket(previewTicketId);
            if(previewTicket != null && previewTicket.getPreviewData() != null)
            {
                return previewTicket.getPreviewData().getPreviewContentCatalogVersion();
            }
        }
        return null;
    }


    public boolean isVersioningPreview()
    {
        return (getPagePreviewCriteria().getVersionUid() != null);
    }


    protected String generateTicketId()
    {
        return PK.createUUIDPK(23).toString() + PK.createUUIDPK(23).toString();
    }


    protected CMSPreviewTicketDao getCmsPreviewTicketDao()
    {
        return this.cmsPreviewTicketDao;
    }


    @Required
    public void setCmsPreviewTicketDao(CMSPreviewTicketDao cmsPreviewTicketDao)
    {
        this.cmsPreviewTicketDao = cmsPreviewTicketDao;
    }


    protected CMSSiteService getCmsSiteService()
    {
        return this.cmsSiteService;
    }


    @Required
    public void setCmsSiteService(CMSSiteService cmsSiteService)
    {
        this.cmsSiteService = cmsSiteService;
    }


    protected TaskService getTaskService()
    {
        return this.taskService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }


    public ObjectFactory<PagePreviewCriteriaData> getCmsPagePreviewCriteriaDataFactory()
    {
        return this.cmsPagePreviewCriteriaDataFactory;
    }


    @Required
    public void setCmsPagePreviewCriteriaDataFactory(ObjectFactory<PagePreviewCriteriaData> cmsPagePreviewCriteriaDataFactory)
    {
        this.cmsPagePreviewCriteriaDataFactory = cmsPagePreviewCriteriaDataFactory;
    }
}
