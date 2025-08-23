package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cmscockpit.components.listview.CmsPageBrowserAction;
import de.hybris.platform.cmscockpit.components.listview.impl.CmsCreatePersonalizedPageAction;
import de.hybris.platform.cmscockpit.components.zk.RawLabel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMultiViewToolbarBrowserComponent;
import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Space;

public class CmsPageToolbarBrowserComponent extends AbstractMultiViewToolbarBrowserComponent
{
    private static final Logger LOG = Logger.getLogger(CmsPageToolbarBrowserComponent.class);
    private final Div actionBox = new Div();


    public CmsPageToolbarBrowserComponent(CmsPageBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((BrowserModel)model, contentBrowser);
    }


    protected TypedObject getCurrentPage()
    {
        if(getPageBrowserModel() != null)
        {
            TypedObject currentPageObject = getPageBrowserModel().getCurrentPageObject();
            return currentPageObject;
        }
        return null;
    }


    protected CmsPageBrowserModel getPageBrowserModel()
    {
        if(getModel() instanceof CmsPageBrowserModel)
        {
            return (CmsPageBrowserModel)getModel();
        }
        LOG.warn("Model not an instance of " + CmsPageBrowserModel.class.getSimpleName());
        return null;
    }


    protected HtmlBasedComponent createToolbar()
    {
        Div toolbar = (Div)super.createToolbar();
        toolbar.setStyle("padding-top:0px");
        return (HtmlBasedComponent)toolbar;
    }


    protected HtmlBasedComponent createLeftToolbarContent()
    {
        HtmlBasedComponent ret = super.createLeftToolbarContent();
        ret.setStyle("width:100%;margin-left: 10px; white-space: nowrap;");
        TypedObject currentPageObject = getCurrentPage();
        if(currentPageObject != null)
        {
            Div div = getMultiSelectActionArea();
            renderLeftActions((HtmlBasedComponent)div, currentPageObject);
            ret.appendChild((Component)div);
        }
        return ret;
    }


    protected HtmlBasedComponent createRightToolbarContent()
    {
        HtmlBasedComponent ret = super.createRightToolbarContent();
        TypedObject currentPageObject = getCurrentPage();
        if(currentPageObject != null)
        {
            renderActions((Component)ret, currentPageObject);
        }
        return ret;
    }


    public boolean update()
    {
        updateViewModeButtons();
        renderLeftActions((HtmlBasedComponent)getMultiSelectActionArea(), getCurrentPage());
        resetActions(getCurrentPage());
        return true;
    }


    protected void resetActions(TypedObject item)
    {
        resetActions(item, (HtmlBasedComponent)this.actionBox, "PageHeaderListActionColumn");
    }


    protected void resetActions(TypedObject item, HtmlBasedComponent actionDiv, String actionSpringBeanID)
    {
        if(actionDiv != null && item != null)
        {
            actionDiv.getChildren().clear();
            ActionColumnConfiguration actionConfiguration = getPageActionConfiguration(actionSpringBeanID);
            if(actionConfiguration == null)
            {
                return;
            }
            List<ListViewAction> listViewActions = actionConfiguration.getActions();
            boolean firstIter = true;
            for(ListViewAction listViewAction : listViewActions)
            {
                ListViewAction.Context context = listViewAction.createContext(null, item);
                if(firstIter)
                {
                    firstIter = false;
                }
                else
                {
                    Space spacer = new Space();
                    spacer.setOrient("vertical");
                    spacer.setSclass("action_spacer");
                    spacer.setBar(true);
                    actionDiv.appendChild((Component)spacer);
                }
                String imgURI = listViewAction.getImageURI(context);
                if(imgURI != null && imgURI.length() > 0)
                {
                    RawLabel rawLabel;
                    Image actionImg = new Image(imgURI);
                    actionImg.setStyle("display: inline; cursor: pointer; vertical-align:middle");
                    Label prefixLabelComp = null;
                    if(listViewAction instanceof CmsPageBrowserAction)
                    {
                        String prefixLabel = ((CmsPageBrowserAction)listViewAction).getPrefixLabel();
                        if(StringUtils.isNotBlank(prefixLabel))
                        {
                            rawLabel = new RawLabel(prefixLabel);
                            actionDiv.appendChild((Component)rawLabel);
                        }
                    }
                    EventListener listener = listViewAction.getEventListener(context);
                    if(listener != null)
                    {
                        actionImg.addEventListener("onClick", listener);
                        actionImg.addEventListener("onLater", listener);
                        if(rawLabel != null)
                        {
                            rawLabel.addEventListener("onClick", listener);
                            rawLabel.addEventListener("onLater", listener);
                        }
                    }
                    if(listViewAction.getTooltip(context) != null && listViewAction.getTooltip(context).length() > 0)
                    {
                        actionImg.setTooltiptext(listViewAction.getTooltip(context));
                    }
                    Menupopup popup = listViewAction.getPopup(context);
                    if(popup != null)
                    {
                        actionDiv.appendChild((Component)popup);
                        actionImg.setPopup((Popup)popup);
                        if(rawLabel != null)
                        {
                            rawLabel.setPopup((Popup)popup);
                        }
                    }
                    actionDiv.appendChild((Component)actionImg);
                    Menupopup contextPopup = listViewAction.getContextPopup(context);
                    if(contextPopup != null)
                    {
                        actionDiv.appendChild((Component)contextPopup);
                        actionImg.setContext((Popup)contextPopup);
                        if(rawLabel != null)
                        {
                            rawLabel.setContext((Popup)contextPopup);
                        }
                    }
                    if(listViewAction instanceof CmsCreatePersonalizedPageAction)
                    {
                        Popup personalizedPagePopup = ((CmsCreatePersonalizedPageAction)listViewAction).getCMSPopup(context);
                        actionDiv.appendChild((Component)personalizedPagePopup);
                        actionImg.addEventListener("onClick", (EventListener)new Object(this, personalizedPagePopup, actionImg, listViewAction));
                    }
                }
            }
        }
    }


    protected void renderLeftActions(HtmlBasedComponent multiViewSelectArea, TypedObject item)
    {
        if(multiViewSelectArea != null)
        {
            multiViewSelectArea.getChildren().clear();
            multiViewSelectArea.setSclass("");
            String viewMode = getPageBrowserModel().getViewMode();
            boolean previousState = multiViewSelectArea.isVisible();
            if("PERSONALIZE".equals(viewMode))
            {
                multiViewSelectArea.setVisible(true);
                multiViewSelectArea.setSclass("cms_multiViewSelectArea");
                resetActions(item, multiViewSelectArea, "PageHeaderMultiViewSelectListActionColumn");
            }
            else
            {
                multiViewSelectArea.setVisible(previousState);
            }
        }
    }


    public ActionColumnConfiguration getPageActionConfiguration()
    {
        return getPageActionConfiguration("PageHeaderListActionColumn");
    }


    public ActionColumnConfiguration getPageActionConfiguration(String actionSpringBeanID)
    {
        return (ActionColumnConfiguration)SpringUtil.getBean(actionSpringBeanID);
    }


    protected void renderActions(Component parent, TypedObject item)
    {
        this.actionBox.setSclass("cms_page_actions");
        parent.appendChild((Component)this.actionBox);
        resetActions(item, (HtmlBasedComponent)this.actionBox, "PageHeaderListActionColumn");
    }
}
