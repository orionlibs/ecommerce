/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.lazyloading.DefaultLazyTask;
import com.hybris.cockpitng.lazyloading.LazyTaskResult;
import com.hybris.cockpitng.lazyloading.LazyTasksExecutor;
import com.hybris.cockpitng.lazyloading.LazyTasksExecutorFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract renderer which allows lazy and non-blocking ui rendering
 *
 * @param <PARENT>    type of parent component on which renderer is able to render
 * @param <CONFIG>    type of configuration for renderer
 * @param <DATA>      type of data that may be rendered
 * @param <LAZY_DATA> type of data that is lazy loaded
 */
public abstract class AbstractLazyRenderer<PARENT, CONFIG, DATA, LAZY_DATA> extends AbstractWidgetComponentRenderer<PARENT, CONFIG, DATA>
{
    private LazyTasksExecutorFactory tasksExecutorFactory;


    @Override
    public void render(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        renderBeforeLoad(parent, configuration, data, dataType, widgetInstanceManager);
        final LazyTasksExecutor executor = tasksExecutorFactory.getInstance(widgetInstanceManager);
        final Supplier<LAZY_DATA> dataSupplier = () ->
                        loadData(configuration, data, dataType);
        final Consumer<LazyTaskResult<LAZY_DATA>> dataConsumer = result ->
                        renderAfterLoad(parent, configuration, data, dataType, widgetInstanceManager, result);
        executor.scheduleTask(new DefaultLazyTask<>(dataSupplier, dataConsumer));
    }


    /**
     * Renders component before lazy loaded data is available.
     *
     * @param parent                parent component on which to render
     * @param configuration         configuration of renderer
     * @param data                  data to be rendered
     * @param dataType              meta information about type of data provided
     * @param widgetInstanceManager widget manager in scope of which renderer is used
     */
    protected abstract void renderBeforeLoad(final PARENT parent, final CONFIG configuration, final DATA data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager);


    /**
     * Loads data of type LAZY_DATA
     *
     * @param configuration configuration of renderer
     * @param data          data to be used for collecting additional information
     * @param dataType      meta information about type of data provided
     * @return Supplier&lt;LAZY_DATA&gt;
     */
    protected abstract LAZY_DATA loadData(final CONFIG configuration, final DATA data, final DataType dataType);


    /**
     * Renders component when lazy loaded data is available.
     *
     * @param parent         parent component on which to render
     * @param configuration  configuration of renderer
     * @param data           data to be rendered
     * @param dataType       meta information about type of data provided
     * @param wim            widget manager in scope of which renderer is used
     * @param lazyLoadedData additional data returned by <b>loadData</b> method, wrapped by LazyTaskResult
     */
    protected abstract void renderAfterLoad(final PARENT parent, final CONFIG configuration, final DATA data,
                    final DataType dataType, final WidgetInstanceManager wim,
                    final LazyTaskResult<LAZY_DATA> lazyLoadedData);


    @Required
    public void setLazyTasksExecutorFactory(final LazyTasksExecutorFactory tasksExecutorFactory)
    {
        this.tasksExecutorFactory = tasksExecutorFactory;
    }
}
