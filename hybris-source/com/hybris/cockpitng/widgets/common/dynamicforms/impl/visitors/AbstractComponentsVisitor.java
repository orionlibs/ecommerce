/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.AbstractDynamicElement;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.ScriptingConfig;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.common.dynamicforms.ExpressionEvaluator;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

/**
 * Abstract Visitor of &lt;C&gt; in UI elements. Gets configuration from specific field of {@link DynamicForms}. It should be
 * extended for specific type of component and dynamic element.
 */
public abstract class AbstractComponentsVisitor<C extends Component, E extends AbstractDynamicElement>
                implements ComponentsVisitor
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentsVisitor.class);
    private final Map<String, Collection<C>> componentKeyComponentsMap = Maps.newConcurrentMap();
    private final List<ModelObserver> registeredObservers = Lists.newArrayList();
    private String typeCode;
    private WidgetInstanceManager widgetInstanceManager;
    private DynamicForms dynamicForms;
    private ExpressionEvaluator expressionEvaluator;
    private PermissionFacade permissionFacade;
    private TypeFacade typeFacade;
    private boolean hasDynamicForms;
    private final ScriptingConfig defaultScriptingConfig = new ScriptingConfig();


    @Override
    public void initialize(final String typeCode, final WidgetInstanceManager wim, final DynamicForms dynamicForms)
    {
        this.typeCode = typeCode;
        this.widgetInstanceManager = wim;
        this.dynamicForms = dynamicForms;
        this.hasDynamicForms = checkIfHasDynamicForms(dynamicForms, widgetInstanceManager.getModel());
        if(hasDynamicForms)
        {
            addModelObserversForDynamicElements();
            addCleanUpObserverOnRootObjectReplacement();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(this.toString().concat(" initialized"));
            }
        }
    }


    @Override
    public void register(final Component component)
    {
        if(hasDynamicForms)
        {
            if(canHandle(component))
            {
                final String componentId = getComponentKey((C)component);
                addToMap(componentId, (C)component);
                doFirstVisit(getDynamicElements(), element -> StringUtils.equals(getElementQualifierKey(element), componentId));
            }
            component.getChildren().forEach(this::register);
        }
    }


    @Override
    public void unRegister(final Component component)
    {
        if(hasDynamicForms)
        {
            if(canHandle(component))
            {
                removeFromMap(getComponentKey((C)component), (C)component);
            }
            component.getChildren().forEach(this::unRegister);
        }
    }


    @Override
    public void cleanUp()
    {
        cleanUpInternal(widgetInstanceManager.getModel());
    }


    /**
     * Removes all components references and model observers.
     *
     * @param model
     */
    protected void cleanUpInternal(final WidgetModel model)
    {
        registeredObservers.forEach(model::removeObserver);
        registeredObservers.clear();
        componentKeyComponentsMap.clear();
    }


    /**
     * Decides if component can be handled by the visitor.
     *
     * @param component
     *           component to accept.
     * @return true if components should be accepted by visitor.
     */
    protected abstract boolean canHandle(final Component component);


    /**
     * Gets component's name key which is used to map components with dynamic forms through
     * {@link AbstractDynamicElement#getQualifier()}.
     *
     * @param component
     *           component which id should be returned.
     * @return extracted key of a component.
     */
    protected abstract String getComponentKey(final C component);


    /**
     * Extracts elements of type &lt;E&gt; from {@link DynamicForms}.
     *
     * @return list of &lt;E&gt; to use in configuration.
     */
    protected abstract List<E> getDynamicElements();


    /**
     * Visits components for dynamic element.
     *
     * @param dynamicElement
     *           dynamic forms element configuration.
     * @param target
     *           currently displayed object.
     * @param initial
     *           flag which says if this is initial visit before any interaction with user.
     */
    protected abstract void visitComponents(final E dynamicElement, final Object target, final boolean initial);


    /**
     * Gets qualifier of &lt;E&gt;. For components which {@link #getComponentKey(Component)} cannot be directly mapped to a
     * {@link E#getQualifier()} the method should be overridden so for matching components and &lt;E&gt; values from these two
     * methods should be equal.
     *
     * @param dynamicElement
     *           element of which qualifier should be returned.
     * @return qualifier of a element.
     */
    protected String getElementQualifierKey(final E dynamicElement)
    {
        return dynamicElement.getQualifier();
    }


    /**
     * Checks configuration from {@link AbstractDynamicElement#visibleIf} against target object.
     *
     * @param element
     *           element of configuration
     * @param target
     *           target object
     * @return true is component should be visible
     */
    protected boolean isVisible(final E element, final Object target)
    {
        final ScriptingConfig sc = element.getScriptingConfig() == null ? defaultScriptingConfig : element.getScriptingConfig();
        return StringUtils.isBlank(element.getVisibleIf()) || Boolean.TRUE.equals(expressionEvaluator
                        .evaluateExpression(sc.getVisibleIfLanguage(), sc.getVisibleIfScriptType(), element.getVisibleIf(), target));
    }


    /**
     * Checks configuration from {@link AbstractDynamicElement#disabledIf} against target object.
     *
     * @param element
     *           element of configuration
     * @param target
     *           target object
     * @return true if component should be disabled
     */
    protected boolean isDisabled(final E element, final Object target)
    {
        final ScriptingConfig sc = element.getScriptingConfig() == null ? defaultScriptingConfig : element.getScriptingConfig();
        return StringUtils.isNotBlank(element.getDisabledIf()) && Boolean.TRUE.equals(expressionEvaluator
                        .evaluateExpression(sc.getDisabledIfLanguage(), sc.getDisabledIfScriptType(), element.getDisabledIf(), target));
    }


    /**
     * @see #getPathToAttributeInModel(AbstractDynamicElement, String) where as qualifier
     *      {@link AbstractDynamicElement#getQualifier()} is passed
     */
    protected String getPathToAttributeInModel(final E dynamicElement)
    {
        return getPathToAttributeInModel(dynamicElement, dynamicElement.getQualifier());
    }


    /**
     * Adds prefix with value of modelProperty. If {@link AbstractDynamicElement#getModelProperty()} is not null then it is
     * used as the prefix otherwise the prefix will be {@link DynamicForms#getModelProperty()}
     *
     * @param dynamicElement
     *           element which modelProperty is taken as a prefix: e.g. currentObject.
     * @param qualifier
     *           qualifier of an object to check in model e.g code.
     * @return qualifier with prefix e.g. currentObject.code .
     */
    protected String getPathToAttributeInModel(final E dynamicElement, final String qualifier)
    {
        final StringBuilder modelPropertyAndQualifier = new StringBuilder();
        if(!isRootModelProperty(dynamicElement))
        {
            final String modelProperty = StringUtils.isNotEmpty(dynamicElement.getModelProperty())
                            ? dynamicElement.getModelProperty()
                            : dynamicForms.getModelProperty();
            modelPropertyAndQualifier.append(modelProperty);
        }
        if(StringUtils.isNotEmpty(qualifier) && !MODEL_ROOT.equals(qualifier))
        {
            if(StringUtils.isNotEmpty(modelPropertyAndQualifier))
            {
                modelPropertyAndQualifier.append(".");
            }
            modelPropertyAndQualifier.append(qualifier);
        }
        return modelPropertyAndQualifier.toString();
    }


    /**
     * Returns object on which operations should be done for passed dynamic element.
     *
     * @param dynamicElement
     *           dynamic element
     * @return target object if {@link AbstractDynamicElement#getModelProperty()} or {@link DynamicForms#getModelProperty()}
     *         equal {@link #MODEL_ROOT} then entire model is returned.
     */
    protected Object getTargetObject(final E dynamicElement)
    {
        if(isRootModelProperty(dynamicElement))
        {
            return widgetInstanceManager.getModel();
        }
        final String modelProperty = dynamicElement.getModelProperty() != null ? dynamicElement.getModelProperty()
                        : dynamicForms.getModelProperty();
        return widgetInstanceManager.getModel().getValue(modelProperty, Object.class);
    }


    private boolean isRootModelProperty(final E dynamicElement)
    {
        return MODEL_ROOT.equals(dynamicElement.getModelProperty()) || MODEL_ROOT.equals(dynamicForms.getModelProperty());
    }


    private void doFirstVisit(final List<E> elements, final Predicate<E> filter)
    {
        elements.stream().filter(filter)
                        .forEach(dynamicElement -> visitComponents(dynamicElement, getTargetObject(dynamicElement), true));
    }


    private void addModelObserversForDynamicElements()
    {
        final List<E> elements = getDynamicElements();
        final WidgetModel model = widgetInstanceManager.getModel();
        if(model != null)
        {
            elements.forEach(dynamicElement -> {
                if(StringUtils.isNotEmpty(dynamicElement.getTriggeredOn()))
                {
                    final List<String> triggers = Arrays.asList(dynamicElement.getTriggeredOn().split(","));
                    final Object target = getTargetObject(dynamicElement);
                    final boolean isRootModelProperty = isRootModelProperty(dynamicElement);
                    final Predicate<String> hasAccess = trigger -> filterAccessRights(target, trigger, isRootModelProperty,
                                    dynamicElement);
                    triggers.stream().filter(hasAccess).forEach(trigger -> registerObserver(model, target, trigger, dynamicElement));
                }
            });
        }
    }


    private boolean filterAccessRights(final Object target, final String trigger, final boolean isRootModelProperty,
                    final AbstractDynamicElement dynamicElement)
    {
        boolean canTrigger = false;
        if(StringUtils.isNotEmpty(trigger))
        {
            if(isRootModelProperty)
            {
                final Object realTarget = getWidgetInstanceManager().getModel().getValue(trigger, Object.class);
                canTrigger = realTarget == null || canReadProperty(realTarget, null);
            }
            else
            {
                canTrigger = canReadProperty(target, trigger);
            }
            if(!canTrigger && dynamicElement != null)
            {
                LOG.warn("Current user has insufficient permissions to use trigger {} from {}", trigger, dynamicElement);
            }
        }
        return canTrigger;
    }


    private void registerObserver(final WidgetModel model, final Object initialTarget, final String trigger,
                    final E dynamicElement)
    {
        final String pathToAttribute = getPathToAttributeInModel(dynamicElement, trigger);
        final VisitorObserverIdentity observerId = new VisitorObserverIdentity(pathToAttribute, this.getClass().getName(),
                        dynamicElement);
        final ModelObserver modelObserver = new ModelObserver()
        {
            @Override
            public VisitorObserverIdentity getId()
            {
                return observerId;
            }


            @Override
            public void modelChanged()
            {
                // not inplemented
            }


            @Override
            public void modelChanged(final String property)
            {
                final Object currentTargetObject = getTargetObject(dynamicElement);
                if(!isRootObjectReplaced(initialTarget, currentTargetObject))
                {
                    final boolean visitWithoutChangingValues = !StringUtils.equals(pathToAttribute, property);
                    visitComponents(dynamicElement, currentTargetObject, visitWithoutChangingValues);
                }
            }
        };
        model.removeObserver(pathToAttribute, observerId);
        model.addObserver(pathToAttribute, modelObserver);
        registeredObservers.add(modelObserver);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Registered model observer on an attribute {} for a rule \n{}", pathToAttribute, dynamicElement);
        }
    }


    /**
     * Check whether user has permission to change property on targetObject. If property equals
     * {@link ComponentsVisitor#MODEL_ROOT} then it is checked whether user can change targetObject
     */
    protected boolean canChangeProperty(final Object target, final String property)
    {
        try
        {
            if(ComponentsVisitor.MODEL_ROOT.equals(property))
            {
                return permissionFacade.canChangeInstance(target);
            }
            final DataType dataType = getDataType();
            final boolean isDataTypeAttribute = dataType != null && dataType.getAttribute(property) != null;
            final boolean isWritable = !isDataTypeAttribute || dataType.getAttribute(property).isWritable();
            return isWritable && permissionFacade.canChangeInstanceProperty(target, property);
        }
        catch(final Exception ex)
        {
            LOG.error(String.format("Cannot check permissions for target: %s and property:%s", target, property), ex);
            return false;
        }
    }


    /**
     * Check whether user has permission to read property on targetObject. If property equals
     * {@link ComponentsVisitor#MODEL_ROOT} then it is checked whether user can read targetObject
     */
    protected boolean canReadProperty(final Object target, final String property)
    {
        try
        {
            if(property == null || ComponentsVisitor.MODEL_ROOT.equals(property))
            {
                return permissionFacade.canReadInstance(target);
            }
            return permissionFacade.canReadInstanceProperty(target, property);
        }
        catch(final Exception ex)
        {
            LOG.error(String.format("Cannot check permissions for target: %s and property:%s", target, property), ex);
            return false;
        }
    }


    private void addToMap(final String propertyKey, final C component)
    {
        if(!componentKeyComponentsMap.containsKey(propertyKey))
        {
            componentKeyComponentsMap.put(propertyKey, Lists.newLinkedList());
        }
        componentKeyComponentsMap.get(propertyKey).add(component);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("%s registered component:%s for a property:%s", this.getClass().getSimpleName(),
                            component.toString(), propertyKey));
        }
    }


    private void removeFromMap(final String propertyKey, final C component)
    {
        if(componentKeyComponentsMap.containsKey(propertyKey))
        {
            final Collection<C> components = componentKeyComponentsMap.get(propertyKey);
            if(CollectionUtils.isNotEmpty(components))
            {
                components.remove(component);
                if(CollectionUtils.isEmpty(components))
                {
                    componentKeyComponentsMap.remove(propertyKey);
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("%s unregistred component:%s for a property:%s", this.getClass().getSimpleName(),
                                component.toString(), propertyKey));
            }
        }
    }


    private boolean checkIfHasDynamicForms(final DynamicForms dynamicForms, final WidgetModel model)
    {
        if(StringUtils.isNotBlank(dynamicForms.getModelProperty())
                        && !ComponentsVisitor.MODEL_ROOT.equals(dynamicForms.getModelProperty()))
        {
            final Object initialValue = model.getValue(dynamicForms.getModelProperty(), Object.class);
            return initialValue != null && CollectionUtils.isNotEmpty(getDynamicElements());
        }
        else
        {
            LOG.warn("Dynamic forms model property has wrong value: {}", dynamicForms.getModelProperty());
        }
        return false;
    }


    private void addCleanUpObserverOnRootObjectReplacement()
    {
        final WidgetModel model = widgetInstanceManager.getModel();
        final Object initialRootObject = model.getValue(dynamicForms.getModelProperty(), Object.class);
        final ModelObserver modelObserver = new CleanUpModelObserver(model, initialRootObject);
        registeredObservers.add(modelObserver);
        model.addObserver(dynamicForms.getModelProperty(), modelObserver);
    }


    private boolean isRootObjectReplaced(final Object initialRootObject, final Object currentObject)
    {
        return ObjectUtils.notEqual(initialRootObject, currentObject);
    }


    protected final DataType getDataType()
    {
        if(getTypeCode() != null)
        {
            try
            {
                return typeFacade.load(getTypeCode());
            }
            catch(final TypeNotFoundException ec)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("DataType for typeCode %s cannot be found.", getTypeCode()), ec);
                }
            }
        }
        else
        {
            LOG.warn("TypeCode is null. DataType cannot be loaded");
        }
        return null;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    protected final DynamicForms getDynamicForms()
    {
        return dynamicForms;
    }


    protected final WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    protected final Map<String, Collection<C>> getComponentKeyComponentsMap()
    {
        return componentKeyComponentsMap;
    }


    protected final ExpressionEvaluator getExpressionEvaluator()
    {
        return expressionEvaluator;
    }


    protected final PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    protected final TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    protected final ScriptingConfig getDefaultScriptingConfig()
    {
        return defaultScriptingConfig;
    }


    @Required
    public void setExpressionEvaluator(final ExpressionEvaluator expressionEvaluator)
    {
        this.expressionEvaluator = expressionEvaluator;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    private static class VisitorObserverIdentity
    {
        private final String modelProperty;
        private final String visitorClassName;
        private final AbstractDynamicElement dynamicElement;


        public VisitorObserverIdentity(final String modelProperty, final String visitorClass,
                        final AbstractDynamicElement dynamicElement)
        {
            this.modelProperty = modelProperty;
            this.visitorClassName = visitorClass;
            this.dynamicElement = dynamicElement;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            final VisitorObserverIdentity that = (VisitorObserverIdentity)o;
            if(!modelProperty.equals(that.modelProperty))
            {
                return false;
            }
            if(!visitorClassName.equals(that.visitorClassName))
            {
                return false;
            }
            return dynamicElement.equals(that.dynamicElement);
        }


        @Override
        public int hashCode()
        {
            int result = modelProperty.hashCode();
            result = 31 * result + visitorClassName.hashCode();
            result = 31 * result + dynamicElement.hashCode();
            return result;
        }
    }


    private class CleanUpModelObserver implements ModelObserver
    {
        private final WidgetModel model;
        private final Object initialRootObject;


        public CleanUpModelObserver(final WidgetModel model, final Object initialRootObject)
        {
            this.model = model;
            this.initialRootObject = initialRootObject;
        }


        @Override
        public void modelChanged()
        {
            // not implemented
        }


        @Override
        public void modelChanged(final String property)
        {
            if(StringUtils.equals(property, dynamicForms.getModelProperty()))
            {
                final Object currentRootObject = model.getValue(dynamicForms.getModelProperty(), Object.class);
                if(isRootObjectReplaced(currentRootObject, initialRootObject))
                {
                    cleanUpInternal(model);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Cleaned up after root object changed ".concat(this.toString()));
                    }
                }
            }
        }
    }
}
