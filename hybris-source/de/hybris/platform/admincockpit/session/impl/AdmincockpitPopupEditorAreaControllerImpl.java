package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.impl.DefaultEditorAreaController;
import java.util.HashMap;
import java.util.Map;

public class AdmincockpitPopupEditorAreaControllerImpl extends DefaultEditorAreaController implements PopupEditorAreaController
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


    protected boolean isEditable(EditorRowConfiguration rowConf, boolean creationMode)
    {
        if(rowConf.getPropertyDescriptor() instanceof ItemAttributePropertyDescriptor)
        {
            String qualifier = ((ItemAttributePropertyDescriptor)rowConf.getPropertyDescriptor()).getAttributeQualifier();
            if("annotation".equalsIgnoreCase(qualifier))
            {
                return false;
            }
        }
        return super.isEditable(rowConf, creationMode);
    }
}
