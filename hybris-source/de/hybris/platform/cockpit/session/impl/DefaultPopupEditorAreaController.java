package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import java.util.HashMap;
import java.util.Map;

public class DefaultPopupEditorAreaController extends DefaultEditorAreaController implements PopupEditorAreaController
{
    protected BrowserModel contextEditorBrowser = null;
    Map<String, Object> attributes = new HashMap<>();


    public Map<String, Object> getAttributesMap()
    {
        return this.attributes;
    }


    public void setContextEditorBrowser(BrowserModel model)
    {
        this.contextEditorBrowser = model;
    }


    public BrowserModel getContextEditorBrowserModel()
    {
        return this.contextEditorBrowser;
    }
}
