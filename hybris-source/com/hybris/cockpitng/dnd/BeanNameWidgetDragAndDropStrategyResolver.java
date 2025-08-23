/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import org.zkoss.spring.SpringUtil;

public class BeanNameWidgetDragAndDropStrategyResolver implements WidgetDragAndDropStrategyResolver
{
    @Override
    public DragAndDropStrategy resolve(final String strategyName)
    {
        final Object beanDnDStrategy = SpringUtil.getBean(strategyName, DragAndDropStrategy.class);
        return beanDnDStrategy != null ? (DragAndDropStrategy)beanDnDStrategy : null;
    }
}
