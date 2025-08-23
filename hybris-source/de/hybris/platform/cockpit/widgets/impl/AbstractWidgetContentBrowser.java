package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractSimpleContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.browsers.WidgetBrowserModel;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public abstract class AbstractWidgetContentBrowser extends AbstractSimpleContentBrowser implements CockpitEventAcceptor
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractWidgetContentBrowser.class);
    private WidgetContainer widgetContainer;


    public void setModel(BrowserModel model)
    {
        if(model instanceof WidgetBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Only browsers of type " + WidgetBrowserModel.class.getCanonicalName() + " supported.");
        }
    }


    public WidgetBrowserModel getModel()
    {
        return (WidgetBrowserModel)super.getModel();
    }


    protected boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            getChildren().clear();
            setWidth("100%");
            setHeight("100%");
            this.widgetContainer = (WidgetContainer)new DefaultWidgetContainer(getModel().getWidgetFactory());
            Map<String, Widget> widgets = this.widgetContainer.initialize(getModel().getWidgetMap());
            String viewTemplateURI = getViewTemplateURI();
            if(StringUtils.isBlank(viewTemplateURI))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No view layout template found. Adding widgets directly.");
                }
                Div mainDiv = new Div();
                mainDiv.setParent((Component)this);
                mainDiv.setHeight("100%");
                mainDiv.setWidth("100%");
                mainDiv.setSclass("widgetContentBrowser");
                createFrame((HtmlBasedComponent)mainDiv, widgets);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("View template defined. Trying to load template with URI '" + viewTemplateURI + "'.");
                }
                Component widgetContainerView = Executions.createComponents(viewTemplateURI, (Component)this, Collections.EMPTY_MAP);
                for(String widgetCode : widgets.keySet())
                {
                    try
                    {
                        Component widgetParent = widgetContainerView.getFellowIfAny(widgetCode);
                        if(widgetParent == null)
                        {
                            LOG.info("Widget with code '" + widgetCode + "' mapped but not available in layout template. Ignoring...");
                            continue;
                        }
                        Widget widget = widgets.get(widgetCode);
                        widget.setParent(widgetParent);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Error occurred while getting widget parent from template", e);
                    }
                }
            }
            registerListeners();
            this.initialized = true;
        }
        return this.initialized;
    }


    protected String getViewTemplateURI()
    {
        return getModel().getViewTemplateURI();
    }


    protected WidgetContainer getWidgetContainer()
    {
        return this.widgetContainer;
    }


    protected void cleanup()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning up widget content browser...");
        }
        super.cleanup();
        unregisterListeners();
        if(getWidgetContainer() != null)
        {
            getWidgetContainer().cleanup();
        }
    }


    protected void registerListeners()
    {
        if(getModel() != null)
        {
            getModel().addCockpitEventAcceptor(this);
        }
        if(getWidgetContainer() != null)
        {
            getWidgetContainer().addCockpitEventAcceptor(this);
        }
    }


    protected void unregisterListeners()
    {
        if(getModel() != null)
        {
            getModel().removeCockpitEventAcceptor(this);
        }
        if(getWidgetContainer() != null)
        {
            getWidgetContainer().removeCockpitEventAcceptor(this);
        }
    }


    protected abstract void createFrame(HtmlBasedComponent paramHtmlBasedComponent, Map<String, Widget> paramMap);
}
