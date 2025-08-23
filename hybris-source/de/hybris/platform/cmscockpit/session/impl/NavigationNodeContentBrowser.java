package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;

public class NavigationNodeContentBrowser extends DefaultAdvancedContentBrowser
{
    protected static final String ADD_BTN = "/cmscockpit/images/node_duplicate.png";
    protected static final String REMOVE_BTN = "/cmscockpit/images/cnt_elem_remove_action.png";
    protected static final String NAVIGATION_TOOLBAR_SCLASS = "navigationToolbar";
    protected static final String TOOLBARBUTTON_SCLASS = "toolbarButton";


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new Object(this, (BrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    public void fireAddRootNavigatioNode()
    {
        AbstractBrowserComponent mainComponent = getMainAreaComponent();
        if(mainComponent instanceof NavigationNodeContentMainComponent)
        {
            ((NavigationNodeContentMainComponent)mainComponent).fireAddRootNavigationNode();
        }
    }


    public void removeSelectedNavigationNode()
    {
        AbstractBrowserComponent mainComponent = getMainAreaComponent();
        if(mainComponent instanceof NavigationNodeContentMainComponent)
        {
            ((NavigationNodeContentMainComponent)mainComponent).removeSelectedNavigationNode();
        }
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new NavigationNodeContentMainComponent((AdvancedBrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    public NavigationNodeBrowserModel getModel()
    {
        return (NavigationNodeBrowserModel)super.getModel();
    }
}
