/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync.decorators;

import com.hybris.backoffice.cronjob.CronJobHistoryDataQuery;
import com.hybris.backoffice.widgets.processes.ProcessesQueryDecorator;
import de.hybris.platform.catalog.model.SyncItemJobModel;

/**
 * Process data query decorator which adds {@link SyncItemJobModel} type code to query's
 * {@link CronJobHistoryDataQuery#getJobTypeCodes()}.
 *
 * @deprecated since 6.6 - no longer used
 */
@Deprecated(since = "6.6", forRemoval = true)
public class SyncJobProcessQueryDecorator implements ProcessesQueryDecorator
{
    @Override
    public CronJobHistoryDataQuery decorateQuery(final CronJobHistoryDataQuery cronJobHistoryDataQuery)
    {
        final CronJobHistoryDataQuery decorated = new CronJobHistoryDataQuery(cronJobHistoryDataQuery);
        decorated.addJobTypeCode(SyncItemJobModel._TYPECODE);
        return decorated;
    }
}
