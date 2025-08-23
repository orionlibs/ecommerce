package de.hybris.platform.configurablebundlecockpits.admincockpit.session.impl;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.impl.DefaultEditorAreaController;
import java.util.HashMap;
import java.util.Map;

public class AdminBundleCockpitPopupEditorAreaControllerImpl extends DefaultEditorAreaController implements PopupEditorAreaController
{
    private transient BrowserModel contextEditorBrowser = null;
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
