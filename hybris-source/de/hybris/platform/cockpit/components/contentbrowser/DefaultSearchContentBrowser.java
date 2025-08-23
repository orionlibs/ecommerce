package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.CreateNewComponent;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultSearchContentBrowser extends DefaultAdvancedContentBrowser
{
    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new QueryCaptionBrowserComponent((BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new SearchToolbarBrowserComponent(getModel(), (AbstractContentBrowser)this);
    }


    public void setModel(BrowserModel model)
    {
        if(model instanceof SearchBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Model not of type 'SearchBrowserModel'.");
        }
    }


    public SearchBrowserModel getModel()
    {
        return (SearchBrowserModel)super.getModel();
    }


    public HtmlBasedComponent createCreateNewComponent()
    {
        CreateNewComponent createNewButton = null;
        if(getModel() instanceof DefaultSearchBrowserModel && ((DefaultSearchBrowserModel)getModel()).isShowCreateButton())
        {
            createNewButton = new CreateNewComponent();
            createNewButton.addEventListener("onClick", (EventListener)new Object(this));
        }
        return (HtmlBasedComponent)createNewButton;
    }
}
