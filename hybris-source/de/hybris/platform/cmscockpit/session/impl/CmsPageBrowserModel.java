package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageContentBrowser;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaEditComponentFactory;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaPersonalizeComponentFactory;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaPreviewComponentFactory;
import de.hybris.platform.cmscockpit.components.listsectionbrowser.impl.CMSStructListBrowserSectionRenderer;
import de.hybris.platform.cmscockpit.components.listsectionbrowser.impl.CmsContentSlotListBrowserSectionRenderer;
import de.hybris.platform.cmscockpit.components.listsectionbrowser.impl.CmsListBrowserSectionRenderer;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.listview.impl.SectionTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractSectionBrowserModel;
import de.hybris.platform.cockpit.session.impl.ListBrowserSectionRenderer;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsPageBrowserModel extends AbstractSectionBrowserModel implements CockpitEventAcceptor
{
    private static final Logger LOG = Logger.getLogger(CmsPageBrowserModel.class);
    private static final String LIST_CONTENT_ELEMENT_SECTION_CONFIG = "listContentElementSection";
    protected CMSAdminSiteService cmsAdminSiteService;
    protected CmsCockpitService cmsCockpitService;
    protected ModelService modelService;
    protected CMSAdminComponentService cmsAdminComponentService;
    protected CMSAdminContentSlotService cmsAdminContentSlotService;
    private TypeService typeService;
    private TypedObject page;
    private ListBrowserSectionModel contentSlotSection = null;
    private ListBrowserSectionModel contentElementSection = null;
    private ListBrowserSectionModel simpleElementSection = null;
    private ContentEditorBrowserSectionModel contentEditorSection = null;
    private GenericRandomNameProducer genericRandomNameProducer;
    private List<MainAreaComponentFactory> viewModes = null;
    protected Map<TypedObject, Set<TypedObject>> updateNotificationMap = new HashMap<>();
    private SectionModelListener drilldownViewListener;


    public CmsPageBrowserModel(CMSAdminSiteService cmsAdminSiteService, CmsCockpitService cmsCockpitService, ModelService modelService, CMSAdminComponentService cmsAdminComponentService, CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
        this.cmsCockpitService = cmsCockpitService;
        this.modelService = modelService;
        this.cmsAdminComponentService = cmsAdminComponentService;
        this.cmsAdminContentSlotService = cmsAdminContentSlotService;
        this.viewMode = "EDIT";
    }


    public List<TypedObject> getAllPagesWithSameLabelOrType()
    {
        return this.cmsCockpitService.getPersonalizedPages(getCurrentPageObject());
    }


    public List<MainAreaComponentFactory> getAvailableViewModes()
    {
        if(this.viewModes == null)
        {
            this.viewModes = new ArrayList<>();
            this.viewModes.add(newCmsPageMainAreaEditComponentFactory());
            this.viewModes.add(newCmsPageMainAreaPreviewComponentFactory());
            this.viewModes.add(newCmsPageMainAreaPersonalizeComponentFactory());
        }
        return this.viewModes;
    }


    public Object clone() throws CloneNotSupportedException
    {
        CmsPageBrowserModel browserModel = newCmsPageBrowserModel();
        browserModel.setCurrentPageObject(getCurrentPageObject());
        browserModel.createProperViewModel();
        browserModel.setViewMode(getViewMode());
        return browserModel;
    }


    protected CmsPageBrowserModel newCmsPageBrowserModel()
    {
        return (CmsPageBrowserModel)SpringUtil.getBean("cmsPageBrowserModel");
    }


    protected CmsPageMainAreaEditComponentFactory newCmsPageMainAreaEditComponentFactory()
    {
        return new CmsPageMainAreaEditComponentFactory();
    }


    protected CmsPageMainAreaPreviewComponentFactory newCmsPageMainAreaPreviewComponentFactory()
    {
        return new CmsPageMainAreaPreviewComponentFactory(getCurrentPageObject());
    }


    protected CmsPageMainAreaPersonalizeComponentFactory newCmsPageMainAreaPersonalizeComponentFactory()
    {
        return new CmsPageMainAreaPersonalizeComponentFactory();
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new CmsPageContentBrowser();
    }


    public void initialize()
    {
        if(this.viewMode == null)
        {
            try
            {
                this.viewMode = ((MainAreaComponentFactory)getAvailableViewModes().iterator().next()).getViewModeID();
            }
            catch(Exception e)
            {
                LOG.error("Could not set viewmode", e);
            }
        }
        createProperViewModel();
    }


    public void setArea(UIBrowserArea area)
    {
        super.setArea(area);
        if(area != null)
        {
            area.getPerspective().addCockpitEventAcceptor(this);
        }
    }


    public void setCurrentPageObject(TypedObject page)
    {
        this.page = page;
    }


    public TypedObject getCurrentPageObject()
    {
        return this.page;
    }


    public ListBrowserSectionModel getContentSlotSection()
    {
        return this.contentSlotSection;
    }


    public ListBrowserSectionModel getContentElementSection()
    {
        return this.contentElementSection;
    }


    public ListBrowserSectionModel getSimpleElementSection()
    {
        return this.simpleElementSection;
    }


    protected List<ContentSlotModel> getContentSlotsForCurrentPage()
    {
        List<ContentSlotModel> contentSlots = null;
        if(getCurrentCmsPage() == null)
        {
            LOG.warn("Can not retrieve content slots since the current page is null.");
            contentSlots = Collections.emptyList();
        }
        else
        {
            Collection<ContentSlotData> contentSlotsForPage = this.cmsAdminContentSlotService.getContentSlotsForPage(getCurrentCmsPage());
            if(contentSlotsForPage == null)
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("No content slots found for the current page.");
                    contentSlots = Collections.emptyList();
                }
            }
            else
            {
                contentSlots = new ArrayList<>(contentSlotsForPage.size());
                if(!contentSlotsForPage.isEmpty())
                {
                    for(ContentSlotData slotData : contentSlotsForPage)
                    {
                        contentSlots.add(slotData.getContentSlot());
                    }
                }
            }
        }
        return contentSlots;
    }


    public List<ContentSlotNameModel> getMissingSlotsForCurrentPage()
    {
        if(getCurrentCmsPage() == null || getCurrentCmsPage().getMasterTemplate() == null)
        {
            LOG.warn("It is not possible to retrive a available slots for current page!");
        }
        List<ContentSlotNameModel> alreadyCreated = new ArrayList<>();
        List<ContentSlotNameModel> availableSlots = new ArrayList<>(getCurrentCmsPage().getMasterTemplate().getAvailableContentSlots());
        for(ContentSlotNameModel availableContentSlotName : availableSlots)
        {
            for(ContentSlotModel existingContentSlot : getContentSlotsForCurrentPage())
            {
                if(availableContentSlotName.getName().equals(existingContentSlot.getCurrentPosition()))
                {
                    alreadyCreated.add(availableContentSlotName);
                }
            }
        }
        availableSlots.removeAll(alreadyCreated);
        return availableSlots;
    }


    public List<ContentSlotNameModel> getAvailableSlotsForCurrentPage()
    {
        List<ContentSlotNameModel> ret = new ArrayList<>();
        if(getCurrentCmsPage() != null && getCurrentCmsPage().getMasterTemplate() != null)
        {
            for(ContentSlotNameModel element : getCurrentCmsPage().getMasterTemplate().getAvailableContentSlots())
            {
                ret.add(element);
            }
        }
        return ret;
    }


    public ContentSlotModel createSlotContentForCurrentPage(String position)
    {
        ContentSlotModel contentSlotModel = getContentaSlotAtPositionForPage(getCurrentPageObject(), position);
        if(contentSlotModel == null)
        {
            contentSlotModel = (ContentSlotModel)this.modelService.create(GeneratedCms2Constants.TC.CONTENTSLOT);
            contentSlotModel.setCurrentPosition(position);
            contentSlotModel.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT, "cs"));
            contentSlotModel.setName(computeReadableName(position));
            contentSlotModel.setCatalogVersion(this.cmsAdminSiteService.getActiveCatalogVersion());
        }
        return contentSlotModel;
    }


    public ContentSlotModel createContentSlotForPage(String position)
    {
        ContentSlotModel contentSlotModel = (ContentSlotModel)this.modelService.create(GeneratedCms2Constants.TC.CONTENTSLOT);
        contentSlotModel.setCurrentPosition(position);
        contentSlotModel.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT, "cs"));
        contentSlotModel.setName(computeReadableName(position));
        contentSlotModel.setActive(Boolean.TRUE);
        contentSlotModel.setCatalogVersion(this.cmsAdminSiteService.getActiveCatalogVersion());
        ContentSlotForPageModel relation = (ContentSlotForPageModel)this.modelService.create(ContentSlotForPageModel.class);
        relation.setPosition(position);
        relation.setPage(getCurrentCmsPage());
        relation.setContentSlot(contentSlotModel);
        relation.setCatalogVersion(this.cmsAdminSiteService.getActiveCatalogVersion());
        this.modelService.saveAll(new Object[] {contentSlotModel, relation});
        UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
        for(BrowserModel visBrowser : getArea().getVisibleBrowsers())
        {
            visBrowser.updateItems();
        }
        return contentSlotModel;
    }


    protected ContentSlotModel getContentaSlotAtPositionForPage(TypedObject wrappedPageModel, String position)
    {
        ContentSlotModel ret = null;
        if(StringUtils.isBlank(position))
        {
            return ret;
        }
        for(ContentSlotData contentSlotData : this.cmsAdminContentSlotService
                        .getContentSlotsForPage((AbstractPageModel)TypeTools.container2Item(getTypeService(), wrappedPageModel)))
        {
            if(position.equals(contentSlotData.getPosition()))
            {
                ret = contentSlotData.getContentSlot();
            }
        }
        return ret;
    }


    protected String computeReadableName(String position)
    {
        return "Default " + position + " for " + getCurrentCmsPage().getName();
    }


    public void removeComponentFromSlot(TypedObject parentSlot, TypedObject component)
    {
        if(parentSlot == null || component == null)
        {
            LOG.warn("Coudld not remove a component from parent content slot!");
            return;
        }
        if(parentSlot.getObject() instanceof ContentSlotModel && component.getObject() instanceof AbstractCMSComponentModel)
        {
            this.cmsAdminComponentService.removeCMSComponentFromContentSlot((AbstractCMSComponentModel)component.getObject(), (ContentSlotModel)parentSlot
                            .getObject());
        }
        else
        {
            LOG.warn("Couldn not remove a compoenent from content slot, ");
        }
    }


    public void deleteSlotContentForCurrentPage(String uid)
    {
        try
        {
            this.cmsAdminContentSlotService.deleteContentSlot(uid);
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().update();
            for(BrowserModel visBrowser : getArea().getVisibleBrowsers())
            {
                visBrowser.updateItems();
            }
        }
        catch(CMSItemNotFoundException e)
        {
            LOG.error("It is not possible to remove content slot (id =" + uid + ") for current page!");
        }
    }


    public boolean isStructViewAvailable()
    {
        boolean ret = false;
        PageTemplateModel pageTemplateModel = getCurrentCmsPage().getMasterTemplate();
        if(pageTemplateModel != null && StringUtils.isNotEmpty(pageTemplateModel.getVelocityTemplate()))
        {
            LOG.info("Structure view is available for current page.");
            ret = true;
        }
        return ret;
    }


    public boolean isAssignedToPage(ContentSlotModel contentSlot)
    {
        if(contentSlot == null)
        {
            return false;
        }
        return this.cmsAdminContentSlotService.hasRelations(contentSlot);
    }


    public void updateItems()
    {
        createProperViewModel();
        fireChanged();
    }


    public String getExtendedLabel()
    {
        String ret = null;
        try
        {
            CatalogVersionModel catalogVersion = getCurrentCmsPage().getCatalogVersion();
            CatalogModel catalog = catalogVersion.getCatalog();
            ret = catalog.getName() + " >> " + catalog.getName() + " >> " + catalogVersion.getVersion();
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieve label for page.", e);
        }
        return ret;
    }


    public void deleteCurrentPage()
    {
        if(getCurrentPageObject() != null)
        {
            TypedObject typedObject = getCurrentPageObject();
            ItemModel itemModel = (ItemModel)typedObject.getObject();
            try
            {
                if(itemModel != null)
                {
                    this.modelService.remove(itemModel);
                    BrowserModel browserModel = UISessionUtils.getCurrentSession().getPerspective("cmscockpit.perspective.catalog").getBrowserArea().getFocusedBrowser();
                    if(browserModel instanceof de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel)
                    {
                        ((CmsCatalogBrowserModel)browserModel).updateItems();
                    }
                    UISessionUtils.getCurrentSession()
                                    .setCurrentPerspective(UISessionUtils.getCurrentSession().getPerspective("cmscockpit.perspective.catalog"));
                }
            }
            finally
            {
                Clients.showBusy(null, false);
            }
        }
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public ContentEditorBrowserSectionModel getContentEditorSection()
    {
        return this.contentEditorSection;
    }


    public void clearSelection(ListBrowserSectionModel browserSectionModel)
    {
        for(BrowserSectionModel sectionModel : getBrowserSectionModels())
        {
            if(!browserSectionModel.equals(sectionModel) && !sectionModel.getSelectedIndexes().isEmpty())
            {
                sectionModel.setModified(true);
                sectionModel.setSelectedIndexes(Collections.EMPTY_LIST);
            }
        }
    }


    public String getLabel()
    {
        String ret = null;
        Object itemModel = getCurrentPageObject().getObject();
        if(itemModel != null && !this.modelService.isRemoved(itemModel))
        {
            ret = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(getCurrentPageObject());
        }
        return ret;
    }


    public void createAndInitializeDrilldownView()
    {
        List<BrowserSectionModel> sections = new ArrayList<>();
        this.contentSlotSection = createAndInitContentSlotSection();
        sections.add(this.contentSlotSection);
        this.contentElementSection = createAndInitContentElementSection(this.contentSlotSection);
        sections.add(this.contentElementSection);
        this.simpleElementSection = createAndInitSimpleElementSection(this.contentElementSection);
        sections.add(this.simpleElementSection);
        this.contentEditorSection = createAndInitContentEditorSection(this.simpleElementSection, this.contentElementSection);
        sections.add(this.contentEditorSection);
        for(BrowserSectionModel section : sections)
        {
            if(!section.getSectionModelListeners().contains(getSectionModelListenerForDrilldownView()))
            {
                section.addSectionModelListener(getSectionModelListenerForDrilldownView());
            }
        }
        setBrowserSectionModels(sections);
    }


    public void createAndInitializeFlatOrStructureView()
    {
        List<ContentSlotModel> slotsForCurrentPage = getContentSlotsForCurrentPage();
        List<BrowserSectionModel> sections = new ArrayList<>();
        List<ObjectType> templateTypes = new ArrayList<>();
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        templateTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.SIMPLECMSCOMPONENT).getSubtypes());
        templateTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENTCONTAINER).getSubtypes());
        if(!slotsForCurrentPage.isEmpty())
        {
            for(ContentSlotModel slot : slotsForCurrentPage)
            {
                if(slot != null)
                {
                    TypedObject wrappedContentSlot = getTypeService().wrapItem(slot);
                    Object object = new Object(this, (SectionBrowserModel)this, UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(wrappedContentSlot), wrappedContentSlot, slot.getCurrentPosition(), slot);
                    boolean templateDefined = !isAssignedToPage(slot);
                    object.setLockable(templateDefined);
                    object.setLocked(templateDefined);
                    object.setListViewConfigurationCode("listContentElementSection");
                    object.setBrowserSectionRenderer((BrowserSectionRenderer)new CMSStructListBrowserSectionRenderer());
                    object.setRootType(typeService.getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT));
                    object.addSectionModelListener(getSectionModelListener());
                    object.setCreatableTypes(templateTypes);
                    object.setVisible(true);
                    object.initialize();
                    sections.add(object);
                }
            }
        }
        this.contentEditorSection = createContentElementEditorSection();
        sections.add(this.contentEditorSection);
        setBrowserSectionModels(sections);
    }


    public List<ObjectType> getAdditableType()
    {
        List<ObjectType> templateTypes = new ArrayList<>();
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        templateTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.SIMPLECMSCOMPONENT).getSubtypes());
        templateTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENTCONTAINER).getSubtypes());
        return templateTypes;
    }


    public CMSSiteModel getActiveSite()
    {
        return this.cmsAdminSiteService.getActiveSite();
    }


    public CatalogVersionModel getActiveCatalogVersion()
    {
        return this.cmsAdminSiteService.getActiveCatalogVersion();
    }


    protected ListBrowserSectionModel createAndInitContentSlotSection()
    {
        Object object = new Object(this, (SectionBrowserModel)this, Labels.getLabel("cmscockpit.contentslots"));
        object.setIcon("/cmscockpit/images/icon_contentslots.gif");
        object.setBrowserSectionRenderer((BrowserSectionRenderer)new CmsContentSlotListBrowserSectionRenderer());
        object
                        .setRootType(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.CONTENTSLOT));
        object.addSectionModelListener(getSectionModelListener());
        object.setVisible(true);
        object.initialize();
        return (ListBrowserSectionModel)object;
    }


    protected ListBrowserSectionModel createAndInitContentElementSection(ListBrowserSectionModel contentSlotSection)
    {
        Object object = new Object(this, (SectionBrowserModel)this, Labels.getLabel("cmscockpit.components"), contentSlotSection);
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        object.setRootType(typeService.getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT));
        object.setIcon("/cmscockpit/images/icon_contentelements.gif");
        object.setBrowserSectionRenderer((BrowserSectionRenderer)new CmsListBrowserSectionRenderer());
        object.addSectionModelListener(getSectionModelListener());
        object.setListViewConfigurationCode("listContentElementSection");
        object.setVisible(false);
        List<ObjectType> creatableTypes = new ArrayList<>();
        creatableTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.SIMPLECMSCOMPONENT).getSubtypes());
        creatableTypes.addAll(typeService.getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT).getSubtypes());
        object.setCreatableTypes(creatableTypes);
        object.initialize();
        return (ListBrowserSectionModel)object;
    }


    protected ListBrowserSectionModel createAndInitSimpleElementSection(ListBrowserSectionModel contentElementSection)
    {
        Object object = new Object(this, (SectionBrowserModel)this, Labels.getLabel("cmscockpit.content"), contentElementSection);
        object.setIcon("/cmscockpit/images/icon_contentelement_edit.gif");
        object.setRootType(
                        UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.SIMPLECMSCOMPONENT));
        object.setBrowserSectionRenderer((BrowserSectionRenderer)new ListBrowserSectionRenderer());
        object.addSectionModelListener(getSectionModelListener());
        object.setVisible(false);
        object.initialize();
        return (ListBrowserSectionModel)object;
    }


    protected ContentEditorBrowserSectionModel createAndInitContentEditorSection(ListBrowserSectionModel simpleElementSection, ListBrowserSectionModel contentElementSection)
    {
        Object object = new Object(this, (SectionBrowserModel)this, Labels.getLabel("cmscockpit.component.editor"), simpleElementSection, contentElementSection);
        object.setIcon("/cmscockpit/images/icon_contentelement_edit.gif");
        object.setBrowserSectionRenderer((BrowserSectionRenderer)new CMSContentEditorSectionRenderer());
        object.addSectionModelListener(getSectionModelListener());
        object.setVisible(false);
        object.initialize();
        return (ContentEditorBrowserSectionModel)object;
    }


    protected ContentEditorBrowserSectionModel createContentElementEditorSection()
    {
        ContentEditorBrowserSectionModel contentEditorSection = new ContentEditorBrowserSectionModel((SectionBrowserModel)this, Labels.getLabel("cmscockpit.component.editor"));
        contentEditorSection.setBrowserSectionRenderer((BrowserSectionRenderer)new CMSContentEditorSectionRenderer());
        contentEditorSection.addSectionModelListener(getSectionModelListener());
        contentEditorSection.setVisible(false);
        contentEditorSection.initialize();
        return contentEditorSection;
    }


    protected AbstractPageModel getCurrentCmsPage()
    {
        AbstractPageModel pageModel = null;
        if(getCurrentPageObject() == null)
        {
            LOG.error("Could not get page. Reason: No page has been set.");
        }
        else
        {
            ItemModel itemModel = (ItemModel)getCurrentPageObject().getObject();
            if(itemModel instanceof AbstractPageModel)
            {
                pageModel = (AbstractPageModel)itemModel;
            }
            else
            {
                LOG.error("Could not get page. Reason: Unexpected type '" + itemModel + "'.");
            }
        }
        return pageModel;
    }


    public BrowserSectionModel retriveSectionModelByContentSlot(String position)
    {
        BrowserSectionModel ret = null;
        for(BrowserSectionModel sectionModel : getBrowserSectionModels())
        {
            ContentSlotModel rootContentSlot = (sectionModel.getRootItem() != null) ? (ContentSlotModel)((TypedObject)sectionModel.getRootItem()).getObject() : null;
            if(rootContentSlot != null && rootContentSlot.getCurrentPosition().equals(position))
            {
                ret = sectionModel;
            }
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    @HybrisDeprecation(sinceVersion = "ages")
    public TypedObject contentSlotDefined(ContentSlotNameModel potentateContetSlot)
    {
        TypedObject ret = null;
        for(ContentSlotModel singleContentSlot : getContentSlotsForCurrentPage())
        {
            if(singleContentSlot.getCurrentPosition() != null && singleContentSlot
                            .getCurrentPosition().equals(potentateContetSlot.getName()))
            {
                ret = UISessionUtils.getCurrentSession().getTypeService().wrapItem(singleContentSlot);
                break;
            }
        }
        return ret;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        if(this.genericRandomNameProducer == null)
        {
            this.genericRandomNameProducer = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
        }
        return this.genericRandomNameProducer;
    }


    protected void createProperViewModel()
    {
        if(getViewMode() != null && getViewMode().equals("EDIT"))
        {
            createAndInitializeFlatOrStructureView();
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            TypedObject createdItem;
            ItemChangedEvent changedEvent = (ItemChangedEvent)event;
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[changedEvent.getChangeType().ordinal()])
            {
                case 1:
                    createdItem = changedEvent.getItem();
                    if(changedEvent.getSource() instanceof BrowserSectionModel)
                    {
                        BrowserSectionModel sectionModel = (BrowserSectionModel)changedEvent.getSource();
                        List<TypedObject> sectionItems = sectionModel.getItems();
                        if(sectionItems != null && !sectionItems.isEmpty())
                        {
                            int itemIndex = sectionItems.indexOf(createdItem);
                            if(itemIndex != -1)
                            {
                                sectionModel.update();
                                sectionModel.setSelectedIndex(itemIndex);
                                getContentEditorSection().setRootItem(createdItem);
                                getContentEditorSection().setVisible(true);
                                getContentEditorSection().update();
                            }
                        }
                        break;
                    }
                    if(changedEvent.getSource() == null)
                    {
                        for(BrowserSectionModel sectionModel : getBrowserSectionModels())
                        {
                            if(sectionModel.getItems().contains(createdItem))
                            {
                                int selectedIndex = sectionModel.getItems().indexOf(createdItem);
                                sectionModel.setSelectedIndex(selectedIndex);
                            }
                        }
                        getContentEditorSection().setRootItem(createdItem);
                        getContentEditorSection().setVisible(true);
                        getContentEditorSection().update();
                        updateItems();
                    }
                    break;
                case 2:
                    if(changedEvent.getItem().equals(getCurrentPageObject()))
                    {
                        getArea().close((BrowserModel)this);
                    }
                    if(changedEvent.getSource() instanceof SectionTableModel)
                    {
                        BrowserSectionModel sectionModel = ((SectionTableModel)changedEvent.getSource()).getModel();
                        List<TypedObject> sectionItems = sectionModel.getItems();
                        if(sectionItems != null && !sectionItems.isEmpty())
                        {
                            if(sectionItems.contains(changedEvent.getItem()))
                            {
                                int removedIndex = sectionItems.indexOf(changedEvent.getItem());
                                if(sectionModel.getSelectedIndex() != null)
                                {
                                    if(removedIndex < sectionModel.getSelectedIndex().intValue())
                                    {
                                        sectionModel.setSelectedIndex(sectionModel.getSelectedIndex().intValue() - 1);
                                    }
                                    else if(removedIndex == sectionModel.getSelectedIndex().intValue())
                                    {
                                        sectionModel.setSelectedIndexes(Collections.EMPTY_LIST);
                                    }
                                }
                                removeComponentFromSlot((TypedObject)sectionModel.getRootItem(), changedEvent.getItem());
                                sectionModel.update();
                            }
                        }
                        if(getContentEditorSection().getRootItem() != null &&
                                        getContentEditorSection().getRootItem().equals(changedEvent.getItem()))
                        {
                            getContentEditorSection().setRootItem(null);
                            getContentEditorSection().setVisible(false);
                        }
                        CatalogBrowserArea area = (CatalogBrowserArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
                        CmsPageContentBrowser content = (CmsPageContentBrowser)area.getCorrespondingContentBrowser((BrowserModel)this);
                        if(content != null && content.getToolbarComponent() != null)
                        {
                            content.getToolbarComponent().update();
                        }
                    }
                    break;
                case 3:
                    for(BrowserSectionModel sectionModel : getBrowserSectionModels())
                    {
                        if(sectionModel.equals(event.getSource()))
                        {
                            continue;
                        }
                        List<TypedObject> sectionItems = sectionModel.getItems();
                        TypedObject changedItem = changedEvent.getItem();
                        if(sectionItems.contains(changedItem))
                        {
                            TypedObject typedObject = sectionItems.get(sectionItems.indexOf(changedItem));
                            this.modelService.refresh(typedObject.getObject());
                            sectionModel.update();
                        }
                        if(sectionModel.getRootItem() != null && sectionModel.getRootItem().equals(changedItem))
                        {
                            TypedObject rootItem = (TypedObject)sectionModel.getRootItem();
                            ItemModel itemModel = (ItemModel)rootItem.getObject();
                            this.modelService.refresh(itemModel);
                            if(sectionModel instanceof Lockable)
                            {
                                getContentEditorSection().setReadOnly(((Lockable)sectionModel).isLocked());
                            }
                            sectionModel.update();
                        }
                    }
                    break;
            }
        }
    }


    public void addToAdditionalItemChangeUpdateNotificationMap(TypedObject itemToUpdate, Collection<TypedObject> changedItems)
    {
        for(TypedObject typedObject : changedItems)
        {
            Set<TypedObject> set = this.updateNotificationMap.get(typedObject);
            if(set == null)
            {
                set = new HashSet<>();
                this.updateNotificationMap.put(typedObject, set);
            }
            set.add(itemToUpdate);
        }
    }


    public Collection<TypedObject> getAdditionalItemsToUpdate(TypedObject changedItem)
    {
        Set<TypedObject> set = this.updateNotificationMap.get(changedItem);
        return (set == null) ? Collections.EMPTY_SET : set;
    }


    public void onClose()
    {
        if(getArea() != null && getArea().getPerspective() != null)
        {
            getArea().getPerspective().removeCockpitEventAcceptor(this);
        }
        onHide();
    }


    public SectionModelListener getSectionModelListenerForDrilldownView()
    {
        if(this.drilldownViewListener == null)
        {
            this.drilldownViewListener = (SectionModelListener)new CmsDrilldownSectionModelListener((SectionBrowserModel)this);
        }
        return this.drilldownViewListener;
    }


    public boolean isBackButtonVisible()
    {
        return "PREVIEW".equals(getViewMode());
    }
}
