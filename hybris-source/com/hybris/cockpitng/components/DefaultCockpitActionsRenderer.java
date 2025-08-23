/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.google.common.collect.Iterables;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Action;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroup;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroupExtended;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroupSplit;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.ActionGroupThreeDots;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Parameter;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.keyboard.KeyboardSupportService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class DefaultCockpitActionsRenderer implements CockpitComponentsRenderer
{
    public static final String DEFAULT_QUALIFIER = "_default_";
    public static final String SCLASS_YA_ACTION_SELECTOR_BUTTON = "ya-create-type-selector-button";
    public static final String SCLASS_YA_ACTION_SELECTOR_BUTTON_OPENED = "ya-create-type-selector-button-opened";
    public static final String SCALSS_YA_EXTENDED_GROUP_CNT = "ya-extended-group-cnt";
    public static final String SCALSS_YA_SPLIT_GROUP_CNT = "ya-split-group-cnt";
    public static final String SCALSS_YA_THREE_DOTS_GROUP_CNT = "ya-three-dots-group-cnt";
    public static final String SCLASS_ACTION_GROUP_POPUP = "ya-cng-action-group-popup";
    public static final String SCLASS_YA_THREE_DOTS_GROUP_POPUP = "ya-three-dots-group-popup";
    public static final String YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER = "ya-cng-extended-action-container-hover";
    private static final String YA_ACTION_SLOT = "ya-action-slot";
    protected static final String ACTION_GROUP_QUALIFIER = "actionGroupQualifier";
    private static final String GROUPED_ACTIONS_POPUP_POSITION = "after_start";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitActionsRenderer.class);
    private static final String YTESTID_EXTENDED_ACTION_GROUP_POPUP = "extendedActionGroupPopup";
    private static final String YTESTID_SPLIT_ACTION_GROUP_POPUP = "splitActionGroupPopup";
    private static final String YTESTID_SPLIT_ACTION_GROUP = "splitActionGroup";
    private static final String YTESTID_EXTENDED_ACTION_GROUP = "extendedActionGroup";
    private static final String YTESTID_THREE_DOTS_ACTION_GROUP = "threeDotsActionGroup";
    private KeyboardSupportService keyboardSupportService;
    private LabelService labelService;


    @Override
    public void render(final AbstractCockpitElementsContainer parent, final Object configuration)
    {
        if(configuration instanceof Actions && parent instanceof com.hybris.cockpitng.components.Actions)
        {
            UITools.addSClass(parent, YA_ACTION_SLOT);
            final Set<HtmlBasedComponent> groupContainers = new LinkedHashSet<>();
            final Actions actions = (Actions)configuration;
            if(isOnlyOneGroup(actions))
            {
                renderSingleGroup(parent, groupContainers, actions);
            }
            else
            {
                renderAllGroups(parent, groupContainers, actions);
            }
            cleanUpActionComponents(parent, groupContainers);
        }
    }


    protected boolean isOnlyOneGroup(final Actions actions)
    {
        return actions.getGroup().size() == 1;
    }


    protected void renderSingleGroup(final AbstractCockpitElementsContainer parent, final Set<HtmlBasedComponent> groupContainers,
                    final Actions actions)
    {
        final ActionGroup groupConfig = getGroupConfig(actions, parent.getGroup());
        if(groupConfig != null)
        {
            final boolean showGroupHeader = Optional.ofNullable(groupConfig.isShowGroupHeader()).orElse(false);
            groupContainers.add(renderGroup(parent, groupConfig, showGroupHeader));
        }
    }


    protected ActionGroup getGroupConfig(final Actions configuration, final String groupId)
    {
        for(final ActionGroup groupConfig : configuration.getGroup())
        {
            if(groupId.equalsIgnoreCase(groupConfig.getQualifier()))
            {
                return groupConfig;
            }
        }
        return null;
    }


    protected void renderAllGroups(final AbstractCockpitElementsContainer parent, final Set<HtmlBasedComponent> groupContainers,
                    final Actions actions)
    {
        final Function<ActionGroup, HtmlBasedComponent> mapper = groupConfig -> renderGroup(parent, groupConfig,
                        resolveShowGroupHeaderWhenManyGroups(groupConfig));
        groupContainers.addAll(actions.getGroup().stream().map(mapper).collect(Collectors.toList()));
    }


    protected boolean resolveShowGroupHeaderWhenManyGroups(final ActionGroup actionGroup)
    {
        return Optional.ofNullable(actionGroup.isShowGroupHeader()).orElse(true);
    }


    protected HtmlBasedComponent renderGroup(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final boolean showGroupHeader)
    {
        final HtmlBasedComponent container = getOrCreateGroupContainer(parent, groupConfig);
        processGroupHeader(parent, groupConfig, container, showGroupHeader);
        if(!groupConfig.getActions().isEmpty())
        {
            final Set<com.hybris.cockpitng.components.Action> actionComponents = new LinkedHashSet<>();
            if(groupConfig instanceof ActionGroupExtended)
            {
                renderActionGroupExtended(parent, (ActionGroupExtended)groupConfig, container, actionComponents);
            }
            else if(groupConfig instanceof ActionGroupSplit)
            {
                renderActionGroupSplit(parent, (ActionGroupSplit)groupConfig, container, actionComponents);
            }
            else if(groupConfig instanceof ActionGroupThreeDots)
            {
                renderActionGroupThreeDots(parent, (ActionGroupThreeDots)groupConfig, container, actionComponents);
            }
            else
            {
                groupConfig.getActions().forEach(config -> renderAction(parent, groupConfig, container, actionComponents, config));
            }
            removeChildActionsNonExistingInActionComponents(actionComponents, container.getChildren());
        }
        return container;
    }


    protected void renderActionGroupThreeDots(final AbstractCockpitElementsContainer parent,
                    final ActionGroupThreeDots groupConfig, final HtmlBasedComponent container,
                    final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        UITools.addSClass(container, SCALSS_YA_THREE_DOTS_GROUP_CNT);
        YTestTools.modifyYTestId(container, YTESTID_THREE_DOTS_ACTION_GROUP);
        final Popup popup = new Popup();
        popup.setSclass(SCLASS_ACTION_GROUP_POPUP + " " + SCLASS_YA_THREE_DOTS_GROUP_POPUP
                        + " yw-pointer-menupopup yw-pointer-menupopup-top yw-pointer-menupopup-top-right");
        groupConfig.getActions().forEach(config -> {
            final String viewMode = extractParam(config, ActionContext.VIEW_MODE_PARAM);
            renderAction(parent, groupConfig, popup, actionComponents, config, viewMode);
        });
        final Button button = new Button();
        button.setSclass("yw-show-menu-action");
        button.addEventListener(Events.ON_CLICK, event -> popup.open(button, "after_end"));
        container.appendChild(button);
        container.appendChild(popup);
        popup.addEventListener(Events.ON_OPEN, e -> closeSubActionsPopups(e.getTarget()));
    }


    protected String extractParam(final Action action, final String paramName)
    {
        final Optional<Parameter> parameter = action.getParameter().stream()
                        .filter(param -> Objects.equals(paramName, param.getName())).findFirst();
        return parameter.isPresent() ? parameter.get().getValue() : null;
    }


    protected void processGroupHeader(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final HtmlBasedComponent container, final boolean showGroupHeader)
    {
        if(showGroupHeader)
        {
            renderGroupHeader(parent, groupConfig, container);
        }
        else
        {
            removeGroupLabel(container);
        }
    }


    protected void renderGroupHeader(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final HtmlBasedComponent container)
    {
        String title = parent.getWidgetInstanceManager().getLabel(groupConfig.getLabel());
        if(StringUtils.isBlank(title))
        {
            title = getLabel(groupConfig.getLabel());
            if(StringUtils.isBlank(title))
            {
                title = groupConfig.getQualifier();
            }
        }
        final Label label = getOrCreateGroupLabel(container);
        label.setValue(title);
    }


    protected String getLabel(final String key)
    {
        return Labels.getLabel(key);
    }


    protected void renderActionGroupExtended(final AbstractCockpitElementsContainer parent, final ActionGroupExtended groupConfig,
                    final HtmlBasedComponent container, final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        YTestTools.modifyYTestId(container, YTESTID_EXTENDED_ACTION_GROUP);
        final Popup popup = new Popup();
        popup.setSclass(SCLASS_ACTION_GROUP_POPUP);
        YTestTools.modifyYTestId(popup, YTESTID_EXTENDED_ACTION_GROUP_POPUP);
        final Optional<com.hybris.cockpitng.components.Action> extendedActionComponent;
        groupConfig.getActions().forEach(
                        config -> renderAction(parent, groupConfig, popup, actionComponents, config, ActionContext.VIEWMODE_TEXTONLY));
        if(groupConfig.getExtendedAction() == null)
        {
            extendedActionComponent = getFirstFromActionComponents(actionComponents);
        }
        else
        {
            final Optional<com.hybris.cockpitng.components.Action> extendedActionOnActionComponents = findExtendedActionOnActionComponents(
                            groupConfig.getExtendedAction(), actionComponents);
            if(extendedActionOnActionComponents.isPresent())
            {
                extendedActionComponent = extendedActionOnActionComponents;
            }
            else
            {
                renderAction(parent, groupConfig, popup, actionComponents, groupConfig.getExtendedAction(),
                                ActionContext.VIEWMODE_TEXTONLY);
                extendedActionComponent = getLastFromActionComponents(actionComponents);
                extendedActionComponent.ifPresent(action -> popup.getChildren().remove(action));
            }
        }
        if(extendedActionComponent.isPresent())
        {
            renderActionGroupExtendedClickableArea(container, popup, extendedActionComponent.get());
            container.appendChild(popup);
        }
    }


    protected Optional<com.hybris.cockpitng.components.Action> getLastFromActionComponents(
                    final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        return Optional.ofNullable(Iterables.getLast(actionComponents, null));
    }


    protected Optional<com.hybris.cockpitng.components.Action> getFirstFromActionComponents(
                    final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        return Optional.ofNullable(Iterables.getFirst(actionComponents, null));
    }


    protected Optional<com.hybris.cockpitng.components.Action> findExtendedActionOnActionComponents(final Action extendedAction,
                    final Collection<com.hybris.cockpitng.components.Action> actionComponents)
    {
        return actionComponents.stream().filter(action -> action.getActionId().equals(extendedAction.getActionId())).findFirst();
    }


    protected void renderActionGroupExtendedClickableArea(final HtmlBasedComponent container, final Popup popup,
                    final com.hybris.cockpitng.components.Action action)
    {
        final Div extendedContainer = new Div();
        extendedContainer.setSclass(SCALSS_YA_EXTENDED_GROUP_CNT);
        container.appendChild(extendedContainer);
        final String iconUri = extractIconUri(action.getActionContext());
        if(isUseImage(iconUri))
        {
            final var image = new Image();
            final String iconHoverUri = extractIconHoverUri(action.getActionContext());
            image.setSrc(iconUri);
            extendedContainer.appendChild(image);
            image.addEventListener(Events.ON_CLICK, event -> popup.open(extendedContainer, GROUPED_ACTIONS_POPUP_POSITION));
            registerHoverEventsOnContainer(extendedContainer, image, iconUri, iconHoverUri);
        }
        else
        {
            final var iconButton = new Button();
            final String iconName = getIconName(action.getActionContext());
            iconButton.setSclass("cng-action-icon cng-font-icon");
            iconButton.addSclass("font-icon--" + iconName);
            extendedContainer.appendChild(iconButton);
            iconButton.addEventListener(Events.ON_CLICK, event -> popup.open(extendedContainer, GROUPED_ACTIONS_POPUP_POSITION));
            registerHoverEventsOnContainer(extendedContainer, iconButton);
        }
        final Toolbarbutton toolbarbutton = new Toolbarbutton();
        toolbarbutton.setSclass(SCLASS_YA_ACTION_SELECTOR_BUTTON);
        extendedContainer.appendChild(toolbarbutton);
        extendedContainer.addEventListener(Events.ON_CLICK, event -> popup.open(extendedContainer, GROUPED_ACTIONS_POPUP_POSITION));
        toolbarbutton.addEventListener(Events.ON_CLICK, event -> popup.open(extendedContainer, GROUPED_ACTIONS_POPUP_POSITION));
        popup.addEventListener(Events.ON_OPEN, e -> closeSubActionsPopups(e.getTarget()));
    }


    protected void closeSubActionsPopups(final Component component)
    {
        component.getChildren().stream() //
                        .filter(com.hybris.cockpitng.components.Action.class::isInstance) //
                        .map(com.hybris.cockpitng.components.Action.class::cast) //
                        .map(com.hybris.cockpitng.components.Action::getChildren) //
                        .flatMap(Collection::stream) //
                        .filter(Window.class::isInstance) //
                        .map(Window.class::cast) //
                        .forEach(Window::detach);
    }


    protected String extractIconUri(final ActionContext context)
    {
        final String param = (String)context.getParameter("iconUri");
        if(StringUtils.isNotBlank(param))
        {
            return param;
        }
        else
        {
            final String uri = context.getIconUri();
            return adjustUri(context, uri);
        }
    }


    protected String extractIconDisabledUri(final ActionContext context)
    {
        final String param = (String)context.getParameter("iconDisabledUri");
        if(StringUtils.isNotBlank(param))
        {
            return param;
        }
        else
        {
            final String uri = context.getIconDisabledUri();
            return adjustUri(context, uri);
        }
    }


    protected String extractIconHoverUri(final ActionContext context)
    {
        final String param = (String)context.getParameter("iconHoverUri");
        if(StringUtils.isNotBlank(param))
        {
            return param;
        }
        else
        {
            final String uri = context.getIconHoverUri();
            return adjustUri(context, uri);
        }
    }


    protected String adjustUri(final ActionContext context, final String uri)
    {
        String localUri = uri;
        if(!StringUtils.isBlank(localUri))
        {
            if(!(localUri.charAt(0) == '/'))
            {
                localUri = "/" + localUri;
            }
            localUri = context.getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM) + localUri;
        }
        return localUri;
    }


    protected void registerHoverEventsOnContainer(final HtmlBasedComponent container, final Image icon, final String iconUri,
                    final String iconHoverUri)
    {
        removeEventListeners(container, Events.ON_MOUSE_OVER);
        removeEventListeners(container, Events.ON_MOUSE_OUT);
        container.addEventListener(Events.ON_MOUSE_OVER, event -> {
            icon.setSrc(StringUtils.defaultIfBlank(iconHoverUri, iconUri));
            UITools.modifySClass(container, YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER, true);
        });
        container.addEventListener(Events.ON_MOUSE_OUT, event -> {
            icon.setSrc(iconUri);
            UITools.modifySClass(container, YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER, false);
        });
    }


    protected void registerHoverEventsOnContainer(final HtmlBasedComponent container, final Button iconBtn)
    {
        removeEventListeners(container, Events.ON_MOUSE_OVER);
        removeEventListeners(container, Events.ON_MOUSE_OUT);
        container.addEventListener(Events.ON_MOUSE_OVER, event -> {
            UITools.modifySClass(container, YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER, true);
            iconBtn.addSclass(YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER);
        });
        container.addEventListener(Events.ON_MOUSE_OUT, event -> {
            UITools.modifySClass(container, YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER, false);
            iconBtn.removeSclass(YA_CNG_EXTENDED_ACTION_CONTAINER_HOVER);
        });
    }


    protected void removeEventListeners(final Component container, final String eventName)
    {
        removeEventListeners(container, eventName, ExtendedActionGroupEventListener.class);
    }


    protected void removeEventListeners(final Component container, final String eventName, final Class type)
    {
        final Iterator<EventListener<? extends Event>> overIterator = container.getEventListeners(eventName).iterator();
        while(overIterator.hasNext())
        {
            if(type.isAssignableFrom(overIterator.next().getClass()))
            {
                overIterator.remove();
            }
        }
    }


    protected void renderActionGroupSplit(final AbstractCockpitElementsContainer parent, final ActionGroupSplit groupConfig,
                    final HtmlBasedComponent container, final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        UITools.addSClass(container, SCALSS_YA_SPLIT_GROUP_CNT);
        YTestTools.modifyYTestId(container, YTESTID_SPLIT_ACTION_GROUP);
        final Popup popup = new Popup();
        popup.setSclass(SCLASS_ACTION_GROUP_POPUP);
        YTestTools.modifyYTestId(popup, YTESTID_SPLIT_ACTION_GROUP_POPUP);
        final Action primaryAction = resolvePrimaryAction(groupConfig);
        final List<Action> actionsForPopup = groupConfig.getActions();
        renderActionsForPopupInActionGroupSplit(parent, groupConfig, actionComponents, popup, actionsForPopup);
        renderPrimaryAction(parent, groupConfig, actionComponents, popup, primaryAction);
        final Optional<com.hybris.cockpitng.components.Action> primaryActionComponent = getLastFromActionComponents(
                        actionComponents);
        primaryActionComponent.ifPresent(container::appendChild);
        if(!actionsForPopup.isEmpty())
        {
            final Toolbarbutton toolbarbutton = createToolbarbuttonForActionGroupSplit(popup);
            container.appendChild(toolbarbutton);
            container.appendChild(popup);
            popup.addEventListener(Events.ON_OPEN, e -> {
                UITools.modifySClass(toolbarbutton, SCLASS_YA_ACTION_SELECTOR_BUTTON_OPENED, false);
                closeSubActionsPopups(e.getTarget());
            });
        }
    }


    protected Action resolvePrimaryAction(final ActionGroupSplit groupConfig)
    {
        if(groupConfig.getPrimaryAction() == null)
        {
            return groupConfig.getActions().get(0);
        }
        else
        {
            return groupConfig.getPrimaryAction();
        }
    }


    protected void renderActionsForPopupInActionGroupSplit(final AbstractCockpitElementsContainer parent,
                    final ActionGroupSplit groupConfig, final Set<com.hybris.cockpitng.components.Action> actionComponents,
                    final Popup popup, final List<Action> actionsForPopup)
    {
        actionsForPopup.forEach(
                        config -> renderAction(parent, groupConfig, popup, actionComponents, config, ActionContext.VIEWMODE_TEXTONLY));
    }


    protected void renderPrimaryAction(final AbstractCockpitElementsContainer parent, final ActionGroupSplit groupConfig,
                    final Set<com.hybris.cockpitng.components.Action> actionComponents, final Popup popup, final Action primaryAction)
    {
        renderAction(parent, groupConfig, popup, actionComponents, primaryAction, ActionContext.VIEWMODE_ICONONLY);
    }


    protected Toolbarbutton createToolbarbuttonForActionGroupSplit(final Popup popup)
    {
        final Toolbarbutton toolbarbutton = new Toolbarbutton();
        toolbarbutton.setSclass(SCLASS_YA_ACTION_SELECTOR_BUTTON);
        toolbarbutton.addEventListener(Events.ON_CLICK, event -> {
            UITools.modifySClass(toolbarbutton, SCLASS_YA_ACTION_SELECTOR_BUTTON_OPENED, true);
            popup.open(toolbarbutton, GROUPED_ACTIONS_POPUP_POSITION);
        });
        return toolbarbutton;
    }


    protected void cleanUpActionComponents(final AbstractCockpitElementsContainer parent,
                    final Set<HtmlBasedComponent> groupContainers)
    {
        final List<Component> children = parent.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            children.removeIf(child -> {
                final boolean attributeExists = child.getAttribute(ACTION_GROUP_QUALIFIER) != null;
                final boolean containersDoesNotHaveChild = !groupContainers.contains(child);
                return attributeExists && containersDoesNotHaveChild;
            });
        }
    }


    protected void renderAction(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final HtmlBasedComponent container, final Set<com.hybris.cockpitng.components.Action> actionComponents,
                    final Action actionConfig)
    {
        renderAction(parent, groupConfig, container, actionComponents, actionConfig, null);
    }


    protected void renderAction(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final HtmlBasedComponent container, final Set<com.hybris.cockpitng.components.Action> actionComponents,
                    final Action actionConfig, final String viewMode)
    {
        final com.hybris.cockpitng.components.Action actionComponent = getOrCreateActionComponent(parent, container, actionConfig,
                        actionComponents);
        if(actionComponent.getActionUID() == null)
        {
            final String actionUID = generateActionUID(parent, groupConfig, actionConfig);
            actionComponent.setActionUID(actionUID);
        }
        renderAction(parent, actionComponent, actionConfig, viewMode);
        actionComponents.add(actionComponent);
        if(container instanceof Popup)
        {
            final int priority = -5000;
            actionComponent.addEventListener(priority, com.hybris.cockpitng.components.Action.ON_ACTION_PERFORMED,
                            event -> ((Popup)container).close());
        }
    }


    protected String generateActionUID(final AbstractCockpitElementsContainer parent, final ActionGroup groupConfig,
                    final Action actionConfig)
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(parent.getWidgetInstanceManager().getWidgetslot().getWidgetInstance().getId()).append("-");
        if(StringUtils.isNotBlank(parent.getId()))
        {
            buffer.append(parent.getId()).append("-");
        }
        if(groupConfig != null)
        {
            buffer.append(groupConfig.getQualifier()).append("-");
            if(actionConfig != null)
            {
                buffer.append(groupConfig.getActions().indexOf(actionConfig));
            }
        }
        return buffer.toString();
    }


    protected void removeChildActionsNonExistingInActionComponents(
                    final Set<com.hybris.cockpitng.components.Action> actionComponents, final List<Component> children)
    {
        if(CollectionUtils.isEmpty(children))
        {
            return;
        }
        children.removeIf(child -> {
            final boolean childIsAnAction = child instanceof com.hybris.cockpitng.components.Action;
            final boolean actionDoesNotContainChild = !actionComponents.contains(child);
            return childIsAnAction && actionDoesNotContainChild;
        });
    }


    protected HtmlBasedComponent getOrCreateGroupContainer(final AbstractCockpitElementsContainer parent,
                    final ActionGroup groupConfig)
    {
        Hbox container = null;
        final List<Component> children = parent.getChildren();
        for(final Component child : children)
        {
            final String qualifier = ObjectUtils.toString(child.getAttribute(ACTION_GROUP_QUALIFIER), null);
            if((child instanceof Hbox) && groupQualifierEquals(groupConfig.getQualifier(), qualifier))
            {
                container = (Hbox)child;
                break;
            }
        }
        if(container == null)
        {
            container = createHbox();
            container.setSpacing("10px");
            container.setAttribute(ACTION_GROUP_QUALIFIER, computeGroupQualifier(groupConfig));
            container.setSclass("cng-action-group");
            UITools.modifySClass(container, "cng-action-separator", groupConfig.isShowSeparator());
            container.setParent(parent);
        }
        return container;
    }


    protected Hbox createHbox()
    {
        return new Hbox();
    }


    protected String computeGroupQualifier(final ActionGroup groupConfig)
    {
        return (StringUtils.isEmpty(groupConfig.getQualifier()) ? DEFAULT_QUALIFIER : groupConfig.getQualifier());
    }


    protected boolean groupQualifierEquals(final String quali1, final String quali2)
    {
        final String localQ1 = (StringUtils.isEmpty(quali1) ? DEFAULT_QUALIFIER : quali1);
        final String localQ2 = (StringUtils.isEmpty(quali2) ? DEFAULT_QUALIFIER : quali2);
        return localQ1.equals(localQ2);
    }


    protected Label getOrCreateGroupLabel(final Component container)
    {
        Label label = null;
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Label)
            {
                label = (Label)child;
                break;
            }
        }
        if(label == null)
        {
            label = new Label();
            label.setParent(container);
        }
        return label;
    }


    protected void removeGroupLabel(final Component container)
    {
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Label)
            {
                children.remove(child);
                break;
            }
        }
    }


    protected com.hybris.cockpitng.components.Action getOrCreateActionComponent(
                    final AbstractCockpitElementsContainer elementsContainer, final Component parent, final Action actionConfig,
                    final Set<com.hybris.cockpitng.components.Action> actionComponents)
    {
        com.hybris.cockpitng.components.Action component = null;
        final List<Component> children = parent.getChildren();
        for(final Component child : children)
        {
            if(child instanceof com.hybris.cockpitng.components.Action)
            {
                final com.hybris.cockpitng.components.Action action = (com.hybris.cockpitng.components.Action)child;
                if((!actionComponents.contains(action)) && Objects.equals(action.getActionId(), actionConfig.getActionId()))
                {
                    component = action;
                    break;
                }
            }
        }
        if(component == null)
        {
            component = new com.hybris.cockpitng.components.Action();
            parent.appendChild(component);
            component.addEventListener(com.hybris.cockpitng.components.Action.ON_ACTION_PERFORMED,
                            new ActionContainerEventListener(elementsContainer));
        }
        return component;
    }


    /**
     * Renders single action with viewMode inherited from parent Actions tag.
     */
    protected com.hybris.cockpitng.components.Action renderAction(final AbstractCockpitElementsContainer parent,
                    final com.hybris.cockpitng.components.Action actionContainer, final Action actionConfig)
    {
        return renderAction(parent, actionContainer, actionConfig, null);
    }


    /**
     * Renders single action with given viewMode. If viewMode is null it will take the viewMode from parent Actions tag.
     */
    protected com.hybris.cockpitng.components.Action renderAction(final AbstractCockpitElementsContainer parent,
                    final com.hybris.cockpitng.components.Action actionContainer, final Action actionConfig, final String viewMode)
    {
        final String qualifier = parent.getQualifier(actionConfig.getProperty());
        actionContainer.setWidgetInstanceManager(parent.getWidgetInstanceManager());
        actionContainer.setInputValue(((com.hybris.cockpitng.components.Actions)parent).getInputValue());
        actionContainer.setProperty(qualifier);
        actionContainer.setOutputProperty(actionConfig.getOutputProperty());
        actionContainer.setActionId(actionConfig.getActionId());
        if(StringUtils.isNotBlank(actionConfig.getTriggerOnKeys()))
        {
            if(getKeyboardSupportService().validateCtrlKeys(actionConfig.getTriggerOnKeys()))
            {
                actionContainer.setTriggerOnKeys(actionConfig.getTriggerOnKeys());
            }
            else
            {
                LOG.error("Invalid triggerOnKeys '{}' for action {}", actionConfig.getTriggerOnKeys(), actionConfig.getActionId());
            }
        }
        final String viewModeToUse = (StringUtils.isBlank(viewMode)
                        ? ((com.hybris.cockpitng.components.Actions)parent).getViewMode() : viewMode);
        if(!StringUtils.isBlank(viewModeToUse))
        {
            actionContainer.setViewMode(viewModeToUse);
        }
        final List<Parameter> parameters = actionConfig.getParameter();
        for(final Parameter parameter : parameters)
        {
            actionContainer.addParameter(parameter.getName(), parameter.getValue());
        }
        actionContainer.afterCompose();
        return actionContainer;
    }


    public KeyboardSupportService getKeyboardSupportService()
    {
        if(keyboardSupportService == null)
        {
            keyboardSupportService = (KeyboardSupportService)SpringUtil.getBean("keyboardSupportService",
                            KeyboardSupportService.class);
        }
        return keyboardSupportService;
    }


    public LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getBean("labelService", LabelService.class);
        }
        return labelService;
    }


    /**
     * Marker interface for events created by action renderer.
     */
    protected interface ExtendedActionGroupEventListener<E extends Event> extends EventListener<E>
    {
        // marker interface
    }


    protected boolean isUseImage(final String iconUri)
    {
        return StringUtils.isNotEmpty(iconUri) &&
                        (iconUri.indexOf(".jpg") > -1 || iconUri.indexOf(".svg") > -1 || iconUri.indexOf(".gif") > -1 || iconUri.indexOf(".png") > -1);
    }


    protected String getIconName(final ActionContext context)
    {
        final String iconName = (String)context.getParameter("iconUri");
        return StringUtils.isNotBlank(iconName) ? iconName : context.getIconUri();
    }


    private static class ActionContainerEventListener implements EventListener<Event>
    {
        private final AbstractCockpitElementsContainer parent;


        public ActionContainerEventListener(final AbstractCockpitElementsContainer parent)
        {
            this.parent = parent;
        }


        @Override
        public void onEvent(final Event event)
        {
            Events.sendEvent(com.hybris.cockpitng.components.Action.ON_ACTION_PERFORMED, parent, event.getData());
        }
    }
}
