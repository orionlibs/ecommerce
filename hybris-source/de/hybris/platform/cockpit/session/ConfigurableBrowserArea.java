package de.hybris.platform.cockpit.session;

import java.util.List;

public interface ConfigurableBrowserArea extends UIBrowserArea
{
    boolean isBrowserSupported(String paramString);


    <T extends BrowserModel> T createBrowser(String paramString, Class<T> paramClass);


    void setSupportedBrowserIds(List<String> paramList);


    List<String> getSupportedBrowserIds();


    void setDefaultBrowserId(String paramString);


    String getDefaultBrowserId();


    BrowserModel getBrowserModel(String paramString);
}
