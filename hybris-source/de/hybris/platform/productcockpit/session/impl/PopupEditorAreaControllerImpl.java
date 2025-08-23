package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRow;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PopupEditorAreaController;
import de.hybris.platform.cockpit.session.impl.EditorPropertyRow;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PopupEditorAreaControllerImpl extends EditorAreaControllerImpl implements PopupEditorAreaController
{
    BrowserModel contextEditorBrowser = null;
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


    public void updateEditorRequest(TypedObject typedObject, PropertyDescriptor descriptor)
    {
        super.updateEditorRequest(typedObject, descriptor);
        if(descriptor == null)
        {
            SectionPanelModel sectionPanelModel = getSectionPanelModel();
            if(sectionPanelModel instanceof RowlayoutSectionPanelModel)
            {
                Set<SectionRow> allRows = ((RowlayoutSectionPanelModel)sectionPanelModel).getAllRows();
                Set<PropertyDescriptor> toUpdatePropList = new HashSet<>();
                for(SectionRow sectionRow : allRows)
                {
                    if(sectionRow instanceof EditorPropertyRow)
                    {
                        EditorRowConfiguration rowConf = ((EditorPropertyRow)sectionRow).getRowConfiguration();
                        toUpdatePropList.add(rowConf.getPropertyDescriptor());
                    }
                }
                getModel().updateValues(toUpdatePropList);
                for(SectionRow toUpdateRow : allRows)
                {
                    ((RowlayoutSectionPanelModel)sectionPanelModel).updateRow(toUpdateRow);
                }
            }
        }
    }
}
