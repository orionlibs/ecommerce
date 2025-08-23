package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ConfigurableBrowserModel;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class DefaultConfigurableBrowserModel extends AbstractBrowserModel implements ConfigurableBrowserModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurableBrowserModel.class);
    private Class viewClass;
    private boolean initiallyOpen = false;
    private String browserCode;


    public void setViewClass(Class<? extends AbstractContentBrowser> viewClass)
    {
        this.viewClass = viewClass;
    }


    public Class<? extends AbstractContentBrowser> getViewClass()
    {
        return this.viewClass;
    }


    public AbstractContentBrowser createViewComponent()
    {
        AbstractContentBrowser view = null;
        try
        {
            Class<?> clazz = getViewClass();
            view = (AbstractContentBrowser)clazz.newInstance();
        }
        catch(Exception e)
        {
            LOG.error("Could not create view component.", e);
        }
        return view;
    }


    public Object clone() throws CloneNotSupportedException
    {
        return null;
    }


    public void collapse()
    {
    }


    public TypedObject getItem(int index)
    {
        return null;
    }


    public List<TypedObject> getItems()
    {
        return Collections.EMPTY_LIST;
    }


    public boolean isCollapsed()
    {
        return false;
    }


    public void updateItems()
    {
    }


    public void setInitiallyOpen(boolean open)
    {
        this.initiallyOpen = open;
    }


    public boolean isInitiallyOpen()
    {
        return this.initiallyOpen;
    }


    @Required
    public void setBrowserCode(String browserCode)
    {
        this.browserCode = browserCode;
    }


    public String getBrowserCode()
    {
        return this.browserCode;
    }


    public String getLabel()
    {
        String label = super.getLabel();
        return Labels.getLabel(label, label);
    }


    public String getExtendedLabel()
    {
        String extLabel = super.getExtendedLabel();
        return Labels.getLabel(extLabel, extLabel);
    }
}
