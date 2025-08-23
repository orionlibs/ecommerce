package de.hybris.platform.cockpit.session;

import java.util.Map;

public interface PopupEditorAreaController
{
    BrowserModel getContextEditorBrowserModel();


    void setContextEditorBrowser(BrowserModel paramBrowserModel);


    Map<String, Object> getAttributesMap();
}
