package de.hybris.platform.adaptivesearchbackoffice.components;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionDefinition;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.components.AbstractCockpitElementsContainer;
import com.hybris.cockpitng.components.DefaultCockpitActionsRenderer;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Action;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.impl.ComponentWidgetAdapter;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Popup;

public class DefaultActionsMenuRenderer extends DefaultCockpitActionsRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultActionsMenuRenderer.class);
    protected static final String ACTIONS_SCLASS = "yas-actions";
    protected static final String ACTIONS_BUTTON_SCLASS = "yas-actions-button";
    protected static final String ACTIONS_POPUP_SCLASS = "yas-actions-popup";
    protected static final String ACTION_MENUITEM_SCLASS = "yas-action";
    protected static final String ACTION_FONT_ICON_OVERFLOW = "font-icon--overflow";
    private CockpitComponentDefinitionService componentDefinitionService;


    public void render(AbstractCockpitElementsContainer parent, Object configuration)
    {
        if(configuration instanceof Actions && parent instanceof ActionsMenu)
        {
            Actions actions = (Actions)configuration;
            ActionsMenu parentDiv = (ActionsMenu)parent;
            parentDiv.setSclass("yas-actions");
            Menupopup actionsMenu = new Menupopup();
            actionsMenu.setParent((Component)parentDiv);
            actionsMenu.setSclass("yas-actions-popup");
            renderActions(parentDiv, actions, actionsMenu);
            Button actionsButton = new Button();
            actionsButton.setParent((Component)parentDiv);
            actionsButton.setSclass("yas-actions-button");
            actionsButton.addSclass("font-icon--overflow");
            actionsButton.setPopup((Popup)actionsMenu);
            if(actionsMenu.getChildren().isEmpty())
            {
                actionsButton.setDisabled(true);
            }
        }
    }


    protected void renderActions(ActionsMenu menuActions, Actions actions, Menupopup parent)
    {
        if(actions == null || actions.getGroup() == null)
        {
            return;
        }
        for(ActionGroup group : actions.getGroup())
        {
            if(group.getActions() != null)
            {
                renderActionsGroup(group, menuActions, parent);
            }
        }
        parent.removeChild(parent.getLastChild());
    }


    public void renderActionsGroup(ActionGroup actionGroup, ActionsMenu menuActions, Menupopup parent)
    {
        for(Action action : actionGroup.getActions())
        {
            Menuitem actionMenuitem = renderAction(menuActions, action);
            if(actionMenuitem != null)
            {
                actionMenuitem.setParent((Component)parent);
            }
        }
        parent.getChildren().add(new Menuseparator());
    }


    protected Menuitem renderAction(ActionsMenu menuActions, Action action)
    {
        try
        {
            ActionDefinition definition = (ActionDefinition)getComponentDefinitionService().getComponentDefinitionForCode(action.getActionId());
            String classname = definition.getActionClassName();
            CockpitAction<?, ?> cockpitAction = (CockpitAction<?, ?>)getComponentDefinitionService().createAutowiredComponent((AbstractCockpitComponentDefinition)definition, classname,
                            SpringUtil.getApplicationContext());
            ActionContext actionContext = createActionContext(menuActions, action, definition);
            if(cockpitAction instanceof ComponentWidgetAdapterAware)
            {
                ComponentWidgetAdapterAware componentWidgetAdapterAware = (ComponentWidgetAdapterAware)cockpitAction;
                componentWidgetAdapterAware.initialize(getComponentWidgetAdapter(), action.getActionId());
            }
            boolean enabled = cockpitAction.canPerform(actionContext);
            if(!enabled)
            {
                return null;
            }
            Menuitem actionMenuitem = new Menuitem();
            actionMenuitem.setDisabled(false);
            actionMenuitem.setSclass("yas-action");
            String name = actionContext.getLabel(definition.getName());
            actionMenuitem.setLabel(name);
            actionMenuitem.addEventListener("onClick", event -> cockpitAction.perform(actionContext));
            return actionMenuitem;
        }
        catch(ReflectiveOperationException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    protected ActionContext createActionContext(ActionsMenu menuActions, Action action, ActionDefinition actionDefinition)
    {
        Map<String, Object> parameters = new HashMap<>();
        if(actionDefinition != null)
        {
            TypedSettingsMap settings = actionDefinition.getDefaultSettings();
            if(MapUtils.isNotEmpty((Map)settings))
            {
                parameters.putAll(settings.getAll());
            }
            parameters.put("componentRoot", actionDefinition.getLocationPath());
            parameters.put("componentResourcePath", actionDefinition.getResourcePath());
        }
        if(MapUtils.isNotEmpty(menuActions.getAttributes()))
        {
            parameters.putAll(menuActions.getAttributes());
        }
        Map<String, Object> labels = (actionDefinition != null) ? CockpitComponentDefinitionLabelLocator.getLabelMap((AbstractCockpitComponentDefinition)actionDefinition) : Collections.<String, Object>emptyMap();
        return new ActionContext(menuActions.getInputValue(), actionDefinition, parameters, labels);
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        if(this.componentDefinitionService == null)
        {
            this.componentDefinitionService = (CockpitComponentDefinitionService)SpringUtil.getBean("componentDefinitionService");
        }
        return this.componentDefinitionService;
    }


    public ComponentWidgetAdapter getComponentWidgetAdapter()
    {
        return (ComponentWidgetAdapter)SpringUtil.getBean("componentWidgetAdapter");
    }
}
