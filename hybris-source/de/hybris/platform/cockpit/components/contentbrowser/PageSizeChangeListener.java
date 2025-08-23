package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.impl.AbstractPageableBrowserModel;
import de.hybris.platform.cockpit.util.CockpitBrowserAreaAutoSearchConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.impl.InputElement;

class PageSizeChangeListener implements EventListener
{
    private static final Logger LOG = LoggerFactory.getLogger(PageSizeChangeListener.class);
    private final PageableBrowserModel model;


    protected PageSizeChangeListener(PageableBrowserModel model)
    {
        this.model = model;
    }


    public void onEvent(Event event) throws Exception
    {
        String eventName = event.getName();
        if(eventName.equals("onChange") && event instanceof InputEvent)
        {
            InputEvent inputEvent = (InputEvent)event;
            try
            {
                int newPageSize = Integer.parseInt(inputEvent.getValue());
                this.model.addPageSize(newPageSize);
                this.model.setPageSize(newPageSize);
                Clients.showBusy(Labels.getLabel("search.busy"), true);
                Events.echoEvent("onLater", event.getTarget(), null);
            }
            catch(NumberFormatException nfe)
            {
                if(Labels.getLabel("browserarea.show_max").equals(inputEvent.getValue()))
                {
                    if(this.model instanceof AbstractPageableBrowserModel)
                    {
                        this.model.addPageSize(((AbstractPageableBrowserModel)this.model).getMaxPageSize());
                        this.model.setPageSize(((AbstractPageableBrowserModel)this.model).getMaxPageSize());
                        Clients.showBusy(Labels.getLabel("search.busy"), true);
                        Events.echoEvent("onLater", event.getTarget(), null);
                    }
                }
                else
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("Entered page size could not be parsed as an integer.");
                    }
                    Component target = event.getTarget();
                    if(target instanceof InputElement)
                    {
                        InputElement inputElement = (InputElement)target;
                        inputElement.setText(String.valueOf(this.model.getPageSize()));
                    }
                }
            }
        }
        else if(eventName.equals("onOK"))
        {
            Component target = event.getTarget();
            if(target instanceof InputElement)
            {
                InputElement inputElement = (InputElement)target;
                try
                {
                    int newPageSize = Integer.parseInt(inputElement.getText());
                    this.model.addPageSize(newPageSize);
                    this.model.setPageSize(newPageSize);
                }
                catch(NumberFormatException nfe)
                {
                    if("Show max".equals(inputElement.getText()))
                    {
                        if(this.model instanceof AbstractPageableBrowserModel)
                        {
                            this.model.addPageSize(((AbstractPageableBrowserModel)this.model).getMaxPageSize());
                            this.model.setPageSize(((AbstractPageableBrowserModel)this.model).getMaxPageSize());
                            Clients.showBusy(Labels.getLabel("search.busy"), true);
                            Events.echoEvent("onLater", event.getTarget(), null);
                        }
                    }
                    else if(LOG.isInfoEnabled())
                    {
                        LOG.info("Entered page size could not be parsed as an integer.");
                    }
                }
            }
        }
        else if("onLater".equals(eventName))
        {
            try
            {
                if(!CockpitBrowserAreaAutoSearchConfigurationUtil.isAutomaticSearchDisabled(Executions.getCurrent()))
                {
                    this.model.updateItems(0);
                }
            }
            finally
            {
                Clients.showBusy(null, false);
            }
        }
    }
}
