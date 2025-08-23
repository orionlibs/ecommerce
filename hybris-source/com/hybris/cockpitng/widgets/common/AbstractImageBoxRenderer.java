/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.ImagePreview;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.renderer.AbstractImageComponentRenderer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractImageBoxRenderer<I extends ImagePreview>
                extends AbstractImageComponentRenderer<HtmlBasedComponent, I, Object>
{
    private LabelService labelService;


    protected abstract List<WidgetComponentRenderer> getAdditionalRenderers(final I configuration);


    protected abstract String getSClassForBoxWithAdditionalRenderers();


    protected abstract String getSClassForAdditionalPanel();


    protected abstract String getSClassForAdditionalPanelInnerComponent();


    protected abstract String getPreviewSuffix();


    protected void appendLabel(final Object object, final HtmlBasedComponent tile, final I configuration)
    {
        final Label label = new Label();
        final String title = getLabelService().getObjectLabel(object);
        label.setValue(title);
        label.setTooltiptext(title);
        label.setAttribute(AbstractMoldStrategy.ATTRIBUTE_HYPERLINK_CANDIDATE, Boolean.TRUE);
        tile.appendChild(label);
        fireComponentRendered(label, tile, configuration, object);
    }


    protected void applyAdditionalRenderers(final HtmlBasedComponent parent, final I configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        if(CollectionUtils.isNotEmpty(getAdditionalRenderers(configuration)))
        {
            UITools.modifySClass(parent, getSClassForBoxWithAdditionalRenderers(), true);
            final HtmlBasedComponent additionalPanel = getAdditionalPanel();
            parent.appendChild(additionalPanel);
            getAdditionalRenderers(configuration).stream().filter(Objects::nonNull)
                            .forEach(widgetComponentRenderer -> renderAdditionalComponent(additionalPanel, widgetComponentRenderer,
                                            configuration, element, dataType, widgetInstanceManager));
        }
    }


    protected HtmlBasedComponent getAdditionalPanel()
    {
        final Div additionalPanel = new Div();
        additionalPanel.setSclass(getSClassForAdditionalPanel());
        return additionalPanel;
    }


    protected void renderAdditionalComponent(final HtmlBasedComponent parent,
                    final WidgetComponentRenderer widgetComponentRenderer, final I configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div container = new Div();
        container.setSclass(getSClassForAdditionalPanelInnerComponent());
        parent.appendChild(container);
        widgetComponentRenderer.render(container, configuration, element, dataType, widgetInstanceManager);
    }


    protected WidgetComponentRenderer getBean(final String renderer)
    {
        return BackofficeSpringUtil.getBean(renderer, WidgetComponentRenderer.class);
    }


    @Override
    protected ObjectPreview getObjectPreview(final I configuration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getObjectPreview(configuration, object, dataType, widgetInstanceManager,
                        Collections.singletonMap(ObjectPreviewService.PREFERRED_PREVIEW_SUFFIX, getPreviewSuffix()));
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
