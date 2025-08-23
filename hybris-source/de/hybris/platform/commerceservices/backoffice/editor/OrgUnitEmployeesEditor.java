package de.hybris.platform.commerceservices.backoffice.editor;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomPanel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

public class OrgUnitEmployeesEditor extends AbstractWidgetComponentRenderer<Component, CustomPanel, OrgUnitModel>
{
    private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;
    private UserService userService;


    public void render(Component parent, CustomPanel customPanel, OrgUnitModel currentObject, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        boolean canEditUnit = getOrgUnitAuthorizationStrategy().canEditUnit(getUserService().getCurrentUser());
        Editor editor = createEditor(widgetInstanceManager);
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("listConfigContext", "referenceListViewUnitEmployees");
        parameters.put("disableDisplayingDetails", String.valueOf(!canEditUnit));
        parameters.put("disableRemoveReference", String.valueOf(!canEditUnit));
        editor.setParameters(parameters);
        editor.setAttribute("parentObject", currentObject);
        editor.setProperty("employeesChanged");
        editor.setReadOnly(!canEditUnit);
        editor.afterCompose();
        editor.addEventListener("onValueChanged", event -> widgetInstanceManager.getModel().changed());
        parent.appendChild((Component)editor);
    }


    protected Editor createEditor(WidgetInstanceManager widgetInstanceManager)
    {
        Editor editor = new Editor();
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType("ExtendedMultiReference-LIST(Employee)");
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
