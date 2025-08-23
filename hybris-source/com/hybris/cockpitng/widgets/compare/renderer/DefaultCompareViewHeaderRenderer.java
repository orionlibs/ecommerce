/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRows;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.CompareViewData;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.model.impl.DefaultPartialRendererData;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCompareViewHeaderRenderer
                extends AbstractWidgetComponentRenderer<TableRows, CompareView, PartialRendererData<Collection>>
{
    private WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Collection>> summaryCellRenderer;
    private WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Object>> itemRenderer;


    @Override
    public void render(final TableRows parent, final CompareView configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableComponentIterator<TableRow> rows = parent.rowsIterator();
        renderHeader(parent, rows, configuration, data, dataType, widgetInstanceManager);
        rows.removeRemaining();
        fireComponentRendered(parent, configuration, data);
    }


    protected void renderHeader(final TableRows parent, final TableComponentIterator<TableRow> rows,
                    final CompareView configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final TableRow row = rows.request();
        final TableComponentIterator<TableCell> cells = row.cellsIterator();
        renderHeaderRow(parent, cells, configuration, data, dataType, widgetInstanceManager);
        cells.removeRemaining();
        fireComponentRendered(row, parent, configuration, data);
    }


    protected void renderHeaderRow(final TableRows parent, final TableComponentIterator<TableCell> cells,
                    final CompareView configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        renderHeaderSummary(parent, cells, configuration, data, dataType, widgetInstanceManager);
        data.getData().forEach(
                        item -> renderHeaderCell(parent, cells.request(), configuration, data, item, dataType, widgetInstanceManager));
    }


    protected void renderHeaderSummary(final TableRows parent, final TableComponentIterator<TableCell> cells,
                    final CompareView configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final TableCell cell = cells.request();
        final ProxyRenderer<TableCell, CompareView, PartialRendererData<Collection>> proxyRenderer = new ProxyRenderer(this, parent,
                        configuration, data);
        proxyRenderer.render(getSummaryCellRenderer(), cell, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(cell, parent, configuration, data);
    }


    protected void renderHeaderCell(final TableRows parent, final TableCell cell, final CompareView configuration,
                    final PartialRendererData<Collection> data, final Object item, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final ProxyRenderer<TableCell, CompareView, CompareViewData> proxyRenderer = new ProxyRenderer(this, parent, configuration,
                        data);
        proxyRenderer.render(getItemRenderer(), cell, configuration, createGridData(data, item), dataType, widgetInstanceManager);
    }


    protected PartialRendererData<Object> createGridData(final PartialRendererData<Collection> data, final Object item)
    {
        final DefaultPartialRendererData<Object> partialRendererData = new DefaultPartialRendererData<>(data.getComparisonResult(),
                        item, data.getComparisonState());
        partialRendererData.setDiffOnly(data.isDiffOnly());
        return partialRendererData;
    }


    protected WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Object>> getItemRenderer()
    {
        return itemRenderer;
    }


    @Required
    public void setItemRenderer(final WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Object>> itemRenderer)
    {
        this.itemRenderer = itemRenderer;
    }


    protected WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Collection>> getSummaryCellRenderer()
    {
        return summaryCellRenderer;
    }


    @Required
    public void setSummaryCellRenderer(
                    final WidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Collection>> summaryCellRenderer)
    {
        this.summaryCellRenderer = summaryCellRenderer;
    }
}
