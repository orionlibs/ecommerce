package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;

public interface MainAreaComponentFactory
{
    String getButtonLabel();


    String getButtonTooltip();


    String getActiveButtonImage();


    String getInactiveButtonImage();


    String getViewModeID();


    AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel paramAdvancedBrowserModel, AbstractContentBrowser paramAbstractContentBrowser);
}
