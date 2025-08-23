package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultOrgUnitEmployeeUnitsEditor extends DefaultEditorAreaPanelRenderer
{
    public void render(Component parent, AbstractPanel panel, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(object instanceof de.hybris.platform.core.model.user.EmployeeModel)
        {
            Div parentWrapper = new Div();
            parentWrapper.setParent(parent);
            UITools.modifySClass((HtmlBasedComponent)parentWrapper, "yw-editorarea-org-units-wrapper", true);
            for(Positioned positioned : getAttributes(panel))
            {
                if(positioned instanceof com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute)
                {
                    createAttributeRenderer().render(parentWrapper, positioned, object, dataType, widgetInstanceManager);
                }
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("referenceSearchCondition_supplier", Boolean.TRUE);
            Editor editor = createEditor(widgetInstanceManager);
            editor.setParameters(parameters);
            editor.setProperty("orgUnits");
            editor.afterCompose();
            editor.addEventListener("onValueChanged", event -> widgetInstanceManager.getModel().changed());
            Div addtional = new Div();
            addtional.appendChild((Component)new Label(Labels.getLabel("organization.sales.employee.units")));
            UITools.modifySClass((HtmlBasedComponent)addtional, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed", true);
            addtional.appendChild((Component)editor);
            parentWrapper.appendChild((Component)addtional);
        }
    }


    protected Editor createEditor(WidgetInstanceManager widgetInstanceManager)
    {
        Editor editor = new Editor();
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType("MultiReference-SET(OrgUnit)");
        return editor;
    }


    protected List<? extends Positioned> getAttributes(AbstractPanel panel)
    {
        List<? extends Positioned> ret = new ArrayList<>();
        if(panel instanceof CustomPanel)
        {
            ret = ((CustomPanel)panel).getAttributeOrCustom();
        }
        return ret;
    }
}
