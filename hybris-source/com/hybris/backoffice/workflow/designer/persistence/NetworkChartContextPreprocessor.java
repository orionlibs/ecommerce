/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;

/**
 * Allows to preprocess parts of the data from Workflow Designer, e.g. only Actions, or only Decisions.
 */
@FunctionalInterface
public interface NetworkChartContextPreprocessor
{
    /**
     * Process some data from the Workflow Designer, given in the context, before saving.
     *
     * @param context
     *           contains the workflow designer's data, from which some will be processed
     */
    void preprocess(NetworkChartContext context);
}
