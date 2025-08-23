package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ContextAreaActionColumnConfigurationRegistry;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewHelper;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.ContextAreaTableController;
import de.hybris.platform.cockpit.model.listview.impl.ContextAreaTableModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.EditorFactory;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.impl.MultiTypeColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDragAndDropContext;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.CockpitListComponent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public class ContextAreaBrowserComponent extends AbstractContextBrowserComponent implements DesktopRemovalAwareComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(ContextAreaBrowserComponent.class);
    protected static final String CONTEXT_AREA_MAIN_DIV_SCLASS = "contextAreaMainDiv";
    protected static final String EMPTY_MESSAGE_I3 = "contextarea.emptymessage";
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewContentBrowserContext";
    protected static final String BROWSERAREA_SHOW_MAX_L10NKEY = "browserarea.show_max";
    protected transient UIListView listView = null;
    protected transient HtmlBasedComponent contextArea = null;
    protected String internalContextViewMode = null;
    protected final DefaultListModel<TypedObject> contextModel = new DefaultListModel();
    protected EventListener gridContextController = null;
    protected transient Div contextAreaMainDiv = null;
    protected UIConfigurationService uiConfigurationService = null;
    protected TypeService typeService = null;
    protected UIAccessRightService uiAccessRightService = null;
    protected ContextAreaTableController tableController = null;
    protected ObjectTemplate lastContextType = null;
    protected LoginService loginService;
    protected Label contextHeader = new Label("");
    protected boolean cleanContextHeader = true;
    protected SystemService systemService = null;
    protected boolean inlineCreationEnabled;
    protected Div toolbarDiv;
    protected Div multiSelectActionArea;
    protected Paging paging;
    protected Combobox pageSizeCombo;
    protected TypedObject previouslyActiveItem;
    protected String headerString = null;
    protected ColumnModelListener columnModelListener;


    public ContextAreaBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            setHeight("100%");
            setWidth("100%");
            getChildren().clear();
            updateContextItems();
            this.internalContextViewMode = getModel().getContextViewMode();
            this.contextArea = createContextArea();
            if(this.contextArea != null)
            {
                appendChild((Component)this.contextArea);
            }
            this.initialized = (this.listView != null);
            updateMultiSelectActionArea();
        }
        return this.initialized;
    }


    public AdvancedBrowserModel getModel()
    {
        return (AdvancedBrowserModel)super.getModel();
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            String newContextViewMode = getModel().getContextViewMode();
            if(newContextViewMode.equals(this.internalContextViewMode))
            {
                if("GRID".equals(this.internalContextViewMode))
                {
                    LOG.error("Grid view currently not available.");
                }
                else if("LIST".equals(this.internalContextViewMode) || "MULTI_TYPE_LIST"
                                .equals(this.internalContextViewMode))
                {
                    this.listView = loadListView();
                    this.toolbarDiv = new Div();
                    if(this.listView == null)
                    {
                        this.contextAreaMainDiv.getChildren().clear();
                        this.contextAreaMainDiv.appendChild((Component)new Label(Labels.getLabel("contextarea.emptymessage")));
                    }
                    else
                    {
                        if(this.contextAreaMainDiv.getChildren().isEmpty() ||
                                        !this.contextAreaMainDiv.getFirstChild().equals(this.listView))
                        {
                            this.contextAreaMainDiv.getChildren().clear();
                            this.contextAreaMainDiv.appendChild((Component)this.listView);
                        }
                        if(getContextTableModel() != null)
                        {
                            MutableListModel listComponentModel = getContextTableModel().getListComponentModel();
                            listComponentModel.setSelectedIndexesDirectly(getModel().getSelectedContextIndexes());
                            listComponentModel.setActiveItem(getModel().getActiveItem());
                        }
                        this.listView.update();
                        updateMultiSelectActionArea();
                        success = true;
                    }
                }
            }
            else
            {
                this.internalContextViewMode = newContextViewMode;
                this.contextAreaMainDiv.getChildren().clear();
                if(this.internalContextViewMode.equals("LIST"))
                {
                    this.listView = loadListView();
                    if(this.listView == null)
                    {
                        this.contextAreaMainDiv.appendChild((Component)new Label(Labels.getLabel("contextarea.emptymessage")));
                    }
                    else
                    {
                        this.contextAreaMainDiv.appendChild((Component)this.listView);
                    }
                    success = true;
                }
                else if(this.internalContextViewMode.equals("GRID"))
                {
                    this.contextAreaMainDiv.appendChild((Component)new Label(Labels.getLabel("contextarea.gridview.notsupported")));
                }
            }
            if(this.contextArea.isVisible() != getModel().isContextVisible())
            {
                this.contextArea.setVisible(getModel().isContextVisible());
                getContentBrowser().resize();
            }
            updatePaging();
        }
        else
        {
            success = initialize();
        }
        this.contextHeader.setValue(createContextHeader());
        return success;
    }


    protected void updateMultiSelectActionArea()
    {
        if(this.multiSelectActionArea != null)
        {
            if(this.multiSelectActionArea.getChildren() != null)
            {
                this.multiSelectActionArea.getChildren().clear();
            }
            ActionColumnConfiguration actions = null;
            if(this.lastContextType != null)
            {
                ContextAreaActionColumnConfigurationRegistry registry = getContentBrowser().getModel().getArea().getMultiSelectContextActionsRegistry();
                if(registry != null)
                {
                    actions = registry.getActionColumnConfigurationForType((ObjectType)this.lastContextType.getBaseType());
                }
            }
            if(actions != null)
            {
                ListViewAction.Context context = new ListViewAction.Context();
                context.setModel((TableModel)getContextTableModel());
                for(ListViewAction action : actions.getActions())
                {
                    updateMultiSelectAction(context, action);
                }
            }
        }
    }


    protected void updateMultiSelectAction(ListViewAction.Context context, ListViewAction action)
    {
        String multiSelectImageURI = action.getMultiSelectImageURI(context);
        if(multiSelectImageURI != null)
        {
            Image actionImg = new Image(multiSelectImageURI);
            this.multiSelectActionArea.appendChild((Component)actionImg);
            EventListener listener = action.getMultiSelectEventListener(context);
            Menupopup popup = action.getMultiSelectPopup(context);
            if(listener == null && popup == null)
            {
                actionImg.setClass("noAction");
            }
            if(listener != null)
            {
                actionImg.addEventListener("onClick", listener);
            }
            if(action.getTooltip(context) != null && action.getTooltip(context).length() > 0)
            {
                actionImg.setTooltiptext(action.getTooltip(context));
            }
            if(popup != null)
            {
                this.multiSelectActionArea.appendChild((Component)popup);
                actionImg.setPopup((Popup)popup);
            }
            this.multiSelectActionArea.appendChild((Component)actionImg);
        }
    }


    protected void updatePaging()
    {
        AbstractPageableBrowserModel browserModel = getPageableBrowserModelIfPresent();
        if(isContextPagingEnabled(browserModel))
        {
            this.paging.setTotalSize(browserModel.getContextItems().size());
            this.paging.setPageSize(browserModel.getContextItemsPageSize());
            this.paging.setActivePage(browserModel.getContextItemsPageIndex());
            this.pageSizeCombo.setValue(String.valueOf(browserModel.getContextItemsPageSize()));
        }
    }


    public void setActiveItem(TypedObject activeItem)
    {
        if(this.initialized)
        {
            if("LIST".equals(this.internalContextViewMode) || "MULTI_TYPE_LIST"
                            .equals(this.internalContextViewMode))
            {
                if(getContextTableModel() != null)
                {
                    if(this.previouslyActiveItem == null || !this.previouslyActiveItem.equals(activeItem))
                    {
                        this.previouslyActiveItem = activeItem;
                        getContextTableModel().getListComponentModel().setActiveItem(activeItem);
                    }
                }
            }
        }
    }


    public void updateActiveItems()
    {
        if(this.initialized)
        {
            if("GRID".equals(this.internalContextViewMode))
            {
                LOG.error("Grid view currently not available.");
            }
            else if("LIST".equals(this.internalContextViewMode) || "MULTI_TYPE_LIST"
                            .equals(this.internalContextViewMode))
            {
                if(this.listView != null)
                {
                    this.listView.updateActiveItems();
                }
            }
        }
        else
        {
            initialize();
        }
    }


    public void updateSelectedItems()
    {
        updateActiveItems();
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        if(this.initialized)
        {
            if("GRID".equals(this.internalContextViewMode))
            {
                LOG.error("Grid view currently not available.");
            }
            else if("LIST".equals(this.internalContextViewMode) || "MULTI_TYPE_LIST"
                            .equals(this.internalContextViewMode))
            {
                if(this.listView != null)
                {
                    int nr_up = this.listView.updateItem(item, modifiedProperties);
                    if(nr_up > 0)
                    {
                        getContentBrowser().updateItem(getModel().getContextRootItem(), Collections.EMPTY_SET);
                    }
                }
            }
        }
        else
        {
            initialize();
        }
    }


    protected DefaultListModel<TypedObject> getContextListModel()
    {
        return this.contextModel;
    }


    protected void updateContextItems()
    {
        if(getModel() == null)
        {
            getContextListModel().clear();
        }
        else
        {
            List<TypedObject> items = getModel().getContextItems();
            if(items != null)
            {
                getContextListModel().clearAndAddAll(items, getModel().isItemsRemovable(), getModel().isItemsMovable());
            }
        }
    }


    protected HtmlBasedComponent createContextArea()
    {
        Div div1 = new Div();
        div1.setSclass("contextArea");
        div1.setHeight("100%");
        div1.setWidth("100%");
        div1.setVisible(getModel().isContextVisible());
        Div mainCnt = new Div();
        mainCnt.setStyle("width: 100%; height: 100%; position: relative;");
        Div headerDiv = new Div();
        headerDiv.setHeight("28px");
        headerDiv.setWidth("100%");
        Div contentDiv = new Div();
        contentDiv.setStyle("position: absolute; top: 50px; bottom: 4px; left: 4px; right: 4px;");
        mainCnt.appendChild((Component)headerDiv);
        mainCnt.appendChild((Component)contentDiv);
        div1.appendChild((Component)mainCnt);
        Div contextHeaderDiv = new Div();
        this.contextHeader = new Label(createContextHeader());
        contextHeaderDiv.appendChild((Component)this.contextHeader);
        contextHeaderDiv.setAlign("left");
        contextHeaderDiv.setSclass("context-header-div");
        contextHeaderDiv.setStyle("height: 20px;");
        headerDiv.appendChild((Component)contextHeaderDiv);
        if(this.toolbarDiv == null)
        {
            this.toolbarDiv = new Div();
            this.toolbarDiv.setSclass("special-toolbar-div");
        }
        headerDiv.appendChild((Component)this.toolbarDiv);
        Groupbox contextGb = createContextGroupbox();
        if(contextGb == null)
        {
            headerDiv.appendChild((Component)new Label());
        }
        else
        {
            headerDiv.appendChild((Component)contextGb);
        }
        this.contextAreaMainDiv = createContextMainDiv();
        if(this.contextAreaMainDiv == null)
        {
            this.contextAreaMainDiv = new Div();
        }
        contentDiv.appendChild((Component)this.contextAreaMainDiv);
        return (HtmlBasedComponent)div1;
    }


    protected Div createContextMainDiv()
    {
        Div contextMainDiv = new Div();
        contextMainDiv.setSclass("contextAreaMainDiv");
        String contextViewMode = getModel().getContextViewMode();
        if(contextViewMode != null)
        {
            if(contextViewMode.equals("LIST") || contextViewMode
                            .equals("MULTI_TYPE_LIST"))
            {
                this.listView = loadListView();
                if(this.listView != null)
                {
                    contextMainDiv.appendChild((Component)this.listView);
                }
                else
                {
                    contextMainDiv.appendChild((Component)new Label(Labels.getLabel("contextarea.emptymessage")));
                }
            }
            else if(contextViewMode.equals("GRID"))
            {
                contextMainDiv.appendChild((Component)new Label(Labels.getLabel("contextarea.gridview.notsupported")));
            }
            else
            {
                LOG.error("Unknown mode set.");
            }
        }
        return contextMainDiv;
    }


    protected String createContextHeader()
    {
        if(!this.cleanContextHeader && getModel() != null && getModel().getContextRootItem() != null)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            this
                            .headerString = (getModel().getContextRootTypePropertyDescriptor() != null) ? getModel().getContextRootTypePropertyDescriptor().getName() : ((getModel().getContextRootType() != null) ? getModel().getContextRootType().toString() : "");
            if(getModel().getContextRootTypePropertyDescriptor() != null)
            {
                this.headerString += " of:  ";
            }
            this.headerString = this.headerString + " " + this.headerString;
        }
        return this.headerString;
    }


    protected Groupbox createContextGroupbox()
    {
        Groupbox contextGb = null;
        contextGb = new Groupbox();
        contextGb.setMold("3d");
        contextGb.setOpen(false);
        contextGb.setClosable(false);
        contextGb.setSclass("contentBrowserGroupbox topflat");
        contextGb.setStyle("border-bottom: 1px solid #aaaaaa");
        Caption contextCaption = new Caption();
        contextGb.appendChild((Component)contextCaption);
        Div headerWrapperDiv = new Div();
        headerWrapperDiv.setStyle("min-width:600px; height:16px");
        Collection<HtmlBasedComponent> headerComponents = getHeaderComponents();
        if(CollectionUtils.isNotEmpty(headerComponents))
        {
            headerWrapperDiv.appendChild((Component)new Hbox((Component[])headerComponents.toArray((Object[])new HtmlBasedComponent[0])));
        }
        Hbox captionHbox = new Hbox();
        contextCaption.appendChild((Component)captionHbox);
        captionHbox.setWidth("100%");
        captionHbox.setAlign("center");
        this.multiSelectActionArea = new Div();
        this.multiSelectActionArea.setSclass("toolbar_multiselect_action_context");
        this.multiSelectActionArea.setAlign("left");
        headerWrapperDiv.appendChild((Component)this.multiSelectActionArea);
        captionHbox.appendChild((Component)headerWrapperDiv);
        Div captionDiv = new Div();
        captionHbox.appendChild((Component)captionDiv);
        captionDiv.setWidth("100%");
        captionDiv.setAlign("right");
        Hbox innerCaptionHbox = new Hbox();
        captionDiv.appendChild((Component)innerCaptionHbox);
        for(HtmlBasedComponent additionalComponent : getGroupboxComponents())
        {
            innerCaptionHbox.appendChild((Component)additionalComponent);
        }
        AbstractPageableBrowserModel browserModel = getPageableBrowserModelIfPresent();
        if(isContextPagingEnabled(browserModel))
        {
            Div pagingCnt = new Div();
            Hbox pgHbox = new Hbox();
            pagingCnt.setStyle("position: relative; height: 10px; top: -6px; margin-right: 10px;");
            this.pageSizeCombo = new Combobox(String.valueOf(browserModel.getContextItemsPageSize()));
            for(Integer pageSize : browserModel.getPageSizes())
            {
                this.pageSizeCombo.appendChild((Component)new Comboitem(pageSize.toString()));
            }
            this.pageSizeCombo.appendChild((Component)new Comboitem(Labels.getLabel("browserarea.show_max")));
            this.pageSizeCombo.setCols(3);
            Object object = new Object(this, browserModel);
            this.pageSizeCombo.addEventListener("onChange", (EventListener)object);
            this.pageSizeCombo.addEventListener("onOK", (EventListener)object);
            this.pageSizeCombo.setTooltiptext(Labels.getLabel("browserarea.pagesize"));
            pgHbox.appendChild((Component)this.pageSizeCombo);
            this.paging = new Paging(browserModel.getContextItems().size(), browserModel.getContextItemsPageSize());
            this.paging.setStyle("padding: 0;");
            this.paging.addEventListener("onPaging", (EventListener)new Object(this, browserModel));
            pgHbox.appendChild((Component)this.paging);
            pagingCnt.appendChild((Component)pgHbox);
            innerCaptionHbox.appendChild((Component)pagingCnt);
        }
        Toolbarbutton closeBtn = createCloseButton("/cockpit/images/close_btn.png");
        innerCaptionHbox.appendChild((Component)closeBtn);
        Div captionDiv2 = new Div();
        captionHbox.appendChild((Component)captionDiv2);
        captionDiv2.setWidth("100%");
        captionDiv2.setAlign("right");
        return contextGb;
    }


    public void resize()
    {
    }


    protected MutableListModel getListComponentModel()
    {
        DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
        defaultListComponentModel.setEditable(true);
        defaultListComponentModel.setSelectable(true);
        defaultListComponentModel.setActivatable(true);
        defaultListComponentModel.setMultiple(true);
        return (MutableListModel)defaultListComponentModel;
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        if(getModel() != null && !getModel().getContextItems().isEmpty())
        {
            MutableListModel listComponentModel = getListComponentModel();
            if(getContextTableModel() == null)
            {
                setContextTableModel((MutableTableModel)new ContextAreaTableModel(listComponentModel, null, (BrowserModel)getModel()));
            }
            else
            {
                getContextTableModel().setListComponentModel(listComponentModel);
            }
            ListViewConfiguration listviewConfig = loadListViewConfig();
            ListViewHelper.ListViewInfo lvInfo = ListViewHelper.loadListView(this.listView, "listViewContentBrowserContext", this.lastContextType,
                            getContextResultType(), getContextTableModel(), getContextListModel(), (CockpitListComponent)new MyModelWrapper(this), getListenerHandler(), (DragAndDropContext)new DefaultDragAndDropContext((BrowserModel)
                                            getModel()), listviewConfig);
            if(lvInfo != null)
            {
                listView = lvInfo.getListView();
                this.lastContextType = lvInfo.getRootType();
            }
            getContextTableModel().addTableModelListener(createContextTableModelListener());
        }
        else
        {
            this.lastContextType = null;
            this.inlineCreationEnabled = false;
        }
        updatePaging();
        return listView;
    }


    protected ListViewConfiguration loadListViewConfig()
    {
        ListViewConfiguration listviewConfig = "MULTI_TYPE_LIST".equals(getModel().getContextViewMode()) ? createMultiTypeListViewConfiguration() : getListViewConfiguration(getContextResultType(), "listViewContentBrowserContext");
        return listviewConfig;
    }


    protected ListViewConfiguration createMultiTypeListViewConfiguration()
    {
        return (ListViewConfiguration)new MultiTypeListMainAreaComponentFactory.MultiTypeListViewConfiguration((ColumnGroupConfiguration)new MultiTypeColumnGroupConfiguration("name"));
    }


    protected ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String code)
    {
        UIConfigurationService uiConfigService = getUIConfigurationService();
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, code, ListViewConfiguration.class);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        if(this.uiAccessRightService == null)
        {
            this.uiAccessRightService = (UIAccessRightService)SpringUtil.getBean("uiAccessRightService");
        }
        return this.uiAccessRightService;
    }


    protected LoginService getLoginService()
    {
        if(this.loginService == null)
        {
            this.loginService = (LoginService)SpringUtil.getBean("loginService");
        }
        return this.loginService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected MutableTableModel getContextTableModel()
    {
        MutableTableModel tableModel = null;
        if(getModel() != null)
        {
            tableModel = getModel().getContextTableModel();
        }
        return tableModel;
    }


    protected void setContextTableModel(MutableTableModel tableModel)
    {
        if(getModel() != null)
        {
            getModel().setContextTableModel(tableModel);
        }
    }


    protected ObjectTemplate getContextResultType()
    {
        ObjectTemplate retType = null;
        if(getModel() != null)
        {
            retType = getModel().getContextRootType();
        }
        return retType;
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    protected void cleanup()
    {
        if(this.tableController != null)
        {
            this.tableController.unregisterListeners();
        }
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    protected void showAddItemPopup()
    {
        Map<String, Object> initialValues = new HashMap<>();
        try
        {
            TypedObject rootItem = getModel().getContextRootItem();
            ObjectTemplate rootItemType = getTypeService().getObjectTemplate(rootItem.getType().getCode());
            BaseConfiguration baseConfiguration = (BaseConfiguration)getUIConfigurationService().getComponentConfiguration(
                            getContextResultType(), "base", BaseConfiguration.class);
            InitialPropertyConfiguration initialPropertyConfig = baseConfiguration.getInitialPropertyConfiguration(rootItemType,
                            getTypeService());
            if(initialPropertyConfig != null)
            {
                initialValues = initialPropertyConfig.getInitialProperties(rootItem, getTypeService());
            }
            getModel().getArea().getPerspective().createItemInPopupEditor((ObjectType)getContextResultType(), initialValues, (BrowserModel)getModel());
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.error("Cannot set initial values.");
                LOG.error(e.getMessage(), e);
            }
            ArrayList<String> msgs = new ArrayList<>();
            msgs.add(Labels.getLabel("contextarea.additempopup.error"));
            msgs.add(e.getMessage());
            Notification notification = new Notification(msgs);
            notification.setMessage(Labels.getLabel("browser.context.error.createnew"));
            UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier().setDialogNotification(notification);
        }
    }


    protected void showAddItemRow()
    {
    }


    protected ObjectValueContainer getValueContainer(ObjectTemplate template)
    {
        ObjectValueContainer valueContainer = new ObjectValueContainer((ObjectType)getContextResultType(), null);
        Set<PropertyDescriptor> propDescriptors = getRequiredPropertyDescriptors(template);
        for(PropertyDescriptor pd : propDescriptors)
        {
            Object currentValue = null;
            String editorType = pd.getEditorType();
            if("TEXT".equals(editorType))
            {
                currentValue = "";
            }
            else if("INTEGER".equals(editorType))
            {
                currentValue = Integer.valueOf(0);
            }
            else if("DECIMAL".equals(editorType))
            {
                currentValue = new Double(0.0D);
            }
            if(pd.isLocalized())
            {
                for(LanguageModel lang : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages())
                {
                    valueContainer.addValue(pd, lang.getIsocode(), currentValue);
                }
                continue;
            }
            valueContainer.addValue(pd, null, currentValue);
        }
        return valueContainer;
    }


    protected Set<PropertyDescriptor> getRequiredPropertyDescriptors(ObjectTemplate template)
    {
        Set<PropertyDescriptor> all = new HashSet<>();
        if(template != null)
        {
            BaseType baseType = template.getBaseType();
            if(baseType != null)
            {
                for(PropertyDescriptor pd : baseType.getPropertyDescriptors())
                {
                    if(PropertyDescriptor.Occurrence.REQUIRED.equals(pd.getOccurence()))
                    {
                        all.add(pd);
                    }
                }
            }
            for(ExtendedType extType : template.getExtendedTypes())
            {
                for(PropertyDescriptor pd : extType.getPropertyDescriptors())
                {
                    if(PropertyDescriptor.Occurrence.REQUIRED.equals(pd.getOccurence()))
                    {
                        all.add(pd);
                    }
                }
            }
        }
        return all;
    }


    protected boolean isContextPagingEnabled(AbstractPageableBrowserModel browserModel)
    {
        return (browserModel != null && browserModel.getContextItemsPageSize() > 0);
    }


    protected AbstractPageableBrowserModel getPageableBrowserModelIfPresent()
    {
        AdvancedBrowserModel model = getModel();
        return (model instanceof AbstractPageableBrowserModel) ? (AbstractPageableBrowserModel)model : null;
    }


    public EditorFactory getEditorFactory()
    {
        return (EditorFactory)SpringUtil.getBean("EditorFactory");
    }


    public boolean update(boolean cleanContextHeader)
    {
        this.cleanContextHeader = cleanContextHeader;
        return update();
    }


    protected ListViewHelper.ListenerHandler getListenerHandler()
    {
        return (ListViewHelper.ListenerHandler)new MyListenerHandler(this);
    }


    protected Collection<HtmlBasedComponent> getGroupboxComponents()
    {
        return Collections.emptyList();
    }


    protected Collection<HtmlBasedComponent> getHeaderComponents()
    {
        return Collections.emptyList();
    }


    protected Toolbarbutton createCloseButton(String closeBtnImg)
    {
        Toolbarbutton closeBtn = new Toolbarbutton("", closeBtnImg);
        closeBtn.setSclass("plainBtn");
        closeBtn.setDisabled(false);
        closeBtn.setTooltiptext(Labels.getLabel("general.close"));
        closeBtn.setTooltiptext(Labels.getLabel("browserarea.closebrowser.button.tooltip"));
        closeBtn.addEventListener("onClick", (EventListener)new Object(this));
        return closeBtn;
    }


    protected TableModelListener createContextTableModelListener()
    {
        return (TableModelListener)new Object(this);
    }


    protected ColumnModelListener getColumnModelListener()
    {
        if(this.columnModelListener == null)
        {
            this.columnModelListener = (ColumnModelListener)new Object(this);
        }
        return this.columnModelListener;
    }


    protected void storeListViewConfiguration()
    {
        if(getContextTableModel() != null && getContextTableModel().getColumnComponentModel() instanceof DefaultColumnModel)
        {
            DefaultColumnModel columnModel = (DefaultColumnModel)getContextTableModel().getColumnComponentModel();
            ListViewConfiguration configuration = columnModel.getConfiguration();
            getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)configuration,
                            UISessionUtils.getCurrentSession().getUser(), this.lastContextType, "listViewContentBrowserContext", ListViewConfiguration.class);
        }
    }
}
