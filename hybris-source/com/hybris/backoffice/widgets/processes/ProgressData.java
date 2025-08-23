/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes;

import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import java.util.Date;

public interface ProgressData
{
    void updateProgress(final CronJobHistoryModel cronJobHistoryModel, final Date currentTime);


    int getEstimatedCurrentPercentage();


    int getMaxPercentage();


    long getTimeToIncreaseOnePercent();
}
