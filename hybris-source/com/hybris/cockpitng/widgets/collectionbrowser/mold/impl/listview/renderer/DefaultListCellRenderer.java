/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.util.QualifierLabel;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class DefaultListCellRenderer extends AbstractWidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final String CSS_READ_RESTRICTED = "yw-listview-cell-restricted";
    private static final String CSS_CELL_LABEL = "yw-listview-cell-label";
    private WidgetRenderingUtils widgetRenderingUtils;


    @Override
    public void render(final Listcell parent, final ListColumn columnConfiguration, final Object object, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManger)
    {
        renderInternal(parent, columnConfiguration, object, dataType, widgetInstanceManger);
    }


    protected void renderInternal(final Listcell parent, final ListColumn columnConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManger)
    {
        final QualifierLabel resolvedLabel = getLabelText(parent, columnConfiguration, object, dataType, widgetInstanceManger);
        if(!resolvedLabel.isSuccessful())
        {
            UITools.modifySClass(parent, CSS_READ_RESTRICTED, true);
        }
        final String labelText = UITools.truncateText(resolvedLabel.getLabel(),
                        widgetInstanceManger.getWidgetSettings().getInt("maxCharsInCell"));
        final HtmlBasedComponent text = renderText(labelText, columnConfiguration, object, dataType, widgetInstanceManger);
        parent.appendChild(text);
        fireComponentRendered(text, parent, columnConfiguration, object);
        fireComponentRendered(parent, columnConfiguration, object);
    }


    protected QualifierLabel getLabelText(final Listcell parent, final ListColumn columnConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManger)
    {
        if(columnConfiguration.isAutoExtract())
        {
            return new QualifierLabel(Objects.toString(object));
        }
        else
        {
            final String qualifier = columnConfiguration.getQualifier();
            return getWidgetRenderingUtils().getAttributeLabel(object, dataType, qualifier);
        }
    }


    protected HtmlBasedComponent renderText(final String text, final ListColumn columnConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManger)
    {
        final Label label = new Label(text);
        UITools.modifySClass(label, CSS_CELL_LABEL, true);
        label.setAttribute(AbstractMoldStrategy.ATTRIBUTE_HYPERLINK_CANDIDATE, Boolean.TRUE);
        return label;
    }


    public WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }
}
