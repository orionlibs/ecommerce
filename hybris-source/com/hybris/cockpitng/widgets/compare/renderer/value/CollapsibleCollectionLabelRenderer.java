/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer.value;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.MapDataType;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.IdentifiableEventListener;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class CollapsibleCollectionLabelRenderer extends CollectionLabelRenderer
{
    private static final String ATTRIBUTE_COLLECTION_COLLAPSED = "collection-collapsed";
    private static final String ATTRIBUTE_COLLECTION_CONTAINER = "collection-container";
    private static final String SCLASS_YW_COLLECTION_COLLAPSIBLE = "yw-collection-collapsible";
    private static final String SCLASS_YW_COLLECTION_COLLAPSED = "yw-collection-collapsed";
    private static final String SCLASS_YW_COLLECTION_VALUES_CONTAINER = "yw-collection-values-container";
    private static final String SCLASS_YW_COLLECTION_SHOW_MORE_CONTAINER = "yw-collection-show-more-container";
    private static final String LABEL_KEY_COLLECTION_SHOW_MORE = "collection.elements.more";
    private static final String LABEL_KEY_COLLECTION_SHOW_LESS = "collection.elements.less";
    private static final int DEFAULT_COLLAPSE_THRESHOLD = 3;
    private static final String EDITOR_PARAM_COLLAPSE_THRESHOLD = "collapse-threshold";
    private static final String EVENT_NAME_PATTERN_COLLAPSE_EXPAND = "onCollapseExpand_%s";
    private static final String EVENT_NAME_POSTFIX_GET_STATE = "_stateRequest";
    private WidgetUtils widgetUtils;
    private CockpitEventQueue cockpitEventQueue;
    private ObjectFacade objectFacade;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Collection<Object> collection = getCollection(context);
        final int threshold = Optional.ofNullable(context.<Integer>getParameterAs(EDITOR_PARAM_COLLAPSE_THRESHOLD))
                        .orElse(DEFAULT_COLLAPSE_THRESHOLD);
        if(CollectionUtils.size(collection) > threshold && (parent instanceof HtmlBasedComponent))
        {
            final Collection<Object> limitedCollection = collection.stream().limit(threshold).collect(Collectors.toList());
            renderValues(parent, context, listener, limitedCollection);
            final HtmlBasedComponent valuesContainer = createValuesContainer((HtmlBasedComponent)parent, context, listener);
            parent.appendChild(valuesContainer);
            final HtmlBasedComponent collectionLabelContainer = createCollectionLabelContainer(parent, context, listener);
            renderCollectionLabelContainer(collectionLabelContainer, context, listener);
            parent.appendChild(collectionLabelContainer);
            setCollapseInitialState((HtmlBasedComponent)parent);
            if(!isEditorGroupCollapsed(context))
            {
                handleCollectionContainerExpandStateChangeRequest(valuesContainer,
                                CollectionUtils.disjunction(collection, limitedCollection), context, listener);
                renderCollectionLabelContainer(collectionLabelContainer, context, listener);
            }
            initializeCollapseExpandHandler((HtmlBasedComponent)parent, valuesContainer, collectionLabelContainer, context,
                            listener, CollectionUtils.disjunction(collection, limitedCollection));
            UITools.addSClass((HtmlBasedComponent)parent, SCLASS_YW_COLLECTION_COLLAPSIBLE);
        }
        else
        {
            super.render(parent, context, listener);
        }
    }


    @Override
    protected Collection<Object> getCollection(final EditorContext<Object> context)
    {
        if(context.getInitialValue() instanceof Collection)
        {
            return (Collection)context.getInitialValue();
        }
        else if(context.getInitialValue() instanceof Map)
        {
            return ((Map)context.getInitialValue()).entrySet();
        }
        else
        {
            return Collections.emptyList();
        }
    }


    @Override
    protected DataType getElementType(final DataType collectionType, final EditorContext<Object> context, final Object value)
    {
        if(collectionType instanceof CollectionDataType)
        {
            return ((CollectionDataType)collectionType).getValueType();
        }
        else if(collectionType instanceof MapDataType)
        {
            return collectionType;
        }
        return null;
    }


    protected void setCollapseInitialState(final HtmlBasedComponent component)
    {
        setCollapseState(component, true);
    }


    protected void setCollapseState(final HtmlBasedComponent component, final boolean collapsed)
    {
        final HtmlBasedComponent parent = Optional
                        .ofNullable((HtmlBasedComponent)component.getAttribute(ATTRIBUTE_COLLECTION_CONTAINER)).orElse(component);
        parent.setAttribute(ATTRIBUTE_COLLECTION_COLLAPSED, collapsed);
        UITools.modifySClass(parent, SCLASS_YW_COLLECTION_COLLAPSED, collapsed);
    }


    protected boolean getCollapseState(final HtmlBasedComponent component)
    {
        final Component parent = Optional.ofNullable((Component)component.getAttribute(ATTRIBUTE_COLLECTION_CONTAINER))
                        .orElse(component);
        return Optional.ofNullable((Boolean)parent.getAttribute(ATTRIBUTE_COLLECTION_COLLAPSED)).orElse(Boolean.TRUE);
    }


    protected HtmlBasedComponent createValuesContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        final Div container = new Div();
        UITools.addSClass(container, SCLASS_YW_COLLECTION_VALUES_CONTAINER);
        container.setAttribute(ATTRIBUTE_COLLECTION_CONTAINER, parent);
        return container;
    }


    protected HtmlBasedComponent createCollectionLabelContainer(final Component parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        final Div container = new Div();
        UITools.addSClass(container, SCLASS_YW_COLLECTION_SHOW_MORE_CONTAINER);
        container.setAttribute(ATTRIBUTE_COLLECTION_CONTAINER, parent);
        return container;
    }


    protected void renderCollectionLabelContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        parent.getChildren().clear();
        final Collection<Object> collection = getCollection(context);
        final Label label = new Label();
        final WidgetInstanceManager wim = context.getParameterAs(Editor.WIDGET_INSTANCE_MANAGER);
        if(getCollapseState(parent))
        {
            label.setValue(wim.getLabel(LABEL_KEY_COLLECTION_SHOW_MORE, new Object[]
                            {CollectionUtils.size(collection)}));
        }
        else
        {
            final int threshold = Optional.ofNullable(context.<Integer>getParameterAs(EDITOR_PARAM_COLLAPSE_THRESHOLD))
                            .orElse(DEFAULT_COLLAPSE_THRESHOLD);
            label.setValue(wim.getLabel(LABEL_KEY_COLLECTION_SHOW_LESS, new Object[]
                            {threshold}));
        }
        parent.appendChild(label);
    }


    protected void initializeCollapseExpandHandler(final HtmlBasedComponent parent, final HtmlBasedComponent valuesContainer,
                    final HtmlBasedComponent collectionLabelContainer, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Collection collapsibleCollection)
    {
        collectionLabelContainer.addEventListener(Events.ON_CLICK, event -> getCockpitEventQueue()
                        .publishEvent(new DefaultCockpitEvent(getCollapseExpandEventName(context), null, null)));
        final Object wim = context.getParameterAs(Editor.WIDGET_INSTANCE_MANAGER);
        if(wim instanceof WidgetInstanceManager)
        {
            getWidgetUtils().addGlobalEventListener(getCollapseExpandEventName(context),
                            ((WidgetInstanceManager)wim).getWidgetslot(),
                            new StateChangeEventListener(valuesContainer, collectionLabelContainer, collapsibleCollection, context, listener),
                            CockpitEvent.DESKTOP);
            getWidgetUtils().addGlobalEventListener(getCollapseStateEventName(context),
                            ((WidgetInstanceManager)wim).getWidgetslot(), new StateRequestedEventListener(valuesContainer, context),
                            CockpitEvent.DESKTOP);
        }
    }


    protected boolean isEditorGroupCollapsed(final EditorContext<Object> context)
    {
        final CollapseStateRequest state = new CollapseStateRequest();
        getCockpitEventQueue().publishEvent(new DefaultCockpitEvent(getCollapseStateEventName(context), state, null));
        return !state.isResponded() || state.getResponse();
    }


    protected String getCollapseStateEventName(final EditorContext<Object> context)
    {
        return getCollapseExpandEventName(context) + EVENT_NAME_POSTFIX_GET_STATE;
    }


    protected String getCollapseExpandEventName(final EditorContext<Object> context)
    {
        return String.format(EVENT_NAME_PATTERN_COLLAPSE_EXPAND, getCollapseExpandIdentity(context));
    }


    protected String getCollapseExpandIdentity(final EditorContext<Object> context)
    {
        final String editorGroup = context.getParameterAs(Editor.EDITOR_GROUP);
        final String editorProperty = context.getParameterAs(Editor.EDITOR_PROPERTY);
        return editorGroup + "_" + editorProperty;
    }


    protected void handleCollectionContainerExpandStateChangeRequest(final HtmlBasedComponent valuesContainer,
                    final Collection<Object> collection, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final boolean collapsed = getCollapseState(valuesContainer);
        if(collapsed && !isValuesContainerRendered(valuesContainer))
        {
            renderValuesContainer(valuesContainer, context, listener, collection);
            renderValuesContainer(valuesContainer, context, listener);
        }
        setCollapseState(valuesContainer, !collapsed);
    }


    protected boolean isValuesContainerRendered(final Component parent)
    {
        return !parent.getChildren().isEmpty();
    }


    protected void renderValuesContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Collection collapsibleCollection)
    {
        renderValues(parent, context, listener, collapsibleCollection);
    }


    /**
     * @deprecated 1905, use {@link #renderValuesContainer(HtmlBasedComponent, EditorContext, EditorListener, Collection)}
     *             instead
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected void renderValuesContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        // Not used anymore
    }


    /**
     * @deprecated since 1905, not used anymore
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected HtmlBasedComponent createCollectionLabelContents(final HtmlBasedComponent parent,
                    final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        return parent;
    }


    /**
     * @deprecated since 1905, not used anymore
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected HtmlBasedComponent createValuesHeaderContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        return parent;
    }


    /**
     * @deprecated since 1905, not used anymore
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected void renderValuesHeaderContainer(final HtmlBasedComponent parent, final EditorContext<Object> context,
                    final EditorListener<Object> listener)
    {
        // Not used anymore
    }


    protected WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    @Required
    public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected static class CollapseStateRequest
    {
        private boolean collapsed = true;
        private boolean responded = false;


        public void responded(final boolean collapsed)
        {
            this.responded = true;
            this.collapsed = collapsed;
        }


        public boolean isResponded()
        {
            return responded;
        }


        public boolean getResponse()
        {
            return collapsed;
        }
    }


    protected class StateChangeEventListener implements IdentifiableEventListener<Event>
    {
        private final HtmlBasedComponent valuesContainer;
        private final HtmlBasedComponent collectionLabelContainer;
        private final Collection<Object> collapsibleCollection;
        private final EditorContext<Object> context;
        private final EditorListener<Object> listener;
        private final Object id;


        public StateChangeEventListener(final HtmlBasedComponent valuesContainer, final HtmlBasedComponent collectionLabelContainer,
                        final Collection<Object> collapsibleCollection, final EditorContext<Object> context,
                        final EditorListener<Object> listener)
        {
            this.valuesContainer = valuesContainer;
            this.collectionLabelContainer = collectionLabelContainer;
            this.collapsibleCollection = collapsibleCollection;
            this.context = context;
            this.listener = listener;
            final Object reference = this.context.getParameter(Editor.PARENT_OBJECT);
            final Object objectId = getObjectFacade().getObjectId(reference);
            this.id = objectId + "_" + getCollapseExpandIdentity(context);
        }


        @Override
        public Object getId()
        {
            return id;
        }


        @Override
        public void onEvent(final Event cockpitEvent)
        {
            handleCollectionContainerExpandStateChangeRequest(valuesContainer, collapsibleCollection, context, listener);
            renderCollectionLabelContainer(collectionLabelContainer, context, listener);
        }
    }


    protected class StateRequestedEventListener implements IdentifiableEventListener<Event>
    {
        private final HtmlBasedComponent valuesContainer;
        private final Object id;


        public StateRequestedEventListener(final HtmlBasedComponent valuesContainer, final EditorContext<Object> context)
        {
            this.valuesContainer = valuesContainer;
            this.id = getCollapseStateEventName(context);
        }


        @Override
        public Object getId()
        {
            return id;
        }


        @Override
        public void onEvent(final Event event)
        {
            if((event.getData() instanceof CockpitEvent)
                            && (((CockpitEvent)event.getData()).getData() instanceof CollapseStateRequest))
            {
                final CollapseStateRequest request = (CollapseStateRequest)((CockpitEvent)event.getData()).getData();
                if(!request.isResponded())
                {
                    request.responded(getCollapseState(valuesContainer));
                }
            }
        }
    }
}
