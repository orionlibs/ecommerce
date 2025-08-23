package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.session.impl.ContentEditorBrowserSectionModel;
import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.LockableGroupbox;
import de.hybris.platform.cockpit.components.contentbrowser.ListBrowserSectionComponent;
import de.hybris.platform.cockpit.components.dialog.DefaultPopupDialog;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.AbstractTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.model.listview.impl.SectionTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDragAndDropContext;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.ListSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

public class CmsListBrowserSectionComponent extends ListBrowserSectionComponent
{
    private static final Logger LOG = Logger.getLogger(CmsListBrowserSectionComponent.class);
    private static final String ADD_BTN = "/cmscockpit/images/add_btn.gif";
    private static final String ADD_BTN_LOCKED = "/cmscockpit/images/add_btn_red.gif";
    private static final String SPLITTER_IMG = "/cockpit/images/splitter_grey.gif";
    private CMSAdminSiteService cmsAdminSiteService = null;
    private transient Toolbarbutton removeButton = null;
    private transient Toolbarbutton addButton = null;
    private transient Toolbarbutton overrideButton = null;
    private transient String position = null;


    public CmsListBrowserSectionComponent(ListBrowserSectionModel sectionModel)
    {
        super(sectionModel);
        if(!(sectionModel instanceof CmsListBrowserSectionModel))
        {
            throw new IllegalArgumentException("Section model must be of type " + CmsListBrowserSectionModel.class.getSimpleName());
        }
        this.position = ((CmsListBrowserSectionModel)sectionModel).getPosition();
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        boolean changed = false;
        if(getModel() == null || getSectionModel() == null || getSectionModel().getItems().isEmpty() ||
                        getRootType() == null)
        {
            this.lastResultType = null;
        }
        else
        {
            if(this.listView == null)
            {
                ListView newListView = new ListView();
                newListView.setDoubleClickDisabled(true);
                newListView.setupLazyLoading(0, 0);
                ListView listView1 = newListView;
                listView1.setShowColumnHeaders(false);
                listView1.setHeight("100%");
                listView1.setDDContext((DragAndDropContext)new DefaultDragAndDropContext((BrowserModel)getModel()));
                changed = true;
            }
            else
            {
                listView = this.listView;
            }
            if(getTableModel() == null || (listView
                            .getModel() != null && listView.getModel().getListComponentModel() != null && listView.getModel()
                            .getListComponentModel().getListModel() == null) || this.lastResultType == null ||
                            !this.lastResultType.getBaseType().isAssignableFrom((ObjectType)getRootType()) || (
                            !this.lastResultType.equals(getRootType()) && getListViewConfiguration(getRootType(),
                                            getListViewConfigurationCode()) != null))
            {
                this.lastResultType = getRootType();
                ListViewConfiguration listViewConfig = null;
                try
                {
                    listViewConfig = getListViewConfiguration(getRootType(), getListViewConfigurationCode());
                }
                catch(Exception e)
                {
                    LOG.warn("Could not load list view configuration (Reason: '" + e.getMessage() + "').", e);
                    return null;
                }
                if(listViewConfig == null)
                {
                    LOG.warn("Can not display list view (Reason: No matching list view configuration found).");
                    return null;
                }
                if(getTableModel() != null)
                {
                    getTableModel().getListComponentModel().setListModel(null);
                }
                DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
                defaultListComponentModel.setEditable(!isLocked());
                defaultListComponentModel.setSelectable(true);
                defaultListComponentModel.setActivatable(true);
                defaultListComponentModel.setMultiple(false);
                DefaultColumnModel defaultColumnModel = new DefaultColumnModel(listViewConfig);
                setTableModel((MutableTableModel)new SectionTableModel((MutableListModel)defaultListComponentModel, (MutableColumnModel)defaultColumnModel, (BrowserSectionModel)getSectionModel()));
                listView.setModel(null);
                getTableModel().getListComponentModel().setListModel(null);
                updateResult();
                getTableModel().getListComponentModel().setListModel((ListModel)getResultModel());
                listView.setModel((TableModel)getTableModel());
                changed = true;
            }
            else if(changed)
            {
                listView.setModel((TableModel)getTableModel());
            }
            if(changed)
            {
                if(this.tableController != null)
                {
                    try
                    {
                        this.tableController.unregisterListeners();
                    }
                    catch(Exception e)
                    {
                        LOG.warn("Could not unregister listeners (Reason: '" + e.getMessage() + "').", e);
                    }
                    if(getTableModel() != null)
                    {
                        getTableModel().getColumnComponentModel().removeColumnModelListener(getColumnModelListener());
                    }
                }
                this.tableController = (ComponentController)createTableController((ListSectionModel)getSectionModel(), getTableModel(), listView);
                this.tableController.initialize();
                if(getTableModel() != null)
                {
                    getTableModel().getColumnComponentModel().addColumnModelListener(getColumnModelListener());
                }
            }
            else
            {
                updateResult();
            }
            getTableModel().getListComponentModel().setSelectedIndexes(getSectionModel().getSelectedIndexes());
            getTableModel().getListComponentModel()
                            .setActiveItem(getSectionModel().getSectionBrowserModel().getActiveItem());
        }
        return listView;
    }


    public void lightUpdateAfterChangeSelectionVersion()
    {
        MutableTableModel tableModel = getTableModel();
        if(tableModel != null)
        {
            tableModel.getListComponentModel().setSelectedIndexes(getSectionModel().getSelectedIndexes());
            tableModel.getListComponentModel().setActiveItem(getSectionModel().getSectionBrowserModel().getActiveItem());
        }
    }


    protected LockableGroupbox createSectionView()
    {
        LockableGroupbox groupBox = getSectionModel().isLockable() ? new LockableGroupbox(true, getSectionModel().isLocked()) : new LockableGroupbox();
        TypedObject object = null;
        Object rootItem = getSectionModel().getRootItem();
        if(rootItem instanceof ItemModel)
        {
            object = UISessionUtils.getCurrentSession().getTypeService().wrapItem(rootItem);
        }
        else if(rootItem instanceof TypedObject)
        {
            object = (TypedObject)rootItem;
        }
        TypedObject item = object;
        EventListener lockListener = getLockListener(item, groupBox);
        groupBox.addLockListener(lockListener);
        configureIconAndPreLabel(groupBox);
        groupBox.setLabel(getSectionModel().getLabel());
        if(getSectionModel().isLockable() && getSectionModel().isLocked())
        {
            groupBox.setOpen(false);
        }
        Div captionDiv = new Div();
        captionDiv.setWidth("100%");
        captionDiv.setAlign("right");
        captionDiv.setSclass("groupboxCaption");
        captionDiv.setStyle("white-space:nowrap;");
        if(item != null && getUiAccessRightService().isWritable((ObjectType)item.getType(), item))
        {
            this.addButton = new Toolbarbutton("", "/cmscockpit/images/add_btn.gif");
            this.addButton.setTooltiptext(Labels.getLabel("cmscockpit.create_item"));
            UITools.addBusyListener((Component)this.addButton, "onClick", getAddBtnEventListener(captionDiv, this.addButton), null, "general.updating.busy");
            captionDiv.appendChild((Component)this.addButton);
            Image splitterAddBtn = new Image("/cockpit/images/splitter_grey.gif");
            splitterAddBtn.setSclass("splitter");
            captionDiv.appendChild((Component)splitterAddBtn);
            ContentSlotModel currentContentSlot = retrieveCurrentContentSlot();
            if(getModel() instanceof CmsPageBrowserModel)
            {
                SystemService systemService = UISessionUtils.getCurrentSession().getSystemService();
                if(((CmsPageBrowserModel)getModel()).isAssignedToPage(currentContentSlot) && systemService
                                .checkPermissionOn(getItemModel().getItemtype(), "remove"))
                {
                    this.removeButton = new Toolbarbutton("", "/cmscockpit/images/icon_func_override_reverse.png");
                    this.removeButton.setTooltiptext(Labels.getLabel("cmscockpit.override_contentslotfortemplate_reverse"));
                    this.removeButton.addEventListener("onClick", getRemoveListener());
                    captionDiv.appendChild((Component)this.removeButton);
                    Image splitterRemoveBtn = new Image("/cockpit/images/splitter_grey.gif");
                    splitterRemoveBtn.setSclass("splitter");
                    captionDiv.appendChild((Component)splitterRemoveBtn);
                }
                else if(systemService.checkPermissionOn(getItemModel().getItemtype(), "create"))
                {
                    this.overrideButton = new Toolbarbutton("", "/cmscockpit/images/icon_func_override.png");
                    this.overrideButton.setTooltiptext(Labels.getLabel("cmscockpit.override_contentslotfortemplate"));
                    this.overrideButton.addEventListener("onClick", getOverrideListener());
                    captionDiv.appendChild((Component)this.overrideButton);
                    Image splitterOverrideBtn = new Image("/cockpit/images/splitter_grey.gif");
                    splitterOverrideBtn.setSclass("splitter");
                    captionDiv.appendChild((Component)splitterOverrideBtn);
                }
                else
                {
                    this.removeButton = null;
                    this.overrideButton = null;
                }
            }
        }
        groupBox.getCaptionContainer().appendChild((Component)captionDiv);
        this.groupBoxContent = new Div();
        this.groupBoxContent.setSclass("browserSectionContent");
        groupBox.appendChild((Component)this.groupBoxContent);
        this.listView = loadListView();
        if(this.listView == null)
        {
            this.groupBoxContent.appendChild((Component)new Label("Nothing to display"));
        }
        else
        {
            this.groupBoxContent.appendChild((Component)this.listView);
        }
        setLocked(getSectionModel().isLocked());
        return groupBox;
    }


    protected ItemModel getItemModel()
    {
        ContentSlotModel contentSlotModel;
        ItemModel itemModel = null;
        if(getSectionModel().getRootItem() instanceof TypedObject)
        {
            itemModel = (ItemModel)((TypedObject)getSectionModel().getRootItem()).getObject();
        }
        else if(getSectionModel().getRootItem() instanceof ContentSlotModel)
        {
            contentSlotModel = (ContentSlotModel)getSectionModel().getRootItem();
        }
        return (ItemModel)contentSlotModel;
    }


    protected void configureIconAndPreLabel(LockableGroupbox groupBox)
    {
        if(getSectionModel().getIcon() != null || getSectionModel().getPreLabel() != null)
        {
            Hbox preLabelHbox = new Hbox();
            if(getSectionModel().getIcon() != null)
            {
                Image icon = new Image(getSectionModel().getIcon());
                if(getSectionModel().getPreLabel() != null)
                {
                    icon.setSclass("advancedGroupboxIcon");
                }
                preLabelHbox.appendChild((Component)icon);
            }
            if(getSectionModel().getPreLabel() != null)
            {
                preLabelHbox.appendChild((Component)getSectionModel().getPreLabel());
            }
            Div preLabelComponent = groupBox.getPreLabelComponent();
            preLabelComponent.setSclass("advancedGroupboxPreLabel");
            preLabelComponent.appendChild((Component)preLabelHbox);
        }
    }


    protected EventListener getAddBtnEventListener(Div captionDiv, Toolbarbutton addElementButton)
    {
        return (EventListener)new Object(this, captionDiv);
    }


    protected EventListener getPopupElementEventListener(DefaultPopupDialog dialogPopup)
    {
        return (EventListener)new Object(this, dialogPopup);
    }


    protected EventListener getLockListener(TypedObject item, LockableGroupbox groupBox)
    {
        return event -> {
            if(getCmsPageLockingService().isContentSlotLockedForUser((ContentSlotModel)item.getObject(), UISessionUtils.getCurrentSession().getSystemService().getCurrentUser()))
            {
                Messagebox.show(Labels.getLabel("cmscockpit.pagelocking.editingDisabled", new Object[] {getLockersNames(getCmsPageLockingService().getSlotLockers((ContentSlotModel)item.getObject()))}), Labels.getLabel("cmscockpit.pagelocking.pageIsLocked"), 1, "z-msgbox z-msgbox-information");
            }
            else if(getSectionModel().isLocked())
            {
                int choice = Messagebox.show(Labels.getLabel("sectionlock.unlock.msg"), Labels.getLabel("sectionlock.unlock.title"), 3, "z-msgbox z-msgbox-exclamation");
                if(choice == 1)
                {
                    setLocked(false);
                    groupBox.setOpen(true);
                }
            }
            else
            {
                setLocked(true);
                groupBox.setOpen(false);
                BaseUICockpitPerspective currentPerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
                if(getSectionModel().getItems() != null && getSectionModel().getItems().contains(currentPerspective.getActiveItem()))
                {
                    currentPerspective.setActiveItem(null);
                    currentPerspective.getEditorArea().setCurrentObject(null);
                    currentPerspective.collapseEditorArea();
                }
            }
        };
    }


    protected EventListener getRemoveListener()
    {
        return event -> {
            if(getModel() instanceof CmsPageBrowserModel)
            {
                ItemModel itemModel = getItemModel();
                if(itemModel instanceof ContentSlotModel)
                {
                    String currentMessage = Labels.getLabel("prompt.confirm_remove_content_slot", (Object[])new String[] {this.position});
                    if(Messagebox.show(currentMessage, Labels.getLabel("dialog.confirmRemove.title"), 48, "z-msgbox z-msgbox-question") == 16)
                    {
                        ((CmsPageBrowserModel)getModel()).deleteSlotContentForCurrentPage(((ContentSlotModel)itemModel).getUid());
                    }
                }
            }
        };
    }


    protected EventListener getOverrideListener()
    {
        return event -> {
            if(getModel() instanceof CmsPageBrowserModel)
            {
                ItemModel itemModel = getItemModel();
                if(itemModel instanceof ContentSlotModel)
                {
                    String currentMessage = Labels.getLabel("prompt.confirm_override_content_slot", (Object[])new String[] {this.position});
                    if(Messagebox.show(currentMessage, Labels.getLabel("dialog.confirmOverride.title"), 48, "z-msgbox z-msgbox-question") == 16)
                    {
                        ((CmsPageBrowserModel)getModel()).createContentSlotForPage(this.position);
                    }
                }
            }
        };
    }


    public ContentSlotModel retrieveCurrentContentSlot()
    {
        ContentSlotModel contentSlotModel;
        ItemModel itemModel = null;
        if(getSectionModel().getRootItem() instanceof TypedObject)
        {
            itemModel = (ItemModel)((TypedObject)getSectionModel().getRootItem()).getObject();
        }
        else if(getSectionModel().getRootItem() instanceof ContentSlotModel)
        {
            contentSlotModel = (ContentSlotModel)getSectionModel().getRootItem();
        }
        if(contentSlotModel instanceof ContentSlotModel)
        {
            return contentSlotModel;
        }
        return null;
    }


    public CmsListBrowserSectionModel getSectionModel()
    {
        CmsListBrowserSectionModel ret = null;
        if(super.getSectionModel() instanceof CmsListBrowserSectionModel)
        {
            return ret = (CmsListBrowserSectionModel)super.getSectionModel();
        }
        LOG.warn("This component requireds CmsListBrowserSectionModel in oder to proper work!");
        return ret;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }


    protected AbstractTableController createTableController(ListSectionModel listSectionModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        if(listSectionModel instanceof ListBrowserSectionModel)
        {
            return (AbstractTableController)new CmsPageBrowserSectionTableController(this, (ListBrowserSectionModel)listSectionModel, mutableTableModel, listView);
        }
        throw new IllegalArgumentException("List section model not a list browser section model.");
    }


    protected LockableGroupbox getSectionGroupbox()
    {
        LockableGroupbox lockBox = null;
        AdvancedGroupbox groupBox = super.getSectionGroupbox();
        if(groupBox instanceof LockableGroupbox)
        {
            lockBox = (LockableGroupbox)groupBox;
        }
        else
        {
            LOG.warn("Section groupbox is not an instance of " + LockableGroupbox.class.getSimpleName());
        }
        return lockBox;
    }


    protected void setLocked(boolean locked)
    {
        boolean changed = (getSectionModel().isLocked() != locked);
        if(changed)
        {
            getSectionModel().setLocked(locked);
        }
        LockableGroupbox gBox = getSectionGroupbox();
        if(gBox != null)
        {
            gBox.setLocked(locked);
        }
        if(this.addButton != null)
        {
            this.addButton.setDisabled(locked);
            this.addButton.setImage(locked ? "/cmscockpit/images/add_btn_red.gif" : "/cmscockpit/images/add_btn.gif");
            this.addButton.setStyle(locked ? "opacity:0.5" : "opacity:1");
        }
        if(this.removeButton != null)
        {
            this.removeButton.setDisabled(locked);
            this.removeButton.setStyle(locked ? "opacity:0.5" : "opacity:1");
        }
        if(getTableModel() != null && getTableModel().getListComponentModel() != null)
        {
            getTableModel().getListComponentModel().setEditable(!locked);
            getTableModel().getListComponentModel().setActivatable(!locked);
            if(changed && !UITools.isFromOtherDesktop((Component)this.listView))
            {
                this.listView.update();
            }
        }
        List<Integer> selIndexes = getSectionModel().getSelectedIndexes();
        if(selIndexes != null && !selIndexes.isEmpty())
        {
            if(getModel() instanceof CmsPageBrowserModel)
            {
                ContentEditorBrowserSectionModel editorSection = ((CmsPageBrowserModel)getModel()).getContentEditorSection();
                editorSection.setReadOnly(locked);
                editorSection.update();
            }
        }
    }


    protected boolean isLocked()
    {
        return getSectionModel().isLocked();
    }


    protected String getLockersNames(Collection<UserModel> users)
    {
        StringBuffer buffer = new StringBuffer();
        for(UserModel user : users)
        {
            buffer.append(user.getDisplayName()).append(", ");
        }
        return buffer.substring(0, buffer.length() - 2).toString();
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }
}
