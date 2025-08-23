package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AddMultiItemCommentAction;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.util.testing.TestIdEnabled;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public abstract class AbstractMultiViewToolbarBrowserComponent extends AbstractBrowserComponent
{
    protected static final String BROWSER_VIEW_NOT_AVAILABLE = "browserViewNotAvailable";
    protected static final String MODE_FACTORY = "modeFactory";
    private transient boolean viewButtonsVisible = true;
    private transient Div commentToolbarSlot = null;
    private transient Div multiSelectActionArea = null;
    private final List<Toolbarbutton> buttons = new ArrayList<>();


    public AbstractMultiViewToolbarBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
        if(contentBrowser instanceof DefaultAdvancedContentBrowser)
        {
            Map<String, MainAreaComponentFactory> viewModes = ((DefaultAdvancedContentBrowser)contentBrowser).getViewModes();
            for(Map.Entry<String, MainAreaComponentFactory> mode : viewModes.entrySet())
            {
                MainAreaComponentFactory componentFactory = mode.getValue();
                Toolbarbutton button = new Toolbarbutton(componentFactory.getButtonLabel(), componentFactory.getInactiveButtonImage());
                button.setSclass("browser_view_mode_switch mode_" + componentFactory.getViewModeID());
                button.setAttribute("modeFactory", componentFactory);
                UITools.addBusyListener((Component)button, "onClick", (EventListener)new Object(this, componentFactory), null, null);
                button.setTooltiptext(componentFactory.getButtonTooltip());
                this.buttons.add(button);
            }
            updateViewModeButtons();
        }
    }


    protected boolean currentViewHasOwnModel()
    {
        boolean ret = false;
        AbstractContentBrowser contentBrowser = getContentBrowser();
        if(contentBrowser instanceof DefaultAdvancedContentBrowser)
        {
            ret = ((DefaultAdvancedContentBrowser)contentBrowser).currentViewHasOwnModel();
        }
        return ret;
    }


    protected HtmlBasedComponent createLeftToolbarContent()
    {
        Div leftDiv = new Div();
        leftDiv.setAlign("left");
        leftDiv.setWidth("100%");
        leftDiv.setStyle("margin-left: 10px; white-space: nowrap;");
        if(isViewButtonsVisible() && this.buttons.size() > 1)
        {
            int index = 0;
            for(Component btn : this.buttons)
            {
                leftDiv.appendChild(btn);
                if(btn instanceof Toolbarbutton && !StringUtils.isBlank(((Toolbarbutton)btn).getLabel()))
                {
                    if(index < this.buttons.size() - 1)
                    {
                        Label spacer = new Label("|");
                        spacer.setSclass("toolbarBtnSpacer");
                        leftDiv.appendChild((Component)spacer);
                    }
                }
                index++;
            }
        }
        updateCommentToolbarSlot();
        initializeCommentToolbarSlot();
        leftDiv.appendChild((Component)getCommentToolbarSlot());
        createMultiViewSelectArea();
        return (HtmlBasedComponent)leftDiv;
    }


    protected void initializeCommentToolbarSlot()
    {
        if(getCommentToolbarSlot() == null)
        {
            Div commentToolbarSlot = new Div();
            commentToolbarSlot.setSclass("commentMultiSelectContainer");
            setCommentToolbarSlot(commentToolbarSlot);
        }
    }


    protected HtmlBasedComponent createRightToolbarContent()
    {
        Div rightDiv = new Div();
        rightDiv.setAlign("right");
        return (HtmlBasedComponent)rightDiv;
    }


    private void enableBtn(Component cmp, boolean enable)
    {
        if(cmp instanceof Toolbarbutton)
        {
            ((Toolbarbutton)cmp).setDisabled(!enable);
            MainAreaComponentFactory componentFactory = (MainAreaComponentFactory)((Toolbarbutton)cmp).getAttribute("modeFactory");
            if(((Toolbarbutton)cmp).isDisabled())
            {
                ((Toolbarbutton)cmp).setImage(componentFactory.getActiveButtonImage());
            }
            else
            {
                ((Toolbarbutton)cmp).setImage(componentFactory.getInactiveButtonImage());
            }
        }
    }


    protected void updateViewModeButtons()
    {
        if(this.viewButtonsVisible)
        {
            for(Component btn : this.buttons)
            {
                Object attribute = btn.getAttribute("modeFactory");
                MainAreaComponentFactory componentFactory = null;
                if(attribute instanceof MainAreaComponentFactory)
                {
                    componentFactory = (MainAreaComponentFactory)attribute;
                }
                UITools.modifySClass((HtmlBasedComponent)btn, "browserViewNotAvailable", false);
                if(componentFactory != null && componentFactory.getViewModeID().equals(getModel().getViewMode()))
                {
                    enableBtn(btn, false);
                    continue;
                }
                enableBtn(btn, true);
                if(componentFactory instanceof ContextAwareMainAreaComponentFactory)
                {
                    if(!((ContextAwareMainAreaComponentFactory)componentFactory).isAvailable((BrowserModel)getModel()))
                    {
                        ((Toolbarbutton)btn).setDisabled(true);
                        UITools.modifySClass((HtmlBasedComponent)btn, "browserViewNotAvailable", true);
                    }
                }
            }
        }
    }


    protected void updateMultiSelectActionArea()
    {
        ActionColumnConfiguration actionConfig = getActionConfig();
        if(actionConfig != null)
        {
            UITools.detachChildren((Component)this.multiSelectActionArea);
        }
        updateActionArea(actionConfig, (HtmlBasedComponent)this.multiSelectActionArea, Boolean.TRUE);
    }


    protected void updateActionArea(ActionColumnConfiguration actionConfig, HtmlBasedComponent actionArea, Boolean isMultiSelect)
    {
        if(actionArea != null)
        {
            ListViewAction.Context context = new ListViewAction.Context();
            context.setBrowserModel((BrowserModel)getModel());
            if("LIST".equals(getModel().getViewMode()) || "MULTI_TYPE_LIST"
                            .equals(getModel().getViewMode()))
            {
                context.setModel((TableModel)getModel().getTableModel());
            }
            if(actionConfig != null && ("LIST"
                            .equals(getModel().getViewMode()) || "GRID"
                            .equals(getModel().getViewMode()) || "MULTI_TYPE_LIST"
                            .equals(getModel().getViewMode())))
            {
                for(ListViewAction action : actionConfig.getActions())
                {
                    updateActionAreaAction(context, action, actionArea, isMultiSelect);
                }
            }
        }
    }


    protected void updateActionAreaAction(ListViewAction.Context context, ListViewAction action, HtmlBasedComponent actionArea, Boolean isMultiSelect)
    {
        String imageURI = null;
        EventListener listener = null;
        Menupopup popup = null;
        if(isMultiSelect.booleanValue())
        {
            imageURI = action.getMultiSelectImageURI(context);
            listener = action.getMultiSelectEventListener(context);
            popup = action.getMultiSelectPopup(context);
        }
        else
        {
            imageURI = action.getImageURI(context);
            listener = action.getEventListener(context);
            popup = action.getPopup(context);
        }
        if(imageURI != null)
        {
            Image actionImg = new Image(imageURI);
            actionArea.appendChild((Component)actionImg);
            if(listener == null && popup == null)
            {
                actionImg.setSclass("noAction");
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
                actionArea.appendChild((Component)popup);
                actionImg.setPopup((Popup)popup);
            }
            if(action instanceof TestIdEnabled)
            {
                UITools.applyTestID((Component)actionImg, ((TestIdEnabled)action).getTestId());
            }
            actionArea.appendChild((Component)actionImg);
        }
    }


    protected ActionColumnConfiguration getActionConfig()
    {
        this.multiSelectActionArea.setVisible(false);
        return null;
    }


    protected ActionColumnConfiguration getAddtionalToolbarActionConfig()
    {
        return null;
    }


    public Div getMultiSelectActionArea()
    {
        return this.multiSelectActionArea;
    }


    public boolean update()
    {
        return initialize();
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


    protected HtmlBasedComponent createToolbar()
    {
        Div toolbar = new Div();
        toolbar.setHeight("2.1em");
        toolbar.setSclass("query-browser-paging");
        Hbox browserToolbarHbox = new Hbox();
        toolbar.appendChild((Component)browserToolbarHbox);
        browserToolbarHbox.setWidth("100%");
        browserToolbarHbox.setAlign("center");
        browserToolbarHbox.appendChild((Component)createLeftToolbarContent());
        browserToolbarHbox.appendChild((Component)createRightToolbarContent());
        return (HtmlBasedComponent)toolbar;
    }


    private void createMultiViewSelectArea()
    {
        this.multiSelectActionArea = new Div();
        this.multiSelectActionArea.setSclass("toolbar_multiselect_action");
        updateMultiSelectActionArea();
    }


    protected void updateCommentToolbarSlot()
    {
        if(this.commentToolbarSlot != null)
        {
            this.commentToolbarSlot.getChildren().clear();
            ListViewAction.Context context = new ListViewAction.Context();
            context.setBrowserModel((BrowserModel)getModel());
            if("LIST".equals(getModel().getViewMode()) || "MULTI_TYPE_LIST"
                            .equals(getModel().getViewMode()))
            {
                context.setModel((TableModel)getModel().getTableModel());
            }
            AddMultiItemCommentAction addMultiItemCommentAction = new AddMultiItemCommentAction(getModel().getSelectedItems());
            String multiSelectImageURI = addMultiItemCommentAction.getMultiSelectImageURI(context);
            if(multiSelectImageURI != null)
            {
                Image actionImg = new Image(multiSelectImageURI);
                actionImg.setSclass("commentMultiSelectAction");
                actionImg.setAlign("absmiddle");
                EventListener listener = addMultiItemCommentAction.getMultiSelectEventListener(context);
                if(listener != null)
                {
                    actionImg.addEventListener("onClick", listener);
                }
                if(!StringUtils.isBlank(addMultiItemCommentAction.getTooltip(context)))
                {
                    actionImg.setTooltiptext(addMultiItemCommentAction.getTooltip(context));
                }
                if("LIST".equals(getModel().getViewMode()) || "GRID"
                                .equals(getModel().getViewMode()))
                {
                    this.commentToolbarSlot.appendChild((Component)actionImg);
                }
            }
        }
    }


    public Div getCommentToolbarSlot()
    {
        return this.commentToolbarSlot;
    }


    public void setCommentToolbarSlot(Div commentToolbarSlot)
    {
        this.commentToolbarSlot = commentToolbarSlot;
    }


    public AdvancedBrowserModel getModel()
    {
        return (AdvancedBrowserModel)super.getModel();
    }


    public boolean isViewButtonsVisible()
    {
        return this.viewButtonsVisible;
    }


    public void setViewButtonsVisible(boolean viewButtonsVisible)
    {
        this.viewButtonsVisible = viewButtonsVisible;
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    public void updateActiveItems()
    {
    }


    public void updateSelectedItems()
    {
    }


    public void resize()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
    }


    public List<Toolbarbutton> getViewModeButtons()
    {
        return this.buttons;
    }
}
