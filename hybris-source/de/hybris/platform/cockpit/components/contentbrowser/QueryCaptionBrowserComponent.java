package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AbstractAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class QueryCaptionBrowserComponent extends CaptionBrowserComponent implements DesktopRemovalAwareComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(QueryCaptionBrowserComponent.class);
    private static final String COCKPIT_ID_BROWSERAREA_PAGESEARCH_INPUT = "BrowserArea_PageSearch_input";
    private static final String COCKPIT_ID_BROWSERAREA_PAGESEARCH_BUTTON = "BrowserArea_PageSearch_button";
    protected transient Textbox queryTextbox = null;
    protected transient Toolbarbutton advSearchBtn = null;
    protected transient Toolbarbutton saveQueryBtn = null;
    protected transient Button searchBtn = null;
    protected transient AbstractAdvancedSearchView advancedSearchArea = null;
    protected transient Checkbox stickyBtn;
    protected EventListener queryController = null;
    protected transient Div advancedSearchDiv = null;
    protected ComponentController advancedSearchController = null;
    private UIRole lastSessionRole = null;
    private ObjectTemplate lastRootType = null;
    private UIConfigurationService uiConfigurationService;


    public QueryCaptionBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            this.queryController = (EventListener)new MyQueryController(this);
            if(super.initialize())
            {
                if(this.mainGroupbox != null)
                {
                    this.advancedSearchDiv = new Div();
                    this.mainGroupbox.appendChild((Component)this.advancedSearchDiv);
                    this.advancedSearchArea = loadAdvancedSearchArea();
                    if(this.advancedSearchArea != null)
                    {
                        this.advancedSearchDiv.appendChild((Component)this.advancedSearchArea);
                    }
                }
                this.initialized = true;
            }
        }
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            if(super.update())
            {
                this.queryTextbox.setValue(getModel().getSimpleQuery());
                this.queryTextbox.setDisabled(getModel().isAdvancedSearchVisible());
                this.searchBtn.setDisabled(getModel().isAdvancedSearchVisible());
                boolean advSearchVisible = getModel().isAdvancedSearchVisible();
                UITools.modifySClass((HtmlBasedComponent)this.searchBtn, "searchbtnDisabled", advSearchVisible);
                UITools.modifySClass((HtmlBasedComponent)this.advSearchBtn, "advsearchEnabled", advSearchVisible);
                if(advSearchVisible)
                {
                    this.advSearchBtn.setImage("/cockpit/images/icon_func_advanced_search_white.png");
                }
                else
                {
                    this.advSearchBtn.setImage("/cockpit/images/icon_func_advanced_search.png");
                }
                if(this.mainGroupbox.isOpen() != advSearchVisible)
                {
                    this.mainGroupbox.setOpen(advSearchVisible);
                }
                this.advancedSearchDiv.getChildren().clear();
                this.advancedSearchArea = loadAdvancedSearchArea();
                if(this.advancedSearchArea == null)
                {
                    this.advancedSearchDiv.appendChild((Component)new Label(Labels.getLabel("advancedsearch.area.empty")));
                }
                else
                {
                    this.advancedSearchDiv.appendChild((Component)this.advancedSearchArea);
                    this.advancedSearchArea.update();
                }
                if(!getModel().isAdvancedSearchVisible() && this.advancedSearchArea != null)
                {
                    this.advancedSearchArea.setEditMode(false);
                }
                this.stickyBtn.setChecked(getModel().isAdvancedSearchSticky());
                UITools.modifySClass((HtmlBasedComponent)this.mainGroupbox, "contentBrowserStickyGroupbox", getModel()
                                .isAdvancedSearchSticky());
                success = true;
            }
            this.queryTextbox.setVisible(true);
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void setModel(BrowserModel model)
    {
        if((getModel() == null && model != null) || getModel() != model)
        {
            super.setModel(model);
            if(model instanceof SearchBrowserModel)
            {
                if(getModel() != null && getModel().getAdvancedSearchModel().getSortedByProperty() == null &&
                                !getModel().getAdvancedSearchModel().getSortableProperties().isEmpty())
                {
                    if(getModel().getAdvancedSearchModel() instanceof DefaultAdvancedSearchModel)
                    {
                        ((DefaultAdvancedSearchModel)getModel().getAdvancedSearchModel()).setSortedByProperty(getModel()
                                        .getAdvancedSearchModel().getSortableProperties().iterator().next());
                    }
                }
                initialize();
            }
        }
    }


    public SearchBrowserModel getModel()
    {
        return (SearchBrowserModel)super.getModel();
    }


    protected HtmlBasedComponent createLeftCaptionContent()
    {
        Div leftCaptionDiv = new Div();
        leftCaptionDiv.setAlign("left");
        leftCaptionDiv.setWidth("100%");
        Hbox innerCaptionLeftHbox = new Hbox();
        leftCaptionDiv.appendChild((Component)innerCaptionLeftHbox);
        innerCaptionLeftHbox.setAlign("center");
        this.queryTextbox = new Textbox(getModel().getSimpleQuery());
        innerCaptionLeftHbox.appendChild((Component)this.queryTextbox);
        this.queryTextbox.setCols(25);
        this.queryTextbox.addEventListener("onFocus", this.queryController);
        this.queryTextbox.addEventListener("onOK", this.queryController);
        this.queryTextbox.addEventListener("onLater", this.queryController);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.queryTextbox, UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop()) + "_BrowserArea_PageSearch_input");
        }
        this.advSearchBtn = new Toolbarbutton("", "/cockpit/images/icon_func_advanced_search.png");
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "ToggleSearchModeBtn_";
            UITools.applyTestID((Component)this.advSearchBtn, "ToggleSearchModeBtn_");
        }
        innerCaptionLeftHbox.appendChild((Component)this.advSearchBtn);
        this.advSearchBtn.setSclass("plainBtn openAdvSearchBtn");
        this.advSearchBtn.setDisabled(false);
        this.advSearchBtn.setTooltiptext(Labels.getLabel("advancedsearch.button.tooltip"));
        this.advSearchBtn.addEventListener("onClick", (EventListener)new Object(this));
        this.searchBtn = new Button(Labels.getLabel(""));
        innerCaptionLeftHbox.appendChild((Component)this.searchBtn);
        this.searchBtn.setSclass("searchbtn");
        this.searchBtn.setDisabled(false);
        this.searchBtn.setTooltiptext(Labels.getLabel("general.search"));
        this.searchBtn.setImage("/cockpit/images/BUTTON_search.png");
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.searchBtn, UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop()) + "_BrowserArea_PageSearch_button");
        }
        this.searchBtn.addEventListener("onClick", (EventListener)new Object(this));
        if(getModel().getArea().isSaveQueryAvailable())
        {
            this.saveQueryBtn = new Toolbarbutton("", "/cockpit/images/icon_func_search_save.png");
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "SaveQueryBtn_";
                UITools.applyTestID((Component)this.saveQueryBtn, "SaveQueryBtn_");
            }
            innerCaptionLeftHbox.appendChild((Component)this.saveQueryBtn);
            this.saveQueryBtn.setSclass("plainBtn saveQueryBtn");
            this.saveQueryBtn.setDisabled(false);
            this.saveQueryBtn.setTooltiptext(Labels.getLabel("search.button.savequery.tooltip"));
            this.saveQueryBtn.addEventListener("onClick", (EventListener)new Object(this));
        }
        return (HtmlBasedComponent)leftCaptionDiv;
    }


    protected AbstractAdvancedSearchView loadAdvancedSearchArea()
    {
        AbstractAdvancedSearchView advArea = null;
        if(getModel() != null &&
                        getModel().getAdvancedSearchModel() instanceof DefaultAdvancedSearchModel && (this.advancedSearchArea == null ||
                        !this.advancedSearchArea.getModel().equals(getModel().getAdvancedSearchModel()) || this.lastSessionRole == null ||
                        !this.lastSessionRole.equals(getUIConfigurationService().getSessionRole()) || this.lastRootType == null ||
                        !this.lastRootType.equals(getModel().getRootType())))
        {
            this.lastSessionRole = getUIConfigurationService().getSessionRole();
            this.lastRootType = getModel().getRootType();
            getModel().setRootType(getModel().getRootType());
            this.stickyBtn = new Checkbox(Labels.getLabel("advancedsearch.sticky"));
            this.stickyBtn.setSclass("plainBtn");
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "Sticky_";
                UITools.applyTestID((Component)this.stickyBtn, "Sticky_");
            }
            this.stickyBtn.setChecked(getModel().isAdvancedSearchSticky());
            this.stickyBtn.addEventListener("onCheck", (EventListener)new Object(this));
            advArea = createAdvancedSearchView((List)Collections.singletonList(this.stickyBtn), Collections.emptyList());
            advArea.setModel(getModel().getAdvancedSearchModel());
            if(this.advancedSearchController != null)
            {
                try
                {
                    this.advancedSearchController.unregisterListeners();
                }
                catch(Exception e)
                {
                    LOG.warn("Could not unregister listeners (Reason: '" + e.getMessage() + "').", e);
                }
            }
            this
                            .advancedSearchController = (ComponentController)new DefaultBrowserAdvancedSearchController(getModel(), (DefaultAdvancedSearchModel)getModel().getAdvancedSearchModel(), (UIAdvancedSearchView)advArea);
            this.advancedSearchController.initialize();
        }
        else if(getModel() != null && this.advancedSearchArea != null && this.advancedSearchArea
                        .getModel().equals(getModel().getAdvancedSearchModel()))
        {
            advArea = this.advancedSearchArea;
        }
        else
        {
            LOG.error("Could not create advanced search area (Reason: Model is null or not of type 'DefaultAdvancedSearchModel').");
        }
        return advArea;
    }


    protected AbstractAdvancedSearchView createAdvancedSearchView(List<? extends Component> leftButtons, List<? extends Component> rightButtons)
    {
        return (AbstractAdvancedSearchView)new AdvancedSearchView(leftButtons, rightButtons);
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
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


    private void cleanup()
    {
        if(this.advancedSearchController != null)
        {
            this.advancedSearchController.unregisterListeners();
        }
    }


    private UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }
}
