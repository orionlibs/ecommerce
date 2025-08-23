package de.hybris.platform.cmscockpit.components.liveedit;

import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cmscockpit.components.liveedit.impl.LiveEditPopupEditDialog;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;

public class LiveEditView<T extends LiveEditPopupEditDialog>
{
    private static final Logger LOG = Logger.getLogger(LiveEditView.class);
    protected static final String ON_INIT_EVENT = "onInit";
    public static final String CALLBACK_EVENT = "defaultCallback";
    public static final String URL_CHANGE_EVENT = "urlChange";
    protected static final String LIVE_EDIT_BROWSER_SCLASS = "liveEditBrowser";
    protected static final String ON_INVALIDATE_LATER_EVENT = "onInvalidateLater";
    private transient Div rootDiv = null;
    private transient Iframe contentFrame = null;
    private transient Div welcomePanel = null;
    private transient T popupEditorDialog = null;
    private final DefaultLiveEditViewModel model;


    public LiveEditView(DefaultLiveEditViewModel model)
    {
        this.model = model;
        initialize();
    }


    public LiveEditView(DefaultLiveEditViewModel model, Div welcomePanel)
    {
        this.model = model;
        this.welcomePanel = welcomePanel;
        initialize();
    }


    public void initialize()
    {
        this.contentFrame = initializeContentFrame();
        this.rootDiv = initializeViewComponent();
        loadWelcomePanel((HtmlBasedComponent)this.rootDiv);
        addEventListeners();
        Events.echoEvent("onInit", (Component)this.contentFrame, null);
    }


    protected Div initializeViewComponent()
    {
        Div div = new Div();
        UITools.maximize((HtmlBasedComponent)div);
        div.setClass("liveEditWrapper");
        div.appendChild((Component)this.contentFrame);
        return div;
    }


    protected Iframe initializeContentFrame()
    {
        Iframe newFrame = new Iframe();
        UITools.maximize((HtmlBasedComponent)newFrame);
        newFrame.setSclass("liveEditBrowser");
        newFrame.setVisible(getModel().isContentVisible());
        return newFrame;
    }


    protected void addEventListeners()
    {
        this.contentFrame.addEventListener("onUser", getUserEventListener());
        this.contentFrame.addEventListener("onInit", getIniEventListener());
        this.contentFrame.addEventListener("onInvalidateLater", getInvalidateListener());
    }


    public HtmlBasedComponent getViewComponent()
    {
        if(this.rootDiv == null)
        {
            initialize();
        }
        return (HtmlBasedComponent)this.rootDiv;
    }


    public Iframe getContentFrame()
    {
        return this.contentFrame;
    }


    public DefaultLiveEditViewModel getModel()
    {
        return this.model;
    }


    public void setWelcomePanel(Div welcomePanel)
    {
        this.welcomePanel = welcomePanel;
    }


    protected EventListener getIniEventListener()
    {
        return (EventListener)new Object(this);
    }


    protected EventListener getInvalidateListener()
    {
        return (EventListener)new Object(this);
    }


    protected EventListener getUserEventListener()
    {
        return (EventListener)new Object(this);
    }


    protected void refreshWelcomePanel()
    {
        if(getWelcomePanel() != null)
        {
            getWelcomePanel().setVisible(getModel().isWelcomePanelVisible());
            if(!getViewComponent().getChildren().contains(getWelcomePanel()))
            {
                getViewComponent().appendChild((Component)getWelcomePanel());
            }
        }
    }


    protected void refreshContentFrame()
    {
        getContentFrame().setVisible(getModel().isContentVisible());
        if(getModel().isContentVisible())
        {
            String generatedUrl = getModel().computeFinalUrl();
            if((getModel().getSite() != null && StringUtils.isBlank(getModel().getSite().getPreviewURL())) ||
                            StringUtils.isBlank(generatedUrl))
            {
                try
                {
                    Messagebox.show(Labels.getLabel("site_url_empty"), Labels.getLabel("general.warning"), 1, "z-msgbox z-msgbox-exclamation");
                }
                catch(InterruptedException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Errors occured while showing message box!", e);
                    }
                }
            }
            else
            {
                getContentFrame().setSrc(generatedUrl);
                RefreshContentHandlerRegistry refreshContentHandlerRegistry = (RefreshContentHandlerRegistry)SpringUtil.getBean("liveEditRefreshContentHandlerRegistry", RefreshContentHandlerRegistry.class);
                List<RefreshContentHandler<LiveEditView>> refreshHandlers = refreshContentHandlerRegistry.getRefreshContentHandlers();
                for(RefreshContentHandler<LiveEditView> handler : refreshHandlers)
                {
                    handler.onRefresh(this);
                }
                Events.echoEvent("onInvalidateLater", (Component)getContentFrame(), null);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Current url : " + getModel().getCurrentUrl());
                }
            }
        }
    }


    public void update()
    {
        refreshWelcomePanel();
        refreshContentFrame();
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties, Object reason)
    {
        if(!(reason instanceof LiveEditPopupEditDialog))
        {
            if(getPopupEditorDialog() != null && getPopupEditorDialog().isVisible() &&
                            !getPopupEditorDialog().equals(reason))
            {
                getPopupEditorDialog().update();
            }
        }
    }


    protected String extractRequestPath(String longUrl)
    {
        String ret = "";
        if(!longUrl.contains("cx-preview"))
        {
            String[] urlParts = longUrl.split("[\\?&]cmsTicketId");
            ret = urlParts[0];
        }
        return ret;
    }


    protected HtmlBasedComponent loadWelcomePanel(HtmlBasedComponent parent)
    {
        if(getWelcomePanel() != null && getModel().isWelcomePanelVisible())
        {
            parent.appendChild((Component)getWelcomePanel());
        }
        return (HtmlBasedComponent)getWelcomePanel();
    }


    protected Div getWelcomePanel()
    {
        return this.welcomePanel;
    }


    public LiveEditPopupEditDialog getPopupEditorDialog()
    {
        return (LiveEditPopupEditDialog)this.popupEditorDialog;
    }


    public void setPopupEditorDialog(T popupEditorDialog)
    {
        this.popupEditorDialog = popupEditorDialog;
    }
}
