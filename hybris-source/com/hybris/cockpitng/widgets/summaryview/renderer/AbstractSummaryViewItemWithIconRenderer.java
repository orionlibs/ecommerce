/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.summaryview.label.AttributeLabelResolver;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Span;

public abstract class AbstractSummaryViewItemWithIconRenderer<DATA>
                extends AbstractWidgetComponentRenderer<Component, Attribute, DATA>
{
    private static final String SCLASS_CONTAINER = "yw-summaryview-icon-item-container";
    private static final String SCLASS_TEXT_CONTAINER = "yw-summaryview-icon-item-text-container";
    private static final String SCLASS_LABEL = "yw-summaryview-icon-item-label";
    private static final String SCLASS_VALUE = "yw-summaryview-icon-item-value";
    private static final String SCLASS_ICON = "yw-summaryview-icon-item-icon";
    private static final String SCLASS_ITEM_RESTRICTED = "yw-summaryview-icon-item-restricted";
    private static final String LABEL_INVALID_DATA = "data.invalid";
    private static final String SCLASS_INVALID_DATA = "yw-summaryview-label-noitem";
    private static final String SCLASS_PATTERN_ICON = "yw-summaryview-%s-%s";
    private LabelService labelService;
    private PermissionFacade permissionFacade;
    private AttributeLabelResolver attributeLabelResolver;


    @Override
    public void render(final Component parent, final Attribute attributeConfiguration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final HtmlBasedComponent container = createContainer(parent, attributeConfiguration, data, dataType, widgetInstanceManager);
        parent.appendChild(container);
        if(canHandle(data, dataType))
        {
            final String qualifier = attributeConfiguration.getQualifier();
            final DataAttribute dataAttribute = dataType.getAttribute(qualifier);
            final Div textContainer = new Div();
            UITools.addSClass(textContainer, SCLASS_TEXT_CONTAINER);
            container.appendChild(textContainer);
            renderLabel(textContainer, attributeConfiguration, data, dataAttribute, dataType, widgetInstanceManager);
            if(hasPermission(data, dataType) && (dataAttribute == null || hasAttributePermission(data, dataAttribute)))
            {
                renderIcon(container, attributeConfiguration, data, dataAttribute, dataType, widgetInstanceManager);
                final Div attributeValue = new Div();
                UITools.addSClass(attributeValue, SCLASS_VALUE);
                textContainer.appendChild(attributeValue);
                renderValue(attributeValue, attributeConfiguration, data, dataAttribute, dataType, widgetInstanceManager);
                fireComponentRendered(attributeValue, parent, attributeConfiguration, data);
            }
            else
            {
                renderNoReadAccess(textContainer, data);
                UITools.modifySClass(textContainer, SCLASS_INVALID_DATA, true);
            }
            fireComponentRendered(textContainer, parent, attributeConfiguration, data);
        }
        else
        {
            renderInvalidDataLabel(container, widgetInstanceManager);
            UITools.modifySClass(container, SCLASS_INVALID_DATA, true);
        }
        fireComponentRendered(container, parent, attributeConfiguration, data);
        fireComponentRendered(parent, attributeConfiguration, data);
    }


    protected HtmlBasedComponent createContainer(final Component parent, final Attribute attributeConfiguration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div container = new Div();
        UITools.addSClass(container, SCLASS_CONTAINER);
        return container;
    }


    /**
     * Check whether renderer supports given data
     *
     * @param data
     *           data
     * @param dataType
     *           type of data
     * @return true if given data is supported
     */
    protected boolean canHandle(final DATA data, final DataType dataType)
    {
        return dataType != null;
    }


    protected HtmlBasedComponent createIcon(final Component parent, final Attribute attributeConfiguration, final DATA data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Span iconContainer = new Span();
        UITools.addSClass(iconContainer, SCLASS_ICON);
        return iconContainer;
    }


    protected void renderIcon(final Component parent, final Attribute attributeConfiguration, final DATA data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final HtmlBasedComponent iconContainer = createIcon(parent, attributeConfiguration, data, dataAttribute, dataType,
                        widgetInstanceManager);
        UITools.modifySClass(iconContainer,
                        getIconStatusSClass(iconContainer, attributeConfiguration, data, dataAttribute, dataType, widgetInstanceManager),
                        true);
        parent.appendChild(iconContainer);
        fireComponentRendered(iconContainer, parent, attributeConfiguration, data);
    }


    /**
     * Check whether there are enough permissions for a renderer to render an attribute. By default it checks whether
     * given object's type and given instance can be read.
     *
     * @param data
     *           data
     * @param dataType
     *           type of data
     * @return true if renderer can access given data and given typeCode.
     */
    protected boolean hasPermission(final DATA data, final DataType dataType)
    {
        return permissionFacade.canReadType(dataType.getCode()) && permissionFacade.canReadInstance(data);
    }


    /**
     * Check whether there are enough permissions for a renderer to render an attribute. By default it checks whether
     * property of given given object and data attribute can be read.
     *
     * @param data
     *           data
     * @param dataAttribute
     *           attribute of data
     * @return true if renderer can access property of data for a given dataAttribute.
     */
    protected boolean hasAttributePermission(final DATA data, final DataAttribute dataAttribute)
    {
        return getPermissionFacade().canReadInstanceProperty(data, dataAttribute.getQualifier());
    }


    /**
     * Provides css class for icon element.
     * <P>
     * Method provided for convenience to be used in scope of
     * {@link #getIconStatusSClass(HtmlBasedComponent, Attribute, Object, DataAttribute, DataType, WidgetInstanceManager)}
     * </P>
     *
     * @param iconGroup
     *           group of icons (in most cases it is strictly bound to name of renderer)
     * @param iconName
     *           particular icon from provided group
     * @return css class to be set in icon element
     */
    protected String getIconStatusSClass(final String iconGroup, final String iconName)
    {
        return String.format(SCLASS_PATTERN_ICON, iconGroup, iconName);
    }


    /**
     * Use {@link #SCLASS_PATTERN_ICON} as a template for building icon's css class name such as
     * yw-summaryview-online-status-Online.
     *
     * @param iconContainer
     *           component that will be marked with returned class
     * @param attributeConfiguration
     *           configuration of rendering
     * @param data
     *           data
     * @param dataAttribute
     *           attribute to be rendered
     * @param dataType
     *           type code of data
     * @param widgetInstanceManager
     *           instance manager of widget on which rendering takes place
     * @return css class which is added to each item's icon container
     * @see #getIconStatusSClass(String, String)
     */
    protected abstract String getIconStatusSClass(final HtmlBasedComponent iconContainer, final Attribute attributeConfiguration,
                    final DATA data, final DataAttribute dataAttribute, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);


    /**
     * Used by {@link #render(Component, Attribute, Object, DataType, WidgetInstanceManager)} method for obtaining
     * attribute representation.
     *
     * @param attributeContainer
     *           attribute representation can be appended to this container
     * @param attributeConfiguration
     *           configuration of rendering
     * @param data
     *           data
     * @param dataAttribute
     *           attribute to be rendered
     * @param dataType
     *           type code of data
     * @param widgetInstanceManager
     *           widget instance manager
     */
    protected abstract void renderValue(final Div attributeContainer, final Attribute attributeConfiguration, final DATA data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    protected void renderLabel(final Div attributeContainer, final Attribute attributeConfiguration, final DATA data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Label attributeLabel = getAttributeLabelResolver().createAttributeLabel(attributeConfiguration, dataAttribute,
                        dataType.getCode());
        UITools.addSClass(attributeLabel, SCLASS_LABEL);
        attributeContainer.appendChild(attributeLabel);
    }


    protected void renderInvalidDataLabel(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final String invalidDataMessage = widgetInstanceManager.getLabel(LABEL_INVALID_DATA);
        final Label invalidDataLabel = new Label(invalidDataMessage);
        parent.appendChild(invalidDataLabel);
    }


    protected void renderNoReadAccess(final Component parent, final DATA data)
    {
        final String labelMessage = labelService.getAccessDeniedLabel(data);
        final Label invalidDataLabel = new Label(labelMessage);
        UITools.addSClass(invalidDataLabel, SCLASS_ITEM_RESTRICTED);
        parent.appendChild(invalidDataLabel);
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected AttributeLabelResolver getAttributeLabelResolver()
    {
        return attributeLabelResolver;
    }


    @Required
    public void setAttributeLabelResolver(final AttributeLabelResolver attributeLabelResolver)
    {
        this.attributeLabelResolver = attributeLabelResolver;
    }
}
