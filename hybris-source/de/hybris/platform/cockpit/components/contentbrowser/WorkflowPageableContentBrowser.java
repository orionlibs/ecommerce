package de.hybris.platform.cockpit.components.contentbrowser;

public class WorkflowPageableContentBrowser extends DefaultPageableContentBrowser
{
    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new WorkflowPagerToolbarBrowserComponent(getModel(), (AbstractContentBrowser)this);
    }
}
