/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.syncpopup;

import de.hybris.platform.catalog.model.SyncItemJobModel;

public class SyncPopupViewModel
{
    private final SyncItemJobModel jobModel;
    private final String name;
    private final String description;
    private final SyncJobType type;
    private final Boolean inSync;


    /**
     * @deprecated since 1808 - use {@link #SyncPopupViewModel(SyncItemJobModel, SyncJobType, Boolean, String)} instead.
     */
    @Deprecated(since = "1808", forRemoval = true)
    public SyncPopupViewModel(final SyncItemJobModel jobModel, final SyncJobType type, final Boolean inSync)
    {
        this(jobModel, type, inSync, //
                        jobModel.getSourceVersion().getCatalog().getName() + " " + jobModel.getSourceVersion().getCatalog().getVersion(), //
                        jobModel.getCode());
    }


    public SyncPopupViewModel(final SyncItemJobModel jobModel, final SyncJobType type, final Boolean inSync, final String name)
    {
        this(jobModel, type, inSync, name, jobModel.getCode());
    }


    private SyncPopupViewModel(final SyncItemJobModel jobModel, final SyncJobType type, final Boolean inSync, final String name,
                    final String description)
    {
        this.jobModel = jobModel;
        this.name = name;
        this.description = description;
        this.type = type;
        this.inSync = inSync;
    }


    public SyncItemJobModel getJobModel()
    {
        return jobModel;
    }


    public String getName()
    {
        return name;
    }


    public String getDescription()
    {
        return description;
    }


    public SyncJobType getType()
    {
        return type;
    }


    public Boolean getInSync()
    {
        return inSync;
    }
}
