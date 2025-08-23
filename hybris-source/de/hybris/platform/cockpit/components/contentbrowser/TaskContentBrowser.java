package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.browsercomponents.TaskMainAreaBrowserComponent;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.List;

public class TaskContentBrowser extends DefaultPageableContentBrowser
{
    private List<String> attachmentTypes = null;


    public TaskContentBrowser()
    {
    }


    public TaskContentBrowser(List<String> attachmentTypes)
    {
        this.attachmentTypes = attachmentTypes;
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new TaskMainAreaBrowserComponent((AdvancedBrowserModel)getModel(), (AbstractContentBrowser)this, getAttachmentTypes());
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        Object object = new Object(this, getModel(), (AbstractContentBrowser)this);
        object.setViewButtonsVisible(false);
        return (AbstractBrowserComponent)object;
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new Object(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    private List<String> getAttachmentTypes()
    {
        return this.attachmentTypes;
    }


    protected AbstractBrowserComponent createContextAreaComponent()
    {
        return (AbstractBrowserComponent)new Object(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }
}
