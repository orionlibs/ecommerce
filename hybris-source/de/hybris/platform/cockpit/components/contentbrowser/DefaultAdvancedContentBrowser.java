package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Span;

public class DefaultAdvancedContentBrowser extends AbstractContentContextBrowser
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAdvancedContentBrowser.class);
    protected Map<String, MainAreaComponentFactory> viewModeMapping = null;
    protected static final String DND_ID = "PerspectiveDND";
    protected static final String COCKPIT_ID_BROWSERAREA_ITEMS_TOTAL = "browserArea_itemsInTotal";
    protected transient Borderlayout mainBorderlayout = null;
    protected boolean desktopInvalid = false;


    public void setModel(BrowserModel model)
    {
        if(model instanceof AdvancedBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Model not of type 'AdvancedBrowserModel'.");
        }
    }


    public AdvancedBrowserModel getModel()
    {
        return (AdvancedBrowserModel)super.getModel();
    }


    public void resize()
    {
        if(this.mainBorderlayout != null)
        {
            North north = this.mainBorderlayout.getNorth();
            if(north != null)
            {
                north.invalidate();
            }
        }
    }


    protected boolean initialize()
    {
        getChildren().clear();
        this.initialized = false;
        if(getModel() != null)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.debug("Initializing...");
            }
            long start = System.currentTimeMillis();
            setRealSclass("contentBrowserComponent");
            AbstractContentBrowser.BrowserFocusDiv mainAndStatusCnt = new AbstractContentBrowser.BrowserFocusDiv((AbstractContentBrowser)this);
            appendChild((Component)mainAndStatusCnt);
            mainAndStatusCnt.setStyle("position: relative; width: 100%; height: 100%;");
            this.statusBar = new Div();
            this.statusBar.setSclass("browserstatus");
            this.statusBar.setStyle("position: absolute; width: 100%; left: 0; bottom: 0;");
            Div mainBLCnt = new Div();
            mainBLCnt.setClass("browserMainContentBox");
            if(getModel().hasStatusBar())
            {
                mainAndStatusCnt.appendChild((Component)this.statusBar);
                UITools.modifySClass((HtmlBasedComponent)mainBLCnt, "mainContentAboveStatus", true);
            }
            mainAndStatusCnt.appendChild((Component)mainBLCnt);
            this.mainBorderlayout = new Borderlayout();
            mainBLCnt.appendChild((Component)this.mainBorderlayout);
            this.mainBorderlayout.setSclass("browserMainBorderLayout");
            this.mainBorderlayout.setStyle("position: absolute; left: 0; top: 0;");
            this.mainBorderlayout.setWidth("100%");
            this.mainBorderlayout.setHeight("100%");
            this.mainBorderlayout.addEventListener("onResize", (EventListener)new Object(this));
            mainBLCnt.setAction("onmouseup: comm.sendUser(this);");
            mainBLCnt.addEventListener("onUser", (EventListener)new Object(this));
            Div focusComponent = new Div();
            mainBLCnt.appendChild((Component)focusComponent);
            focusComponent.setWidth("100%");
            focusComponent.setHeight("100%");
            focusComponent.setSclass("contentBrowserFocusedOverlay");
            focusComponent.setDroppable("PerspectiveDND");
            setFocusComponent((HtmlBasedComponent)focusComponent);
            focusComponent.addEventListener("onClick", (EventListener)new Object(this));
            focusComponent.addEventListener("onDrop", (EventListener)new Object(this));
            North north = new North();
            north.setBorder("0");
            north.setStyle("overflow: visible");
            Div containerDiv = new Div();
            containerDiv.setStyle("position:relative;");
            north.appendChild((Component)containerDiv);
            this.captionComponent = createCaptionComponent();
            if(this.captionComponent != null)
            {
                this.captionComponent.initialize();
                Div captionDiv = new Div();
                captionDiv.setWidth("100%");
                captionDiv.appendChild((Component)this.captionComponent);
                containerDiv.appendChild((Component)captionDiv);
            }
            this.toolbarComponent = createToolbarComponent();
            if(this.toolbarComponent != null)
            {
                this.toolbarComponent.initialize();
                Div toolbarDiv = new Div();
                toolbarDiv.setWidth("100%");
                toolbarDiv.appendChild((Component)this.toolbarComponent);
                containerDiv.appendChild((Component)toolbarDiv);
            }
            this.mainBorderlayout.appendChild((Component)north);
            Center center = new Center();
            center.setBorder("none");
            this.mainBorderlayout.appendChild((Component)center);
            this.mainAreaComponent = createMainAreaComponent();
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.initialize();
                center.appendChild((Component)this.mainAreaComponent);
            }
            else
            {
                center.appendChild((Component)new Span());
            }
            South south = new South();
            south.setBorder("none");
            south.setHeight("30%");
            south.setVisible(false);
            south.setSplittable(true);
            this.mainBorderlayout.appendChild((Component)south);
            this.contextAreaComponent = createContextAreaComponent();
            if(this.contextAreaComponent != null)
            {
                this.contextAreaComponent.initialize();
                south.appendChild((Component)this.contextAreaComponent);
            }
            else
            {
                south.appendChild((Component)new Span());
            }
            if(LOG.isInfoEnabled())
            {
                LOG.debug("Initialization done (" + System.currentTimeMillis() - start + " ms).");
            }
            this.initialized = true;
            updateStatusBar();
        }
        return this.initialized;
    }


    public void updateActiveItems()
    {
        if(!this.desktopInvalid)
        {
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.updateActiveItems();
            }
            if(this.contextAreaComponent != null)
            {
                this.contextAreaComponent.updateActiveItems();
            }
        }
    }


    public void updateSelectedItems()
    {
        if(!this.desktopInvalid)
        {
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.updateSelectedItems();
            }
            if(this.contextAreaComponent != null)
            {
                this.contextAreaComponent.updateSelectedItems();
            }
        }
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        if(!this.desktopInvalid)
        {
            if(this.captionComponent != null)
            {
                this.captionComponent.updateItem(item, modifiedProperties, reason);
            }
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.updateItem(item, modifiedProperties, reason);
            }
            if(this.contextAreaComponent != null)
            {
                this.contextAreaComponent.updateItem(item, modifiedProperties, reason);
            }
        }
    }


    public boolean update()
    {
        boolean success = false;
        if(!this.desktopInvalid)
        {
            if(this.initialized)
            {
                if(this.captionComponent != null)
                {
                    this.captionComponent.update();
                }
                if(this.toolbarComponent != null)
                {
                    this.toolbarComponent.update();
                }
                if(this.mainAreaComponent != null)
                {
                    this.mainAreaComponent.update();
                }
                if(this.contextAreaComponent != null)
                {
                    if(this.contextAreaComponent instanceof AbstractContextBrowserComponent)
                    {
                        ((AbstractContextBrowserComponent)this.contextAreaComponent).update(false);
                    }
                    else
                    {
                        this.contextAreaComponent.update();
                    }
                }
                if(this.statusBar != null)
                {
                    updateStatusBar();
                }
                success = true;
            }
            else
            {
                success = initialize();
            }
        }
        return success;
    }


    public void updateStatusBar()
    {
        if(!this.desktopInvalid)
        {
            if(this.statusBar != null && !UITools.isFromOtherDesktop((Component)this.statusBar))
            {
                String statusLabel = "";
                int totalCount = getModel().getTotalCount();
                String itemLabel = (totalCount == 1) ? Labels.getLabel("browser.item") : Labels.getLabel("browser.items");
                statusLabel = statusLabel + statusLabel + " " + totalCount + " - " + itemLabel + " " + (getModel().isAllMarked() ? totalCount : getModel().getSelectedItems().size());
                this.statusBar.getChildren().clear();
                Label label = new Label(statusLabel);
                UITools.modifySClass((HtmlBasedComponent)label, "itemLabel", true);
                if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                {
                    UITools.applyTestID((Component)label, "browserArea_itemsInTotal");
                }
                Hbox hbox = new Hbox();
                hbox.setWidth("100%");
                this.statusBar.appendChild((Component)hbox);
                hbox.appendChild((Component)label);
                if(getModel() instanceof PageableBrowserModel)
                {
                    Div rightPagingComponents = new Div();
                    rightPagingComponents.setAlign("right");
                    hbox.appendChild((Component)rightPagingComponents);
                    Hbox pagingContainer = new Hbox();
                    rightPagingComponents.appendChild((Component)pagingContainer);
                    PageableBrowserModel pageableModel = (PageableBrowserModel)getModel();
                    pagingContainer.appendChild((Component)createPageSizeCombobox(pageableModel));
                    pagingContainer.appendChild((Component)createCustomPager(pageableModel));
                }
            }
            updateToolbar();
        }
    }


    public void updateActivation()
    {
        if(!this.desktopInvalid)
        {
            TypedObject activeItem = getModel().getActiveItem();
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.setActiveItem(activeItem);
            }
            if(this.contextAreaComponent != null)
            {
                this.contextAreaComponent.setActiveItem(activeItem);
            }
        }
    }


    protected void showContextArea()
    {
        if(!this.desktopInvalid)
        {
            if(getModel().isContextVisible() != this.mainBorderlayout.getSouth().isVisible())
            {
                this.mainBorderlayout.getSouth().setVisible(getModel().isContextVisible());
                this.mainBorderlayout.resize();
            }
        }
    }


    public void updateContextArea()
    {
        if(!this.desktopInvalid)
        {
            if(this.contextAreaComponent != null)
            {
                showContextArea();
                this.contextAreaComponent.update();
            }
        }
    }


    public void updateContextArea(boolean cleanContextHeader)
    {
        if(!this.desktopInvalid)
        {
            if(this.contextAreaComponent != null)
            {
                if(this.contextAreaComponent instanceof AbstractContextBrowserComponent)
                {
                    showContextArea();
                    ((AbstractContextBrowserComponent)this.contextAreaComponent).update(cleanContextHeader);
                }
                else
                {
                    showContextArea();
                    this.contextAreaComponent.update();
                }
            }
        }
    }


    public void updateViewMode()
    {
        if(!this.desktopInvalid)
        {
            updateToolbar();
            this.mainAreaComponent = createMainAreaComponent();
            this.mainAreaComponent.initialize();
            this.mainBorderlayout.getCenter().getChildren().clear();
            this.mainBorderlayout.getCenter().appendChild((Component)this.mainAreaComponent);
        }
    }


    public void updateMainArea()
    {
        if(!this.desktopInvalid)
        {
            if(this.mainAreaComponent != null)
            {
                this.mainAreaComponent.update();
            }
        }
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new CaptionBrowserComponent((BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return null;
    }


    private void initViewModeMapping(Collection<? extends MainAreaComponentFactory> modes)
    {
        this.viewModeMapping = new LinkedHashMap<>();
        for(MainAreaComponentFactory mainAreaComponentFactory : modes)
        {
            this.viewModeMapping.put(mainAreaComponentFactory.getViewModeID(), mainAreaComponentFactory);
        }
    }


    public Map<String, MainAreaComponentFactory> getViewModes()
    {
        if(this.viewModeMapping == null)
        {
            initViewModeMapping(getModel().getAvailableViewModes());
        }
        return (this.viewModeMapping == null) ? Collections.EMPTY_MAP : this.viewModeMapping;
    }


    protected AbstractBrowserComponent createDefaultViewModeComponent()
    {
        return (AbstractBrowserComponent)new MainAreaGridviewBrowserComponent(getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        AbstractMainAreaBrowserComponent abstractMainAreaBrowserComponent;
        AbstractBrowserComponent abstractBrowserComponent1;
        MainAreaComponentFactory viewModeFactory = getViewModes().get(getModel().getViewMode());
        AbstractBrowserComponent ret = null;
        if(viewModeFactory != null)
        {
            try
            {
                abstractMainAreaBrowserComponent = viewModeFactory.createInstance(getModel(), (AbstractContentBrowser)this);
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        if(abstractMainAreaBrowserComponent == null)
        {
            abstractBrowserComponent1 = createDefaultViewModeComponent();
            LOG.warn("Could not create viewmode '" + getModel().getViewMode() + "', using default instead");
        }
        return abstractBrowserComponent1;
    }


    protected AbstractBrowserComponent createContextAreaComponent()
    {
        return (AbstractBrowserComponent)new ContextAreaBrowserComponent((BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    public HtmlBasedComponent createCustomPager(PageableBrowserModel pageableModel)
    {
        CustomPaging customPaging = new CustomPaging(pageableModel.isSimplePaging(), getDefaultPagingMold());
        customPaging.init();
        customPaging.addEventListener("onPagingModeChange", (EventListener)new Object(this, pageableModel));
        UITools.addBusyListener((Component)customPaging, "onPaging", (EventListener)new Object(this, pageableModel, customPaging), null, null);
        customPaging.setAutohide(false);
        customPaging.setDetailed(false);
        customPaging.setTotalSize(pageableModel.getTotalCount());
        customPaging.setPageSize(pageableModel.getPageSize());
        customPaging.setActivePage(pageableModel.getCurrentPage());
        customPaging.setVisible(!currentViewHasOwnModel());
        return (HtmlBasedComponent)customPaging;
    }


    public HtmlBasedComponent createPageSizeCombobox(PageableBrowserModel pageableModel)
    {
        Combobox pageSizeCombobox = new Combobox();
        PageSizeChangeListener pageSizeChangeListener = new PageSizeChangeListener(pageableModel);
        pageSizeCombobox.setTooltiptext(Labels.getLabel("browserarea.pagesize"));
        pageSizeCombobox.addEventListener("onChange", (EventListener)pageSizeChangeListener);
        pageSizeCombobox.addEventListener("onOK", (EventListener)pageSizeChangeListener);
        pageSizeCombobox.addEventListener("onLater", (EventListener)pageSizeChangeListener);
        pageSizeCombobox.setReadonly(false);
        pageSizeCombobox.setCols(3);
        List<Object> pageSizesList = new ArrayList(pageableModel.getPageSizes());
        pageSizesList.add(Labels.getLabel("browserarea.show_max"));
        ListModelList pageSizes = new ListModelList(pageSizesList);
        pageSizeCombobox.setModel((ListModel)pageSizes);
        pageSizeCombobox.setValue(String.valueOf(pageableModel.getPageSize()));
        Object pageSize = Integer.valueOf(pageableModel.getPageSize());
        if(((AbstractPageableBrowserModel)pageableModel).getMaxPageSize() == pageableModel.getPageSize())
        {
            pageSize = Labels.getLabel("browserarea.show_max");
        }
        int selectedPageSizeIndex = pageSizes.indexOf(pageSize);
        if(selectedPageSizeIndex >= 0 && selectedPageSizeIndex < pageSizeCombobox.getChildren().size())
        {
            pageSizeCombobox.setSelectedIndex(selectedPageSizeIndex);
        }
        pageSizeCombobox.setVisible(!currentViewHasOwnModel());
        if(Executions.getCurrent().getDesktop() != null)
        {
            String modifier = UITools.getCockpitParameter("default.pageSizeCombobox.readOnly", Executions.getCurrent()
                            .getDesktop());
            pageSizeCombobox.setReadonly(Boolean.parseBoolean(modifier));
        }
        return (HtmlBasedComponent)pageSizeCombobox;
    }


    protected String getDefaultPagingMold()
    {
        String pagingMold = "";
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        if(perspective != null)
        {
            pagingMold = UITools.getCockpitParameter("default.pagingMold-" + perspective.getUid(), Executions.getCurrent());
        }
        if(StringUtils.isBlank(pagingMold))
        {
            pagingMold = UITools.getCockpitParameter("default.pagingMold", Executions.getCurrent());
        }
        return pagingMold;
    }


    public boolean currentViewHasOwnModel()
    {
        boolean ret = false;
        String viewMode = getModel().getViewMode();
        MainAreaComponentFactory componentFactory = getViewModes().get(viewMode);
        if(componentFactory instanceof ContextAwareMainAreaComponentFactory)
        {
            ret = ((ContextAwareMainAreaComponentFactory)componentFactory).hasOwnModel();
        }
        return ret;
    }


    public void updateCaption()
    {
        if(!this.desktopInvalid)
        {
            if(this.captionComponent != null && !UITools.isFromOtherDesktop((Component)this.captionComponent))
            {
                this.captionComponent.update();
            }
        }
    }


    public void updateToolbar()
    {
        if(!this.desktopInvalid)
        {
            if(this.toolbarComponent != null && !UITools.isFromOtherDesktop((Component)this.toolbarComponent))
            {
                this.toolbarComponent.update();
            }
        }
    }


    public void desktopRemoved(Desktop desktop)
    {
        super.desktopRemoved(desktop);
        this.desktopInvalid = true;
    }
}
