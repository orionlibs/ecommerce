package de.hybris.platform.cmscockpit.wizard.cmssite.util;

import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zkplus.spring.SpringUtil;

public class CMSSiteUtils
{
    private static final Logger LOG = Logger.getLogger(CMSSiteUtils.class);
    private static GenericRandomNameProducer uidGenerator;
    private static Object lock = new Object();
    protected static final String CMSITEM_UID_PREFIX = "comp";


    public static GenericRandomNameProducer getGenericRandomNameProducer()
    {
        synchronized(lock)
        {
            if(uidGenerator == null)
            {
                uidGenerator = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
            }
        }
        return uidGenerator;
    }


    public static void populateCmsSite(List sourceTemplates, Set targetCatalogVersions, ContentCatalogModel contentCatalog, CMSSiteModel cmsSiteModel, String homepageName, String homepageLabel)
    {
        for(Iterator<CatalogVersionModel> iter = targetCatalogVersions.iterator(); iter.hasNext(); )
        {
            CatalogVersionModel catversion = iter.next();
            List<PageTemplateModel> clonedTemplates = copyPageTemplatesDeep(sourceTemplates, catversion, contentCatalog);
            PageTemplateModel masterTemplate = clonedTemplates.iterator().next();
            createHomepage(homepageName, homepageLabel, catversion, contentCatalog, cmsSiteModel, masterTemplate);
        }
    }


    public static void createHomepage(String name, String label, CatalogVersionModel catversion, ContentCatalogModel contentCatalog, CMSSiteModel cmsSiteModel, PageTemplateModel masterTemplate)
    {
        ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
        ContentPageModel contentPage = (ContentPageModel)modelService.create("ContentPage");
        contentPage.setUid("contentpage_" + cmsSiteModel.getUid() + catversion.getVersion() + contentCatalog.getName());
        contentPage.setName(name);
        contentPage.setLabel(label);
        contentPage.setHomepage(true);
        contentPage.setCatalogVersion(catversion);
        contentPage.setMasterTemplate(masterTemplate);
        contentPage.setDefaultPage(Boolean.TRUE);
        cmsSiteModel.setStartingPage(contentPage);
        modelService.save(contentPage);
    }


    public static List<PageTemplateModel> copyPageTemplatesDeep(List sourceTemplates, CatalogVersionModel catversion, ContentCatalogModel contentCatalog)
    {
        ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
        List<PageTemplateModel> clonedTemplates = new ArrayList<>();
        for(Object object : sourceTemplates)
        {
            PageTemplateModel template = (PageTemplateModel)object;
            PageTemplateModel clonetemp = (PageTemplateModel)modelService.clone(object);
            clonetemp.setUid(contentCatalog.getId() + "-" + contentCatalog.getId() + "_" + template.getUid());
            clonetemp.setName(template.getName() + " " + template.getName());
            clonetemp.setCatalogVersion(catversion);
            for(ContentSlotNameModel contentSlotNameModel : template.getAvailableContentSlots())
            {
                ContentSlotNameModel targetSlotNameModel = (ContentSlotNameModel)modelService.create(ContentSlotNameModel.class);
                targetSlotNameModel.setName(contentSlotNameModel.getName());
                targetSlotNameModel.setTemplate(clonetemp);
            }
            for(ContentSlotForTemplateModel contentSlotForTemplate : template.getContentSlots())
            {
                ContentSlotForTemplateModel targetSlotForTemplate = (ContentSlotForTemplateModel)modelService.clone(contentSlotForTemplate);
                targetSlotForTemplate.setCatalogVersion(catversion);
                targetSlotForTemplate.setPageTemplate(clonetemp);
                ContentSlotModel contentSlotModel = contentSlotForTemplate.getContentSlot();
                ContentSlotModel clonedContentSlotModel = (ContentSlotModel)modelService.clone(contentSlotModel);
                clonedContentSlotModel.setUid(RandomStringUtils.randomAlphanumeric(4) + "_" + RandomStringUtils.randomAlphanumeric(4));
                clonedContentSlotModel.setCatalogVersion(catversion);
                List<AbstractCMSComponentModel> clonedComponents = new ArrayList<>();
                for(AbstractCMSComponentModel component : contentSlotModel.getCmsComponents())
                {
                    AbstractCMSComponentModel clonedComponent = (AbstractCMSComponentModel)modelService.clone(component);
                    clonedComponent.setUid(
                                    getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT, "comp"));
                    clonedComponent.setSlots(Collections.EMPTY_LIST);
                    clonedComponent.setCatalogVersion(catversion);
                    clonedComponents.add(clonedComponent);
                }
                clonedContentSlotModel.setCmsComponents(clonedComponents);
                targetSlotForTemplate.setContentSlot(clonedContentSlotModel);
                modelService.save(targetSlotForTemplate);
            }
            modelService.save(clonetemp);
            clonedTemplates.add(clonetemp);
        }
        return clonedTemplates;
    }


    public static CatalogVersionSyncJobModel createDefaultSyncJob(String code, CatalogVersionModel catVerStaged, CatalogVersionModel catVerOnline)
    {
        ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
        CatalogVersionSyncJobModel catalogVersionSyncJob = (CatalogVersionSyncJobModel)modelService.create("CatalogVersionSyncJob");
        catalogVersionSyncJob.setCode(code);
        catalogVersionSyncJob.setSourceVersion(catVerStaged);
        catalogVersionSyncJob.setTargetVersion(catVerOnline);
        catalogVersionSyncJob.setRemoveMissingItems(Boolean.TRUE);
        catalogVersionSyncJob.setCreateNewItems(Boolean.TRUE);
        return catalogVersionSyncJob;
    }


    public static void synchronizeCatVersions(CatalogVersionSyncJobModel catalogVersionSyncJob, ModelService modelService)
    {
        SyncItemJob job = setupStoreTemplateSyncJobs(catalogVersionSyncJob, modelService);
        try
        {
            performSynchronization(job);
            LOG.info("\t" + job.getCode() + " - OK");
        }
        catch(Exception e)
        {
            LOG.warn("\t" + (Objects.nonNull(job) ? job.getCode() : "null") + " - FAILED (Reason: " + e.getMessage() + ")", e);
        }
    }


    protected static void performSynchronization(SyncItemJob job)
    {
        SyncItemCronJob cronJob = job.newExecution();
        cronJob.setLogToDatabase(false);
        cronJob.setLogToFile(false);
        cronJob.setForceUpdate(false);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Generating cronjob " + cronJob.getCode() + " to synchronize staged to online version, configuring ...");
        }
        job.configureFullVersionSync(cronJob);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Starting synchronization, this may take a while ...");
        }
        job.perform((CronJob)cronJob, true);
    }


    protected static SyncItemJob setupStoreTemplateSyncJobs(CatalogVersionSyncJobModel syncJobModel, ModelService modelService)
    {
        SyncItemJob syncJob = (SyncItemJob)modelService.getSource(syncJobModel);
        if(syncJob == null)
        {
            LOG.warn("Could not setup catalog version synchronization job. Reason: Synchronization job not found.");
        }
        else
        {
            List<ComposedType> rootTypes = new ArrayList<>(2);
            ComposedType cmsItemType = TypeManager.getInstance().getComposedType(GeneratedCms2Constants.TC.CMSITEM);
            rootTypes.add(cmsItemType);
            rootTypes.add(TypeManager.getInstance().getComposedType(GeneratedCms2Constants.TC.CMSRELATION));
            rootTypes.add(TypeManager.getInstance().getComposedType(Media.class));
            syncJob.setRootTypes(JaloSession.getCurrentSession().getSessionContext(), rootTypes);
            syncJob.setSyncLanguages(JaloSession.getCurrentSession().getSessionContext(),
                            C2LManager.getInstance().getAllLanguages());
            Collection<SyncAttributeDescriptorConfig> syncAttributeConfigs = syncJob.getSyncAttributeConfigurations();
            for(SyncAttributeDescriptorConfig syncAttributeDescriptorConfig : syncAttributeConfigs)
            {
                Type attributeType = syncAttributeDescriptorConfig.getAttributeDescriptor().getAttributeType();
                if((syncAttributeDescriptorConfig.getAttributeDescriptor().getEnclosingType().isAssignableFrom((Type)cmsItemType) && cmsItemType
                                .isAssignableFrom(attributeType)) || (attributeType instanceof CollectionType && cmsItemType
                                .isAssignableFrom(((CollectionType)attributeType).getElementType())))
                {
                    syncAttributeDescriptorConfig.setCopyByValue(true);
                }
            }
        }
        return syncJob;
    }
}
