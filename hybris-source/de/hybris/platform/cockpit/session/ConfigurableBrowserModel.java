package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;

public interface ConfigurableBrowserModel extends BrowserModel
{
    void setViewClass(Class<? extends AbstractContentBrowser> paramClass);


    Class<? extends AbstractContentBrowser> getViewClass();


    void setInitiallyOpen(boolean paramBoolean);


    boolean isInitiallyOpen();


    void setBrowserCode(String paramString);


    String getBrowserCode();
}
