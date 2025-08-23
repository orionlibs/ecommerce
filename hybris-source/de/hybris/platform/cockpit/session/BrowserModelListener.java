package de.hybris.platform.cockpit.session;

public interface BrowserModelListener
{
    void changed(BrowserModel paramBrowserModel);


    void itemsChanged(BrowserModel paramBrowserModel);


    void selectionChanged(BrowserModel paramBrowserModel);


    void rootTypeChanged(BrowserModel paramBrowserModel);
}
