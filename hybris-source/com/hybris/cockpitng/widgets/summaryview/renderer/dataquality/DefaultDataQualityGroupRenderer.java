/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer.dataquality;

import com.hybris.cockpitng.config.summaryview.jaxb.DataQualityGroup;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.impl.AbstractOperation;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataquality.DataQualityCalculationService;
import com.hybris.cockpitng.dataquality.model.DataQuality;
import com.hybris.cockpitng.dataquality.model.DataQualityProperty;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.text.NumberFormat;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Vlayout;

public class DefaultDataQualityGroupRenderer extends AbstractWidgetComponentRenderer<Component, DataQualityGroup, Object>
{
    protected static final String SCLASS_DATAQUALITY_CONTAINER = "yw-summaryview-dataquality-container";
    protected static final String SCLASS_DATAQUALITY_CONTAINER_CONTENT = "yw-summaryview-dataquality-container-content";
    protected static final String SCLASS_DATAQUALITY_CONTAINER_CAPTION = "yw-summaryview-dataquality-container-caption";
    protected static final String SCLASS_DATAQUALITY_BAR = "yw-summaryview-dataquality-bar";
    protected static final String SCLASS_DATAQUALITY_BAR_ERROR = SCLASS_DATAQUALITY_BAR + "-error";
    protected static final String SCLASS_DATAQUALITY_BAR_WARNING = SCLASS_DATAQUALITY_BAR + "-warning";
    protected static final String SCLASS_DATAQUALITY_BAR_VALID = SCLASS_DATAQUALITY_BAR + "-valid";
    protected static final String LOADING_LABEL = "loading";
    protected static final int MAX_PERCENT_VALUE = 100;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDataQualityGroupRenderer.class);
    private DataQualityCalculationService dataQualityCalculationService;
    private WidgetComponentRenderer<Component, DataQualityProperty, Object> dataQualityItemRenderer;


    @Override
    public void render(final Component parent, final DataQualityGroup dataQualityGroup, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final Component qualityGroupContainer = new Div();
        parent.appendChild(qualityGroupContainer);
        final EventListener calculateDataQualityResultHandler = getCalculateDataQualityResultHandler(qualityGroupContainer,
                        dataQualityGroup, data, dataType, widgetInstanceManager);
        calculateDataQualityAsync(widgetInstanceManager, dataQualityGroup.getDomainId(), data, calculateDataQualityResultHandler);
    }


    protected EventListener getCalculateDataQualityResultHandler(final Component parent,
                    final DataQualityGroup dataQualityGroup, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return event -> {
            final Optional<DataQuality> dataQuality = (Optional)event.getData();
            if(dataQuality.isPresent())
            {
                renderDataQualityGroup(parent, dataQualityGroup, data, dataType, widgetInstanceManager, dataQuality.get());
            }
            else
            {
                LOG.warn("Data quality does not exist for domain '{}' and object '{}'", dataQualityGroup.getDomainId(), data);
            }
        };
    }


    protected void renderDataQualityGroup(final Component parent, final DataQualityGroup dataQualityGroup, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager, final DataQuality dataQuality)
    {
        final Component qualityGroupContainer = createDataQualityContainer(dataQualityGroup, dataQuality);
        parent.appendChild(qualityGroupContainer);
        addGroupBarToDataQualityGroup((Groupbox)qualityGroupContainer, dataQualityGroup, dataQuality);
        renderDataQualityContent(qualityGroupContainer, dataQualityGroup, data, dataType, dataQuality, widgetInstanceManager);
        fireComponentRendered(qualityGroupContainer, qualityGroupContainer.getParent(), dataQualityGroup, data);
        fireComponentRendered(qualityGroupContainer.getParent(), dataQualityGroup, data);
        widgetInstanceManager.getWidgetslot().invalidate();
    }


    protected void calculateDataQualityAsync(final WidgetInstanceManager widgetInstanceManager, final String domainId,
                    final Object data, final EventListener resultHandler)
    {
        final Operation calculateDataQualityOperation = getCalculateDataQualityOperation(widgetInstanceManager, domainId, data);
        widgetInstanceManager.executeOperation(calculateDataQualityOperation, resultHandler, null);
    }


    protected Operation getCalculateDataQualityOperation(final WidgetInstanceManager widgetInstanceManager, final String domainId,
                    final Object data)
    {
        return new AbstractOperation(widgetInstanceManager.getLabel(LOADING_LABEL), false, Progress.ProgressType.NONE)
        {
            @Override
            public Object execute(final Progress progress)
            {
                return calculateDataQuality(domainId, data);
            }
        };
    }


    protected Optional<DataQuality> calculateDataQuality(final String domainId, final Object data)
    {
        return getDataQualityCalculationService().calculate(data, domainId);
    }


    /**
     * @deprecated since 2005 - use{@link #createDataQualityContainer(DataQualityGroup, DataQuality)} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Component createDataQualityContainer(final DataQualityGroup dataQualityGroup)
    {
        return createDataQualityContainer(dataQualityGroup, null);
    }


    protected Component createDataQualityContainer(final DataQualityGroup dataQualityGroup, final DataQuality dataQuality)
    {
        final Groupbox dataQualityContainer = new Groupbox();
        dataQualityContainer.setOpen(false);
        UITools.modifySClass(dataQualityContainer, SCLASS_DATAQUALITY_CONTAINER, true);
        YTestTools.modifyYTestId(dataQualityContainer, getDataQualityGroupLabel(dataQualityGroup));
        final Component caption = createDataQualityContainerCaption(dataQualityGroup, dataQuality);
        dataQualityContainer.appendChild(caption);
        return dataQualityContainer;
    }


    protected String getDataQualityGroupLabel(final DataQualityGroup dataQualityGroup)
    {
        return StringUtils.isNotEmpty(dataQualityGroup.getLabel()) ? UILabelUtil.resolveLocalizedLabel(dataQualityGroup.getLabel())
                        : dataQualityGroup.getDomainId();
    }


    /**
     * @deprecated since 2005 - use{@link #createDataQualityContainerCaption(DataQualityGroup, DataQuality)} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Component createDataQualityContainerCaption(final DataQualityGroup dataQualityGroup)
    {
        return createDataQualityContainerCaption(dataQualityGroup, null);
    }


    protected Component createDataQualityContainerCaption(final DataQualityGroup dataQualityGroup, final DataQuality dataQuality)
    {
        final String dataQualityGroupName;
        if(StringUtils.isBlank(dataQualityGroup.getLabel()) && dataQuality != null && dataQuality.getGroupLabel() != null)
        {
            dataQualityGroupName = dataQuality.getGroupLabel();
        }
        else
        {
            dataQualityGroupName = getDataQualityGroupLabel(dataQualityGroup);
        }
        final HtmlBasedComponent caption = new Caption(dataQualityGroupName);
        UITools.modifySClass(caption, SCLASS_DATAQUALITY_CONTAINER_CAPTION, true);
        YTestTools.modifyYTestId(caption, dataQualityGroupName.concat("_caption"));
        return caption;
    }


    protected void addGroupBarToDataQualityGroup(final Groupbox container, final DataQualityGroup dataQualityGroup,
                    final DataQuality dataQuality)
    {
        container.setClosable(CollectionUtils.isNotEmpty(dataQuality.getDataQualityProperties()));
        final String dataQualityGroupName = getDataQualityGroupLabel(dataQualityGroup);
        final Double validatedQualityIndex = validateQualityIndex(dataQuality, dataQualityGroupName);
        final Component qualityGroupBar = createDataQualityGroupBar(dataQualityGroup, validatedQualityIndex);
        container.getLastChild().appendChild(qualityGroupBar);
    }


    private static Double validateQualityIndex(final DataQuality dataQuality, final String dataQualityGroupName)
    {
        final double qualityIndexAfterValidation;
        final double qualityIndex = dataQuality.getDataQualityIndex();
        if(qualityIndex > 0)
        {
            qualityIndexAfterValidation = qualityIndex;
        }
        else
        {
            qualityIndexAfterValidation = 0;
            LOG.warn("Quality index for group {} is negative, progress bar will be rendered with value: 0", dataQualityGroupName);
        }
        return Double.valueOf(Math.abs(qualityIndexAfterValidation));
    }


    protected Component createDataQualityGroupBar(final DataQualityGroup dataQualityGroup, final Double qualityIndex)
    {
        final Progressmeter bar = new Progressmeter();
        final String barSclass = determineDataQualityThresholdClass(dataQualityGroup, qualityIndex);
        UITools.modifySClass(bar, barSclass, true);
        UITools.modifySClass(bar, SCLASS_DATAQUALITY_BAR, true);
        bar.setValue((int)(qualityIndex.doubleValue() * MAX_PERCENT_VALUE));
        bar.setTooltiptext(NumberFormat.getPercentInstance().format(qualityIndex));
        bar.setWidth("100%");
        return bar;
    }


    protected String determineDataQualityThresholdClass(final DataQualityGroup dataQualityGroup,
                    final Double currentDataQualityIndex)
    {
        if(dataQualityGroup.getError() != null
                        && dataQualityGroup.getError().doubleValue() > currentDataQualityIndex.doubleValue())
        {
            return SCLASS_DATAQUALITY_BAR_ERROR;
        }
        else if(dataQualityGroup.getWarning() != null
                        && dataQualityGroup.getWarning().doubleValue() > currentDataQualityIndex.doubleValue())
        {
            return SCLASS_DATAQUALITY_BAR_WARNING;
        }
        else
        {
            return SCLASS_DATAQUALITY_BAR_VALID;
        }
    }


    protected void renderDataQualityContent(final Component parent, final DataQualityGroup dataQualityGroup, final Object data,
                    final DataType dataType, final DataQuality dataQuality, final WidgetInstanceManager widgetInstanceManager)
    {
        final Vlayout dataQualityContent = createDataQualityContentContainer();
        dataQualityContent.setParent(parent);
        dataQuality.getDataQualityProperties().forEach(
                        property -> invokeDataQualityPropertyRenderer(dataQualityContent, property, data, dataType, widgetInstanceManager));
        fireComponentRendered(dataQualityContent, parent, dataQualityGroup, data);
    }


    protected Vlayout createDataQualityContentContainer()
    {
        final Vlayout sectionContent = new Vlayout();
        sectionContent.setSclass(SCLASS_DATAQUALITY_CONTAINER_CONTENT);
        sectionContent.setSpacing("auto");
        return sectionContent;
    }


    protected void invokeDataQualityPropertyRenderer(final Component parent, final DataQualityProperty property, final Object data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        new ProxyRenderer(this, parent, property, data).render(getDataQualityPropertyRenderer(), parent, property, data, dataType,
                        widgetInstanceManager);
    }


    protected DataQualityCalculationService getDataQualityCalculationService()
    {
        return dataQualityCalculationService;
    }


    @Required
    public void setDataQualityCalculationService(final DataQualityCalculationService dataQualityCalculationService)
    {
        this.dataQualityCalculationService = dataQualityCalculationService;
    }


    protected WidgetComponentRenderer<Component, DataQualityProperty, Object> getDataQualityPropertyRenderer()
    {
        return dataQualityItemRenderer;
    }


    @Required
    public void setDataQualityPropertyRenderer(
                    final WidgetComponentRenderer<Component, DataQualityProperty, Object> dataQualityItemRenderer)
    {
        this.dataQualityItemRenderer = dataQualityItemRenderer;
    }
}
