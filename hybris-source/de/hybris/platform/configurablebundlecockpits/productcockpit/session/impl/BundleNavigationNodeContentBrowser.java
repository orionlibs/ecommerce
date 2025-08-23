package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;

public class BundleNavigationNodeContentBrowser extends DefaultAdvancedContentBrowser
{
    protected static final String ADD_BTN = "/productcockpit/images/node_duplicate.png";
    protected static final String REMOVE_BTN = "/productcockpit/images/cnt_elem_remove_action.png";
    protected static final String NAVIGATION_TOOLBAR_SCLASS = "navigationToolbar";
    protected static final String TOOLBARBUTTON_SCLASS = "toolbarButton";


    public void fireAddRootNavigatioNode()
    {
        AbstractBrowserComponent mainComponent = getMainAreaComponent();
        if(mainComponent instanceof BundleNavigationNodeContentMainComponent)
        {
            ((BundleNavigationNodeContentMainComponent)mainComponent).fireAddRootNavigationNode();
        }
    }


    public void removeSelectedNavigationNode()
    {
        AbstractBrowserComponent mainComponent = getMainAreaComponent();
        if(mainComponent instanceof BundleNavigationNodeContentMainComponent)
        {
            ((BundleNavigationNodeContentMainComponent)mainComponent).removeSelectedNavigationNode();
        }
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new BundleNavigationNodeContentMainComponent((AdvancedBrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    public BundleNavigationNodeBrowserModel getModel()
    {
        return (BundleNavigationNodeBrowserModel)super.getModel();
    }
}
