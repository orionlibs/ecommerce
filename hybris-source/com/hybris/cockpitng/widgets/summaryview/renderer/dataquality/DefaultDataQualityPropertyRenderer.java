/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer.dataquality;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataquality.model.DataQualityProperty;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.baseeditorarea.FocusRequest;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.summaryview.SummaryViewController;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultDataQualityPropertyRenderer extends AbstractWidgetComponentRenderer<Component, DataQualityProperty, Object>
{
    protected static final String SCLASS_DATAQUALITY_ITEM_STATUS_INVALID = "yw-summaryview-dataquality-item-status-invalid";
    protected static final String SCLASS_DATAQUALITY_ITEM_CONTAINER = "yw-summaryview-dataquality-item-container";
    private static final String RESTRICTED_DATA = "data.restricted";
    protected final String ATTRIBUTE_QUALITY_PROPERTY = "quality-property";
    private PermissionFacade permissionFacade;


    @Override
    public void render(final Component parent, final DataQualityProperty dataQualityProperty, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Component dataQualityPropertyContainer = createDataQualityPropertyContainer();
        parent.appendChild(dataQualityPropertyContainer);
        dataQualityPropertyContainer.setAttribute(ATTRIBUTE_QUALITY_PROPERTY, dataQualityProperty);
        addFocusTransferListener(dataQualityPropertyContainer, data, widgetInstanceManager);
        appendDataQualityItem(dataQualityPropertyContainer, dataQualityProperty, data);
        fireComponentRendered(dataQualityPropertyContainer, parent, dataQualityProperty, data);
        fireComponentRendered(parent, dataQualityProperty, data);
    }


    protected void addFocusTransferListener(final Component dataQualityPropertyContainer, final Object data,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        dataQualityPropertyContainer.addEventListener(Events.ON_CLICK, e -> {
            final Object attribute = dataQualityPropertyContainer.getAttribute(ATTRIBUTE_QUALITY_PROPERTY);
            if(attribute instanceof DataQualityProperty)
            {
                final FocusRequest request = new FocusRequest(data, ((DataQualityProperty)attribute).getPropertyQualifier());
                widgetInstanceManager.sendOutput(SummaryViewController.SOCKET_OUTPUT_FOCUS, request);
            }
        });
    }


    protected Component createDataQualityPropertyContainer()
    {
        final Div container = new Div();
        container.setSclass(SCLASS_DATAQUALITY_ITEM_CONTAINER);
        return container;
    }


    protected void appendDataQualityItem(final Component container, final DataQualityProperty dataQualityProperty,
                    final Object data)
    {
        final String notLocalizedDataQualityProperty = ObjectValuePath
                        .getNotLocalizedPath(dataQualityProperty.getPropertyQualifier());
        if(getPermissionFacade().canReadInstanceProperty(data, notLocalizedDataQualityProperty))
        {
            appendDataQualityItem(container, dataQualityProperty);
        }
        else
        {
            appendNoReadAccess(container, dataQualityProperty);
        }
    }


    /**
     * @deprecated since 1905 use please use
     *             {@link DefaultDataQualityPropertyRenderer#appendDataQualityItem(org.zkoss.zk.ui.Component, com.hybris.cockpitng.dataquality.model.DataQualityProperty, java.lang.Object)}
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected void appendDataQualityItem(final Component container, final DataQualityProperty dataQualityProperty)
    {
        final Label messageLabel = new Label(dataQualityProperty.getMessage());
        UITools.addSClass(messageLabel, SCLASS_DATAQUALITY_ITEM_STATUS_INVALID);
        container.appendChild(messageLabel);
    }


    protected void appendNoReadAccess(final Component container, final DataQualityProperty dataQualityProperty)
    {
        final Label messageLabel = new Label(getNoReadAccessMessage());
        UITools.addSClass(messageLabel, SCLASS_DATAQUALITY_ITEM_STATUS_INVALID);
        container.appendChild(messageLabel);
    }


    protected String getNoReadAccessMessage()
    {
        return Labels.getLabel(RESTRICTED_DATA);
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
}
