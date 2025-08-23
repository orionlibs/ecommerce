/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import java.util.Collection;

public interface WorkflowItemExtractor
{
    /**
     * Extracts {@link WorkflowItem} from the {@link NetworkChartContext}
     *
     * @param context
     *           which contains WorkflowModel objects
     * @return extracted workflow items
     */
    Collection<WorkflowItem> extract(NetworkChartContext context);
}
