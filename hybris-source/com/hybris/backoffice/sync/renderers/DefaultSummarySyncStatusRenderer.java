/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync.renderers;

import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.config.summaryview.jaxb.CustomAttribute;
import com.hybris.cockpitng.config.summaryview.jaxb.Parameter;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.summaryview.renderer.AbstractSummaryViewItemWithIconRenderer;
import de.hybris.platform.core.model.ItemModel;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.impl.XulElement;

public class DefaultSummarySyncStatusRenderer extends AbstractSummaryViewItemWithIconRenderer<ItemModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSummarySyncStatusRenderer.class);
    protected static final String SETTING_SYNC_OUTPUT_SOCKET = "syncOutputSocket";
    protected static final String PARAMETER_ALLOW_SYNC = "allowSync";
    private static final String ATTRIBUTE_SYNC_STATUS = "syncStatus";
    private static final String LABEL_IN_SYNC = "summaryview.insync.label";
    private static final String LABEL_OUT_OF_SYNC = "summaryview.outofsync.label";
    private static final String SCLASS_EDITABLE = "yw-summaryview-container-editable";
    private static final String SCLASS_SYNC_STATUS = "yw-summaryview-sync-status";
    private SynchronizationFacade synchronizationFacade;
    private ObjectFacade objectFacade;
    private WidgetComponentRenderer<XulElement, Object, ItemModel> partialSyncInfoRenderer;


    @Override
    protected HtmlBasedComponent createContainer(final Component parent, final Attribute attributeConfiguration,
                    final ItemModel data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final HtmlBasedComponent container = super.createContainer(parent, attributeConfiguration, data, dataType,
                        widgetInstanceManager);
        if(!objectFacade.isNew(data) && !objectFacade.isModified(data))
        {
            UITools.addSClass(container, SCLASS_SYNC_STATUS);
            if(isSyncAllowed(attributeConfiguration, widgetInstanceManager))
            {
                UITools.addSClass(container, SCLASS_EDITABLE);
                container.addEventListener(Events.ON_CLICK, event -> onStatusIconClick(data, widgetInstanceManager));
            }
            try
            {
                final Optional<Boolean> isInSync = getSynchronizationFacade().isInSync(data, getCtxMap());
                container.setAttribute(ATTRIBUTE_SYNC_STATUS, isInSync);
            }
            catch(final IllegalStateException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
                container.setAttribute(ATTRIBUTE_SYNC_STATUS, null);
            }
        }
        return container;
    }


    protected boolean isSyncAllowed(final Attribute attributeConfiguration, final WidgetInstanceManager widgetInstanceManager)
    {
        if(attributeConfiguration instanceof CustomAttribute)
        {
            final CustomAttribute customAttribute = (CustomAttribute)attributeConfiguration;
            return BooleanUtils.isNotFalse(customAttribute.getRenderParameter().stream()
                            .filter(parameter -> PARAMETER_ALLOW_SYNC.equals(parameter.getName())).map(Parameter::getValue)
                            .map(BooleanUtils::toBooleanObject).filter(Objects::nonNull).reduce(Boolean.TRUE, (b1, b2) -> b1 && b2));
        }
        else
        {
            return true;
        }
    }


    protected Optional<Boolean> getInSyncStatus(final Component parentComponent)
    {
        Component parent = parentComponent;
        while(parent != null)
        {
            if(parent.hasAttribute(ATTRIBUTE_SYNC_STATUS))
            {
                final Object attribute = parent.getAttribute(ATTRIBUTE_SYNC_STATUS);
                return attribute instanceof Optional ? (Optional<Boolean>)attribute : Optional.empty();
            }
            else
            {
                parent = parent.getParent();
            }
        }
        return Optional.empty();
    }


    @Override
    protected void renderIcon(final Component parent, final Attribute attributeConfiguration, final ItemModel data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Optional<Boolean> isInSync = getInSyncStatus(parent);
        final HtmlBasedComponent iconContainer = createIcon(parent, attributeConfiguration, data, dataAttribute, dataType,
                        widgetInstanceManager);
        parent.appendChild(iconContainer);
        UITools.addSClass(iconContainer,
                        getIconStatusSClass(iconContainer, attributeConfiguration, data, dataAttribute, dataType, widgetInstanceManager));
        if(isInSync.isPresent())
        {
            partialSyncInfoRenderer.render((XulElement)iconContainer, attributeConfiguration, data, dataType,
                            widgetInstanceManager);
        }
        else
        {
            iconContainer.setTooltiptext(Labels.getLabel(SyncRenderConstants.LABEL_SYNC_UNDEFINED_TOOLTIP));
            UITools.addSClass(iconContainer, SyncRenderConstants.YW_IMAGE_ATTRIBUTE_SYNC_STATUS_UNDEFINED);
        }
        fireComponentRendered(iconContainer, parent, attributeConfiguration, data);
    }


    @Override
    protected String getIconStatusSClass(final HtmlBasedComponent iconContainer, final Attribute attributeConfiguration,
                    final ItemModel data, final DataAttribute dataAttribute, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Optional<Boolean> isInSync = getInSyncStatus(iconContainer);
        return getStatusIconClass(data, isInSync);
    }


    protected String getStatusIconClass(final ItemModel item, final Optional<Boolean> inSyncStatus)
    {
        if(inSyncStatus.isPresent())
        {
            return inSyncStatus.get().booleanValue() ? SyncRenderConstants.YW_IMAGE_ATTRIBUTE_SYNC_STATUS_IN_SYNC
                            : SyncRenderConstants.YW_IMAGE_ATTRIBUTE_SYNC_STATUS_OUT_OF_SYNC;
        }
        else
        {
            return SyncRenderConstants.YW_IMAGE_ATTRIBUTE_SYNC_STATUS_UNDEFINED;
        }
    }


    @Override
    protected void renderValue(final Div attributeContainer, final Attribute attributeConfiguration, final ItemModel data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Optional<Boolean> isInSync = getInSyncStatus(attributeContainer);
        final Label label = new Label();
        if(isInSync.isPresent())
        {
            label.setValue(Labels.getLabel(isInSync.get().booleanValue() ? LABEL_IN_SYNC : LABEL_OUT_OF_SYNC));
        }
        else
        {
            label.setValue(Labels.getLabel(SyncRenderConstants.LABEL_SYNC_UNDEFINED));
        }
        attributeContainer.appendChild(label);
    }


    protected void onStatusIconClick(final ItemModel item, final WidgetInstanceManager wim)
    {
        final String outputSocket = wim.getWidgetSettings().getString(SETTING_SYNC_OUTPUT_SOCKET);
        if(StringUtils.isNotBlank(outputSocket))
        {
            wim.sendOutput(outputSocket, item);
        }
    }


    protected HashMap<String, Object> getCtxMap()
    {
        return new HashMap<>();
    }


    public SynchronizationFacade getSynchronizationFacade()
    {
        return synchronizationFacade;
    }


    @Required
    public void setSynchronizationFacade(final SynchronizationFacade synchronizationFacade)
    {
        this.synchronizationFacade = synchronizationFacade;
    }


    public WidgetComponentRenderer<XulElement, Object, ItemModel> getPartialSyncInfoRenderer()
    {
        return partialSyncInfoRenderer;
    }


    @Required
    public void setPartialSyncInfoRenderer(final WidgetComponentRenderer<XulElement, Object, ItemModel> partialSyncInfoRenderer)
    {
        this.partialSyncInfoRenderer = partialSyncInfoRenderer;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
