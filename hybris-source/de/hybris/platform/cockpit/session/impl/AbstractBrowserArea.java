package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.inspector.InspectorRenderer;
import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ContextAreaActionColumnConfigurationRegistry;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.events.impl.SearchEvent;
import de.hybris.platform.cockpit.events.impl.SelectIndexesEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.CockpitBrowserAreaAutoSearchConfigurationUtil;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.cockpit.util.ListProviders;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.East;
import org.zkoss.zkex.zul.LayoutRegion;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabs;

public abstract class AbstractBrowserArea implements UIBrowserArea
{
    public static final String INFO_AREA_CONTAINER = "infoAreaContainer";
    private static final String BROWSER_MODEL = "browserModel";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBrowserArea.class);
    public static final String RESULT_GRID_RENDERER = "resultGridRenderer";
    public static final String BROWSER_TAB_COMPONENT = "browserTab";
    private UICockpitPerspective perspective;
    private BrowserModel focusedBrowser = null;
    private BrowserModel lastMinimizedBrowser = null;
    private boolean splitmodeActivated = false;
    private boolean splittable = false;
    protected final List<BrowserModel> browsers = new LinkedList<>();
    protected final List<BrowserModel> visibleBrowsers = new ArrayList<>();
    private final transient Set<BrowserAreaListener> browserAreaListeners = new LinkedHashSet<>();
    private String viewURI;
    protected final UISessionListener mySessionListener = (UISessionListener)new MySessionListener(this);
    private String defaultBrowserClass;
    private ActionColumnConfiguration multiSelectActions;
    private ActionColumnConfiguration multiSelectContextActions;
    private ActionColumnConfiguration additionalToolbarActions;
    private ContextAreaActionColumnConfigurationRegistry multiSelectContextActionsRegistry;
    private Map<String, String> defaultBrowserViewMapping = null;
    private boolean showCreateDefaultBrowserButton = true;
    private boolean possibleAddContextSubTypes = false;
    protected final transient Map<BrowserModel, AbstractContentBrowser> componentMap = new HashMap<>();
    private transient Div tabPopupContainer;
    private Tabs tabs;
    private Borderlayout borderlayout;
    private final List<CockpitEventAcceptor> cockpitEventAcceptors = new LinkedList<>();
    protected boolean initialized = false;
    protected static final String COCKPIT_ID_BROWSERAREA_TAB_PREFIX = "BrowserArea";
    private InspectorRenderer inspectorRenderer;
    private boolean openInspectorOnSelect = false;
    private boolean keepClosed = false;
    private String infoAreaContainerId;
    private final EventListener inspectorOnOpenListener = (EventListener)new Object(this);


    public boolean isSaveQueryAvailable()
    {
        return false;
    }


    public String getViewURI()
    {
        return this.viewURI;
    }


    public void setViewURI(String viewURI)
    {
        this.viewURI = viewURI;
    }


    public Set<BrowserAreaListener> getBrowserAreaListeners()
    {
        return this.browserAreaListeners;
    }


    public void initialize(Map<String, Object> params)
    {
        if(!this.initialized)
        {
            UISessionUtils.getCurrentSession().addSessionListener(this.mySessionListener);
            if(this.visibleBrowsers == null || this.visibleBrowsers.isEmpty())
            {
                BrowserModel browserModel = null;
                if(this.browsers == null || this.browsers.isEmpty())
                {
                    if(providesDefaultBrowser())
                    {
                        browserModel = createNewDefaultBrowser();
                    }
                    else
                    {
                        LOG.error("No default browser class has been configured. Either configure your browser area in Spring or override the initialize method of your browser area.");
                        return;
                    }
                }
                else
                {
                    browserModel = this.browsers.get(0);
                }
                addVisibleBrowser(browserModel);
                setFocusedBrowser(browserModel);
                if(!CockpitBrowserAreaAutoSearchConfigurationUtil.isInitialSearchDisabled(Executions.getCurrent()) &&
                                !CockpitBrowserAreaAutoSearchConfigurationUtil.isAutomaticSearchDisabled(Executions.getCurrent()))
                {
                    browserModel.updateItems();
                }
            }
            this.initialized = true;
        }
    }


    public Component getInfoArea()
    {
        return getInfoArea(getInfoAreaContainerId());
    }


    protected String getInfoAreaContainerId()
    {
        return StringUtils.isEmpty(this.infoAreaContainerId) ? "infoAreaContainer" : this.infoAreaContainerId;
    }


    public Component getInfoArea(String divId)
    {
        if(this.borderlayout != null)
        {
            Object attribute = this.borderlayout.getAttribute(divId);
            if(attribute instanceof Component)
            {
                return (Component)attribute;
            }
            if(attribute == null)
            {
                IdSpace spaceOwner = this.borderlayout.getSpaceOwner();
                if(spaceOwner != null)
                {
                    Component component = spaceOwner.getFellowIfAny(divId);
                    if(component != null)
                    {
                        this.borderlayout.setAttribute(divId, component);
                        return component;
                    }
                    this.borderlayout.setAttribute(divId, ObjectUtils.NULL);
                }
            }
        }
        return null;
    }


    public void openInspector(TypedObject item)
    {
        Component infoArea = getInfoArea();
        openInspectorInArea(item, infoArea);
    }


    public void openInspectorInDiv(TypedObject item, String divId)
    {
        Component infoArea = getInfoArea(divId);
        openInspectorInArea(item, infoArea);
    }


    protected void openInspectorInArea(TypedObject item, Component infoArea)
    {
        if(infoArea != null && infoArea.isVisible() && getInspectorRenderer() != null)
        {
            East parentEast = (East)UITools.getNextParentOfType(East.class, infoArea, 10);
            if(parentEast != null && !parentEast.isOpen())
            {
                parentEast.setOpen(true);
                parentEast.addEventListener("onInvLater", (EventListener)new Object(this, parentEast));
                Events.echoEvent("onInvLater", (Component)parentEast, null);
            }
            if(item != null)
            {
                infoArea.getChildren().clear();
                getInspectorRenderer().render(infoArea, item);
            }
        }
    }


    public void closeInspector()
    {
        closeInspectorInComponent(getInfoArea(getInfoAreaContainerId()));
    }


    protected void closeInspectorInComponent(Component infoArea)
    {
        if(infoArea != null)
        {
            East parentEast = (East)UITools.getNextParentOfType(East.class, infoArea, 10);
            if(parentEast != null && parentEast.isOpen())
            {
                parentEast.setOpen(false);
            }
        }
    }


    public void closeInspector(String divId)
    {
        closeInspectorInComponent(getInfoArea(divId));
    }


    public void updateInfoArea(ListProvider<TypedObject> listProvider)
    {
        updateInfoArea(listProvider, false);
    }


    public void updateInfoArea(ListProvider<TypedObject> listProvider, boolean handleVisibility)
    {
        Component infoArea = getInfoArea();
        if(infoArea != null && infoArea.isVisible() && getInspectorRenderer() != null)
        {
            LayoutRegion parentRegion = (LayoutRegion)UITools.getNextParentOfType(LayoutRegion.class, infoArea, 10);
            infoArea.getChildren().clear();
            ListProvider.ListInfo listInfo = listProvider.getListInfo();
            checkShowInspector(parentRegion, listProvider, handleVisibility);
            if(parentRegion.isOpen())
            {
                if(ListProvider.ListInfo.EMPTY.equals(listInfo))
                {
                    getInspectorRenderer().renderEmpty(infoArea);
                }
                else if(ListProvider.ListInfo.SINGLETON.equals(listInfo))
                {
                    getInspectorRenderer().render(infoArea, (TypedObject)listProvider.getFirst());
                }
                else if(ListProvider.ListInfo.MULTIPLE.equals(listInfo))
                {
                    getInspectorRenderer().render(infoArea, listProvider);
                }
            }
            else
            {
                UITools.setManagedEventListener((Component)parentRegion, "onOpen", (EventListener)new Object(this, listProvider));
            }
        }
    }


    protected void checkShowInspector(LayoutRegion parentRegion, ListProvider<TypedObject> listProvider)
    {
        checkShowInspector(parentRegion, listProvider, false);
    }


    protected void checkShowInspector(LayoutRegion parentRegion, ListProvider<TypedObject> listProvider, boolean closeEditor)
    {
        if(parentRegion != null)
        {
            if(!parentRegion.isOpen())
            {
                parentRegion.addEventListener("onOpen", this.inspectorOnOpenListener);
                if(ListProvider.ListInfo.EMPTY.equals(listProvider.getListInfo()) || this.keepClosed)
                {
                    return;
                }
                if(this.openInspectorOnSelect)
                {
                    parentRegion.setOpen(true);
                }
            }
        }
    }


    public void initBrowsers(Borderlayout container)
    {
        this.borderlayout = container;
        update();
    }


    public AbstractContentBrowser getCorrespondingContentBrowser(BrowserModel browserModel)
    {
        AbstractContentBrowser corrContentBrowser = this.componentMap.get(browserModel);
        if(getContainerComponent() != null && (corrContentBrowser == null || corrContentBrowser
                        .getDesktop() == null || !corrContentBrowser.getDesktop().equals(
                        getContainerComponent().getDesktop())))
        {
            this.componentMap.clear();
            if(this.borderlayout != null && Executions.getCurrent().getDesktop().equals(this.borderlayout.getDesktop()))
            {
                this.borderlayout.getChildren().clear();
                for(BrowserModel b : getVisibleBrowsers())
                {
                    AbstractContentBrowser contentBrowser = (AbstractContentBrowser)createBrowserView(this.borderlayout, b);
                    if(b.equals(browserModel))
                    {
                        corrContentBrowser = contentBrowser;
                    }
                    this.componentMap.put(b, contentBrowser);
                    if(!contentBrowser.isInitialized())
                    {
                        getPerspective().handleItemRemoved(null);
                    }
                }
            }
            else
            {
                LOG.warn("Could not get content browser. Reason: Internal browser area component no longer valid.");
            }
        }
        updateTabbar();
        return corrContentBrowser;
    }


    protected AbstractComponent getContainerComponent()
    {
        return (AbstractComponent)this.borderlayout;
    }


    protected void resetBrowserView(BrowserModel browserModel)
    {
        AbstractContentBrowser view = this.componentMap.get(browserModel);
        if(view == null || UITools.isFromOtherDesktop((Component)view))
        {
            LOG.debug("Could not reset browser view since view component is not in component map or has been detached.");
        }
        else
        {
            if(this.focusedBrowser == browserModel)
            {
                view.setFocus(true);
            }
            else
            {
                view.setFocus(false);
            }
            view.update();
        }
    }


    public void updateActivation(BrowserModel browserModel)
    {
        AbstractContentBrowser view = this.componentMap.get(browserModel);
        if(view == null)
        {
            LOG.debug("Could not reset browser view since view component is not in component map.");
        }
        else
        {
            if(this.focusedBrowser == browserModel)
            {
                view.setFocus(true);
            }
            else
            {
                view.setFocus(false);
            }
            view.updateActivation();
        }
    }


    public void update()
    {
        this.componentMap.clear();
        if(this.borderlayout == null || UITools.isFromOtherDesktop((Component)this.borderlayout))
        {
            LOG.info("Can not update browser area. Reason: Browser area has not been initialized yet.");
        }
        else
        {
            if(this.borderlayout.getCenter() == null)
            {
                this.borderlayout.appendChild((Component)new Center());
                this.borderlayout.getCenter().setBorder("none");
            }
            else
            {
                UITools.detachChildren((Component)this.borderlayout.getCenter());
                South south = this.borderlayout.getSouth();
                if(south != null)
                {
                    south.detach();
                }
            }
            Iterator<BrowserModel> iterator = getVisibleBrowsers().iterator();
            BrowserModel visibleBrowser = iterator.next();
            BrowserModel splittedBrowser = iterator.hasNext() ? iterator.next() : null;
            boolean initNeeded = false;
            AbstractContentBrowser visContentBrowser = (AbstractContentBrowser)createBrowserView(this.borderlayout, visibleBrowser);
            AbstractContentBrowser oldView = this.componentMap.get(visibleBrowser);
            if(oldView != null && !UITools.isFromOtherDesktop((Component)oldView))
            {
                oldView.detach();
            }
            this.componentMap.put(visibleBrowser, visContentBrowser);
            initNeeded = !visContentBrowser.isInitialized();
            UITools.modifySClass((HtmlBasedComponent)this.borderlayout, "browserAreaSplitted", (splittedBrowser != null));
            if(splittedBrowser != null)
            {
                AbstractContentBrowser splitContentBrowser = (AbstractContentBrowser)createBrowserView(this.borderlayout, splittedBrowser);
                this.componentMap.put(splittedBrowser, splitContentBrowser);
                initNeeded = !splitContentBrowser.isInitialized();
                splitContentBrowser.setDroppable("browserTab");
                splitContentBrowser.addEventListener("onDrop", (EventListener)new Object(this, splittedBrowser));
            }
            if(initNeeded)
            {
                getPerspective().handleItemRemoved(null);
            }
            updateTabbar();
        }
    }


    private void updateTabbar()
    {
        if(this.initialized && this.tabPopupContainer != null &&
                        Executions.getCurrent().getDesktop().equals(this.tabPopupContainer.getDesktop()))
        {
            Iterator<BrowserModel> iterator = getVisibleBrowsers().iterator();
            BrowserModel firstVisible = iterator.next();
            BrowserModel secondVisible = iterator.hasNext() ? iterator.next() : null;
            Tabbox tabbox = new Tabbox();
            UITools.maximize((HtmlBasedComponent)tabbox);
            this.tabPopupContainer.getChildren().clear();
            this.tabs = new Tabs();
            UITools.addBusyListener((Component)tabbox, "onSelect", (EventListener)new Object(this, tabbox), null, "search.busy");
            tabbox.addEventListener("onCheckClose", (EventListener)new Object(this));
            tabbox.appendChild((Component)this.tabs);
            tabbox.setSclass("browserareatabs");
            for(BrowserModel browserModel : getBrowsers())
            {
                if(!browserModel.equals(secondVisible))
                {
                    Tab tab = new Tab();
                    tab.setLabel(computeTabLabel(browserModel.getLabel()));
                    tab.setTooltiptext(browserModel.getLabel());
                    tab.setClosable((isClosable(browserModel) && isShowAsClosable(browserModel)));
                    createAndAddTabPopup(tab, browserModel);
                    UITools.applyTestID((Component)tab, "BrowserArea_" + tab.getLabel().hashCode() + "_tab");
                    this.tabs.appendChild((Component)tab);
                    tab.setAttribute("browserModel", browserModel);
                    tab.setDraggable("browserTab");
                    tab.setDroppable("browserTab");
                    tab.addEventListener("onClose", (EventListener)new Object(this, browserModel, tabbox));
                    tab.addEventListener("onClick", (EventListener)new Object(this));
                    tab.addEventListener("onDrop", (EventListener)new Object(this, browserModel));
                    if(browserModel.equals(firstVisible))
                    {
                        tab.setSelected(true);
                    }
                }
            }
            checkTabsClosable();
            if(this.borderlayout.getNorth() == null)
            {
                North north = new North();
                north.setBorder("none");
                north.setHeight("35px");
                this.borderlayout.appendChild((Component)north);
            }
            else
            {
                this.borderlayout.getNorth().getChildren().clear();
            }
            Hbox hbox = new Hbox();
            hbox.setWidth("100%");
            hbox.setWidths("none,40px");
            hbox.setStyle("table-layout: fixed;");
            hbox.appendChild((Component)tabbox);
            if(providesDefaultBrowser() && isShowCreateDefaultBrowserButton())
            {
                Div buttonDiv = new Div();
                Button createNewTabButton = new Button("+");
                createNewTabButton.setSclass("btnblue");
                buttonDiv.setStyle("margin: 8px;");
                buttonDiv.appendChild((Component)createNewTabButton);
                createNewTabButton.setTooltiptext(Labels.getLabel("tooltip_open_new_tab_btn"));
                UITools.addBusyListener((Component)createNewTabButton, "onClick", (EventListener)new Object(this), null, "search.busy");
                hbox.appendChild((Component)buttonDiv);
            }
            Div tabDiv = new Div();
            tabDiv.setSclass("browserareatabs-cnt");
            tabDiv.appendChild((Component)hbox);
            this.borderlayout.getNorth().appendChild((Component)tabDiv);
        }
    }


    protected void createAndAddTabPopup(Tab tab, BrowserModel browserModel)
    {
        if(!this.initialized || tab == null || browserModel == null || this.tabPopupContainer == null)
        {
            return;
        }
        Menupopup popup = new Menupopup();
        if(providesDefaultBrowser())
        {
            Menuitem newTabMenu = new Menuitem(Labels.getLabel("tabs.new"));
            newTabMenu.setParent((Component)popup);
            UITools.addBusyListener((Component)newTabMenu, "onClick", (EventListener)new Object(this), null, null);
        }
        if(isClosable(browserModel) && isShowAsClosable(browserModel))
        {
            Menuitem closeTabMenu = new Menuitem(Labels.getLabel("tabs.close"));
            closeTabMenu.setParent((Component)popup);
            closeTabMenu.addEventListener("onClick", (EventListener)new Object(this, tab));
            Menuitem closeOtherTabsMenu = new Menuitem(Labels.getLabel("tabs.close_others"));
            closeOtherTabsMenu.setParent((Component)popup);
            closeOtherTabsMenu.addEventListener("onClick", (EventListener)new Object(this, browserModel));
        }
        popup.setParent((Component)this.tabPopupContainer);
        if(popup.getChildren().size() > 0)
        {
            tab.setContext((Popup)popup);
        }
    }


    private void checkTabsClosable()
    {
        if(this.tabs != null && this.tabs.getChildren().size() == 1)
        {
            ((Tab)this.tabs.getFirstChild()).setClosable(false);
        }
    }


    private void updateTab(BrowserModel browser)
    {
        if(this.initialized && this.tabs != null)
        {
            List<Tab> tabs = new ArrayList<>((this.tabs.getChildren() == null) ? Collections.EMPTY_LIST : this.tabs.getChildren());
            for(Tab tab : tabs)
            {
                BrowserModel tabBrowser = (BrowserModel)tab.getAttribute("browserModel");
                if(tabBrowser != null && tabBrowser.equals(browser))
                {
                    tab.setLabel(computeTabLabel(browser.getLabel()));
                    break;
                }
            }
        }
    }


    public BrowserModel createNewDefaultBrowser()
    {
        BrowserModel browser = null;
        if(providesDefaultBrowser())
        {
            try
            {
                browser = (BrowserModel)Class.forName(getDefaultBrowserClass()).newInstance();
            }
            catch(Exception e)
            {
                LOG.error("Could not create default browser.", e);
            }
        }
        return browser;
    }


    public boolean providesDefaultBrowser()
    {
        return StringUtils.isNotBlank(getDefaultBrowserClass());
    }


    public String computeTabLabel(String label)
    {
        String ret = label;
        int maxLength = 30;
        if(ret == null)
        {
            LOG.warn("Browser label was null.");
            return "*";
        }
        if(ret.length() > 30)
        {
            String first = ret.substring(0, 10);
            String second = ret.substring(ret.length() - 10);
            ret = first + " ... " + first;
        }
        return ret;
    }


    protected Component createBrowserView(Borderlayout parent, BrowserModel browserModel)
    {
        AbstractContentBrowser view = browserModel.createViewComponent();
        view.setWidth("100%");
        view.setHeight("100%");
        view.setFocus(browserModel.equals(getFocusedBrowser()));
        if(parent != null)
        {
            South south;
            Center center = parent.getCenter();
            if(center == null)
            {
                center = new Center();
                parent.appendChild((Component)center);
            }
            else if(!center.getChildren().isEmpty())
            {
                if(parent.getSouth() != null)
                {
                    parent.getSouth().detach();
                }
                south = new South();
                parent.appendChild((Component)south);
                south.setHeight("50%");
                south.setSplittable(true);
            }
            south.setBorder("0");
            RemovalAwareDiv removalAwareDiv = new RemovalAwareDiv(this);
            removalAwareDiv.setParent((Component)south);
            removalAwareDiv.setHeight("100%");
            removalAwareDiv.setWidth("100%");
            removalAwareDiv.appendChild((Component)view);
            this.tabPopupContainer = new Div();
            removalAwareDiv.appendChild((Component)this.tabPopupContainer);
        }
        view.setModel(browserModel);
        return (Component)view;
    }


    public void updateSelectedItems(BrowserModel browserModel)
    {
        AbstractContentBrowser acb = this.componentMap.get(browserModel);
        if(acb != null)
        {
            acb.updateSelectedItems();
        }
        else
        {
            LOG.info("The browser '" + browserModel + "' has not been mapped to a content browser. Can not refresh active items.");
        }
    }


    public void updateActiveItems()
    {
        for(BrowserModel b : getVisibleBrowsers())
        {
            if(b instanceof AdvancedBrowserModel)
            {
                ((AdvancedBrowserModel)b).setActiveItem(getPerspective().getActiveItem());
            }
            AbstractContentBrowser acb = this.componentMap.get(b);
            if(acb != null)
            {
                acb.updateActiveItems();
                continue;
            }
            LOG.info("The browser '" + b + "' has not been mapped to a content browser. Can not refresh active items.");
        }
    }


    public void duplicateBrowser(BrowserModel browserModel)
    {
        try
        {
            boolean resetNeeded;
            setFocusedBrowser(browserModel);
            BrowserModel newBrowser = (BrowserModel)browserModel.clone();
            newBrowser.addBrowserModelListener(getBrowserListener());
            if(newBrowser.getArea() != this)
            {
                newBrowser.setArea(this);
            }
            if(isSplitModeActive() && this.visibleBrowsers.size() < 2)
            {
                resetNeeded = addVisibleBrowser(1, newBrowser);
            }
            else
            {
                resetNeeded = addVisibleBrowser(newBrowser);
            }
            setFocusedBrowser(newBrowser);
            if(resetNeeded)
            {
                update();
            }
        }
        catch(CloneNotSupportedException e)
        {
            LOG.error("Could not duplicate browser", e);
        }
    }


    public List<BrowserModel> getBrowsers()
    {
        return Collections.unmodifiableList(this.browsers);
    }


    public List<BrowserModel> getVisibleBrowsers()
    {
        return this.visibleBrowsers;
    }


    public List<BrowserModel> getHiddenBrowsers()
    {
        List<BrowserModel> ret = new ArrayList<>(3);
        for(BrowserModel b : this.browsers)
        {
            if(!this.visibleBrowsers.contains(b))
            {
                ret.add(b);
            }
            if(ret.size() == 2)
            {
                break;
            }
        }
        return Collections.unmodifiableList(ret);
    }


    public void hide(BrowserModel browserModel)
    {
        if(addHiddenBrowser(browserModel))
        {
            if(getFocusedBrowser() == browserModel && !getVisibleBrowsers().isEmpty())
            {
                setFocusedBrowser(getVisibleBrowsers().get(0));
            }
            update();
        }
    }


    public void show(BrowserModel browserModel)
    {
        boolean resetNeeded = addVisibleBrowser(browserModel, true);
        setFocusedBrowser(browserModel);
        if(resetNeeded)
        {
            update();
        }
    }


    public void close(BrowserModel browserModel)
    {
        if(isClosable(browserModel))
        {
            int vis_position = getVisibleBrowsers().indexOf(browserModel);
            if(removeBrowser(browserModel))
            {
                if(getFocusedBrowser() == browserModel && !getVisibleBrowsers().isEmpty())
                {
                    setFocusedBrowser(getVisibleBrowsers().get(0));
                }
                if(this.lastMinimizedBrowser == browserModel)
                {
                    this.lastMinimizedBrowser = null;
                }
                if(getBrowsers().size() < 2)
                {
                    this.splitmodeActivated = false;
                }
                if(getVisibleBrowsers().isEmpty())
                {
                    BrowserModel previousBrowser = getPreviousBrowser();
                    show((previousBrowser == null) ? this.browsers.get(0) : previousBrowser);
                }
                else if(isSplitModeActive() && getVisibleBrowsers().size() == 1)
                {
                    if(getHiddenBrowsers().size() > 0)
                    {
                        BrowserModel toShow = getPreviousBrowser();
                        if(toShow != null)
                        {
                            if(vis_position == 1)
                            {
                                addVisibleBrowser(1, toShow);
                            }
                            else
                            {
                                addVisibleBrowser(toShow);
                            }
                            update();
                            this.lastMinimizedBrowser = null;
                        }
                        else
                        {
                            this.splitmodeActivated = false;
                        }
                    }
                    else
                    {
                        LOG.error("Inconsistent state in browser area.");
                    }
                }
                else
                {
                    update();
                }
            }
            fireBrowserClosed(browserModel);
        }
    }


    public void closeOthers(BrowserModel browserModel)
    {
        List<BrowserModel> closed = new ArrayList<>();
        for(Object o : getBrowsers().toArray())
        {
            if(o instanceof BrowserModel)
            {
                BrowserModel browser = (BrowserModel)o;
                if(!browser.equals(browserModel))
                {
                    if(isClosable(browser))
                    {
                        removeBrowser(browser);
                        closed.add(browser);
                    }
                }
            }
        }
        setFocusedBrowser(browserModel);
        this.lastMinimizedBrowser = null;
        if(getVisibleBrowsers().isEmpty())
        {
            show(browserModel);
        }
        else
        {
            update();
        }
        if(!closed.isEmpty())
        {
            fireBrowsersClosed(closed);
        }
    }


    public void replaceBrowser(BrowserModel oldBrowser, BrowserModel newBrowser)
    {
        if(newBrowser != null)
        {
            boolean focused = (getFocusedBrowser() == oldBrowser);
            boolean visible = removeBrowser(oldBrowser);
            boolean newAdded = !this.browsers.contains(newBrowser);
            boolean newOpened = false;
            if(visible)
            {
                newOpened = addVisibleBrowser(newBrowser);
                update();
                if(focused)
                {
                    setFocusedBrowser(newBrowser);
                }
            }
            if(visible)
            {
                fireBrowserClosed(oldBrowser);
            }
            if(newAdded)
            {
                fireBrowserAdded(newBrowser);
            }
            else if(newOpened)
            {
                fireBrowserOpened(newBrowser);
            }
        }
    }


    public void replaceBrowser(int index, BrowserModel newBrowser)
    {
        if(index >= 0 && index < this.visibleBrowsers.size())
        {
            replaceBrowser(this.visibleBrowsers.get(index), newBrowser);
        }
    }


    public boolean isPossibleAddContextSubTypes()
    {
        return this.possibleAddContextSubTypes;
    }


    public boolean isClosable(BrowserModel browserModel)
    {
        return (this.browsers.size() > 1);
    }


    public boolean isMinimizable(BrowserModel browserModel)
    {
        return (this.browsers.contains(browserModel) && this.browsers.size() > 1);
    }


    public UICockpitPerspective getPerspective()
    {
        return this.perspective;
    }


    public void setPerspective(UICockpitPerspective perspective)
    {
        this.perspective = perspective;
    }


    public void setFocusedBrowser(BrowserModel browserModel)
    {
        setFocus(true);
        if(this.focusedBrowser != browserModel && getVisibleBrowsers().contains(browserModel))
        {
            if(isBrowserMinimized(browserModel))
            {
                addVisibleBrowser(browserModel, true);
            }
            if(this.focusedBrowser != null)
            {
                AbstractContentBrowser oldCb = this.componentMap.get(this.focusedBrowser);
                if(oldCb != null)
                {
                    oldCb.setFocus(false);
                }
            }
            this.focusedBrowser = browserModel;
            if(this.focusedBrowser != null)
            {
                AbstractContentBrowser contentBrowser = this.componentMap.get(browserModel);
                if(contentBrowser != null)
                {
                    contentBrowser.setFocus(true);
                }
            }
            fireBrowserFocused(browserModel);
        }
    }


    public BrowserModel getFocusedBrowser()
    {
        return this.focusedBrowser;
    }


    public boolean isBrowserMinimized(BrowserModel browserModel)
    {
        boolean minimized = true;
        if(this.browsers.contains(browserModel) && this.visibleBrowsers.contains(browserModel))
        {
            minimized = false;
        }
        return minimized;
    }


    public void addBrowserAreaListener(BrowserAreaListener listener)
    {
        if(!this.browserAreaListeners.contains(listener))
        {
            this.browserAreaListeners.add(listener);
        }
    }


    public void removeBrowserAreaListener(BrowserAreaListener listener)
    {
        if(this.browserAreaListeners.contains(listener))
        {
            this.browserAreaListeners.remove(listener);
        }
    }


    protected boolean addBrowser(BrowserModel browserModel)
    {
        boolean added = false;
        if(browserModel == null)
        {
            LOG.error("browser is null");
        }
        else if(!this.browsers.contains(browserModel))
        {
            this.browsers.add(browserModel);
            browserModel.addBrowserModelListener(getBrowserListener());
            browserModel.setArea(this);
            added = true;
        }
        return added;
    }


    protected boolean addBrowser(int index, BrowserModel browserModel)
    {
        boolean added = false;
        if(index >= 0 && index <= this.browsers.size())
        {
            if(browserModel == null)
            {
                LOG.error("browser is null");
            }
            else if(!this.browsers.contains(browserModel))
            {
                this.browsers.add(index, browserModel);
                browserModel.addBrowserModelListener(getBrowserListener());
                browserModel.setArea(this);
                added = true;
            }
        }
        else
        {
            LOG.warn("Invalid browser index specified.");
            added = addBrowser(browserModel);
        }
        return added;
    }


    public BrowserModel getPreviousBrowser()
    {
        BrowserModel ret = null;
        if(this.lastMinimizedBrowser != null)
        {
            ret = this.lastMinimizedBrowser;
        }
        else if(getHiddenBrowsers().size() > 0)
        {
            ret = getHiddenBrowsers().get(0);
        }
        return ret;
    }


    public boolean addHiddenBrowser(BrowserModel browserModel)
    {
        boolean resetNeeded = false;
        addBrowser(browserModel);
        if(isMinimizable(browserModel))
        {
            if(this.visibleBrowsers.contains(browserModel))
            {
                this.visibleBrowsers.remove(browserModel);
                this.splitmodeActivated = false;
                this.lastMinimizedBrowser = browserModel;
                if(browserModel instanceof AbstractBrowserModel)
                {
                    ((AbstractBrowserModel)browserModel).onHide();
                }
            }
            resetNeeded = true;
            fireBrowserMinimized(browserModel);
        }
        return resetNeeded;
    }


    public boolean addVisibleBrowser(BrowserModel browserModel)
    {
        return addVisibleBrowser(0, browserModel);
    }


    @Deprecated
    public boolean addVisibleBrowser(BrowserModel browserModel, boolean replaceInactive)
    {
        return addVisibleBrowser(browserModel);
    }


    public boolean addVisibleBrowser(int index, BrowserModel browserModel)
    {
        boolean viewChanged = false;
        boolean newAdded = addBrowser(index, browserModel);
        List<BrowserModel> visBrowsers = getVisibleBrowsers();
        if(!visBrowsers.contains(browserModel))
        {
            viewChanged = true;
            if(isSplitModeActive())
            {
                if(index == 0 && visBrowsers.size() == 1)
                {
                    BrowserModel splittedBrowser = this.visibleBrowsers.get(0);
                    this.visibleBrowsers.clear();
                    this.visibleBrowsers.add(browserModel);
                    this.visibleBrowsers.add(splittedBrowser);
                }
                else if(index >= 0 && index < visBrowsers.size())
                {
                    this.lastMinimizedBrowser = this.visibleBrowsers.get(index);
                    fireBrowserMinimized(this.visibleBrowsers.get(index));
                    this.visibleBrowsers.set(index, browserModel);
                }
                else
                {
                    if(visBrowsers.size() > 1)
                    {
                        this.lastMinimizedBrowser = this.visibleBrowsers.get(1);
                        visBrowsers.remove(1);
                    }
                    this.visibleBrowsers.add(browserModel);
                }
            }
            else
            {
                if(visBrowsers.size() == 1)
                {
                    this.lastMinimizedBrowser = this.visibleBrowsers.get(0);
                }
                for(BrowserModel prevVisBrowsers : visBrowsers)
                {
                    if(prevVisBrowsers instanceof AbstractBrowserModel)
                    {
                        try
                        {
                            ((AbstractBrowserModel)prevVisBrowsers).onHide();
                        }
                        catch(Exception e)
                        {
                            LOG.error("An error occurred when minimizing browser.", e);
                        }
                    }
                }
                visBrowsers.clear();
                this.visibleBrowsers.add(browserModel);
                if(browserModel instanceof AbstractBrowserModel)
                {
                    try
                    {
                        ((AbstractBrowserModel)browserModel).onShow();
                    }
                    catch(Exception e)
                    {
                        LOG.error("An error occurred when restoring browser.", e);
                    }
                }
            }
            if(newAdded)
            {
                fireBrowserAdded(browserModel);
            }
            fireBrowserOpened(browserModel);
        }
        return viewChanged;
    }


    protected boolean removeBrowser(BrowserModel browser)
    {
        if(browser != null)
        {
            if(browser instanceof AbstractBrowserModel)
            {
                ((AbstractBrowserModel)browser).onClose();
            }
            browser.setArea(null);
            browser.removeBrowserModelListener(getBrowserListener());
            this.browsers.remove(browser);
            this.visibleBrowsers.remove(browser);
            if(this.lastMinimizedBrowser == browser)
            {
                this.lastMinimizedBrowser = null;
            }
        }
        return true;
    }


    protected void fireSelectionChanged(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.selectionChanged(browserModel);
        }
    }


    protected void fireBrowserClosed(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browsersClosed(Collections.singletonList(browserModel));
        }
    }


    protected void fireBrowsersClosed(List<BrowserModel> browsers)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browsersClosed(browsers);
        }
    }


    protected void fireBrowserMinimized(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserMinimized(browserModel);
        }
    }


    protected void fireBrowserOpened(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserOpened(browserModel);
        }
    }


    protected void fireBrowserAdded(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserAdded(browserModel);
        }
    }


    protected void fireBrowserChanged(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserChanged(browserModel);
        }
        updateTab(browserModel);
    }


    protected void fireBrowserFocused(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserFocused(browserModel);
        }
    }


    protected void fireBrowserQuerySaved(BrowserModel browserModel)
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.browserQuerySaved(browserModel);
        }
    }


    protected void fireSplitmodeChanged()
    {
        for(BrowserAreaListener bal : this.browserAreaListeners)
        {
            bal.splitmodeChanged();
        }
    }


    protected void fireItemActivated(TypedObject activeItem)
    {
        for(BrowserAreaListener browserAreaListener : getBrowserAreaListeners())
        {
            browserAreaListener.itemActivated(activeItem);
        }
    }


    protected void fireItemsDropped(BrowserModel browser, Collection<TypedObject> items)
    {
        for(BrowserAreaListener browserAreaListener : getBrowserAreaListeners())
        {
            browserAreaListener.itemsDropped(browser, items);
        }
    }


    public UICockpitPerspective getManagingPerspective()
    {
        return getPerspective();
    }


    public boolean isFocused()
    {
        return equals(getManagingPerspective().getFocusedArea());
    }


    public void setPossibleAddContextSubTypes(boolean possibleAddContextSubTypes)
    {
        this.possibleAddContextSubTypes = possibleAddContextSubTypes;
    }


    public void setFocus(boolean focus)
    {
        if(getManagingPerspective() != null)
        {
            getManagingPerspective().setFocusedArea((FocusablePerspectiveArea)this);
        }
    }


    public abstract BrowserModelListener getBrowserListener();


    public void setSplitModeActiveDirectly(boolean splitmodeActivated)
    {
        this.splitmodeActivated = splitmodeActivated;
    }


    public void setSplitModeActive(boolean splitmodeActivated)
    {
        this.splitmodeActivated = splitmodeActivated;
        if(this.splittable)
        {
            if(splitmodeActivated)
            {
                if(getBrowsers().size() < 2)
                {
                    if(((BrowserModel)getBrowsers().get(0)).isDuplicatable())
                    {
                        duplicateBrowser(getBrowsers().get(0));
                    }
                    else if(providesDefaultBrowser())
                    {
                        addVisibleBrowser(createNewDefaultBrowser());
                    }
                    else
                    {
                        return;
                    }
                }
                else
                {
                    addVisibleBrowser(1, getPreviousBrowser());
                    update();
                }
                fireSplitmodeChanged();
            }
            else if(getVisibleBrowsers().size() > 1)
            {
                List<BrowserModel> visBrowsers = new ArrayList<>(getVisibleBrowsers());
                for(BrowserModel b : visBrowsers)
                {
                    if(!b.equals(getFocusedBrowser()))
                    {
                        hide(b);
                    }
                }
                fireSplitmodeChanged();
            }
        }
    }


    public boolean isSplitModeActive()
    {
        return (this.splittable && this.splitmodeActivated);
    }


    public void setSplittable(boolean splittable)
    {
        this.splittable = splittable;
    }


    public boolean isSplittable()
    {
        return (this.splittable && checkSplitActivatable());
    }


    private boolean checkSplitActivatable()
    {
        return (getBrowsers().size() > 1 || getFocusedBrowser().isDuplicatable());
    }


    public void addCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.add(acceptor);
    }


    public void removeCockpitEventAcceptor(CockpitEventAcceptor acceptor)
    {
        this.cockpitEventAcceptors.remove(acceptor);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent ice;
            TypedObject object;
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[((ItemChangedEvent)event).getChangeType().ordinal()])
            {
                case 1:
                    ice = (ItemChangedEvent)event;
                    object = ice.getItem();
                    for(BrowserModel b : getVisibleBrowsers())
                    {
                        AbstractContentBrowser acb = this.componentMap.get(b);
                        if(acb != null)
                        {
                            updateTab(b);
                            if(object == null)
                            {
                                BrowserModel model = acb.getModel();
                                if(model != null)
                                {
                                    model.updateItems();
                                }
                                continue;
                            }
                            acb.updateItem(object, ice.getProperties(), ice.getSource());
                            continue;
                        }
                        LOG.info("The browser '" + b + "' has not been mapped to a content browser. Can not refresh items.");
                    }
                    if(object == null)
                    {
                        closeInspector();
                        break;
                    }
                    if(isInfoAreaOpen())
                    {
                        updateInfoArea(ListProviders.singletonListProvider(object));
                    }
                    break;
                case 2:
                    for(BrowserModel visBrowser : getVisibleBrowsers())
                    {
                        visBrowser.updateItems();
                    }
                    break;
            }
        }
        else if(event instanceof SearchEvent)
        {
            BrowserModel browser = createNewDefaultBrowser();
            if(browser instanceof SearchBrowserModel)
            {
                SearchBrowserModel searchBrowser = (SearchBrowserModel)browser;
                searchBrowser.setSimpleQuery(((SearchEvent)event).getQuery());
                searchBrowser.updateItems();
                if(addVisibleBrowser((BrowserModel)searchBrowser))
                {
                    update();
                }
            }
            else
            {
                LOG.info("Can not execute search. Reason: Browser area's default browser is not a search browser.");
            }
        }
        if(event instanceof SelectIndexesEvent)
        {
            boolean update = false;
            for(BrowserModel b : getVisibleBrowsers())
            {
                if(b.getClass().isAssignableFrom(((SelectIndexesEvent)event).getBrowserClass()))
                {
                    b.setSelectedIndexes(((SelectIndexesEvent)event).getIndexes());
                    update = true;
                }
            }
            if(update)
            {
                update();
            }
        }
        for(CockpitEventAcceptor acceptor : this.cockpitEventAcceptors)
        {
            acceptor.onCockpitEvent(event);
        }
    }


    protected boolean isInfoAreaOpen()
    {
        Component infoArea = getInfoArea();
        if(infoArea != null)
        {
            East parentEast = (East)UITools.getNextParentOfType(East.class, infoArea, 10);
            if(parentEast != null && parentEast.isOpen())
            {
                return true;
            }
        }
        return false;
    }


    public String getLabel()
    {
        return null;
    }


    public void setMultiSelectActions(ActionColumnConfiguration multiSelectActions)
    {
        this.multiSelectActions = multiSelectActions;
    }


    public ActionColumnConfiguration getMultiSelectActions()
    {
        return this.multiSelectActions;
    }


    public void setMultiSelectContextActions(ActionColumnConfiguration multiSelectContextActions)
    {
        this.multiSelectContextActions = multiSelectContextActions;
    }


    public ActionColumnConfiguration getMultiSelectContextActions()
    {
        return this.multiSelectContextActions;
    }


    public void setMultiSelectContextActionsRegistry(ContextAreaActionColumnConfigurationRegistry multiSelectContextActionsRegistry)
    {
        this.multiSelectContextActionsRegistry = multiSelectContextActionsRegistry;
    }


    public ContextAreaActionColumnConfigurationRegistry getMultiSelectContextActionsRegistry()
    {
        return this.multiSelectContextActionsRegistry;
    }


    public void setAdditionalToolbarActions(ActionColumnConfiguration additionalToolbarActions)
    {
        this.additionalToolbarActions = additionalToolbarActions;
    }


    public ActionColumnConfiguration getAdditionalToolbarActions()
    {
        return this.additionalToolbarActions;
    }


    public Map<String, String> getDefaultBrowserViewMapping()
    {
        return (this.defaultBrowserViewMapping == null) ? Collections.EMPTY_MAP : this.defaultBrowserViewMapping;
    }


    public void setDefaultBrowserViewMapping(Map<String, String> defaultBrowserViewMapping)
    {
        this.defaultBrowserViewMapping = defaultBrowserViewMapping;
    }


    public void setDefaultBrowserClass(String defaultBrowserClass)
    {
        this.defaultBrowserClass = defaultBrowserClass;
    }


    public String getDefaultBrowserClass()
    {
        return this.defaultBrowserClass;
    }


    protected boolean isShowAsClosable(BrowserModel browserModel)
    {
        return true;
    }


    public void setShowCreateDefaultBrowserButton(boolean showCreateDefaultBrowserButton)
    {
        this.showCreateDefaultBrowserButton = showCreateDefaultBrowserButton;
    }


    public boolean isShowCreateDefaultBrowserButton()
    {
        return this.showCreateDefaultBrowserButton;
    }


    public void setInspectorRenderer(InspectorRenderer inspectorRenderer)
    {
        this.inspectorRenderer = inspectorRenderer;
    }


    protected InspectorRenderer getInspectorRenderer()
    {
        return this.inspectorRenderer;
    }


    public void setOpenInspectorOnSelect(boolean openInspectorOnSelect)
    {
        this.openInspectorOnSelect = openInspectorOnSelect;
    }


    public boolean isOpenInspectorOnSelect()
    {
        return this.openInspectorOnSelect;
    }


    public void setInfoAreaContainerId(String infoAreaContainerId)
    {
        this.infoAreaContainerId = infoAreaContainerId;
    }
}
