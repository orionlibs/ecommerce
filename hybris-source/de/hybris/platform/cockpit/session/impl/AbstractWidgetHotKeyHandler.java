package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.browsers.WidgetBrowserModel;

public abstract class AbstractWidgetHotKeyHandler extends DefaultHotKeyHandler
{
    protected void focusWidget(String widgetCode)
    {
        BrowserModel focusedBrowser = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        if(focusedBrowser instanceof WidgetBrowserModel)
        {
            ((WidgetBrowserModel)focusedBrowser).focusWidget(widgetCode);
        }
    }
}
