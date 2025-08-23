package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaPanelRenderer;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ParentUnitEditor extends DefaultEditorAreaPanelRenderer
{
    private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;
    private UserService userService;


    public void render(Component parent, AbstractPanel panel, Object o, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(o instanceof de.hybris.platform.commerceservices.model.OrgUnitModel)
        {
            boolean isReadOnly = !getOrgUnitAuthorizationStrategy().canEditParentUnit(getUserService().getCurrentUser());
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("referenceSearchCondition_supplier", Boolean.TRUE);
            parameters.put("disableDisplayingDetails", String.valueOf(isReadOnly));
            Editor editor = createEditor(widgetInstanceManager);
            UITools.modifySClass((HtmlBasedComponent)editor, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-editor", true);
            editor.setParameters(parameters);
            editor.setProperty("parentUnit");
            editor.setAttribute("parentObject", o);
            editor.setReadOnly(isReadOnly);
            editor.afterCompose();
            editor.addEventListener("onValueChanged", event -> widgetInstanceManager.getModel().changed());
            Div additional = new Div();
            UITools.modifySClass((HtmlBasedComponent)additional, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed", true);
            Label unitLabel = new Label(Labels.getLabel("organization.unit.parent"));
            UITools.modifySClass((HtmlBasedComponent)unitLabel, "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-label", true);
            additional.appendChild((Component)unitLabel);
            additional.appendChild((Component)editor);
            parent.appendChild((Component)additional);
        }
    }


    protected Editor createEditor(WidgetInstanceManager widgetInstanceManager)
    {
        Editor editor = new Editor();
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType("Reference(OrgUnit)");
        return editor;
    }


    protected OrgUnitAuthorizationStrategy getOrgUnitAuthorizationStrategy()
    {
        return this.orgUnitAuthorizationStrategy;
    }


    @Required
    public void setOrgUnitAuthorizationStrategy(OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy)
    {
        this.orgUnitAuthorizationStrategy = orgUnitAuthorizationStrategy;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
