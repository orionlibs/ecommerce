/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.ExcelConstants;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * @deprecated since 6.7 no longer used
 */
@Deprecated(since = "6.7", forRemoval = true)
public class ExcelValidator
{
    private Set<String> formats = Sets.newHashSet("xlsx", "zip");
    private NotificationService notificationService;


    public boolean fileAlreadyExists(final Set<FileUploadResult> fileUploadResults, final FileUploadResult fileUploadResult)
    {
        final boolean exists = fileUploadResults.stream()
                        .anyMatch(e -> StringUtils.equals(e.getName(), fileUploadResult.getName()));
        if(exists)
        {
            getNotificationService().notifyUser(ExcelConstants.NOTIFICATION_SOURCE_EXCEL_IMPORT,
                            ExcelConstants.NOTIFICATION_EVENT_TYPE_FILE_EXISTS, NotificationEvent.Level.WARNING, fileUploadResult.getName());
        }
        return exists;
    }


    public boolean isCorrectFormat(final FileUploadResult fileUploadResult)
    {
        if(getFormats().stream().noneMatch(format -> format.equalsIgnoreCase(fileUploadResult.getFormat())))
        {
            getNotificationService().notifyUser(ExcelConstants.NOTIFICATION_SOURCE_EXCEL_IMPORT,
                            ExcelConstants.NOTIFICATION_EVENT_TYPE_INCORRECT_FORMAT, NotificationEvent.Level.WARNING,
                            fileUploadResult.getName(), fileUploadResult.getFormat());
            return false;
        }
        else
        {
            return true;
        }
    }


    public Set<String> getFormats()
    {
        return formats;
    }


    public void setFormats(final Set<String> formats)
    {
        this.formats = formats;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
