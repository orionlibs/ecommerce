package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

public class PagerToolbarBrowserComponent extends AbstractMultiViewToolbarBrowserComponent
{
    private transient Div additionalToolbarSlot = null;
    protected transient Div createNewToolbarSlot = null;


    public PagerToolbarBrowserComponent(PageableBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((BrowserModel)model, contentBrowser);
    }


    public PageableBrowserModel getModel()
    {
        return (PageableBrowserModel)super.getModel();
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            setHeight("100%");
            setWidth("100%");
            getChildren().clear();
            appendChild((Component)createToolbar());
            this.initialized = true;
        }
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateViewModeButtons();
            updateMultiSelectActionArea();
            updateCommentToolbarSlot();
            if(getModel().getViewMode() == null || (this.additionalToolbarSlot != null &&
                            !getModel().getViewMode().equals(this.additionalToolbarSlot
                                            .getAttribute("viewmodeid"))))
            {
                this.additionalToolbarSlot.getChildren().clear();
            }
            updateAddtionalToolbarActionArea();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    protected void updateAddtionalToolbarActionArea()
    {
        ActionColumnConfiguration actionConfig = getAddtionalToolbarActionConfig();
        updateActionArea(actionConfig, (HtmlBasedComponent)getAdditionalToolbarSlot(), Boolean.FALSE);
    }


    protected HtmlBasedComponent createToolbar()
    {
        Div toolbar = new Div();
        toolbar.setHeight("2.4em");
        toolbar.setSclass("query-browser-paging");
        Hbox browserToolbarHbox = new Hbox();
        toolbar.appendChild((Component)browserToolbarHbox);
        browserToolbarHbox.setWidth("100%");
        browserToolbarHbox.setAlign("center");
        browserToolbarHbox.appendChild((Component)createLeftToolbarContent());
        browserToolbarHbox.appendChild((Component)createRightToolbarContent());
        return (HtmlBasedComponent)toolbar;
    }


    protected HtmlBasedComponent createLeftToolbarContent()
    {
        HtmlBasedComponent leftDiv = super.createLeftToolbarContent();
        Hbox leftHbox = new Hbox();
        leftHbox.appendChild((Component)leftDiv);
        leftHbox.appendChild((Component)getMultiSelectActionArea());
        this.additionalToolbarSlot = new Div();
        this.additionalToolbarSlot.setSclass("toolbar_multiselect_action");
        leftHbox.appendChild((Component)getAdditionalToolbarSlot());
        Div commentToolbarSlot = new Div();
        commentToolbarSlot.setStyle("display:inline");
        setCommentToolbarSlot(commentToolbarSlot);
        leftHbox.appendChild((Component)getCommentToolbarSlot());
        this.createNewToolbarSlot = new Div();
        this.createNewToolbarSlot.setWidth("20px");
        this.createNewToolbarSlot.setHeight("16px");
        this.createNewToolbarSlot.setSclass("toolbar_newitem_action");
        leftHbox.appendChild((Component)this.createNewToolbarSlot);
        return (HtmlBasedComponent)leftHbox;
    }


    protected ActionColumnConfiguration getActionConfig()
    {
        ActionColumnConfiguration actions = getContentBrowser().getModel().getArea().getMultiSelectActions();
        if(actions == null && getMultiSelectActionArea() != null)
        {
            getMultiSelectActionArea().setVisible(false);
        }
        return actions;
    }


    protected ActionColumnConfiguration getAddtionalToolbarActionConfig()
    {
        ActionColumnConfiguration actions = null;
        if(getContentBrowser().getModel().getArea() instanceof AbstractBrowserArea)
        {
            actions = ((AbstractBrowserArea)getContentBrowser().getModel().getArea()).getAdditionalToolbarActions();
        }
        return actions;
    }


    protected HtmlBasedComponent createRightToolbarContent()
    {
        Div rightDiv = new Div();
        rightDiv.setAlign("right");
        rightDiv.appendChild((Component)createRightToolbarHbox());
        return (HtmlBasedComponent)rightDiv;
    }


    protected Hbox createRightToolbarHbox()
    {
        Hbox innerToolbarRightHbox = new Hbox();
        innerToolbarRightHbox.setAlign("center");
        return innerToolbarRightHbox;
    }


    public Div getAdditionalToolbarSlot()
    {
        return this.additionalToolbarSlot;
    }


    public Div getCreateNewToolbarSlot()
    {
        return this.createNewToolbarSlot;
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
}
