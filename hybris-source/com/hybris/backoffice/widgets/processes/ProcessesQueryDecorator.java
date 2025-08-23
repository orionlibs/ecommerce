/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes;

import com.hybris.backoffice.cronjob.CronJobHistoryDataQuery;

/**
 * @deprecated since 6.6 - not used anymore
 */
@Deprecated(since = "6.6", forRemoval = true)
public interface ProcessesQueryDecorator
{
    /**
     * Decorates given query
     *
     * @param cronJobHistoryDataQuery
     *           query to decorate
     * @return decorated query
     */
    CronJobHistoryDataQuery decorateQuery(CronJobHistoryDataQuery cronJobHistoryDataQuery);
}
