package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentContextBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.FocusEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.FocusablePerspectiveArea;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModelListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;

public class DefaultSearchBrowserModelListener implements SearchBrowserModelListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSearchBrowserModelListener.class);
    private final AbstractBrowserArea area;


    public DefaultSearchBrowserModelListener(AbstractBrowserArea area)
    {
        if(area == null)
        {
            throw new IllegalArgumentException("Area can not be null");
        }
        this.area = area;
    }


    public void contextItemsChanged(AdvancedBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                if(contentBrowser instanceof AbstractContentContextBrowser)
                {
                    ((AbstractContentContextBrowser)contentBrowser).updateContextArea(false);
                }
                else
                {
                    contentBrowser.updateContextArea();
                }
            }
        }
        this.area.fireBrowserChanged((BrowserModel)browserModel);
    }


    public void contextRootTypeChanged(AdvancedBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                if(contentBrowser instanceof AbstractContentContextBrowser)
                {
                    ((AbstractContentContextBrowser)contentBrowser).updateContextArea(false);
                }
                else
                {
                    contentBrowser.updateContextArea();
                }
            }
        }
    }


    public void contextSelectionChanged(AdvancedBrowserModel browserModel)
    {
        if(browserModel instanceof AbstractSearchBrowserModel)
        {
            AbstractSearchBrowserModel abstractSearchBrowserModel = (AbstractSearchBrowserModel)browserModel;
            String stringValue = UITools.getCockpitParameter("show.context.items.in.inspector", Executions.getCurrent()
                            .getDesktop());
            if(stringValue != null && Boolean.parseBoolean(stringValue))
            {
                this.area.updateInfoArea((ListProvider)new Object(this, abstractSearchBrowserModel, browserModel), true);
            }
        }
    }


    public void contextViewModeChanged(AdvancedBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                if(contentBrowser instanceof AbstractContentContextBrowser)
                {
                    ((AbstractContentContextBrowser)contentBrowser).updateContextArea(false);
                }
                else
                {
                    contentBrowser.updateContextArea();
                }
            }
        }
    }


    public void contextVisibilityChanged(AdvancedBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                if(contentBrowser instanceof AbstractContentContextBrowser)
                {
                    ((AbstractContentContextBrowser)contentBrowser).updateContextArea(false);
                }
                else
                {
                    contentBrowser.updateContextArea();
                }
            }
        }
    }


    public void itemActivated(TypedObject item)
    {
        this.area.fireItemActivated(item);
    }


    public void itemsDropped(AdvancedBrowserModel browserModel, Collection<TypedObject> items)
    {
        this.area.fireItemsDropped((BrowserModel)browserModel, items);
    }


    public void viewModeChanged(AdvancedBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                contentBrowser.updateViewMode();
            }
        }
    }


    public void changed(BrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized(browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser(browserModel);
            if(contentBrowser == null)
            {
                this.area.resetBrowserView(browserModel);
            }
            else
            {
                contentBrowser.update();
            }
        }
        this.area.fireBrowserChanged(browserModel);
    }


    public void itemsChanged(BrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized(browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser(browserModel);
            if(contentBrowser != null)
            {
                UISessionUtils.getCurrentSession().getCurrentPerspective().onCockpitEvent((CockpitEvent)new FocusEvent(browserModel, (FocusablePerspectiveArea)this.area));
                contentBrowser.update();
            }
        }
        this.area.fireBrowserChanged(browserModel);
    }


    public void rootTypeChanged(BrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized(browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser(browserModel);
            if(contentBrowser != null)
            {
                contentBrowser.update();
            }
        }
        this.area.fireBrowserChanged(browserModel);
    }


    public void selectionChanged(BrowserModel browserModel)
    {
        this.area.updateInfoArea((ListProvider)new Object(this, browserModel), true);
    }


    public void advancedSearchVisibiltyChanged(SearchBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                contentBrowser.updateCaption();
            }
        }
    }


    public void pagingChanged(PageableBrowserModel browserModel)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                contentBrowser.updateToolbar();
            }
        }
    }
}
