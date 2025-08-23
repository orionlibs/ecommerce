package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import java.util.HashMap;
import java.util.Map;

public class CmsPopupEditorAreaControllerImpl extends CmsEditorAreaControllerImpl implements PopupEditorAreaController
{
    private BrowserModel contextEditorBrowser = null;
    private final Map<String, Object> attributes = new HashMap<>();


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
