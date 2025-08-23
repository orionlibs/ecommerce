/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes.renderer;

import com.hybris.backoffice.widgets.processes.ProcessItemRenderingStrategy;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import java.util.Collections;
import java.util.List;

public class ProcessItemRenderingStrategyRegistry
                extends AbstractStrategyRegistry<ProcessItemRenderingStrategy, CronJobHistoryModel>
{
    @Override
    public boolean canHandle(final ProcessItemRenderingStrategy strategy, final CronJobHistoryModel context)
    {
        return strategy.canHandle(context);
    }


    public List<ProcessItemRenderingStrategy> getStrategiesList()
    {
        return getStrategies().orElse(Collections.emptyList());
    }
}
