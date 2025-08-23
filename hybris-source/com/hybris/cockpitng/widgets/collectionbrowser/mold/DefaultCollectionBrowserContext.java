/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

public class DefaultCollectionBrowserContext implements CollectionBrowserMoldContext
{
    protected final CollectionBrowserController controller;


    public DefaultCollectionBrowserContext(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }


    @Override
    public <E> Collection<E> getSelectedItems()
    {
        return Collections.unmodifiableCollection(controller.getSelectedItems());
    }


    @Override
    public <E> E getFocusedItem()
    {
        return controller.getFocusedItem();
    }


    @Override
    public void notifyItemsSelected(final Collection<?> items)
    {
        controller.setSelectedItems(ObjectUtils.defaultIfNull(items, Collections.emptyList()).stream().filter(Objects::nonNull)
                        .collect(Collectors.toList()));
    }


    @Override
    public boolean areHyperlinksSupported()
    {
        return controller.areHyperlinksEnabled();
    }


    @Override
    public void notifyHyperlinkClicked(final Object value)
    {
        controller.notifyHyperlinkClicked(value);
    }


    @Override
    public void notifyItemClicked(final Object item)
    {
        controller.notifyItemClicked(item);
    }


    @Override
    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return controller.getWidgetInstanceManager();
    }


    @Override
    public boolean isSortable(final String attributeQualifier)
    {
        final DataType currentType = getCurrentType();
        if(currentType != null)
        {
            final Context context = new DefaultContext();
            context.addAttribute(CollectionBrowserController.MODEL_PAGEABLE,
                            controller.getModel().getValue(CollectionBrowserController.MODEL_PAGEABLE, Pageable.class));
            return controller.getFieldSearchFacade().isSortable(currentType, attributeQualifier, context);
        }
        else
        {
            return false;
        }
    }


    @Override
    public SinglePage getCurrentPage()
    {
        return controller.getPagingDelegateController().getCurrentSinglePage();
    }


    @Override
    public void sort(final SortData sortData)
    {
        controller.sort(sortData);
    }


    @Override
    public DataType getCurrentType()
    {
        return controller.getCurrentType();
    }


    @Override
    public String getCurrentTypeCode()
    {
        return controller.getCurrentTypeCode();
    }


    @Override
    public boolean isMultiSelectEnabled()
    {
        return controller.isMultiSelectEnabled();
    }


    @Override
    public DragAndDropStrategy getDragAndDropStrategy()
    {
        return controller.getDragAndDropStrategy();
    }


    @Override
    public boolean isDragEnabled()
    {
        return controller.isDragEnabled();
    }


    @Override
    public boolean isDropEnabled()
    {
        return controller.isDropEnabled();
    }
}
