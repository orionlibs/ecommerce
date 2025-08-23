/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.ModalWindowStack;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.lang3.ObjectUtils;

public class DefaultModalWindowStack implements ModalWindowStack, Serializable
{
    protected static final String MODEL_Z_INDEX = "_window_stack_zIndex";
    protected static final int DEFAULT_BASE_INDEX = 10000;
    private final TreeSet<Integer> zIndexTree = new TreeSet<>();
    private Integer baseZIndex;


    @Override
    public int getModalWindowZIndex(final WidgetInstance widgetInstance)
    {
        final Map model = (Map)widgetInstance.getModel();
        Integer zIndex = extractExistingZIndex(model);
        if(zIndex == null)
        {
            if(!zIndexTree.isEmpty())
            {
                zIndex = zIndexTree.last();
                zIndex++;
            }
            else
            {
                zIndex = getBaseZIndex();
            }
            zIndexTree.add(zIndex);
            model.put(MODEL_Z_INDEX, zIndex);
        }
        else if(!zIndexTree.contains(zIndex))
        {
            zIndexTree.add(zIndex);
        }
        return zIndex;
    }


    @Override
    public void onWindowClosed(final WidgetInstance widgetInstance)
    {
        final Map model = (Map)widgetInstance.getModel();
        final Integer zIndex = extractExistingZIndex(model);
        if(zIndex != null)
        {
            zIndexTree.remove(zIndex);
            model.remove(MODEL_Z_INDEX);
        }
    }


    @Override
    public void resetModalWindowStack()
    {
        zIndexTree.clear();
    }


    protected Integer extractExistingZIndex(final Map model)
    {
        final Object zIndex = model.get(MODEL_Z_INDEX);
        return zIndex instanceof Integer ? (Integer)zIndex : null;
    }


    public void setBaseZIndex(final Integer baseZIndex)
    {
        this.baseZIndex = baseZIndex;
    }


    public Integer getBaseZIndex()
    {
        return ObjectUtils.defaultIfNull(baseZIndex, DEFAULT_BASE_INDEX);
    }
}
