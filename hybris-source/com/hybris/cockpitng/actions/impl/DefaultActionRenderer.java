/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.impl;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.CockpitActionRenderer;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.keyboard.KeyboardSupportService;
import com.hybris.cockpitng.util.ClickTrackingTools;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.impl.XulElement;

/**
 * Default renderer for cockpit action. Shows an icon (action icon uri) with tooltip text (action name).
 */
public class DefaultActionRenderer<I, O> implements CockpitActionRenderer<I, O>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActionRenderer.class);
    protected static final String LABEL_CONTAINER_SCLASS = "cng-action-text";
    private static final String FONT_ICON_PREFIX = "font-icon--";
    private static final String ACTION_CONTAINER_HOVER_CLASS = "ya-cng-action-container-hover";
    private KeyboardSupportService keyboardSupportService;
    protected String tooltipText;


    @Override
    public void render(final Component parent, final CockpitAction<I, O> action, final ActionContext<I> context,
                    final boolean updateMode, final ActionListener<O> listener)
    {
        if(!checkDataCompatibleWithDefinedInput(action, context))
        {
            parent.getChildren().clear();
            return;
        }
        final HtmlBasedComponent container = getOrCreateContainer(parent);
        String sclass = getActionBodySclass(context);
        final boolean enabled = action.canPerform(context);
        if(enabled)
        {
            sclass += " cng-action-enabled";
        }
        else
        {
            sclass += " cng-action-disabled";
        }
        container.setSclass(sclass);
        ClickTrackingTools.modifyClickTrackingId(container, (String)context.getParameter("actionName"));
        YTestTools.modifyYTestId(container, createYTestId(context));
        final boolean showText = context.isShowText();
        final boolean showIcon = context.isShowIcon() || !(context.isHidden() || showText);
        final String iconUri = getIconUri(context, enabled);
        final String localizedName = tooltipText != null ? tooltipText : getLocalizedName(context);
        final String iconHoverUri = getIconHoverUri(context, enabled);
        final boolean useImage = useImage(iconUri);
        if(showIcon)
        {
            if(!useImage)
            {
                String iconName;
                final String configuredIconName = getIconName(context);
                iconName = StringUtils.isNotBlank(configuredIconName) ? configuredIconName : iconUri;
                final var iconButton = getOrCreateIconButton(container, enabled, FONT_ICON_PREFIX + iconName);
                setTips(showText, iconButton, context, localizedName);
                registerHoverEventsOnContainer(container, iconButton, enabled);
                final EventListener<? extends Event> eventListener = createEventListener(action, context, listener);
                addEventListener(iconButton, eventListener);
            }
            else
            {
                final Image icon = getOrCreateIcon(container, context);
                icon.setSrc(iconUri);
                setTips(showText, icon, context, localizedName);
                registerHoverEventsOnContainer(container, icon, showIcon, iconUri, iconHoverUri);
            }
        }
        else
        {
            removeIcon(container);
            if(useImage)
            {
                registerHoverEventsOnContainer(container, null, showIcon, iconUri, iconHoverUri);
            }
        }
        // tip label
        if(!context.isHidden() && (StringUtils.isEmpty(iconUri) || showText))
        {
            final var label = createTipsLabel(container);
            label.setValue(localizedName);
            label.setTooltiptext(getToolTipWithCtrlKeysText(null, context));
        }
        else
        {
            removeLabel(container);
        }
        removeEventListeners(container, Events.ON_CLICK, EventListener.class);
        container.removeForward(Events.ON_CLICK, container, Events.ON_MOUSE_OUT);
        if(enabled)
        {
            container.addForward(Events.ON_CLICK, container, Events.ON_MOUSE_OUT);
            final EventListener<? extends Event> eventListener = createEventListener(action, context, listener);
            addEventListener(container, eventListener);
            addKeyboardSupport(container, action, context, eventListener);
        }
    }


    protected boolean checkDataCompatibleWithDefinedInput(final CockpitAction<I, O> action, final ActionContext<I> context)
    {
        final Class<Object> expectedInput = ReflectionUtils.extractGenericParameterType(action.getClass(), CockpitAction.class, 0);
        if(context.getData() != null && expectedInput != null && !expectedInput.isAssignableFrom(context.getData().getClass()))
        {
            if(LOGGER.isWarnEnabled())
            {
                LOGGER.warn(String.format(
                                "Illegal configuration! Action class '%s' requires '%s' as input, yet configured to be used in context of '%s'",
                                action.getClass().getName(), expectedInput.getName(), context.getData().getClass().getName()));
            }
            return false;
        }
        return true;
    }


    public String getTooltipText()
    {
        return tooltipText;
    }


    public void setTooltipText(final String tooltipText)
    {
        this.tooltipText = tooltipText;
    }


    protected synchronized void registerHoverEventsOnContainer(final HtmlBasedComponent container, final Button iconButton,
                    final boolean enabled)
    {
        removeEventListeners(container, Events.ON_MOUSE_OVER);
        removeEventListeners(container, Events.ON_MOUSE_OUT);
        if(enabled)
        {
            container.addEventListener(Events.ON_MOUSE_OVER, (DefaultActionRendererEventListener<Event>)event -> {
                UITools.modifySClass(container, ACTION_CONTAINER_HOVER_CLASS, true);
                iconButton.addSclass(ACTION_CONTAINER_HOVER_CLASS);
            });
            container.addEventListener(Events.ON_MOUSE_OUT, (DefaultActionRendererEventListener<Event>)event -> {
                UITools.modifySClass(container, ACTION_CONTAINER_HOVER_CLASS, false);
                iconButton.removeSclass(ACTION_CONTAINER_HOVER_CLASS);
            });
        }
    }


    protected synchronized void registerHoverEventsOnContainer(final HtmlBasedComponent container, final Image icon,
                    final boolean showIcon, final String iconUri, final String iconHoverUri)
    {
        removeEventListeners(container, Events.ON_MOUSE_OVER);
        removeEventListeners(container, Events.ON_MOUSE_OUT);
        container.addEventListener(Events.ON_MOUSE_OVER, (DefaultActionRendererEventListener<Event>)event -> {
            if(showIcon && icon != null)
            {
                icon.setSrc(StringUtils.defaultIfBlank(iconHoverUri, iconUri));
            }
            UITools.modifySClass(container, ACTION_CONTAINER_HOVER_CLASS, true);
        });
        container.addEventListener(Events.ON_MOUSE_OUT, (DefaultActionRendererEventListener<Event>)event -> {
            if(showIcon && icon != null)
            {
                icon.setSrc(iconUri);
            }
            UITools.modifySClass(container, ACTION_CONTAINER_HOVER_CLASS, false);
        });
    }


    /**
     * Removes all even listeners of a given type from the container if they are assignable from
     * com.hybris.cockpitng.actions.impl.DefaultActionRenderer.DefaultActionRendererEventListener.
     *
     * @param container
     *           a component from which the listeners should be removed
     * @param eventName
     *           name of the event on which the listeners are registered
     * @see com.hybris.cockpitng.actions.impl.DefaultActionRenderer.DefaultActionRendererEventListener
     */
    protected void removeEventListeners(final Component container, final String eventName)
    {
        removeEventListeners(container, eventName, DefaultActionRendererEventListener.class);
    }


    /**
     * Removes all even listeners of a given type from the container if they are assignable from the given type.
     *
     * @param container
     *           a component from which the listeners should be removed
     * @param eventName
     *           name of the event on which the listeners are registered
     * @param type
     *           type to which the target listeners should be assignable
     */
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


    /**
     * Creates a default ytestid
     *
     * @return ytestid
     */
    protected String createYTestId(final ActionContext<I> context)
    {
        return StringUtils.replace(context.getCode(), ".", "_");
    }


    /**
     * Creates a default sclass
     *
     * @return default sclass
     */
    protected String getActionBodySclass(final ActionContext<I> context)
    {
        return "ya-" + StringUtils.replace(context.getCode(), ".", "_") + " cng-action "
                        + (Objects.toString(context.getParameter("actionStyleClass"), ""));
    }


    protected void perform(final CockpitAction<I, O> action, final ActionContext<I> context, final ActionListener<O> listener)
    {
        final ActionResult<O> result = action.perform(context);
        listener.actionPerformed(result);
    }


    protected void performWithConfirmationCheck(final CockpitAction<I, O> action, final ActionContext<I> context,
                    final ActionListener<O> listener)
    {
        final boolean confirmationNeeded = action.needsConfirmation(context);
        if(confirmationNeeded)
        {
            Messagebox.show(action.getConfirmationMessage(context), getLocalizedName(context), new Messagebox.Button[]
                            {Messagebox.Button.YES, Messagebox.Button.CANCEL}, null, Messagebox.QUESTION, null, clickEvent -> {
                if(Messagebox.Button.YES == clickEvent.getButton())
                {
                    perform(action, context, listener);
                }
            }, null);
        }
        else
        {
            perform(action, context, listener);
        }
    }


    protected HtmlBasedComponent getOrCreateContainer(final Component parent)
    {
        final List<Component> children = parent.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            return (Hbox)children.iterator().next();
        }
        else
        {
            final Hbox container = new Hbox();
            container.setParent(parent);
            return container;
        }
    }


    /**
     * @deprecated since 1811. Please use {@link #getOrCreateIcon(Component, ActionContext)} instead.
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Image getOrCreateIcon(final Component container)
    {
        Image image = null;
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Image)
            {
                image = (Image)child;
                break;
            }
        }
        if(image == null)
        {
            image = new Image();
            image.setSclass("cng-action-icon");
            image.setParent(container);
        }
        return image;
    }


    protected Button getOrCreateIconButton(final Component container, boolean enabled, String iconClass)
    {
        Button button = null;
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Button)
            {
                button = (Button)child;
                break;
            }
        }
        if(button == null)
        {
            button = new Button();
            button.setSclass("cng-action-icon cng-font-icon");
            button.addSclass(iconClass);
            button.setParent(container);
        }
        if(!enabled)
        {
            button.setDisabled(true);
        }
        return button;
    }


    protected Label createTipsLabel(final Component container)
    {
        final var label = new Label();
        final var div = new Div();
        div.setSclass(LABEL_CONTAINER_SCLASS);
        label.setParent(div);
        div.setParent(container);
        return label;
    }


    protected void setTips(final boolean showText, final XulElement element, final ActionContext<I> context,
                    final String localizedName)
    {
        // tooltip
        if(showText || StringUtils.isBlank(localizedName))
        {
            element.setTooltiptext(getToolTipWithCtrlKeysText(null, context));
        }
        else
        {
            element.setTooltiptext(getToolTipWithCtrlKeysText(localizedName, context));
        }
    }


    protected Image getOrCreateIcon(final Component container, final ActionContext<I> ctx)
    {
        return getOrCreateIcon(container);
    }


    protected void removeIcon(final Component container)
    {
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Image || child instanceof Button)
            {
                children.remove(child);
                break;
            }
        }
    }


    protected Label getOrCreateLabel(final Component container, final ActionContext<I> ctx)
    {
        return getOrCreateLabel(container);
    }


    protected Label getOrCreateLabel(final Component container)
    {
        Label label = null;
        final List<Component> children = container.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Div)
            {
                final Optional<Component> firstLabel = child.getChildren().stream().filter(ch -> ch instanceof Label).findFirst();
                label = (Label)firstLabel.orElse(null);
            }
        }
        if(label == null)
        {
            final Div div = new Div();
            div.setSclass(LABEL_CONTAINER_SCLASS);
            label = new Label();
            label.setParent(div);
            div.setParent(container);
        }
        return label;
    }


    protected void removeLabel(final Component container)
    {
        final List<Component> children = container.getChildren();
        boolean labelFound = false;
        for(final Component child : children)
        {
            if(!labelFound && child instanceof Div)
            {
                final List<Component> divChildren = child.getChildren();
                for(final Component divChild : divChildren)
                {
                    if(divChild instanceof Label)
                    {
                        labelFound = true;
                        break;
                    }
                }
            }
            if(labelFound)
            {
                children.remove(child);
            }
        }
    }


    protected void addKeyboardSupport(final Component component, final CockpitAction<I, O> cockpitAction,
                    final ActionContext<I> ctx, final EventListener onClickEventListener)
    {
        if(StringUtils.isNotBlank(ctx.getTriggerOnKeys()))
        {
            final HtmlBasedComponent actionComponent = findActionComponent(component);
            if(actionComponent != null)
            {
                actionComponent.addEventListener(Events.ON_CTRL_KEY, event -> {
                    final KeyEvent keyEvent = extractKeyEvent(event);
                    if(keyEvent != null && getKeyboardSupportService().containsKeyEvent(ctx.getTriggerOnKeys(), keyEvent))
                    {
                        triggerFromKeyEvent(keyEvent, cockpitAction, ctx, component, onClickEventListener);
                    }
                });
            }
        }
    }


    protected String getToolTipWithCtrlKeysText(final String toolTipToDecorate, final ActionContext<I> ctx)
    {
        String decorated = toolTipToDecorate;
        if(StringUtils.isNotBlank(ctx.getTriggerOnKeys()))
        {
            final String ctrlKeysToolTip = getKeyboardSupportService().getToolTip(ctx.getTriggerOnKeys());
            if(StringUtils.isNotBlank(toolTipToDecorate))
            {
                decorated = String.format("%s %s", toolTipToDecorate, ctrlKeysToolTip);
            }
            else
            {
                decorated = ctrlKeysToolTip;
            }
        }
        return decorated;
    }


    /**
     * Extracts KeyEvent from given event.
     *
     * @param event
     *           event to cast.
     * @return KeyEvent or null if given event is not KeyEvent nor Forward Event with origin KeyEvent
     */
    protected KeyEvent extractKeyEvent(final Event event)
    {
        if(event instanceof KeyEvent)
        {
            return (KeyEvent)event;
        }
        else if(event instanceof ForwardEvent)
        {
            return extractKeyEvent(((ForwardEvent)event).getOrigin());
        }
        return null;
    }


    protected void triggerFromKeyEvent(final KeyEvent keyEvent, final CockpitAction<I, O> cockpitAction,
                    final ActionContext<I> ctx, final Component container, final EventListener onClickEventListener)
    {
        if(cockpitAction.canPerform(ctx))
        {
            Clients.evalJavaScript("$(\"#" + container.getUuid() + "\").fadeOut('fast').fadeIn('fast');");
            try
            {
                onClickEventListener.onEvent(new Event(Events.ON_CLICK, container, null));
            }
            catch(final Exception e)
            {
                throw new IllegalStateException(e);
            }
        }
    }


    protected EventListener<? extends Event> createEventListener(final CockpitAction<I, O> action, final ActionContext<I> context,
                    final ActionListener<O> listener)
    {
        return event -> {
            event.stopPropagation();
            performWithConfirmationCheck(action, context, listener);
        };
    }


    protected void addEventListener(final Component container, final EventListener<? extends Event> eventListener)
    {
        container.addEventListener(Events.ON_CLICK, eventListener);
    }


    protected String getIconHoverUri(final ActionContext<I> context, final boolean canPerform)
    {
        String uri;
        if(canPerform)
        {
            uri = extractIconHoverUri(context);
            if(StringUtils.isBlank(uri))
            {
                uri = extractIconUri(context);
            }
        }
        else
        {
            uri = extractIconDisabledUri(context);
            if(StringUtils.isEmpty(uri))
            {
                uri = extractIconUri(context);
            }
        }
        return uri;
    }


    protected String getIconName(final ActionContext<I> context)
    {
        final String iconName = (String)context.getParameter("iconUri");
        return StringUtils.isNotBlank(iconName) ? iconName : context.getIconUri();
    }


    protected String getIconUri(final ActionContext<I> context, final boolean canPerform)
    {
        String uri;
        if(canPerform)
        {
            uri = extractIconUri(context);
        }
        else
        {
            uri = extractIconDisabledUri(context);
            if(StringUtils.isEmpty(uri))
            {
                uri = extractIconUri(context);
            }
        }
        return uri;
    }


    protected String extractIconUri(final ActionContext<I> context)
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


    protected String extractIconHoverUri(final ActionContext<I> context)
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


    protected String extractIconDisabledUri(final ActionContext<I> context)
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


    protected String adjustUri(final ActionContext<I> context, String uri)
    {
        if(!StringUtils.isBlank(uri))
        {
            if(!(uri.charAt(0) == '/'))
            {
                uri = "/" + uri;
            }
            uri = context.getParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM) + uri;
        }
        return uri;
    }


    protected String getLocalizedName(final ActionContext<?> context)
    {
        final String actionName = "actionName";
        // search for label using key defined in actionName parameter
        final String labelKeyFromConfiguration = (String)context.getParameter(actionName);
        if(StringUtils.isNotBlank(labelKeyFromConfiguration))
        {
            final String label = context.getLabel(labelKeyFromConfiguration);
            if(StringUtils.isNotBlank(label))
            {
                return label;
            }
        }
        // fallback to default label key <action-id>.actionName
        final String label = context.getLabel(actionName);
        if(StringUtils.isNotBlank(label))
        {
            return label;
        }
        // fallback to action name from definition.xml
        return context.getName();
    }


    protected void storeInParentModel(final Object value, final ActionContext<?> context, final String key)
    {
        final WidgetModel model = (WidgetModel)context.getParameter(ActionContext.PARENT_WIDGET_MODEL);
        if(model != null)
        {
            model.setValue(key, value);
        }
    }


    protected <T> T loadFromParentModel(final ActionContext<?> context, final String key, final Class<T> _clazz)
    {
        final WidgetModel model = (WidgetModel)context.getParameter(ActionContext.PARENT_WIDGET_MODEL);
        if(model != null)
        {
            return model.getValue(key, _clazz);
        }
        return null;
    }


    protected <C extends Component> C findParentComponent(final Component component, final Class<C> clazz)
    {
        if(clazz.isAssignableFrom(component.getClass()))
        {
            return (C)component;
        }
        else if(component.getParent() != null)
        {
            return findParentComponent(component.getParent(), clazz);
        }
        else
        {
            return null;
        }
    }


    protected HtmlBasedComponent findActionComponent(final Component component)
    {
        return findParentComponent(component, Action.class);
    }


    protected HtmlBasedComponent findGroupComponent(final Component component)
    {
        return findParentComponent(component, Actions.class);
    }


    protected boolean useImage(final String iconUri)
    {
        return StringUtils.isNotEmpty(iconUri) && (iconUri.indexOf(".jpg") > -1 || iconUri.indexOf(".svg") > -1
                        || iconUri.indexOf(".gif") > -1 || iconUri.indexOf(".png") > -1);
    }


    /**
     * Marker interface for events created by action renderer.
     */
    protected interface DefaultActionRendererEventListener<T extends Event> extends EventListener<T>
    {
        // marker interface
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
}
