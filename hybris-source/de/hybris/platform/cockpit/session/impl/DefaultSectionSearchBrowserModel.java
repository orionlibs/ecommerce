package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSectionSearchContentBrowser;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.session.BrowserSectionModel;

public class DefaultSectionSearchBrowserModel extends AbstractSectionSearchBrowserModel
{
    public DefaultSectionSearchBrowserModel()
    {
        this(null);
    }


    public DefaultSectionSearchBrowserModel(ObjectTemplate rootType)
    {
        super(rootType);
    }


    public void initialize()
    {
        if(getBrowserSectionModels() != null)
        {
            for(BrowserSectionModel sectionModel : getBrowserSectionModels())
            {
                sectionModel.initialize();
            }
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        return null;
    }


    public AbstractContentBrowser createViewComponent()
    {
        return (AbstractContentBrowser)new DefaultSectionSearchContentBrowser();
    }
}
