package de.hybris.platform.commerceservices.backoffice.editor;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.bool.AbstractBooleanEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.strategies.OrgUnitAuthorizationStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Optional;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;

public class OrgUnitActiveCheckboxBooleanEditor extends AbstractBooleanEditorRenderer
{
    private static final String LISTENER_ID = "orgunitactivecheckbox";
    private boolean initialActive;
    private Component parent;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "orgUnitService")
    private OrgUnitService orgUnitService;
    @Resource(name = "orgUnitAuthorizationStrategy")
    private OrgUnitAuthorizationStrategy orgUnitAuthorizationStrategy;


    public void render(Component parent, EditorContext<Boolean> context, EditorListener<Boolean> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        this.parent = parent;
        this.initialActive = ((Boolean)context.getInitialValue()).booleanValue();
        OrgUnitActiveCheckboxBooleanEditorView view = createView();
        view.setChecked(this.initialActive);
        String label = context.getLabel(context.getEditorLabel());
        view.setEditorLabel(label);
        if(!hasEditPermission())
        {
            view.update(OrgUnitActiveCheckboxBooleanEditorView.ViewMode.DISABLED_NO_PERMISSION);
        }
        else if(activateBlockedByParent())
        {
            view.update(OrgUnitActiveCheckboxBooleanEditorView.ViewMode.DISABLED_BLOCKED_BY_PARENT);
        }
        else
        {
            view.update(OrgUnitActiveCheckboxBooleanEditorView.ViewMode.EDITABLE_CLEAN);
            view.addCheckEventListener(checkEvent -> {
                listener.onValueChanged(Boolean.valueOf(checkEvent.isChecked()));
                boolean currentActive = checkEvent.isChecked();
                if(this.initialActive == currentActive)
                {
                    view.update(OrgUnitActiveCheckboxBooleanEditorView.ViewMode.EDITABLE_CLEAN);
                }
                else
                {
                    view.update(currentActive ? OrgUnitActiveCheckboxBooleanEditorView.ViewMode.EDITABLE_ACTIVATING : OrgUnitActiveCheckboxBooleanEditorView.ViewMode.EDITABLE_DEACTIVATING);
                }
            });
            registerSaveHandlers(parent, view);
        }
        parent.appendChild(view.getComponent());
    }


    private boolean activateBlockedByParent()
    {
        return (!this.initialActive && !isParentUnitActive(getCurrentOrgUnitModel()));
    }


    protected void registerSaveHandlers(Component parent, OrgUnitActiveCheckboxBooleanEditorView view)
    {
        WidgetModel widgetModel = getWidgetModel(parent);
        EditorAreaRendererUtils.setAfterSaveListener(widgetModel, "orgunitactivecheckbox", event -> {
            deactivateSubUnitsIfNecessary();
            this.initialActive = view.isChecked();
            view.update(OrgUnitActiveCheckboxBooleanEditorView.ViewMode.EDITABLE_CLEAN);
        } false);
    }


    protected OrgUnitActiveCheckboxBooleanEditorView createView()
    {
        return new OrgUnitActiveCheckboxBooleanEditorView();
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected OrgUnitService getOrgUnitService()
    {
        return this.orgUnitService;
    }


    protected OrgUnitAuthorizationStrategy getOrgUnitAuthorizationStrategy()
    {
        return this.orgUnitAuthorizationStrategy;
    }


    private boolean hasEditPermission()
    {
        return getOrgUnitAuthorizationStrategy().canEditUnit(getUserService().getCurrentUser());
    }


    protected OrgUnitModel getCurrentOrgUnitModel()
    {
        return (OrgUnitModel)getWidgetModel(this.parent).getValue("currentObject", OrgUnitModel.class);
    }


    protected WidgetModel getWidgetModel(Component parent)
    {
        return findAncestorEditor(parent).getWidgetInstanceManager().getModel();
    }


    private boolean isParentUnitActive(OrgUnitModel orgUnitModel)
    {
        if(orgUnitModel == null)
        {
            return true;
        }
        Optional<OrgUnitModel> parentUnitOptional = this.orgUnitService.getParent(orgUnitModel);
        return ((Boolean)parentUnitOptional.<Boolean>map(OrgUnitModel::getActive).orElse(Boolean.valueOf(true))).booleanValue();
    }


    private void deactivateSubUnitsIfNecessary()
    {
        OrgUnitModel orgUnitModel = getCurrentOrgUnitModel();
        if(this.initialActive && orgUnitModel != null && Boolean.FALSE.equals(orgUnitModel.getActive()))
        {
            getOrgUnitService().deactivateUnit(orgUnitModel);
        }
    }
}
