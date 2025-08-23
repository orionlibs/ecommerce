package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.cms.events.LiveEditBrowserCockpitEventHandler;
import de.hybris.platform.cmscockpit.events.impl.CmsLiveEditEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModelListener;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LiveEditBrowserArea extends AbstractBrowserArea implements CMSBrowserArea
{
    private CommonI18NService commonI18NService;
    private static final Logger LOG = Logger.getLogger(LiveEditBrowserArea.class);
    private boolean initialized = false;
    private final DefaultSearchBrowserModelListener liveEditBrowserListener = new DefaultSearchBrowserModelListener(this);
    private CMSSiteModel currentSite = null;
    private boolean liveEditModeEnabled = false;
    private AdvancedBrowserModel welcomeBrowserModel = null;
    private List<LiveEditBrowserCockpitEventHandler> liveEditBrowserCockpitEventHandlers;


    public void initialize(Map<String, Object> params)
    {
        if(!this.initialized)
        {
            this.initialized = true;
            LiveEditBrowserModel browserModel = newDefaultLiveEditBrowserModel();
            browserModel.setCurrentSite(this.currentSite);
            browserModel.addBrowserModelListener((BrowserModelListener)this.liveEditBrowserListener);
            addVisibleBrowser((BrowserModel)browserModel);
            setFocusedBrowser((BrowserModel)browserModel);
            UISessionUtils.getCurrentSession().addSessionListener((UISessionListener)newLiveEditBrowserAreaUISessionListener());
        }
    }


    protected LiveEditBrowserModel newDefaultLiveEditBrowserModel()
    {
        return new LiveEditBrowserModel();
    }


    protected DefaultSearchBrowserModelListener newDefaultSearchBrowserModelListener()
    {
        return new DefaultSearchBrowserModelListener(this);
    }


    protected LiveEditBrowserAreaUISessionListener newLiveEditBrowserAreaUISessionListener()
    {
        return new LiveEditBrowserAreaUISessionListener(this);
    }


    public boolean addVisibleBrowser(int index, BrowserModel browserModel)
    {
        if(browserModel instanceof LiveEditBrowserModel)
        {
            return super.addVisibleBrowser(index, browserModel);
        }
        LOG.warn("Not showing browser " + browserModel + ". Reason: Only " + LiveEditBrowserModel.class.getCanonicalName() + " allowed.");
        return false;
    }


    public BrowserModelListener getBrowserListener()
    {
        return null;
    }


    public void saveQuery(BrowserModel browserModel)
    {
    }


    public void refreshContent(CMSSiteModel siteModel)
    {
        this.currentSite = siteModel;
        if(getFocusedBrowser() instanceof LiveEditBrowserModel)
        {
            ((LiveEditBrowserModel)getFocusedBrowser()).setCurrentSite(siteModel);
            ((LiveEditBrowserModel)getFocusedBrowser()).updateItems();
        }
        else
        {
            LOG.warn("It is not possible to load LiveEdit Browser Model");
        }
    }


    public void refreshContent()
    {
        if(getFocusedBrowser() instanceof LiveEditBrowserModel)
        {
            ((LiveEditBrowserModel)getFocusedBrowser()).refresh();
        }
        else
        {
            LOG.warn("It is not possible to load LiveEdit Browser Model");
        }
    }


    public void fireModeChange()
    {
        if(getFocusedBrowser() instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel model = (LiveEditBrowserModel)getFocusedBrowser();
            if(isLiveEditModeEnabled())
            {
                setLiveEditModeEnabled(false);
            }
            else
            {
                setLiveEditModeEnabled(true);
            }
            model.fireModeChange(getCorrespondingContentBrowser(getFocusedBrowser()));
        }
        else
        {
            LOG.warn("It is not possible to load LiveEdit Browser Model");
        }
    }


    public void fireModeChange(boolean liveEditMode)
    {
        if(getFocusedBrowser() instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel model = (LiveEditBrowserModel)getFocusedBrowser();
            setLiveEditModeEnabled(liveEditMode);
            model.fireModeChange(getCorrespondingContentBrowser(getFocusedBrowser()));
        }
        else
        {
            LOG.warn("It is not possible to load LiveEdit Browser Model");
        }
    }


    public boolean isLiveEditModeEnabled()
    {
        return this.liveEditModeEnabled;
    }


    public void setLiveEditModeEnabled(boolean liveEditModeEnabled)
    {
        this.liveEditModeEnabled = liveEditModeEnabled;
    }


    public CMSSiteModel getCurrentSite()
    {
        return this.currentSite;
    }


    public AdvancedBrowserModel getWelcomeBrowserModel()
    {
        return this.welcomeBrowserModel;
    }


    public void setWelcomeBrowserModel(AdvancedBrowserModel welcomeBrowserModel)
    {
        this.welcomeBrowserModel = welcomeBrowserModel;
    }


    public boolean isClosable(BrowserModel browserModel)
    {
        boolean closable = super.isClosable(browserModel);
        if(browserModel instanceof LiveEditBrowserModel)
        {
            closable = false;
        }
        return closable;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsLiveEditEvent)
        {
            if(!((CmsLiveEditEvent)event).getUrl().isEmpty())
            {
                if(getFocusedBrowser() instanceof LiveEditBrowserModel)
                {
                    ((LiveEditBrowserModel)getFocusedBrowser()).setCurrentUrl(((CmsLiveEditEvent)event).getUrl());
                }
                refreshContent(getCurrentSite());
            }
        }
        else if(event instanceof ItemChangedEvent)
        {
            AbstractContentBrowser abstractContentBrowser = getCorrespondingContentBrowser(getFocusedBrowser());
            if(abstractContentBrowser != null)
            {
                abstractContentBrowser.updateItem(((ItemChangedEvent)event).getItem(), Collections.EMPTY_SET);
            }
        }
        else if(event instanceof CmsUrlChangeEvent)
        {
            if(!event.getSource().equals(getPerspective()))
            {
                return;
            }
            AbstractContentBrowser abstractContentBrowser = getCorrespondingContentBrowser(getFocusedBrowser());
            if(abstractContentBrowser != null)
            {
                LiveEditContentBrowser liveEditContentBrowser = (LiveEditContentBrowser)abstractContentBrowser;
                liveEditContentBrowser.updateAfterChangedUrl((CmsUrlChangeEvent)event);
            }
        }
        else if(event instanceof de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent)
        {
            if(event.getSource() == null || !event.getSource().equals(getPerspective()))
            {
                return;
            }
            BrowserModel focusedBrowserModel = getFocusedBrowser();
            if(focusedBrowserModel instanceof LiveEditBrowserModel)
            {
                LiveEditBrowserModel liveBrowserModel = (LiveEditBrowserModel)focusedBrowserModel;
                liveBrowserModel.onCmsPerpsectiveInitEvent();
            }
        }
        else
        {
            BrowserModel focusedBrowserModel = getFocusedBrowser();
            if(focusedBrowserModel instanceof LiveEditBrowserModel)
            {
                LiveEditBrowserModel liveBrowserModel = (LiveEditBrowserModel)focusedBrowserModel;
                liveBrowserModel.setRelatedPagePk(null);
                if(event instanceof CmsNavigationEvent)
                {
                    CmsNavigationEvent cmsNavigationEvent = (CmsNavigationEvent)event;
                    liveBrowserModel.setNavigationEventAttributes(cmsNavigationEvent);
                }
            }
        }
        if(getLiveEditBrowserCockpitEventHandlers() != null)
        {
            for(LiveEditBrowserCockpitEventHandler eventHandler : getLiveEditBrowserCockpitEventHandlers())
            {
                if(eventHandler.canHandleEvent(event, (UIBrowserArea)this))
                {
                    eventHandler.handleCockpitEvent(event, (UIBrowserArea)this);
                }
            }
        }
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    public List<LiveEditBrowserCockpitEventHandler> getLiveEditBrowserCockpitEventHandlers()
    {
        return this.liveEditBrowserCockpitEventHandlers;
    }


    public void setLiveEditBrowserCockpitEventHandlers(List<LiveEditBrowserCockpitEventHandler> liveEditBrowserCockpitEventHandlers)
    {
        this.liveEditBrowserCockpitEventHandlers = liveEditBrowserCockpitEventHandlers;
    }
}
