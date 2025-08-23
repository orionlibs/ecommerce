/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea;

import com.hybris.backoffice.widgets.notificationarea.event.ClearNotificationsEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.common.model.ObjectWithComponentContext;
import com.hybris.cockpitng.config.notification.jaxb.Destination;
import com.hybris.cockpitng.config.notification.jaxb.Notification;
import com.hybris.cockpitng.config.notification.jaxb.NotificationArea;
import com.hybris.cockpitng.config.notification.jaxb.NotificationDefaults;
import com.hybris.cockpitng.config.notification.jaxb.NotificationDestination;
import com.hybris.cockpitng.config.notification.jaxb.NotificationParameter;
import com.hybris.cockpitng.config.notification.jaxb.NotificationReference;
import com.hybris.cockpitng.config.notification.jaxb.NotificationReferences;
import com.hybris.cockpitng.config.notification.jaxb.NotificationTimeout;
import com.hybris.cockpitng.config.notification.jaxb.NotificationTimeouts;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;

public class NotificationController extends DefaultWidgetController
{
    public static final String RENDERER_PARAMETER_WIDGET_INSTANCE_MANAGER = "wim";
    public static final String SELECTED_ELEMENT_OUT_SOCKET = "selectedElement";
    public static final String NOTIFICATION_MESSAGE_YTESTID_PREFIX = "notificationMessage";
    public static final String MODEL_DYNAMIC_NOTIFICATION_ID = "dynamicNotificationId";
    protected static final String SETTING_DEBUG_LOG = "debugLog";
    protected static final String MESSAGE_COMPONENT = "notificationContainer";
    protected static final String SETTING_NOTIFICATION_ID = "notificationId";
    protected static final String SETTING_STACK_SIZE = "stackSize";
    protected static final String SETTING_USE_DYNAMIC_NOTIFICATION_ID = "useDynamicNotificationId";
    protected static final String ATTRIBUTE_NOTIFICATION_EVENT = "notificationEventAttribute";
    private static final String SETTING_CONFIG_CONTEXT = "configurationContext";
    private static final String CONTEXT_ATTRIBUTE_SOURCE = "source";
    private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);
    private static final Pattern PATTERN_LINK_PLACEHOLDERS = Pattern.compile("([^\\{]*)\\{([0-9]+)\\}");
    private final Deque<Component> notificationEventsStack = new ArrayDeque<>();
    @WireVariable
    private transient ExpressionResolverFactory expressionResolverFactory;
    @WireVariable
    private transient NotificationStack notificationStack;
    private transient LabelService labelService;
    private Div notificationContainer;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        notificationContainer.setVisible(false);
    }


    /**
     * Starting point of notification processing.
     *
     * @param notificationEvent
     *           contains all data necessary for a notification to be shown or discarded.
     */
    @GlobalCockpitEvent(eventName = NotificationEvent.EVENT_NAME, scope = CockpitEvent.SESSION)
    public void onNotification(final NotificationEvent notificationEvent)
    {
        try
        {
            final NotificationArea notificationArea = getNotificationAreaConfig(notificationEvent);
            final Notification notificationConfig = extractNotificationConfig(notificationEvent, notificationArea);
            final NotificationDefaults notificationDefaults = extractNotificationDefaults(notificationArea);
            if(isMatchingNotification(notificationEvent, notificationConfig, notificationDefaults))
            {
                addNotification(notificationEvent, notificationConfig, notificationDefaults);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * Processes clearEvent which removes notifications of specified source and type (if specified in clearEvent
     * {@link ClearNotificationsEvent#getLevel()}).
     *
     * @param clearEvent
     *           contains an id and notification type to remove (not mandatory).
     */
    @GlobalCockpitEvent(eventName = ClearNotificationsEvent.EVENT_NAME, scope = CockpitEvent.SESSION)
    public void onClearNotifications(final ClearNotificationsEvent clearEvent)
    {
        notificationEventsStack.stream().filter(component -> {
            final NotificationEvent notificationEvent = (NotificationEvent)component.getAttribute(ATTRIBUTE_NOTIFICATION_EVENT);
            boolean matching = notificationEvent != null
                            && StringUtils.equals(notificationEvent.getSource(), clearEvent.getSource());
            matching &= clearEvent.getLevel() == null || clearEvent.getLevel().equals(notificationEvent.getLevel());
            return matching;
        }).forEach(this::removeNotification);
    }


    protected NotificationArea getNotificationAreaConfig(final NotificationEvent notificationEvent)
                    throws CockpitConfigurationException
    {
        final String component = (String)getWidgetSettings().getOrDefault(SETTING_CONFIG_CONTEXT, "notification-area");
        final DefaultConfigContext context = new DefaultConfigContext(component);
        context.addAttribute(CONTEXT_ATTRIBUTE_SOURCE, notificationEvent.getSource());
        return getWidgetInstanceManager().loadConfiguration(context, NotificationArea.class);
    }


    protected Notification extractNotificationConfig(final NotificationEvent notificationEvent,
                    final NotificationArea notificationArea)
    {
        if(notificationArea != null && notificationEvent != null)
        {
            final Optional<Notification> notification = notificationArea.getNotifications().stream()
                            .filter(not -> StringUtils.equals(not.getEventType(), notificationEvent.getEventType())
                                            && StringUtils.equals(not.getLevel().value(), notificationEvent.getLevel().name())
                                            && referencedTypeMatches(not.getReferencesType(), notificationEvent.getReferencedObjects()))
                            .reduce(this::resolveConfigurationDuplications);
            return notification.orElse(null);
        }
        return null;
    }


    protected Notification resolveConfigurationDuplications(final Notification a, final Notification b)
    {
        if(StringUtils.isEmpty(a.getReferencesType()) && StringUtils.isEmpty(b.getReferencesType()))
        {
            LOG.warn("Duplicated configuration for type \'{}\' and level \'{}\' found", b.getEventType(), b.getLevel().value());
        }
        else if(StringUtils.isNotEmpty(a.getReferencesType()) && StringUtils.isNotEmpty(b.getReferencesType()))
        {
            LOG.warn("Duplicated configuration for type \'{}\', level \'{}\' and referenceType \\'{}\\' found", b.getEventType(),
                            b.getLevel().value(), b.getReferencesType());
        }
        else if(StringUtils.isEmpty(a.getReferencesType()) && StringUtils.isNotEmpty(b.getReferencesType()))
        {
            // better match with referenceType
            return b;
        }
        return a;
    }


    protected boolean referencedTypeMatches(final String notificationReferenceType, final Object[] referencedTypes)
    {
        if(StringUtils.isNotEmpty(notificationReferenceType))
        {
            final Optional<Class<?>> referencedType = findElementsClass(referencedTypes);
            return referencedType.isPresent() && StringUtils.equals(notificationReferenceType, referencedType.get().getSimpleName());
        }
        return true;
    }


    protected Optional<Class<?>> findElementsClass(final Object ob)
    {
        if(ob == null)
        {
            return Optional.empty();
        }
        else if(ob instanceof Object[])
        {
            return findElementClassFromStream(Stream.of((Object[])ob));
        }
        else if(ob instanceof Map)
        {
            return findElementsClass(((Map)ob).keySet());
        }
        else if(ob instanceof Collection<?>)
        {
            return findElementClassFromStream(((Collection<?>)ob).stream());
        }
        else
        {
            return Optional.of(ob.getClass());
        }
    }


    protected Optional<Class<?>> findElementClassFromStream(final Stream<?> stream)
    {
        final List<Optional<Class<?>>> classes = stream.map(this::findElementsClass).distinct().collect(Collectors.toList());
        if(classes.size() != 1)
        {
            return Optional.empty(); // ambiguous
        }
        return classes.get(0);
    }


    protected NotificationDefaults extractNotificationDefaults(final NotificationArea notificationArea)
    {
        return notificationArea != null ? notificationArea.getDefaults() : null;
    }


    protected BigInteger getNotificationTimeout(final NotificationEvent notificationEvent, final Notification notificationConfig,
                    final NotificationDefaults notificationDefaults)
    {
        BigInteger timeout = notificationConfig != null ? notificationConfig.getTimeout() : null;
        if(timeout == null)
        {
            final NotificationTimeouts timeouts = notificationDefaults == null ? null : notificationDefaults.getTimeouts();
            timeout = timeouts == null ? null : timeouts.getDefault();
            if(timeouts != null && timeouts.getTimeout() != null)
            {
                final Optional<NotificationTimeout> notificationTimeout = notificationDefaults.getTimeouts().getTimeout().stream()
                                .filter(t -> StringUtils.equals(notificationEvent.getLevel().name(), t.getLevel().name())).findFirst();
                if(notificationTimeout.isPresent())
                {
                    timeout = notificationTimeout.get().getValue();
                }
            }
        }
        return timeout;
    }


    protected Destination getNotificationDestination(final NotificationEvent notificationEvent,
                    final Notification notificationConfig, final NotificationDefaults notificationDefaults)
    {
        Destination destination = notificationConfig != null ? notificationConfig.getDestination() : null;
        if(destination == null && notificationDefaults != null)
        {
            final Optional<NotificationDestination> first = notificationDefaults.getDestinations().stream()
                            .filter(d -> StringUtils.equals(notificationEvent.getLevel().name(), d.getLevel().value())).findFirst();
            if(first.isPresent())
            {
                destination = first.get().getDestination();
            }
        }
        return destination;
    }


    protected void addNotification(final NotificationEvent notificationEvent, final Notification notificationConfig,
                    final NotificationDefaults notificationDefaults)
    {
        final Destination destination = getNotificationDestination(notificationEvent, notificationConfig, notificationDefaults);
        if(!Destination.GLOBAL.equals(destination) && isAlreadyNotified(notificationEvent))
        {
            return;
        }
        logNotificationIfNecessary(notificationEvent);
        // create new message component
        final Component component = createNotificationComponent(notificationEvent, notificationConfig, notificationDefaults);
        if(component == null)
        {
            return;
        }
        notificationEventsStack.addLast(component);
        if(notificationEventsStack.size() > getWidgetSettings().getInt(SETTING_STACK_SIZE))
        {
            removeNotification(notificationEventsStack.getFirst());
            if(isDebugLog())
            {
                LOG.warn("Stack is full, removing first one!");
            }
        }
        // add close listener
        component.addEventListener(Events.ON_CLICK, event -> removeNotification(component));
        final BigInteger timeout = getNotificationTimeout(notificationEvent, notificationConfig, notificationDefaults);
        // add timer
        if(timeout != null && timeout.intValue() > 0)
        {
            final Timer timer = new Timer();
            component.appendChild(timer);
            timer.setDelay(timeout.intValue());
            timer.setRepeats(false);
            timer.addEventListener(Events.ON_TIMER, event -> removeNotification(component));
        }
        notificationContainer.appendChild(component);
        notificationContainer.setVisible(true);
    }


    @InextensibleMethod
    private void logNotificationIfNecessary(final NotificationEvent notificationEvent)
    {
        if(isDebugLog())
        {
            final String message = String.format("Received notification from \'%s\' of type \'%s\' with importance \'%s\' for: %s",
                            notificationEvent.getSource(), notificationEvent.getEventType(), notificationEvent.getLevel().name(),
                            ArrayUtils.toString(notificationEvent.getReferencedObjects()));
            switch(notificationEvent.getLevel())
            {
                case FAILURE:
                    LOG.error(message);
                    break;
                case INFO:
                case SUCCESS:
                    LOG.info(message);
                    break;
                case WARNING:
                    LOG.warn(message);
                    break;
            }
        }
    }


    protected boolean isAlreadyNotified(final NotificationEvent notificationEvent)
    {
        for(final Component component : notificationEventsStack)
        {
            if(notificationEvent.equals(component.getAttribute(ATTRIBUTE_NOTIFICATION_EVENT)))
            {
                return true;
            }
        }
        return false;
    }


    protected boolean isGlobalNotification(final String notificationId)
    {
        return StringUtils.equals(Destination.GLOBAL.value(), notificationId);
    }


    protected boolean notificationsMatches(final Destination expected, final String current)
    {
        String expectedId = null;
        if(expected != null)
        {
            switch(expected)
            {
                case TOPMOST:
                    expectedId = notificationStack.getTopmostId();
                    break;
                case PREVIOUS:
                    expectedId = notificationStack.getPreviousId();
                    break;
                case GLOBAL:
                    break;
            }
        }
        return expectedId != null ? StringUtils.equals(current, expectedId) : isGlobalNotification(current);
    }


    protected boolean notificationsMatches(final String expectedId, final String currentId)
    {
        return StringUtils.equals(expectedId, currentId);
    }


    protected String getNotificationId()
    {
        final boolean useDynamicNotificationId = getWidgetSettings().getBoolean(SETTING_USE_DYNAMIC_NOTIFICATION_ID);
        final String notificationId;
        if(useDynamicNotificationId && StringUtils.isNotEmpty(getModel().getValue(MODEL_DYNAMIC_NOTIFICATION_ID, String.class)))
        {
            notificationId = getModel().getValue(MODEL_DYNAMIC_NOTIFICATION_ID, String.class);
        }
        else
        {
            notificationId = getWidgetSettings().getString(SETTING_NOTIFICATION_ID);
        }
        return notificationId;
    }


    protected boolean isMatchingNotification(final NotificationEvent notificationEvent, final Notification notificationConfig,
                    final NotificationDefaults notificationDefaults)
    {
        final String notificationId = getNotificationId();
        final Destination destination = getNotificationDestination(notificationEvent, notificationConfig, notificationDefaults);
        return notificationsMatches(destination, notificationId);
    }


    protected void removeNotification(final Component notificationComponent)
    {
        if(notificationComponent != null)
        {
            notificationEventsStack.remove(notificationComponent);
            notificationComponent.detach();
            if(notificationEventsStack.isEmpty())
            {
                notificationContainer.setVisible(false);
            }
            refreshView();
        }
    }


    /**
     * Calls invalidate on {@link #getWidgetslot()}.
     */
    protected void refreshView()
    {
        if(getWidgetslot() != null)
        {
            getWidgetslot().invalidate();
        }
    }


    protected Component createNotificationComponent(final NotificationEvent notificationEvent,
                    final Notification notificationConfig, final NotificationDefaults notificationDefaults)
    {
        final Div div = new Div();
        div.setAttribute(ATTRIBUTE_NOTIFICATION_EVENT, notificationEvent);
        div.setSclass("yw-notification-message");
        div.setWidgetListener("onShow", "$(this).hide().slideDown()");
        div.setWidgetListener("onClose", "$(this).show().slideUp()");
        // type
        final Label typeLabel = new Label();
        typeLabel.setSclass("yw-notification-type");
        div.appendChild(typeLabel);
        if(notificationConfig == null && notificationDefaults != null && notificationDefaults.getFallback() != null
                        && notificationDefaults.getFallback().getRenderer() != null)
        {
            final Component component = getNotificationRendererComponent(notificationEvent,
                            notificationDefaults.getFallback().getRenderer(), notificationDefaults.getFallback().getParameter());
            div.appendChild(component);
        }
        else if(notificationConfig != null && notificationConfig.getRenderer() != null)
        {
            final Component component = getNotificationRendererComponent(notificationEvent, notificationConfig.getRenderer(),
                            notificationConfig.getParameter());
            div.appendChild(component);
        }
        else
        {
            addMessageComponent(div, notificationEvent, notificationConfig, notificationDefaults);
        }
        // style
        final String className = StringUtils.lowerCase(notificationEvent.getLevel().name());
        UITools.modifySClass(div, className, true);
        return div;
    }


    protected Component getNotificationRendererComponent(final NotificationEvent notificationEvent, final String rendererBean,
                    final List<NotificationParameter> parameters)
    {
        final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
        final NotificationRenderer renderer = getNotificationRenderer(rendererBean);
        return renderer.render(notificationEvent,
                        parameters.stream().collect(Collectors.toMap(NotificationParameter::getName, param -> {
                            if(param.isEvaluate())
                            {
                                return resolver.getValue(notificationEvent, param.getValue());
                            }
                            else
                            {
                                return param.getValue();
                            }
                        })));
    }


    protected NotificationRenderer getNotificationRenderer(final String rendererBean)
    {
        return BackofficeSpringUtil.getBean(rendererBean, NotificationRenderer.class);
    }


    protected boolean isLinkEnabled(final NotificationEvent notificationEvent, final NotificationReference reference,
                    final ExpressionResolver resolver)
    {
        return reference.getLink() != null && (Boolean.parseBoolean(reference.getLink())
                        || (Boolean)resolver.getValue(getReferencedObject(notificationEvent, reference), reference.getLink()));
    }


    protected boolean isLinkEnabled(final NotificationEvent event, final Notification notificationConfig,
                    final NotificationDefaults notificationDefaults)
    {
        boolean links = Optional.ofNullable(notificationDefaults).map(NotificationDefaults::isLinksEnabled).orElse(false);
        final NotificationReferences references = notificationConfig.getReferences();
        if(references != null)
        {
            if(references.isLinksEnabled() != null)
            {
                links = BooleanUtils.isTrue(references.isLinksEnabled()) && ArrayUtils.isNotEmpty(event.getReferencedObjects());
            }
            if(!links || CollectionUtils.isNotEmpty(references.getReference()))
            {
                final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
                links = references.getReference().stream().anyMatch(
                                ref -> ref.getLink() != null && validateReferenceIndex(ref, event) && isLinkEnabled(event, ref, resolver));
            }
        }
        return links;
    }


    protected boolean validateReferenceIndex(final NotificationReference ref, final NotificationEvent event)
    {
        return getIndexOrPlaceholder(ref) < (event.getReferencedObjects() != null ? event.getReferencedObjects().length : 0);
    }


    protected int getIndexOrPlaceholder(final NotificationReference reference)
    {
        return (reference.getIndex() != null ? reference.getIndex() : reference.getPlaceholder()).intValue();
    }


    protected NotificationReference getNotificationReference(final int index, final Notification notificationConfig)
    {
        final Optional<NotificationReference> reference = notificationConfig.getReferences().getReference().stream()
                        .filter(ref -> ref.getPlaceholder().intValue() == index).findAny();
        return reference.isPresent() ? reference.get() : null;
    }


    protected void addMessageComponent(final Div container, final NotificationEvent notificationEvent,
                    final Notification notificationConfig, final NotificationDefaults notificationDefaults)
    {
        if(validateMessage(notificationConfig, notificationEvent))
        {
            final boolean linksEnabled = isLinkEnabled(notificationEvent, notificationConfig, notificationDefaults);
            if(!linksEnabled)
            {
                addMessageComponentWithoutLinks(container, notificationEvent, notificationConfig);
            }
            else
            {
                addMessageComponentWithLinks(container, notificationEvent, notificationConfig, notificationDefaults);
            }
        }
    }


    @InextensibleMethod
    private void addMessageComponentWithLinks(final Div container, final NotificationEvent notificationEvent,
                    final Notification notificationConfig, final NotificationDefaults notificationDefaults)
    {
        final boolean linksEnabledByDefaults = (notificationConfig.getReferences() != null
                        && BooleanUtils.isTrue(notificationConfig.getReferences().isLinksEnabled()))
                        || (notificationDefaults != null && notificationDefaults.isLinksEnabled());
        final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
        final String messageText = Labels.getLabel(notificationConfig.getMessage());
        final Matcher matcher = PATTERN_LINK_PLACEHOLDERS.matcher(messageText);
        int counter = 0;
        int done = 0;
        while(matcher.find())
        {
            final String message = matcher.group(1);
            if(StringUtils.isNotBlank(message))
            {
                final Label label = new Label(message);
                YTestTools.modifyYTestId(label, NOTIFICATION_MESSAGE_YTESTID_PREFIX + counter++);
                container.appendChild(label);
            }
            final int placeholder = Integer.parseInt(matcher.group(2));
            final NotificationReference referenceConfig = ObjectUtils.defaultIfNull(
                            getNotificationReference(placeholder, notificationConfig),
                            getFallbackReferenceConfig(placeholder, linksEnabledByDefaults));
            final boolean isLinkEnabled = referenceConfig.getLink() != null
                            ? isLinkEnabled(notificationEvent, referenceConfig, resolver)
                            : linksEnabledByDefaults;
            if(isLinkEnabled)
            {
                final Component link = createLink(notificationEvent, referenceConfig, resolver);
                container.appendChild(link);
                YTestTools.modifyYTestId(link, NOTIFICATION_MESSAGE_YTESTID_PREFIX + counter++);
            }
            else
            {
                container.appendChild(new Label(getReferenceLabel(notificationEvent, referenceConfig, resolver)));
            }
            done = matcher.end();
        }
        final String remainingText = messageText.substring(done);
        if(StringUtils.isNotBlank(remainingText))
        {
            final Label label = new Label(remainingText);
            YTestTools.modifyYTestId(label, NOTIFICATION_MESSAGE_YTESTID_PREFIX + counter);
            container.appendChild(label);
        }
    }


    @InextensibleMethod
    private void addMessageComponentWithoutLinks(final Div container, final NotificationEvent notificationEvent,
                    final Notification notificationConfig)
    {
        Object[] args = notificationEvent.getReferencedObjects();
        if(notificationConfig.getReferences() != null)
        {
            final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
            args = notificationConfig.getReferences().getReference().stream()
                            .sorted(Comparator.comparing(NotificationReference::getPlaceholder))//
                            .map(ref -> getReferenceLabel(notificationEvent, ref, resolver))//
                            .toArray();
        }
        final String message = Labels.getLabel(notificationConfig.getMessage(), args);
        final Label label = new Label(message);
        container.appendChild(label);
    }


    protected boolean validateMessage(final Notification notification, final NotificationEvent notificationEvent)
    {
        if(notification == null || StringUtils.isBlank(notification.getMessage()))
        {
            LOG.error("No message defined for event {} from {}", notificationEvent.getEventType(), notificationEvent.getSource());
            return false;
        }
        final String messageText = Labels.getLabel(notification.getMessage().trim());
        if(messageText == null && LOG.isErrorEnabled())
        {
            LOG.error("Could not find message for event {} from {}: {}", notificationEvent.getEventType(),
                            notificationEvent.getSource(), notification.getMessage().trim());
            return false;
        }
        return true;
    }


    protected NotificationReference getFallbackReferenceConfig(final int placeHolder, final boolean linksEnabledByDefaults)
    {
        final NotificationReference fallback = new NotificationReference();
        fallback.setLink(Boolean.toString(linksEnabledByDefaults));
        fallback.setPlaceholder(BigInteger.valueOf(placeHolder));
        return fallback;
    }


    protected String getReferenceLabel(final NotificationEvent notificationEvent, final NotificationReference referenceConfig,
                    final ExpressionResolver resolver)
    {
        Object reference = getReferencedObject(notificationEvent, referenceConfig);
        if(reference != null && StringUtils.isNotBlank(referenceConfig.getLabel()))
        {
            reference = resolver.getValue(reference, referenceConfig.getLabel());
        }
        return getLabelService().getObjectLabel(reference);
    }


    protected Component createLink(final NotificationEvent notificationEvent, final NotificationReference referenceConfig,
                    final ExpressionResolver resolver)
    {
        final A link = new A(getReferenceLabel(notificationEvent, referenceConfig, resolver));
        link.addEventListener(Events.ON_CLICK, e -> handleLinkClick(notificationEvent, referenceConfig, resolver));
        return link;
    }


    @InextensibleMethod
    private void handleLinkClick(final NotificationEvent notificationEvent, final NotificationReference referenceConfig,
                    final ExpressionResolver resolver)
    {
        Object reference = getReferencedObject(notificationEvent, referenceConfig);
        if(StringUtils.isNotBlank(referenceConfig.getMessage()))
        {
            reference = resolver.getValue(reference, referenceConfig.getMessage());
        }
        final ObjectWithComponentContext socketMessage = new ObjectWithComponentContext(reference);
        referenceConfig.getContext().forEach(context -> {
            final Object value;
            if(context.isEvaluate())
            {
                value = resolver.getValue(notificationEvent, context.getValue());
            }
            else
            {
                value = context.getValue();
            }
            socketMessage.setParameter(context.getParameter(), value);
        });
        sendOutput(SELECTED_ELEMENT_OUT_SOCKET, socketMessage);
    }


    protected Object getReferencedObject(final NotificationEvent event, final NotificationReference ref)
    {
        final int index = getIndexOrPlaceholder(ref);
        return validateReferenceIndex(ref, event) ? event.getReferencedObjects()[index] : null;
    }


    protected Deque<Component> getNotificationEventsStack()
    {
        return notificationEventsStack;
    }


    protected boolean isDebugLog()
    {
        return getWidgetSettings().getBoolean(SETTING_DEBUG_LOG);
    }


    public Div getNotificationContainer()
    {
        return notificationContainer;
    }


    protected ExpressionResolverFactory getExpressionResolverFactory()
    {
        return expressionResolverFactory;
    }


    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public void setNotificationStack(final NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }
}
