/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes;

import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import org.springframework.core.Ordered;

/**
 * Process item rendering strategy
 */
public interface ProcessItemRenderingStrategy extends Ordered
{
    /**
     * Checks if given process should be rendered by the strategy.
     *
     * @param cronJobHistory
     *           cron job history representing a process
     * @return handle flag
     */
    boolean canHandle(final CronJobHistoryModel cronJobHistory);


    /**
     * Checks if it's possible to rerun given process
     *
     * @param cronJobHistory
     *           cron job history representing a process
     * @return rerun applicable flag
     */
    boolean isRerunApplicable(CronJobHistoryModel cronJobHistory);


    /**
     * Reruns given process
     *
     * @param cronJobHistory
     *           cron job history representing a process
     */
    void rerunCronJob(CronJobHistoryModel cronJobHistory);


    /**
     * Gets title
     *
     * @param cronJobHistory
     *           cron job history representing a process
     * @return title
     */
    String getTitle(CronJobHistoryModel cronJobHistory);


    /**
     * Gets job title
     *
     * @param cronJobHistory
     *           cron job history representing a process
     * @return title of the job
     */
    String getJobTitle(CronJobHistoryModel cronJobHistory);


    /**
     * Checks if process supports progress bar. If not then it will not be rendered.
     *
     * @param cronJobHistory
     *           cron job history representing a process
     * @return rerun applicable flag
     */
    boolean isProgressSupported(CronJobHistoryModel cronJobHistory);
}
