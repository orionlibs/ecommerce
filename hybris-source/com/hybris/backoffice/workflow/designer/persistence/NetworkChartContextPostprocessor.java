/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;

/**
 * Allows to postprocess parts of the data from Workflow Designer, e.g. only Actions, or only Decisions.
 */
@FunctionalInterface
public interface NetworkChartContextPostprocessor
{
    /**
     * Process some data from the Workflow Designer, given in the context, after saving.
     *
     * @param context
     *           contains the workflow designer's data, from which some will be processed
     */
    void postprocess(NetworkChartContext context);
}
