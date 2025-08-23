/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.components.table.TableCell;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.Header;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractImageBoxRenderer;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class DefaultCompareViewHeaderItemRenderer
                extends AbstractWidgetComponentRenderer<TableCell, CompareView, PartialRendererData<Object>>
{
    private static final String SCLASS_CONTAINER_CELL = "yw-compareview-header-item";
    private static final String SCLASS_HEADER_ITEM_MARK = "yw-compareview-mark-icon";
    private AbstractImageBoxRenderer<Header> imageBoxRenderer;
    private ItemComparisonFacade itemComparisonFacade;
    private ObjectFacade objectFacade;


    @Override
    public void render(final TableCell parent, final CompareView configuration, final PartialRendererData<Object> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        renderItemCell(parent, configuration, data, dataType, widgetInstanceManager);
        itemCellRendered(parent, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(parent, configuration, data);
    }


    protected void renderItemCell(final TableCell parent, final CompareView configuration, final PartialRendererData<Object> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div container = getContentsContainer(parent);
        final HtmlBasedComponent itemCellContent = getItemCellContent(parent, configuration, data, dataType, widgetInstanceManager);
        itemCellContentRendered(itemCellContent, parent, configuration, data, dataType, widgetInstanceManager);
        final HtmlBasedComponent markPlaceholder = getMarkPlaceholder(parent, configuration, data, dataType, widgetInstanceManager);
        markPlaceholderRendered(markPlaceholder, parent, configuration, data, dataType, widgetInstanceManager);
        contentsContainerRendered(container, parent, configuration, data, dataType, widgetInstanceManager);
    }


    protected Div getContentsContainer(final TableCell parent)
    {
        return UITools.appendChildIfNeeded(parent, 0, Div.class::isInstance, Div::new);
    }


    protected HtmlBasedComponent getItemCellContent(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Object> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div itemCell = getItemCell(parent);
        final ProxyRenderer<Component, Header, PartialRendererData<Object>> proxyRenderer = new ProxyRenderer(this, parent,
                        configuration, data);
        proxyRenderer.render(getImageBoxRenderer(), itemCell, configuration.getHeader(), data.getData(), dataType,
                        widgetInstanceManager);
        return itemCell;
    }


    protected Div getItemCell(final TableCell parent)
    {
        final Div contentsContainer = getContentsContainer(parent);
        return UITools.appendChildIfNeeded(contentsContainer, 0, Div.class::isInstance, Div::new);
    }


    protected HtmlBasedComponent getMarkPlaceholder(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Object> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return getMarkPlaceholder(parent);
    }


    protected Div getMarkPlaceholder(final TableCell parent)
    {
        final Div contentsContainer = getContentsContainer(parent);
        return UITools.appendChildIfNeeded(contentsContainer, 1, Div.class::isInstance, Div::new);
    }


    protected void markPlaceholderRendered(final HtmlBasedComponent markPlaceholder, final TableCell parent,
                    final CompareView configuration, final PartialRendererData<Object> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(markPlaceholder, SCLASS_HEADER_ITEM_MARK);
    }


    protected void itemCellContentRendered(final HtmlBasedComponent itemCellContent, final TableCell parent,
                    final CompareView configuration, final PartialRendererData<Object> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        // Do nothing
    }


    protected void contentsContainerRendered(final HtmlBasedComponent container, final TableCell parent,
                    final CompareView configuration, final PartialRendererData<Object> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        DefaultCompareViewLayout.markAsContentsContainer(container);
    }


    protected void itemCellRendered(final TableCell parent, final CompareView configuration,
                    final PartialRendererData<Object> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        UITools.addSClass(parent, SCLASS_CONTAINER_CELL);
        final Object comparedObjectId = getObjectFacade().getObjectId(data.getData());
        if(isNotEqual(data.getComparisonResult(), comparedObjectId))
        {
            DefaultCompareViewLayout.markAsNotEqual(parent);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(parent);
        }
        if(!data.getComparisonState().getComparedObjectIds().contains(comparedObjectId))
        {
            DefaultCompareViewLayout.markAsDuringCalculation(parent);
        }
        else
        {
            DefaultCompareViewLayout.markAsCalculated(parent);
        }
        parent.setSticky(getItemComparisonFacade().isSameItem(data.getComparisonState().getReference(), data.getData()));
    }


    /**
     * Checks if there is any difference for between compareObjectId and reference object.
     *
     * @param result
     *           ComparisonResult loaded from engine
     *           {@link com.hybris.cockpitng.compare.ItemComparisonFacade#getCompareViewResult(Object, Collection, Collection)}
     * @param compareObjectId
     *           object id compares with a referenceObjectId {@link ComparisonResult#getReferenceObjectId()}
     * @return true if a compareObjectId is different than a reference object
     */
    protected boolean isNotEqual(final ComparisonResult result, final Object compareObjectId)
    {
        return result.getObjectsIdWithDifferences().contains(compareObjectId);
    }


    protected AbstractImageBoxRenderer<Header> getImageBoxRenderer()
    {
        return imageBoxRenderer;
    }


    @Required
    public void setImageBoxRenderer(final AbstractImageBoxRenderer<Header> imageBoxRenderer)
    {
        this.imageBoxRenderer = imageBoxRenderer;
    }


    public ItemComparisonFacade getItemComparisonFacade()
    {
        return itemComparisonFacade;
    }


    @Required
    public void setItemComparisonFacade(final ItemComparisonFacade itemComparisonFacade)
    {
        this.itemComparisonFacade = itemComparisonFacade;
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
