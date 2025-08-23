/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync;

/**
 * Pojo which holds scheduled sync task and sync cron job code which runs the sync.
 */
public class SyncTaskExecutionInfo
{
    private final SyncTask syncTask;
    private final String syncCronJobCode;


    public SyncTaskExecutionInfo(final SyncTask syncTask, final String syncCronJobCode)
    {
        this.syncTask = syncTask;
        this.syncCronJobCode = syncCronJobCode;
    }


    public SyncTask getSyncTask()
    {
        return syncTask;
    }


    public String getSyncCronJobCode()
    {
        return syncCronJobCode;
    }
}
